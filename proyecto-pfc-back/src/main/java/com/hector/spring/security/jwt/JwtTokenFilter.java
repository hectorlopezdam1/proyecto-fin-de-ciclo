package com.hector.spring.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

//Se ejecuta por cada peticion y copmprueba que el token es valido gracias al provider, si es valido permite el acceso y si no no.
public class JwtTokenFilter extends OncePerRequestFilter{
	
	private final static Logger LOG = LoggerFactory.getLogger(JwtTokenFilter.class);
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) //utiliza la clase provider para comprobar que el token es válido y proporcionar acceso al recurso
			throws ServletException, IOException {
		try {
			String token = getToken(req); //obtiene el token del header
			if(token != null && jwtProvider.validateToken(token)) {//lo valida el provider
				String nombreUsuario = jwtProvider.getNombreUsuarioFromToken(token);//obtiene el nombre de us
				UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);//carga el usuario
				UsernamePasswordAuthenticationToken auth =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}catch(Exception e) {
			LOG.error("Error en el método doFilterInternal()");
		}
		filterChain.doFilter(req, res);
	}
	
	private String getToken(HttpServletRequest req) {
		String header = req.getHeader("Authorization");
		if(header != null && header.startsWith("Bearer")) {
			return header.replace("Bearer", "");
		}
		return null;
	}

}
