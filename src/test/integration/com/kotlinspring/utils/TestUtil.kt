package com.kotlinspring.utils

import com.kotlinspring.dto.TodoDTO
import com.kotlinspring.dto.TodoPriority
import com.kotlinspring.entity.Todo

fun todoEntityList() = listOf(
    Todo(null, "Drink water", false, TodoPriority.MEDIUM),
    Todo(null, "Have my hair cut", false, TodoPriority.LOW),
    Todo(null, "Take the garbage out", false, TodoPriority.HIGH)
)

fun createTodoDTO(
    id: Int? = null,
    name: String = "Drink water",
    complete: Boolean = false,
    priority: TodoPriority = TodoPriority.LOW
) = TodoDTO(id, name, complete, priority)
