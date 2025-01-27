package com.yunhalee.msa.architecture.common.domain.valueobject

import java.math.BigDecimal
import java.math.RoundingMode

data class Money(val amount: BigDecimal) {

    companion object {
        val ZERO = Money(BigDecimal.ZERO)
    }

    fun isGreaterThanZero(): Boolean {
        // amount.compareTo(BigDecimal.ZERO) > 0
        return amount > BigDecimal.ZERO
    }

    fun isGreaterThan(money: Money): Boolean {
        return amount > money.amount
    }

    fun add(money: Money): Money {
        return Money(amount.add(money.amount))
    }

    fun subtract(money: Money): Money {
        return Money(amount.subtract(money.amount))
    }

    fun multiply(multiplier: Int): Money {
        return Money(amount.multiply(BigDecimal(multiplier)))
    }


    private fun setScale(input: BigDecimal): BigDecimal {
        // 0.33333333.... ->
        return input.setScale(2, RoundingMode.HALF_EVEN)
    }
}