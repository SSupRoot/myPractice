package com.example.myHome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // mappedBy = User 테이블에 설정된 컬럼 네임
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> user;
}
