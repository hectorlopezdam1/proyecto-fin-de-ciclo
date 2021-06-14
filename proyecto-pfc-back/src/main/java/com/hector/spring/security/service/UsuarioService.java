package com.hector.spring.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hector.spring.security.dao.IUsuarioDao;
import com.hector.spring.security.entity.Usuario;

@Service
@Transactional //para evitar incoerencias, si falla una operación se hace un rolback, y se vuelve al estado anterior
public class UsuarioService {

	@Autowired
	IUsuarioDao usuarioDao;
	
	public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
		return usuarioDao.findByNombreUsuario(nombreUsuario);
	}
	
	public boolean existsByNombreUsuario(String nombreUsuario) {
		return usuarioDao.existsByNombreUsuario(nombreUsuario);
	}
	
	public boolean existsByEmail(String email) {
		return usuarioDao.existsByEmail(email);
	}
	
	public void save(Usuario usuario) {
		usuarioDao.save(usuario);
	}
	
}
