package com.yunhalee.msa.architecture.service.order.domain.core.valueobject

import com.yunhalee.msa.architecture.common.domain.valueobject.BaseId
import java.util.UUID

class TrackingId(value: UUID) : BaseId<UUID>(value) {
}

