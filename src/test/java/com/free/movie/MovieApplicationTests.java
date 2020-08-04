package com.free.movie;

import com.free.movie.mapper.MovieMapper;
import com.free.movie.mapper.UserMapper;
import com.free.movie.model.Movie;
import com.free.movie.model.MovieExample;
import com.free.movie.model.User;
import com.free.movie.model.UserExample;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		UserMapper.class,
		MovieMapper.class,
})
@SpringBootTest(classes = MovieApplication.class)
class MovieApplicationTests {

	@Autowired
    UserMapper userMapper;
	@Autowired
    MovieMapper movieMapper;

	@Test
	void contextLoads() {
		List<User> users = userMapper.selectByExample(new UserExample());
		List<Movie> movies = movieMapper.selectByExample(new MovieExample());
		System.out.println(users);
		System.out.println(movies);
	}

}
