package com.example.myHome;

import com.example.myHome.model.Board;
import com.example.myHome.model.User;
import com.example.myHome.repository.BoardRepository;
import com.example.myHome.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MyHomeApplicationTests {

	@Autowired
	UserRepository userRepository;
	@Autowired
	BoardRepository boardRepository;
	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {

		Board board1 = Board.builder()
				.title("테스트제목1")
				.content("내용1")
				.build();

		boardRepository.save(board1);
		List<Board> boardList = new ArrayList<>();
		boardList.add(board1);

		User user1 = User.builder()
				.boards(boardList)
				.username("테스트코드")
				.password("1234")
				.enabled(true)
				.build();

		userRepository.save(user1);

		em.flush();

	}

}
