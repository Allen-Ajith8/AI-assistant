package com.example.gigshield.data.model

enum class InsuranceTier(
    val dailyPremium: Int,
    val displayName: String,
    val description: String,
    val accidentalDeath: Int? = null,
    val hospitalization: Int? = null,
    val thirdPartyLiability: Int? = null,
    val familySupport: Int? = null,
    val childEducation: Int? = null,
    val vehicleEmiWaiver: Boolean = false
) {
    SHIFT_SHIELD(
        dailyPremium = 3,
        accidentalDeath = 500000,
        hospitalization = 50000,
        thirdPartyLiability = 50000,
        displayName = "Shift Shield (Basic)",
        description = "Basic coverage during shifts"
    ),
    INCOME_PROTECTOR(
        dailyPremium = 5,
        familySupport = 400000,
        childEducation = 100000,
        vehicleEmiWaiver = true,
        displayName = "Income Protector (Premium)",
        description = "Comprehensive income protection"
    );

    fun getBenefitDescription(): String = when (this) {
        SHIFT_SHIELD -> "₹5L Accidental Death & Disability\n₹50K Hospitalization\n₹50K Third-Party Liability"
        INCOME_PROTECTOR -> "₹4L Family Support + ₹1L Child Education\nVehicle EMI Waiver\nAll Shift Shield benefits included"
    }

    fun getBenefitsList(): List<String> = when (this) {
        SHIFT_SHIELD -> listOf(
            "₹5,00,000 standard lump sum to nominee",
            "Up to ₹50,000 for treatments (In-Patient)",
            "Up to ₹50,000 for damages (Third-Party)",
            "For Part-time gig workers"
        )
        INCOME_PROTECTOR -> listOf(
            "The \"4+1\" Lump Sum:",
            "  • ₹4,00,000 Family Support",
            "  • ₹1,00,000 Child Education Fund",
            "Up to ₹50,000 for treatments (In-Patient)",
            "Up to ₹50,000 for damages (Third-Party)",
            "For Full-time gig workers"
        )
    }
}
