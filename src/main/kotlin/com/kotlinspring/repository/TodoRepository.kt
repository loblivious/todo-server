package com.kotlinspring.repository

import com.kotlinspring.dto.TodoPriority
import com.kotlinspring.entity.Todo
import org.springframework.data.repository.CrudRepository

interface TodoRepository : CrudRepository<Todo, Int> {
    fun findByPriority(priority: TodoPriority): List<Todo>
}
