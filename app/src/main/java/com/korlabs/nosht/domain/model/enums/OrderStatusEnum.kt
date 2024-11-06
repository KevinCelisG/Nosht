package com.korlabs.nosht.domain.model.enums

enum class OrderStatusEnum(val status: String) {
    PENDING("Pending"), COOKING("Cooking"), READY("Ready"), SERVED("Served"), PAID("Paid"), ALL("All")
}