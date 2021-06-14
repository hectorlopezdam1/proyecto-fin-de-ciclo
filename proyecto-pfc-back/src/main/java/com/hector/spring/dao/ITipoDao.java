package com.hector.spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hector.spring.entity.Tipo;


public interface ITipoDao  extends JpaRepository<Tipo, Integer>{

}
