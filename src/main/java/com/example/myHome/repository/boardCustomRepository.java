package com.example.myHome.repository;

import com.example.myHome.model.Board;

import java.util.List;

public interface boardCustomRepository {
    List<Board> findByTitleJpa(String title);

    List<Board> findByTitleJdbc(String title);
}
