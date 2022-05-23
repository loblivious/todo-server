package com.kotlinspring.entity

import com.kotlinspring.dto.TodoPriority
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "todo")
data class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    var description: String,
    var complete: Boolean = false,
    var priority: TodoPriority = TodoPriority.LOW
)
