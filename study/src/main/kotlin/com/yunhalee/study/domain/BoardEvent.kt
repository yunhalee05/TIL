package com.yunhalee.study.domain

class BoardEvent(
    val eventType: String,
    val eventMessage: String,
    val board: Board
)
