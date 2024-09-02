package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.User;

@Repository
public interface HomeRepo extends JpaRepository<User, Integer> {

	@Query( value="select email from user",nativeQuery = true)
	List<String> findAllEmailId();
	
	User findByEmailAndPassword(String email,String password);
}
