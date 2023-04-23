package com.example.myHome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private Boolean enabled;

    // Set으로 하기도 함 중복 없는 배열
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private List<Role> roles = new ArrayList<>();

    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)   // Board 에 있는 컬럼 user
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //@JsonIgnore // 이렇게 했을 때 Board 쪽 테이블을 조회하지 않음
    private List<Board> boards = new ArrayList<>();
}
