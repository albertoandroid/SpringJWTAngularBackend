package com.example.curso.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.example.curso.contants.Contants;

public class JwtAuthentiacionTokenFilter extends AbstractAuthenticationProcessingFilter {

	protected JwtAuthentiacionTokenFilter() {
		super("/api/**");
		
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		String header = request.getHeader(Contants.AUTHORIZATION_HEADER);
		if(header == null || !header.startsWith(Contants.BEARER_TOKEN)){
			throw new RuntimeException("JWT es incorrecto o no ha llegado nada");
		}
		
		
		String authentacitonToken = header.substring(7);
		JwtAuthenticationToken token = new JwtAuthenticationToken(authentacitonToken);
		
		return getAuthenticationManager().authenticate(token);
	}

}
