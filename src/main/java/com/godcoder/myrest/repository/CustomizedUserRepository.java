package com.godcoder.myrest.repository;

import com.godcoder.myrest.model.User;

import java.util.List;

public interface CustomizedUserRepository {
    Iterable<User> findByCustomUsername(String userName);

    List<User> findByJdbcUsername(String userName);
}
