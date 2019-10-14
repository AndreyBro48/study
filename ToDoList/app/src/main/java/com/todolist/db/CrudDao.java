package com.todolist.db;

import java.util.List;

public interface CrudDao<T> {
    T find(Integer id);
    void save(T model);
    void update(T model);
    void delete(T model);
    List<T> findAll();
}
