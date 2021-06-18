package com.example.demo.security;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacade {

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public Principal getPrincipal() {
		return (Principal) getAuthentication().getPrincipal();
	}
	
	public String getName() {
		return getAuthentication().getName();
	}
}
