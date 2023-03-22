package com.godcoder.myrest.controller;

import com.godcoder.myrest.mapper.UserMapper;
import com.godcoder.myrest.model.Board;
import com.godcoder.myrest.model.QUser;
import com.godcoder.myrest.model.User;
import com.godcoder.myrest.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
class UserApiController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;

    // Aggregate root
    // tag::get-aggregate-root[]
    /*
     * querydsl 추가하면서 return type을 List<User> --> Iterable<User>로 변경함
     * repository.findAll(predicate)의 return type이 Iterable<T>임
     */
    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method
                 , @RequestParam(required = false) String text) {
        Iterable<User> users = null;
        /* in POSTMAN
         * GET: localhost:8100/api/users?method=query&text=tori
         * GET: localhost:8100/api/users?method=nativeQuery&text=ong
         * GET: localhost:8100/api/users?method=querydsl&text=ong
         * GET: localhost:8100/api/users?method=querydslCustom&text=ong
         * GET: localhost:8100/api/users?method=queryJdbc&text=ong
         * GET: localhost:8100/api/users?method=mybatis&text=ong
         */
        if("query".equals(method)) {
            users = repository.findByUsernameQuery(text);
        } else if("nativeQuery".equals(method)) {
            users = repository.findByUsernameNativeQuery(text);
        } else if("querydsl".equals(method)) {
            QUser user = QUser.user;
            /*
             * querydsl을 사용하면 일반적인 query에서 조건을 추가하는 것처럼
             * 비즈니스 로직에 따라 아래와 같이 조건들을 추가할 수 있음
             * BooleanExpression be = user.username.contains(text);
             * if(true) {
             *    be = be.and(user.username.eq("woo"));
             * }
             * Predicate predicate = user.username.contains(text);
             * users = repository.findAll(be);
             */
            Predicate predicate = user.username.contains(text);
            users = repository.findAll(predicate);
        } else if("querydslCustom".equals(method)) {
            users = repository.findByCustomUsername(text);
        } else if("queryJdbc".equals(method)) {
            users = repository.findByJdbcUsername(text);
        } else if("mybatis".equals(method)) {
            users = userMapper.getUsers(text);
        } else {
            users = repository.findAll();
        }
        return users;
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newuser(@RequestBody User newuser) {
        return repository.save(newuser);
    }
    // Single item
    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceuser(@RequestBody User newuser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    user.getBoards().clear();
                    user.getBoards().addAll(newuser.getBoards());
                    for(Board board : user.getBoards()) {
                        board.setUser(user);
                    }
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newuser.setId(id);
                    return repository.save(newuser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}