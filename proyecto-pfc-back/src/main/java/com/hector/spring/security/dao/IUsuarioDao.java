package com.hector.spring.security.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hector.spring.security.entity.Usuario;

@Repository
public interface IUsuarioDao extends JpaRepository<Usuario, Integer>{
	
	Optional<Usuario> findByNombreUsuario(String nombreUsuario);
	
	boolean existsByNombreUsuario(String nombreUsuario);
	
	boolean existsByEmail(String email);

}
