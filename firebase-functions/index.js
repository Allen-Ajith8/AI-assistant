const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();

// ==========================================
// 1. AUTH & PROFILE
// ==========================================

// Triggered when a new user signs up via Firebase Auth
exports.onUserSignup = functions.auth.user().onCreate(async (user) => {
    console.log(`Creating profile for user: ${user.uid}`);
    const userRef = db.collection('users').document(user.uid);
    return userRef.set({
        email: user.email,
        displayName: user.displayName || 'Gig Worker',
        createdAt: admin.firestore.FieldValue.serverTimestamp(),
        totalPremiumPaid: 0,
        activePolicy: false,
        trustScore: 100,
        kycStatus: 'PENDING'
    });
});

// Triggered when KYC documents are uploaded (Profile & KYC page)
exports.onKycSubmitted = functions.firestore
    .document('kyc_docs/{docId}')
    .onCreate(async (snap, context) => {
        const docData = snap.data();
        const userId = docData.userId;
        
        console.log(`Processing KYC for user: ${userId}`);
        
        // Mock AI Verification for KYC
        const isVerified = Math.random() > 0.1; // 90% pass rate
        
        // Update the KYC doc
        await snap.ref.set({
            status: isVerified ? 'VERIFIED' : 'REJECTED',
            verifiedAt: admin.firestore.FieldValue.serverTimestamp()
        }, { merge: true });

        // Update the user profile
        if (userId) {
            await db.collection('users').document(userId).set({
                kycStatus: isVerified ? 'VERIFIED' : 'REJECTED',
                activePolicy: isVerified // Automatically activate policy if KYC passes
            }, { merge: true });
        }
        return null;
    });

// ==========================================
// 2. VEHICLE GARAGE
// ==========================================

// Triggered when a new vehicle is added (Vehicle Garage page)
exports.onVehicleAdded = functions.firestore
    .document('vehicles/{vehicleId}')
    .onCreate(async (snap, context) => {
        const vehicle = snap.data();
        console.log(`Verifying vehicle: ${vehicle.registrationNumber}`);
        
        // Mock RTO / VAHAN API verification
        return snap.ref.set({
            verificationStatus: 'VERIFIED',
            insuranceValid: true,
            fitnessValid: true,
            verifiedAt: admin.firestore.FieldValue.serverTimestamp()
        }, { merge: true });
    });

// ==========================================
// 3. WORK ACTIVITY & TELEMETRY
// ==========================================

// Triggered when a new work_session is added to Firestore (Start Work)
exports.onWorkSessionStart = functions.firestore
    .document('work_sessions/{sessionId}')
    .onCreate(async (snap, context) => {
        const session = snap.data();
        console.log(`Session ${context.params.sessionId} started`);
        
        return snap.ref.set({
            startTime: admin.firestore.FieldValue.serverTimestamp(),
            status: 'ACTIVE',
            verified: false,
            premiumDeducted: 0
        }, { merge: true });
    });

// Triggered when an existing work_session is updated (e.g. marked as completed) (Work History / Daily Premium)
exports.onWorkSessionEnd = functions.firestore
    .document('work_sessions/{sessionId}')
    .onUpdate(async (change, context) => {
        const newData = change.after.data();
        const previousData = change.before.data();
        
        // If it was just marked as ended
        if (newData.status === 'ENDED' && previousData.status === 'ACTIVE') {
            console.log(`Session ${context.params.sessionId} ended, calculating premiums`);
            
            // Mock verification logic based on device sensors/location
            const verificationScore = Math.floor(Math.random() * (100 - 80 + 1) + 80); // 80-100 score
            const isVerified = verificationScore >= 85;
            
            // Deduct standard micro-premium for the shift
            const premiumAmount = 25; // ₹25

            // Record payment for Daily Premium
            if (newData.userId) {
                await db.collection('payments').add({
                    userId: newData.userId,
                    sessionId: context.params.sessionId,
                    amount: premiumAmount,
                    type: 'DAILY_PREMIUM',
                    status: 'SUCCESS',
                    timestamp: admin.firestore.FieldValue.serverTimestamp()
                });
            }

            return change.after.ref.set({
                endTime: admin.firestore.FieldValue.serverTimestamp(),
                verified: isVerified,
                verificationScore: verificationScore,
                premiumDeducted: premiumAmount,
                duration: "8h 0m" // Mock calculation for now
            }, { merge: true });
        }
        return null;
    });

// Process telemetry data (Work Activity Detection / iQOO Device Status)
exports.processTelemetryData = functions.firestore
    .document('telemetry_logs/{logId}')
    .onCreate(async (snap, context) => {
        const telemetry = snap.data();
        // Analyze driving behavior, sudden braking, screen usage, etc.
        const riskLevel = telemetry.speed > 80 ? 'HIGH_RISK' : 'SAFE';
        
        return snap.ref.set({
            processed: true,
            drivingRiskLevel: riskLevel,
            timestamp: admin.firestore.FieldValue.serverTimestamp()
        }, { merge: true });
    });

