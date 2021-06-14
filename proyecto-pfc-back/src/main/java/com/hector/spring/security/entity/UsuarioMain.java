package com.hector.spring.security.entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//implementa privilegios (roles) de cada usuario
public class UsuarioMain implements UserDetails{
	
	private String nombre;
	private String nombreUsuario;
	private String password;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	
	
	public UsuarioMain(String nombre, String nombreUsuario, String password, String email,
			Collection<? extends GrantedAuthority> authorities) {
		this.nombre = nombre;
		this.nombreUsuario = nombreUsuario;
		this.password = password;
		this.email = email;
		this.authorities = authorities;
	}
	
	//asignamos rol a cada usuario, convertimos un usuario de la bbdd en un usuario main para saber el rol (convierte el rol en authorities, en priviliegios)
	public static UsuarioMain build(Usuario usuario) {
		List<GrantedAuthority> authorities = usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol
				.getRolNombre().name())).collect(Collectors.toList());
		
		return new UsuarioMain(usuario.getNombre(), usuario.getNombreUsuario(), usuario.getPassword(), usuario.getEmail(), authorities);
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return nombreUsuario;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getNombre() {
		return nombre;
	}
	public String getEmail() {
		return email;
	}

}
