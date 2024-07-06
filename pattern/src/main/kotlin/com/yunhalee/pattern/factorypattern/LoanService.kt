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

        // Step2. Option 객체 사용하기
        val option = CapitalStrategyOption()
        option.changeCapitalStrategy(capitalStrategy)
        return Loan.createLoan(option.getCapitalStrategy(), commitment, outstanding, riskRating, maturity, expiry)

        // Step3. Option 객체의 메서드 사용하기 (새로운 팩토리 메서드)
        val Loan = option.createLoan(commitment, outstanding, riskRating, maturity, expiry)
    }
}