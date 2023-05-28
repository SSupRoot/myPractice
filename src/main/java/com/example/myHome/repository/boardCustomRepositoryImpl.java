package com.example.myHome.repository;

import com.example.myHome.model.Board;
import com.example.myHome.model.QBoard;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class boardCustomRepositoryImpl implements boardCustomRepository{

    @PersistenceContext
    EntityManager em;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Board> findByTitleJpa(String title) {
        QBoard qBoard = QBoard.board;
        JPAQuery<Board> query = new JPAQuery<>(em);
        List<Board> boards = query.select(qBoard)
                .from(qBoard)
                .where(qBoard.title.contains(title)).fetch();

        return boards;
    }

    @Override
    public List<Board> findByTitleJdbc(String title) {
        String sql = "SELECT * FROM BOARD WHERE title like ?";
        Object[] args = new Object[]{"%" + title + "%"};

        List<Board> boards = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Board.class),
                args
                );

        return boards;
    }
}
