package com.example.myHome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @Size(min=2, max=30, message = "ERRORERROR")
//    @Size(min=2, max=30)
    private String title;
    private String content;

    // user_id는 Board에 있는 테이블 컬럼
    // referencedColumnName="id"는 User 테이블에 참조하는 컬럼(생략 가능하다)
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;
}
