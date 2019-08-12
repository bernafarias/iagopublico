package br.com.sgdw.api.security;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	static final long EXPIRATION_TIME = 860_000_000;
	static final String SECRET = "f5e7d0fd207dcb0249f46b4196bd1151";
	static final String TOKEN_PREFIX = "AUTH-IAGO";
	static final String HEADER_STRING = "Authorization";
	
	private TokenAuthenticationService(){
		
	}
	
	static void addAuthentication(HttpServletResponse response, String username){
		String jwt = Jwts.builder()
						.setSubject(username)
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS512, SECRET)
						.compact();
		
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
	}
	
	static Authentication getAuthentication(HttpServletRequest request){
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null){
			String user = Jwts.parser()
							.setSigningKey(SECRET)
							.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
							.getBody()
							.getSubject();
			
			if(user != null){
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}
		return null;
	}
}
