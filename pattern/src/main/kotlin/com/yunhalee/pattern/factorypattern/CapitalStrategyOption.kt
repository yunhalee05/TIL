package com.yunhalee.pattern.factorypattern

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
}