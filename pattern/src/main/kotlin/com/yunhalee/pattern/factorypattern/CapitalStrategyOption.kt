package com.yunhalee.pattern.factorypattern

import java.util.Date

class CapitalStrategyOption {


    private var capitalStrategy = "TermLoan"

    fun changeCapitalStrategy(strategy: String) {
        this.capitalStrategy = strategy
    }

    fun getCapitalStrategy(): String {
        return this.capitalStrategy
    }

    fun isTermLoan(): Boolean {
        return this.capitalStrategy == "TermLoan"
    }

    fun isRevolver(): Boolean {
        return this.capitalStrategy == "Revolver"
    }

    fun isRCTL(): Boolean {
        return this.capitalStrategy == "RCTL"
    }


    fun createLoan(commitment: Double, outstanding: Double, riskRating: Int, maturity: Date, expiry: Date): Loan {
        if (isRCTL()) {
            return Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = expiry,
                capitalStrategy = capitalStrategy
            )
        } else if (isRevolver()) {
            return Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = expiry,
                capitalStrategy = capitalStrategy
            )
        } else {
            return Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = null,
                capitalStrategy = capitalStrategy
            )
        }
    }
}