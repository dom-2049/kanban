package com.dgsystems.kanban.infrastructure.security;

import java.util.ArrayList;

import com.dgsystems.kanban.infrastructure.persistence.UserRepository;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.UserAccount;
import com.dgsystems.kanban.web.UserAccountDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	public JwtUserDetailsService(UserRepository userRepository, PasswordEncoder bcryptEncoder) {
		this.userRepository = userRepository;
		this.bcryptEncoder = bcryptEncoder;
	}

	private final UserRepository userRepository;
	private final PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount user = userRepository.findByUsername(username).orElse(null);
		if(user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}
	
	public UserAccount save(UserAccountDTO user) {
		UserAccount newUser = new UserAccount();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userRepository.save(newUser);
	}
}
