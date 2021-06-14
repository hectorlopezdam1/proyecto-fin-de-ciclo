package com.hector.spring.security.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hector.spring.dto.Mensaje;
import com.hector.spring.security.dto.JwtDto;
import com.hector.spring.security.dto.LoginUsuario;
import com.hector.spring.security.dto.NuevoUsuario;
import com.hector.spring.security.entity.Rol;
import com.hector.spring.security.entity.Usuario;
import com.hector.spring.security.enums.RolNombre;
import com.hector.spring.security.jwt.JwtProvider;
import com.hector.spring.security.service.RolService;
import com.hector.spring.security.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RolService rolService;
	
	@Autowired
	JwtProvider jwtProvider;
	
	
	@PostMapping("/nuevo")
	public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){ //requestbody para json y convertir en clase java, bindingResult para validar
		System.err.println("error");
		if(bindingResult.hasErrors()) {
			return new ResponseEntity(new Mensaje("Error en los campos o email inválido"), HttpStatus.BAD_REQUEST);
		}
		if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())) {
			return new ResponseEntity(new Mensaje("Ese nombre de usuario ya está en uso."), HttpStatus.BAD_REQUEST);
		}
		if(usuarioService.existsByEmail(nuevoUsuario.getEmail())) {
			return new ResponseEntity(new Mensaje("Ese email ya está en uso"), HttpStatus.BAD_REQUEST);
		}
		Usuario usuario = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(),
				passwordEncoder.encode(nuevoUsuario.getPassword()), nuevoUsuario.getEmail());
		Set<Rol> roles = new HashSet<>();
		roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
		if(nuevoUsuario.getRoles().contains("admin")) {
			roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
		}
		System.err.println(nuevoUsuario.getNombre() + " ; " + nuevoUsuario.getNombreUsuario() + " ; " + nuevoUsuario.getPassword() + " ; " + nuevoUsuario.getEmail());
		System.err.println(bindingResult.hasErrors());
		System.err.println("error1");
		usuario.setRoles(roles);
		System.err.println("error2");
		usuarioService.save(usuario);
		System.err.println("error3");
		return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			return new ResponseEntity(new Mensaje("Error en los campos"), HttpStatus.BAD_REQUEST);
		}
		Authentication auth = authManager.authenticate
				(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = jwtProvider.generateToken(auth);
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		return new ResponseEntity(jwtDto, HttpStatus.OK);
	}
	
}
