package com.hector.spring.security.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.spring.security.entity.Rol;
import com.hector.spring.security.enums.RolNombre;

public interface IRolDao extends JpaRepository<Rol, Integer>{

	Optional<Rol> findByRolNombre(RolNombre rolNombre);
	
}
