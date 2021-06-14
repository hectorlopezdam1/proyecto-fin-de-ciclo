package com.hector.spring.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tipo")
public class Tipo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_tipo;
	
	private String nombre;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipo", cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value={"tipo","hibernateLazyInitializer", "handler"}, allowSetters=true)
	private List<Animal> animales;

	public Tipo(int id_tipo, String nombre, List<Animal> animales) {
		this.id_tipo = id_tipo;
		this.nombre = nombre;
		this.animales = animales;
	}
	
	public Tipo() {}

	public int getId_tipo() {
		return id_tipo;
	}

	public void setId_tipo(int id_tipo) {
		this.id_tipo = id_tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Animal> getAnimales() {
		return animales;
	}

	public void setAnimales(List<Animal> animales) {
		this.animales = animales;
	}
	
	

}
