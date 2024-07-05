package com.yunhalee.pattern.factorypattern

import java.util.Date


class LoanFactory {

    fun createLoan(capitalStrategy: String, commitment: Double, outstanding: Double, riskRating: Int, maturity: Date, expiry: Date): Loan {
        return when (capitalStrategy) {
            "TermLoan" -> Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = null,
                capitalStrategy = capitalStrategy
            )

            "Revolver" -> Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = expiry,
                capitalStrategy = capitalStrategy
            )

            "RCTL" -> Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = expiry,
                capitalStrategy = capitalStrategy
            )

            else -> throw IllegalArgumentException("Unknown capital strategy")
        }
    }
}