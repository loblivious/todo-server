package com.kotlinspring.repository

import com.kotlinspring.dto.TodoPriority
import com.kotlinspring.utils.PostgreSQLContainerInitializer
import com.kotlinspring.utils.todoEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryIntegrationTest : PostgreSQLContainerInitializer() {
    @Autowired
    lateinit var todoRepository: TodoRepository

    @BeforeEach
    fun setUp() {
        todoRepository.deleteAll()
        todoRepository.saveAll(todoEntityList())
    }

    @Test
    fun findByPriority() {
        val todos = todoRepository.findByPriority(TodoPriority.LOW)
        Assertions.assertEquals(1, todos.size)
    }
}
