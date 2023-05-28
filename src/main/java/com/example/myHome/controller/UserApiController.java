package com.example.myHome.controller;

import com.example.myHome.mapper.UserMapper;
import com.example.myHome.model.Board;
import com.example.myHome.model.QUser;
import com.example.myHome.model.User;
import com.example.myHome.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api")
@Slf4j
class UserApiController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/users")
    List<User> all(@RequestParam(required = false) String method, @RequestParam(required = false) String text) {
        List<User> users = null;
        if("query".equals(method)){
            users = repository.findByUsernameQuery(text);
        } else if("nativequery".equals(method)) {
            users = repository.findByUsernameNativeQuery(text);
        } else if ("querydsl".equals(method)) {
            QUser user = QUser.user;

            BooleanExpression b = user.username.contains(text);
//            if(true){
//                b = b.and(user.username.eq("HI"));
//            }

            users = (List<User>) repository.findAll(b);

        } else if("querydslCustom".equals(method)) {
            users = repository.findByUsernameCustom(text);
        } else if("queryjdslJdbc".equals(method)) {
            users = repository.findByUsernameJdbc(text);
        } else if("mybatis".equals(method)) {
            users = userMapper.getUsers(text);
        }else {
            users = repository.findAll();
        }

        return users;
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newU, @PathVariable Long id) {

        return repository.findById(id)
                .map(u -> {
//                    User.setTitle(newUser.getTitle());
//                    User.setContent(newUser.getContent());
                    // 근데 이거 덮어지지가 않네?
                    //user.setBoards(newUser.getBoards());
                    u.getBoards().clear();
                    u.getBoards().addAll(newU.getBoards());
                    //u.setUsername(newUser.getUsername());
                    for(Board board : u.getBoards()){
                        board.setUser(u);
                    }
                    return repository.save(u);
                })
                .orElseGet(() -> {
                    newU.setId(id);
                    return repository.save(newU);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/users/boards/{id}")
    void deleteUserBoard(@PathVariable Long id) {
        repository.findById(id).map(user -> {

            user.getBoards().remove(1);
           return repository.save(user);
        }).orElse(null);
    }
}