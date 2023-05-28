package com.example.myHome.controller;
import java.util.List;

import com.example.myHome.model.Board;
import com.example.myHome.model.QBoard;
import com.example.myHome.repository.BoardRepository;
import com.example.myHome.service.BoardService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@RestController
@RequestMapping("/api")
@Slf4j
class BoardApiController {

    @Autowired
    private BoardRepository repository;

    @GetMapping("/boards")
    List<Board> all(@RequestParam(required = false) String title,
                    @RequestParam(required = false, defaultValue = "") String content,
                    @RequestParam(required = false) String method,
                    @RequestParam(required = false) String queryText) {

        List<Board> boards = null;
        // jpql, nativeQuery, querydsl 테스트
        if("jpql".equals(method)){
            boards = repository.findByBoardTitlejpql(queryText);
        } else if("native".equals(method)){
            boards = repository.findByBoardTitlenative(queryText);
        } else if("jdbctemplate".equals(method)){
            boards = repository.findByTitleJdbc(queryText);
        } else if("jpaquery".equals(method)){
            boards = repository.findByTitleJpa(queryText);
            log.debug("1111111");
        }

        // title이 없을 경우 전체 검색 / 있을 경우 title로 검색
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content) ) {
//            boards = repository.findAll();
            return repository.findAll();
        }  else {
            boards = repository.findByTitleOrContent(title, content);
//            return repository.findByTitleOrContent(title, content);
        }

        return boards;
    }

    @PostMapping("/boards")
    Board newBoard(@RequestBody Board newBoard) {
        return repository.save(newBoard);
    }

    // Single item

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

        return repository.findById(id)
                .map(Board -> {
                    Board.setTitle(newBoard.getTitle());
                    Board.setContent(newBoard.getContent());
                    return repository.save(Board);
                })
                .orElseGet(() -> {
                    newBoard.setId(id);
                    return repository.save(newBoard);
                });
    }


//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/boards/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') and @boardService.checkDeleteAuth(#id)")
    void deleteBoard(@PathVariable Long id) {
        System.out.println("@@@@@@@@@2" + id);
        repository.deleteById(id);
    }
}