package com.example.myHome.service;

import com.example.myHome.model.Board;
import com.example.myHome.model.Role;
import com.example.myHome.model.User;
import com.example.myHome.repository.BoardRepository;
import com.example.myHome.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 외부 클래스 + public 이여야 @Transactional이 적용 된다
    // RuntimeExcpetion은 자동 롤백
    // 다른 Exception은 Rollbackfor 처리 해 ㅜ줘야 됨
//    @Transactional(propagation = Propagation.REQUIRED, isolation = , rollbackFor = {Exception.class})

//    public User save(User paramUser){
//        log.info("@@@@@@@@@@@@@@@@@ just save");
//        User user = this.insave(paramUser);
//        return user;
//    }
    @Transactional
    public User save(User user){

        log.info("@@@@@@@@@@@@@@@@@ in save");
        // 저장하는 로직 추가 필요 service를 따로 뺀이유
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        Role role = new Role();
        role.setId(Long.valueOf(1)) ;
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        try{
            if(true) {
                throw new RuntimeException("에러 발생");
            }
        } catch(RuntimeException e){
            log.info("RunTimeException 발생했는 데 뭐");
        }

        //throw new NullPointerException();

        // 사용자 가입인사글 자동 작성
        Board board = new Board();
        board.setTitle(savedUser.getUsername() + "인사 올립니다 ");
        board.setContent(savedUser.getUsername() + "열심히 하겠습니당 ㅎㅎ ");
        board.setUser(savedUser);
        boardRepository.save(board);

        // active Transaction?

        return savedUser;
    }
}