// ==========================================
// 4. CLAIMS & AI VERIFICATION
// ==========================================

// Triggered when a new claim is created (Report Accident / My Claims)
exports.processNewClaim = functions.firestore
    .document('claims/{claimId}')
    .onCreate(async (snap, context) => {
        const claim = snap.data();
        console.log(`Processing new claim: ${context.params.claimId}`);
        
        let updateData = {
            aiVerificationComplete: true,
            processedAt: admin.firestore.FieldValue.serverTimestamp()
        };

        // Basic AI/rule-based mock for risk assessment (Fraud/Risk Status)
        let riskScore = "LOW";
        if (claim.amount && typeof claim.amount === 'string') {
            const numAmount = parseInt(claim.amount.replace(/\D/g,''));
            if (numAmount > 100000) {
                riskScore = "HIGH";
            } else if (numAmount > 20000) {
                riskScore = "MEDIUM";
            }
        }
        updateData.riskStatus = riskScore;
        updateData.status = riskScore === "HIGH" ? "MANUAL_REVIEW" : "APPROVED_PENDING_DISBURSAL";

        // If it's a voice claim, mock transcription and NLP extraction
        if (claim.type && claim.type.toLowerCase() === 'voice') {
            updateData.transcription = "I was delivering an order on Main St when a car suddenly pulled out and hit my bike. My leg is injured and the bike is damaged.";
            updateData.extractedEntities = {
                incidentType: "Accident",
                location: "Main St",
                injuriesMentioned: true,
                vehicleDamage: true
            };
        }
        
        return snap.ref.set(updateData, { merge: true });
    });

// Triggered when evidence is uploaded (Upload Evidence / AI Verification)
exports.analyzeEvidence = functions.firestore
    .document('evidence/{evidenceId}')
    .onCreate(async (snap, context) => {
        const evidence = snap.data();
        // Mock AI image analysis for damage assessment
        console.log(`Analyzing evidence for claim: ${evidence.claimId}`);
        
        return snap.ref.set({
            aiAnalysisComplete: true,
            damageDetected: true,
            fraudProbability: 'LOW',
            estimatedDamageCost: '₹15,000'
        }, { merge: true });
    });

// ==========================================
// 5. PAYMENTS & PAYOUTS
// ==========================================

// Triggered on claims approved to process payout (Payouts page)
exports.processPayout = functions.firestore
    .document('payouts/{payoutId}')
    .onCreate(async (snap, context) => {
        const payout = snap.data();
        console.log(`Processing payout for claim: ${payout.claimId}`);
        
        // Mock Razorpay/Stripe transfer
        return snap.ref.set({
            status: 'COMPLETED',
            transactionId: 'TXN_' + Math.floor(Math.random() * 1000000000),
            processedAt: admin.firestore.FieldValue.serverTimestamp()
        }, { merge: true });
    });

// ==========================================
// 6. EMERGENCY & NOTIFICATIONS
// ==========================================

// HTTP endpoint to trigger an emergency response (Emergency SOS / Contacts)
exports.triggerEmergencySos = functions.https.onRequest(async (req, res) => {
    // Enable CORS
    res.set('Access-Control-Allow-Origin', '*');
    
    if (req.method === 'OPTIONS') {
        res.set('Access-Control-Allow-Methods', 'POST');
        res.status(204).send('');
        return;
    }

    if (req.method !== 'POST') {
        res.status(405).send('Method Not Allowed');
        return;
    }

    try {
        const { userId, location, timestamp } = req.body;
        
        // Log the SOS to Firestore
        const sosRef = await db.collection('sos_alerts').add({
            userId: userId || 'unknown',
            location: location || 'unknown',
            timestamp: timestamp || admin.firestore.FieldValue.serverTimestamp(),
            status: 'DISPATCHED_HELP',
            resolved: false
        });

        // Trigger SMS to Emergency Contacts (mocked)
        console.log(`EMERGENCY SOS TRIGGERED for user ${userId} at ${location}. Alert ID: ${sosRef.id}`);
        console.log(`Notifying Emergency Contacts for user ${userId}...`);

        res.status(200).json({
            success: true,
            message: 'Emergency services and contacts have been alerted',
            alertId: sosRef.id
        });
    } catch (error) {
        console.error('Error triggering SOS:', error);
        res.status(500).json({ success: false, error: 'Internal Server Error' });
    }
});

// Triggered to send FCM Notifications (Notifications page)
exports.sendNotification = functions.firestore
    .document('notifications/{notificationId}')
    .onCreate(async (snap, context) => {
        const notification = snap.data();
        console.log(`Sending notification to user ${notification.userId}: ${notification.title}`);
        
        // In a real app, use admin.messaging().sendToDevice(...)
        return snap.ref.set({
            sent: true,
            sentAt: admin.firestore.FieldValue.serverTimestamp()
        }, { merge: true });
    });
