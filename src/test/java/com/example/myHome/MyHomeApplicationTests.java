package com.example.myHome;

import com.example.myHome.model.Board;
import com.example.myHome.model.QUser;
import com.example.myHome.model.User;
import com.example.myHome.repository.BoardRepository;
import com.example.myHome.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

		QUser qUser = QUser.user;
		JPAQuery<?> query = new JPAQuery<>(em);
		List<User> users = query.select(qUser)
				.from(qUser)
				.where(qUser.username.contains("song"))
				.fetch();

		assertThat(users.size()).isEqualTo(2);

	}

	@Test
	void Test(){
		int result = 4+6;
		assertThat(result).isEqualTo(9);
	}

}
