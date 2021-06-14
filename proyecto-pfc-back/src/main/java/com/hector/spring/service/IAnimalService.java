package com.hector.spring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hector.spring.entity.Animal;

public interface IAnimalService {
	
	public List<Animal> findAll();
	
	public Page<Animal> findAll(Pageable pageable);
	
	public Animal findById(Integer id);
	
	public Animal save(Animal animal);
	
	public void delete(Integer id);
	
	public Page<Animal> findByTipo(Integer id_tipo, Pageable pageable);

}
