package com.kotlinspring.controller

import com.kotlinspring.dto.TodoDTO
import com.kotlinspring.dto.TodoPriority
import com.kotlinspring.entity.Todo
import com.kotlinspring.repository.TodoRepository
import com.kotlinspring.utils.PostgreSQLContainerInitializer
import com.kotlinspring.utils.todoEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class TodoControllerIntegrationTest : PostgreSQLContainerInitializer() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var todoRepository: TodoRepository

    @BeforeEach
    fun setUp() {
        todoRepository.deleteAll()
        todoRepository.saveAll(todoEntityList())
    }

    @Test
    fun getAllTodo() {
        val todoDTOs = webTestClient.get()
            .uri("/v1/todo")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(TodoDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(3, todoDTOs?.size)
    }

    @Test
    fun getTodoByPriority() {
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

        Assertions.assertEquals(1, todoDTOs?.size)
    }

    @Test
    fun addTodo() {
        val todoDTO = TodoDTO(null, "Drink water", false, TodoPriority.HIGH)
        val savedTodoDTO = webTestClient.post()
            .uri("/v1/todo")
            .bodyValue(todoDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(TodoDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedTodoDTO?.id != null
        }
    }

    @Test
    fun updateTodo() {
        val todo = Todo(null, "Have my hair cut", false, TodoPriority.LOW)
        todoRepository.save(todo)
        val expectedTodoDTO = TodoDTO(null, "Drink water", false, TodoPriority.LOW)
        val updatedTodoDTO = webTestClient.put()
            .uri("/v1/todo/{id}", todo.id)
            .bodyValue(expectedTodoDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(TodoDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(expectedTodoDTO.description, updatedTodoDTO?.description)
    }

    @Test
    fun deleteAllTodo() {
        webTestClient.delete()
            .uri("/v1/todo")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun deleteTodo() {
        val todo = Todo(null, "Have my hair cut", false, TodoPriority.LOW)
        todoRepository.save(todo)
        webTestClient.delete()
            .uri("/v1/todo/{id}", todo.id)
            .exchange()
            .expectStatus().isNoContent
    }
}
