package com.kotlinspring.utils

import com.kotlinspring.dto.TodoDTO
import com.kotlinspring.entity.Todo

fun convertDtoToEntity(todoDTO: TodoDTO): Todo {
    return todoDTO.let {
        Todo(it.id, it.description, it.complete, it.priority)
    }
}

fun convertEntityToDto(todo: Todo): TodoDTO {
    return todo.let {
        TodoDTO(it.id, it.description, it.complete, it.priority)
    }
}
