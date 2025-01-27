package com.yunhalee.msa.architecture.common.domain.event.publisher

import com.yunhalee.msa.architecture.common.domain.event.DomainEvent

interface  DomainEventPublisher <T : DomainEvent<*>> {

    fun publish(event: T)
}

