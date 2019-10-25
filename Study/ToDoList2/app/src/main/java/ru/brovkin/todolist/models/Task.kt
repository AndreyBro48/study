package ru.brovkin.todolist.models

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.ToString

@Data
@AllArgsConstructor
@Builder
@ToString
class Task {
    private var id: Int? = null
    private var definition: String? = null
}