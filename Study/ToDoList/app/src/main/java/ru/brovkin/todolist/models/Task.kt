package ru.brovkin.todolist.models

import kotlinx.serialization.Serializable

@Serializable
class Task {
    var id: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var definition: String? = null
        get() = field
        set(value) {
            field = value
        }
    constructor(definition: String?) {
        this.definition = definition
    }

    constructor(id: Int, definition: String?) {
        this.id = id
        this.definition = definition
    }

    override fun toString(): String {
        return definition.toString()
    }
}