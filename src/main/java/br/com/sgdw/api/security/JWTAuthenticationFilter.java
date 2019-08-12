package br.com.sgdw.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		Authentication authentication = TokenAuthenticationService
				.getAuthentication((HttpServletRequest) request);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		HttpServletResponse resp = (HttpServletResponse) response;

		resp.setHeader("Access-Control-Allow-Headers", "*");
		resp.setHeader("Access-Control-Expose-Headers", "*");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
		resp.setHeader("Access-Control-Max-Age", "3600");

		HttpServletRequest req = (HttpServletRequest) request;

		if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
			resp.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, resp);
		}		
	}

}
