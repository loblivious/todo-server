package com.kotlinspring.service

import com.kotlinspring.dto.TodoDTO
import com.kotlinspring.dto.TodoPriority
import com.kotlinspring.exception.TodoNotFoundException
import com.kotlinspring.repository.TodoRepository
import com.kotlinspring.utils.convertDtoToEntity
import com.kotlinspring.utils.convertEntityToDto
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class TodoService(val todoRepository: TodoRepository) {

    companion object : KLogging()

    fun getTodo(priority: String?): List<TodoDTO> {
        return run {
            priority?.let {
                getTodoByPriority(TodoPriority.valueOf(priority.uppercase()))
            } ?: getAllTodo()
        }
    }

    private fun getAllTodo(): List<TodoDTO> {
        logger.info("Getting all todos...")
        return todoRepository.findAll()
            .map { convertEntityToDto(it) }
    }

    private fun getTodoByPriority(todoPriority: TodoPriority): List<TodoDTO> {
        logger.info("Getting all todos that priority is ${todoPriority.toString().lowercase()}...")
        return todoRepository.findByPriority(todoPriority)
            .map { convertEntityToDto(it) }
    }

    fun addTodo(todoDTO: TodoDTO): TodoDTO {
        val todoEntity = convertDtoToEntity(todoDTO)
        todoRepository.save(todoEntity)
        logger.info("Successful persisted todo: $todoEntity")
        return convertEntityToDto(todoEntity)
    }

    fun updateTodo(todoId: Int, todoDTO: TodoDTO): TodoDTO {
        val existingTodo = todoRepository.findById(todoId)
        return if (existingTodo.isPresent) {
            existingTodo.get().let {
                it.description = todoDTO.description
                it.complete = todoDTO.complete
                it.priority = todoDTO.priority
                todoRepository.save(it)
                logger.info("Successful updated todo: $it")
                convertEntityToDto(it)
            }
        } else {
            throw TodoNotFoundException("No todo found for the id: $todoId")
        }
    }

    fun deleteAllTodo() {
        todoRepository.deleteAll()
    }

    fun deleteTodo(todoId: Int) {
        val existingTodoOption = todoRepository.findById(todoId)
        return if (existingTodoOption.isPresent) {
            existingTodoOption.get().let {
                todoRepository.deleteById(todoId)
                logger.info("Successful deleted todo: $it")
            }
        } else {
            throw TodoNotFoundException("No todo found for the id: $todoId")
        }
    }
}
