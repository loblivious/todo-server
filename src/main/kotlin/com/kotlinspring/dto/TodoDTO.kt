package com.kotlinspring.dto

import javax.validation.constraints.NotBlank

data class TodoDTO(
    val id: Int?,
    @get:NotBlank(message = "Todo description must not be empty")
    val description: String,
    val complete: Boolean = false,
    val priority: TodoPriority = TodoPriority.LOW
)
