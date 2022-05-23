package com.kotlinspring.controller

import com.kotlinspring.dto.TodoDTO
import com.kotlinspring.dto.TodoPriority
import com.kotlinspring.service.TodoService
import com.kotlinspring.utils.createTodoDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@WebMvcTest(controllers = [TodoController::class])
@AutoConfigureWebTestClient
class TodoControllerUnitTest {
    companion object {
        const val TEST_TODO_ID = 100
    }

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var todoServiceMockk: TodoService

    @Test
    fun getAllTodo() {
        every { todoServiceMockk.getTodo(null) }.returnsMany(
            listOf(
                createTodoDTO(1),
                createTodoDTO(2, "Have my hair cut")
            )
        )
        val todoDTOs = webTestClient.get()
            .uri("/v1/todo")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(TodoDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(2, todoDTOs?.size)
    }

    @Test
    fun getTodoByPriority() {
        every { todoServiceMockk.getTodo("low") }.returnsMany(
            listOf(
                createTodoDTO(1),
                createTodoDTO(2, "Have my hair cut"),
                createTodoDTO(3, "Take out the garbage")
            )
        )
        val uri = UriComponentsBuilder.fromUriString("/v1/todo")
            .queryParam("priority", "low")
            .toUriString()
        val todoDTOs = webTestClient.get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(TodoDTO::class.java)
            .returnResult()
            .responseBody
        assertEquals(3, todoDTOs?.size)
    }

    @Test
    fun addTodo() {
        val todoDTO = TodoDTO(null, "Drink water", false, TodoPriority.LOW)
        every { todoServiceMockk.addTodo(any()) } returns createTodoDTO(TEST_TODO_ID)
        val savedTodoDTO = webTestClient.post()
            .uri("/v1/todo")
            .bodyValue(todoDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(TodoDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedTodoDTO?.id == TEST_TODO_ID
        }
    }

    @Test
    fun addTodoValidation() {
        val todoDTO = TodoDTO(null, "", false, TodoPriority.LOW)
        every { todoServiceMockk.addTodo(any()) } returns createTodoDTO(TEST_TODO_ID)
        val response = webTestClient.post()
            .uri("/v1/todo")
            .bodyValue(todoDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        assertEquals("Todo name must not be empty", response)
    }

    @Test
    fun addTodoRuntimeException() {
        val todoDTO = TodoDTO(null, "Drink water", false, TodoPriority.LOW)
        val errorMessage = "Unexpected error occurred"
        every { todoServiceMockk.addTodo(any()) } throws RuntimeException(errorMessage)
        val response = webTestClient.post()
            .uri("/v1/todo")
            .bodyValue(todoDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        assertEquals(errorMessage, response)
    }

    @Test
    fun updateTodo() {
        every { todoServiceMockk.updateTodo(any(), any()) } returns createTodoDTO(TEST_TODO_ID, "Drink water")
        val expectedTodoDTO = TodoDTO(null, "Drink water", false, TodoPriority.LOW)
        val updatedTodoDTO = webTestClient.put()
            .uri("/v1/todo/{id}", TEST_TODO_ID)
            .bodyValue(expectedTodoDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(TodoDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(expectedTodoDTO.description, updatedTodoDTO?.description)
    }

    @Test
    fun deleteAllTodo() {
        every { todoServiceMockk.deleteAllTodo() } just runs
        webTestClient.delete()
            .uri("/v1/todo")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun deleteTodo() {
        every { todoServiceMockk.deleteTodo(any()) } just runs
        webTestClient.delete()
            .uri("/v1/todo/{id}", TEST_TODO_ID)
            .exchange()
            .expectStatus().isNoContent
    }
}
