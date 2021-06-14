package com.hector.spring.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hector.spring.security.entity.UsuarioMain;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

//Genera el Token y comprueba que está bien
@Component
public class JwtProvider {
	
	private final static Logger LOG = LoggerFactory.getLogger(JwtProvider.class);
	
	private final String SECRET = "secret";
	private final int EXPIRATION = 36000;
	
	public String generateToken(Authentication auth) {
		UsuarioMain usuarioMain = (UsuarioMain) auth.getPrincipal();
		return Jwts.builder().setSubject(usuarioMain.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + EXPIRATION * 1000))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
	}
	
	public String getNombreUsuarioFromToken(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			return true;
		}catch(MalformedJwtException e) {
			LOG.error("Token mal formado");
		}catch(UnsupportedJwtException e) {
			LOG.error("Token no soportado");
		}catch(ExpiredJwtException e) {
			LOG.error("Token expirado");
		}catch(IllegalArgumentException e) {
			LOG.error("Token vacío");
		}catch(SignatureException e) {
			LOG.error("Fallo en la firma");
		}
		return false;
	}

}
