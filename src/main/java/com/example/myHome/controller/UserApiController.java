package com.example.myHome.controller;

import com.example.myHome.model.Board;
import com.example.myHome.model.User;
import com.example.myHome.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
class UserApiController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    List<User> all() {
        List<User> users = repository.findAll();
        log.debug("getBoards().size() be    fore @@@@@");
        users.get(0).getBoards().size();               // 이 시점에 Board 호출 됨
        log.debug("getBoards().size() : {} ", users.get(0).getBoards().size());
        log.debug("getBoards().size() after @@@@@");

        return users;
//        return repository.findAll();
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