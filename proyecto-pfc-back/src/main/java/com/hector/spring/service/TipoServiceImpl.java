package com.hector.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hector.spring.dao.ITipoDao;
import com.hector.spring.entity.Tipo;

@Service
public class TipoServiceImpl implements ITipoService{
	
	@Autowired
	private ITipoDao tipoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Tipo> findAll() {
		return tipoDao.findAll();
	}
	
	

}
