package com.kotlinspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodoListServiceApplication

fun main(args: Array<String>) {
    runApplication<TodoListServiceApplication>(*args)
}
