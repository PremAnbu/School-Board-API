package com.school.sba.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.User;

public interface UserRepositary extends JpaRepository<User, Integer>{

	Optional<User> findByUserName(String userName);

}
