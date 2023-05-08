package com.example.myHome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "boards")
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
    @JsonIgnore // 이렇게 했을 때 Board 쪽 테이블을 조회하지 않음
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public User(long id, String username, String password, Boolean enabled, List<Role> roles, List<Board> boards) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.boards = boards;
    }
}
