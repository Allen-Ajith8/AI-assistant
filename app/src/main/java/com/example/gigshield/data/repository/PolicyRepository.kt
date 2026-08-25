package com.example.gigshield.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class PaymentTx(val date: String, val desc: String, val amount: String, val status: String)
data class PolicyDetails(val policyNumber: String, val status: String, val coverage: String)

interface PolicyRepository {
    fun getPolicyDetails(): Flow<PolicyDetails>
    fun getPayments(): Flow<List<PaymentTx>>
}

class MockPolicyRepository : PolicyRepository {
    override fun getPolicyDetails(): Flow<PolicyDetails> = flow {
        emit(PolicyDetails("POL-882910", "ACTIVE", "₹2,00,000"))
    }

    override fun getPayments(): Flow<List<PaymentTx>> = flow {
        emit(listOf(
            PaymentTx("25 Aug 2026", "Daily Protection Premium", "₹25", "SUCCESS"),
            PaymentTx("24 Aug 2026", "Daily Protection Premium", "₹25", "SUCCESS")
        ))
    }
}
