package com.godcoder.myrest.repository;

import com.godcoder.myrest.model.QUser;
import com.godcoder.myrest.model.User;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Iterable<User> findByCustomUsername(String userName) {
        QUser qUser = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(em);
        List<User> users = query.select(qUser)
                .from(qUser)
                .where(qUser.username.contains(userName))
                .fetch();
        return users;
    }

    @Override
    public List<User> findByJdbcUsername(String userName) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM USER WHERE USERNAME LIKE ?"
                , new Object[]{"%" + userName + "%"}
                , new BeanPropertyRowMapper(User.class));
        return users;
    }
}
