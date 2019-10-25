package ru.brovkin.todolist.repositories

import ru.brovkin.todolist.models.Task

interface TaskRepository {
    fun find(id: Int?): Task?
    fun save(model: Task)
    fun update(model: Task)
    fun delete(model: Task)
    fun findAll(): List<Task>
}