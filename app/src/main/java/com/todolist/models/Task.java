package com.todolist.models;

import androidx.annotation.NonNull;

public class Task {
    private Integer id;
    private String definition;

    public Task(Integer id, String definition) {
        this.id = id;
        this.definition = definition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @NonNull
    @Override
    public String toString() {
        return definition;
    }
}
