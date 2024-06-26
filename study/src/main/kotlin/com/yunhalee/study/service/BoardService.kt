package com.yunhalee.study.service

import com.yunhalee.study.domain.Board
import com.yunhalee.study.domain.BoardEvent
import com.yunhalee.study.repository.BoardRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun saveBoard() {
        val board = boardRepository.save(Board(1, "testBoardCreate", "this is test board create"))
        applicationEventPublisher.publishEvent(BoardEvent("CREATE", "test board created", board))
    }
}
