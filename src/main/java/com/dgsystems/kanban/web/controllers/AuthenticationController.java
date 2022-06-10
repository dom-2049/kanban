package com.dgsystems.kanban.web.controllers;

import com.dgsystems.kanban.web.JwtRequest;
import com.dgsystems.kanban.web.JwtResponse;
import com.dgsystems.kanban.web.UserAccountDTO;
import com.dgsystems.kanban.web.security.JwtTokenUtil;
import com.dgsystems.kanban.web.security.JwtUserDetailsService;
import com.jcabi.aspects.LogExceptions;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5019") //TODO: Configure cross origin once development is done
public class AuthenticationController {
	public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, @Lazy JwtUserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
	}

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final JwtUserDetailsService userDetailsService;

	@LogExceptions
	@Loggable(skipArgs = true, skipResult = true)
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		//TODO: Use basic auth to protect this endpoint
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@Loggable(skipResult = true, skipArgs = true)
	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody UserAccountDTO user) {
		return ResponseEntity.ok(userDetailsService.save(user));
	}
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
		catch(BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
