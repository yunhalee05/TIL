package com.yunhalee.pattern.factorypattern

import org.springframework.stereotype.Component
import java.util.Date

@Component
class LoanService {


    fun create(capitalStrategy: String, commitment: Double, outstanding: Double, riskRating: Int, maturity: Date, expiry: Date): Loan {
        // Step1. factory 메서드 사용하기
//        return Loan.createLoan(capitalStrategy, commitment, outstanding, riskRating, maturity, expiry)
        val factory = LoanFactory()
        return factory.createLoan(capitalStrategy, commitment, outstanding, riskRating, maturity, expiry)
    }
}