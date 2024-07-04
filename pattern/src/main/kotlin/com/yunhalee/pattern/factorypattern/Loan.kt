package com.yunhalee.pattern.factorypattern

import java.util.Date

class Loan(
    val commitment: Double,
    val riskRating: Int,
    val maturity: Date,
    val expiry: Date?,
    val outstanding: Double,
    val capitalStrategy: String?
) {
    private constructor(commitment: Double, riskRating: Int, maturity: Date) : this(
        commitment = commitment,
        outstanding = 0.00,
        riskRating = riskRating,
        maturity = maturity,
        expiry = null,
        capitalStrategy = null
    )

    private constructor(commitment: Double, riskRating: Int, maturity: Date, expiry: Date) : this(
        commitment = commitment,
        outstanding = 0.00,
        riskRating = riskRating,
        maturity = maturity,
        expiry = expiry,
        capitalStrategy = null
    )

    private constructor(commitment: Double, outstanding: Double, riskRating: Int, maturity: Date, expiry: Date) : this(
        commitment = commitment,
        outstanding = outstanding,
        riskRating = riskRating,
        maturity = maturity,
        expiry = expiry,
        capitalStrategy = null
    )

    private constructor(capitalStrategy: String, commitment: Double, riskRating: Int, maturity: Date, expiry: Date) : this(
        commitment = commitment,
        outstanding = 0.00,
        riskRating = riskRating,
        maturity = maturity,
        expiry = expiry,
        capitalStrategy = capitalStrategy
    )

//    constructor(capitalStrategy: String, commitment: Double, outstanding: Double, riskRating: Int, maturity: Date, expiry: Date) : this(
//        commitment = commitment,
//        outstanding = outstanding,
//        riskRating = riskRating,
//        maturity = maturity,
//        expiry = expiry,
//        capitalStrategy = capitalStrategy
//    )

    companion object {
        fun createTermLoan(commitment: Double, riskRating: Int, maturity: Date): Loan {
            // Step1. static method로 빼내기
//            return Loan(commitment, riskRating, maturity)
            // Step2. Inline method로 변경
            return Loan(
                commitment = commitment,
                outstanding = 0.00,
                riskRating = riskRating,
                maturity = maturity,
                expiry = null,
                capitalStrategy = null
            )
        }

        // null을 파라미터로 전달해서 생성하지 못하게 함수를 별도로 구현
        fun createTermLoan(capitalStrategy: String, commitment: Double, outstanding: Double, riskRating: Int, maturity: Date): Loan {
            // Step1. static method로 빼내기
//            return Loan(capitalStrategy, commitment, outstanding, riskRating, maturity)
            // Step2. Inline method로 변경
            return Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = null,
                capitalStrategy = capitalStrategy
            )
        }

        fun createRevolver(commitment: Double, riskRating: Int, maturity: Date, expiry: Date): Loan {
            // Step1. static method로 빼내기
//            return Loan(commitment, riskRating, maturity, expiry)
            // Step2. Inline method로 변경
            return Loan(
                commitment = commitment,
                outstanding = 0.00,
                riskRating = riskRating,
                maturity = maturity,
                expiry = expiry,
                capitalStrategy = null
            )
        }

        fun createRevolver(capitalStrategy: String, commitment: Double, outstanding: Double, riskRating: Int, maturity: Date, expiry: Date): Loan {
            // Step1. static method로 빼내기
//            return Loan(capitalStrategy, commitment, outstanding, riskRating, maturity, expiry)
            // Step2. Inline method로 변경
            return Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = expiry,
                capitalStrategy = capitalStrategy
            )
        }

        fun createRCTL(commitment: Double, outstanding: Double, riskRating: Int, maturity: Date, expiry: Date): Loan {
            // Step1. static method로 빼내기
//            return Loan(commitment, outstanding, riskRating, maturity, expiry)
            // Step2. Inline method로 변경
            return Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = expiry,
                capitalStrategy = null
            )
        }

        fun createRCTL(capitalStrategy: String, commitment: Double, outstanding: Double, riskRating: Int, maturity: Date, expiry: Date): Loan {
            // Step1. static method로 빼내기
//            return Loan(capitalStrategy, commitment, outstanding, riskRating, maturity, expiry)
            // Step2. Inline method로 변경
            return Loan(
                commitment = commitment,
                outstanding = outstanding,
                riskRating = riskRating,
                maturity = maturity,
                expiry = expiry,
                capitalStrategy = capitalStrategy
            )
        }
    }
}