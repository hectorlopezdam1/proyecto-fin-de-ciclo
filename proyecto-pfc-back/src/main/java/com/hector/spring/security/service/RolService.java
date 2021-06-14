package com.hector.spring.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hector.spring.security.dao.IRolDao;
import com.hector.spring.security.entity.Rol;
import com.hector.spring.security.enums.RolNombre;

@Service
@Transactional
public class RolService {

	@Autowired
	IRolDao rolDao;
	
	public Optional<Rol> getByRolNombre(RolNombre rolNombre){
		return rolDao.findByRolNombre(rolNombre);
	}
	
	public void save(Rol rol) {
        rolDao.save(rol);
    }
	
}
