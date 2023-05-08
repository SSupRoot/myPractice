package com.example.myHome.repository;

import com.example.myHome.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


//
//    @Query("select u from User u join fetch u.boards")
//    @EntityGraph(attributePaths = {"boards"})
    List<User> findAll();

    User findByUsername(String username);
}
