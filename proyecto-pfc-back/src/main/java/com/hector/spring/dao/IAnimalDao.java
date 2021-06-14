package com.hector.spring.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hector.spring.entity.Animal;

public interface IAnimalDao extends JpaRepository<Animal, Integer>{ //esta interfaz proporciona los métodos de un crud
	
	@Query("select a from Animal a left join a.tipo t where t.id_tipo = :id_tipo")
	public Page<Animal> findByTipo(@Param("id_tipo") Integer id_tipo, Pageable pageable);

}
