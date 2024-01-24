package com.school.sba.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.school.sba.repositary.UserRepositary;


@Service
public class CustomeUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepositary userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByUserName(username).map(user -> new CustomeUserDetails(user))
				          .orElseThrow(()-> new UsernameNotFoundException("User Not Found!! "));
	}

}
