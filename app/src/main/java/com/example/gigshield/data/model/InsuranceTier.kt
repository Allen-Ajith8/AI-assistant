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
        displayName = "Shift Shield",
        description = "Basic coverage during shifts"
    ),
    INCOME_PROTECTOR(
        dailyPremium = 5,
        familySupport = 400000,
        childEducation = 100000,
        vehicleEmiWaiver = true,
        displayName = "Income Protector",
        description = "Comprehensive income protection"
    );

    fun getBenefitDescription(): String = when (this) {
        SHIFT_SHIELD -> "₹5L Accidental Death & Disability\n₹50K Hospitalization\n₹50K Third-Party Liability"
        INCOME_PROTECTOR -> "₹4L Family Support + ₹1L Child Education\nVehicle EMI Waiver\nAll Shift Shield benefits included"
    }

    fun getBenefitsList(): List<String> = when (this) {
        SHIFT_SHIELD -> listOf(
            "₹5 Lakh Accidental Death & Disability",
            "₹50,000 In-Patient Hospitalization",
            "₹50,000 Third-Party Liability"
        )
        INCOME_PROTECTOR -> listOf(
            "₹4 Lakh Family Support Lump Sum",
            "₹1 Lakh Child Education Fund",
            "Vehicle EMI Waiver",
            "All Shift Shield benefits included"
        )
    }
}
