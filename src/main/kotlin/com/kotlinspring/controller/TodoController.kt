package com.kotlinspring.controller

import com.kotlinspring.dto.TodoDTO
import com.kotlinspring.service.TodoService
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/v1/todo")
@Validated
class TodoController(val todoService: TodoService) {

    companion object : KLogging()

    @GetMapping
    fun getTodo(@RequestParam priority: String?): List<TodoDTO> =
        todoService.getTodo(priority)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addTodo(@RequestBody @Valid todoDTO: TodoDTO): TodoDTO = todoService.addTodo(todoDTO)

    @PutMapping("/{id}")
    fun updateTodo(@RequestBody todoDTO: TodoDTO, @PathVariable("id") todoId: Int): TodoDTO =
        todoService.updateTodo(todoId, todoDTO)

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAllTodo() = todoService.deleteAllTodo()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodo(@PathVariable("id") todoId: Int) = todoService.deleteTodo(todoId)
}
