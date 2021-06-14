package com.hector.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hector.spring.dao.IAnimalDao;
import com.hector.spring.dao.ITipoDao;
import com.hector.spring.entity.Animal;

@Service
public class AnimalServiceImpl implements IAnimalService{

	@Autowired //inyectar dependencias
	private IAnimalDao animalDao;
	
	
	@Override
	@Transactional(readOnly = true) // esta anotación suspende cualquier otra transacción e inicia una de sólo lectura
	public List<Animal> findAll() {
		// TODO Auto-generated method stub
		return (List<Animal>) animalDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Animal findById(Integer id) {
		// TODO Auto-generated method stub
		return animalDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Animal save(Animal animal) {
		// TODO Auto-generated method stub
		return animalDao.save(animal);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		animalDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Animal> findAll(Pageable pageable) {
		return animalDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Animal> findByTipo(Integer id_tipo, Pageable pageable) {
		return animalDao.findByTipo(id_tipo, pageable);
	}

}
