package com.example.myHome.controller;

import com.example.myHome.model.Board;
import com.example.myHome.repository.BoardRepository;
import com.example.myHome.service.BoardService;
import com.example.myHome.validator.BoardValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable, @RequestParam(required = false,defaultValue ="") String searchText) {
//        List<Board> boards = boardRepository.findAll();
        // pageable 을 받음
        // ex) ghttp://localhost:8080/board/list?page=1&size=2
//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);

        int startPage = Math.max(1, boards.getPageable().getPageNumber()-4);       // 현재 페이지 넘버를 가져 옴
        int endPage = Math.min(boards.getTotalElements() == 0 ? 1 :boards.getTotalPages(), boards.getPageable().getPageNumber()+4);


        model.addAttribute("boards", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "board/list";
    }


    // Get으로 호출 했을 때 Board 객체를 생성해서 전달 한 다음
    // Post요청 할 때 객체 받아오기?
    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if(id == null) {
            model.addAttribute("board", new Board());
        }
        else {
            // orElse 는 없을 경우 처리
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }

        return "board/form";
    }

    @PostMapping("/form")
    public String postform(@Valid Board board, BindingResult bindingResult, Authentication authentication){
        boardValidator.validate(board, bindingResult);

        if(bindingResult.hasErrors()){
            // 이거를 호출하면은 위에 @GetMapping("/form") 을 통해 호출 하게 됨
            return "board/form";
        }

        // 가져오는 방법은 많다고 합니다
        //Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // 키 값이 있을 때는 update 없을 때는 insert가 됨
        boardService.save(username,board);
//        boardRepository.save(board);
        // 위에 정의한 board/list 쪽으로 redirect 하는 곳을 한번 타서 게시판 데이터 값을 가져 감
        return "redirect:/board/list";
    }
}
