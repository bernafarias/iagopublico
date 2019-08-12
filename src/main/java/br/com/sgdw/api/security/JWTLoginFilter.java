package br.com.sgdw.api.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{

	protected JWTLoginFilter(String url, AuthenticationManager authManager){
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		AccountCredentials credentials = new ObjectMapper()
											.readValue(request.getInputStream(), AccountCredentials.class);
		
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Expose-Headers", "*");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
		response.setHeader("Access-Control-Max-Age", "3600");
		
		return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							credentials.getUsername(),
							credentials.getPassword(),
							Collections.emptyList()));
	}
	
	@Override
	protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterchain,
			Authentication auth) throws IOException, ServletException{
		TokenAuthenticationService.addAuthentication(response, auth.getName());
	}

}
