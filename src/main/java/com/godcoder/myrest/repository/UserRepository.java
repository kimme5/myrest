package com.godcoder.myrest.repository;

import com.godcoder.myrest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, CustomizedUserRepository {

    @EntityGraph(attributePaths = {"boards"})
    List<User> findAll();

    User findByUsername(String username);

    @Query(value = "select u from User u where u.username like %?1%")
    List<User> findByUsernameQuery(String username);
    @Query(value = "select * from User u where u.username like %?1%", nativeQuery = true)
    List<User> findByUsernameNativeQuery(String username);
}
