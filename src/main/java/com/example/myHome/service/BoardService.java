package com.example.myHome.service;

import com.example.myHome.model.Board;
import com.example.myHome.model.User;
import com.example.myHome.repository.BoardRepository;
import com.example.myHome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board){
        User user = userRepository.findByUsername(username);
        board.setUser(user);

        return boardRepository.save(board);
    }

    // API
    public Boolean checkDeleteAuth(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        // boardId로 주인을 확인 해야 됨
        Board board = boardRepository.getById(id);

        return userDetails.getUsername().equals(board.getUser().getUsername());
    }
}
