package com.example.myHome.repository;

import com.example.myHome.model.Board;
import com.example.myHome.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<User> ,boardCustomRepository{
    List<Board> findByTitle(String title);
    List<Board> findByTitleOrContent(String title, String content);

    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    // jpql
    @Query("select b from Board b where b.title like %:title%")
    List<Board> findByBoardTitlejpql(String title);

    //nativeQuery
    @Query(value = "select * from Board b where b.title like %:title%", nativeQuery = true)
    List<Board> findByBoardTitlenative(String title);

}
