package com.hector.spring.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "animal")
public class Animal implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //se genera el id automáticamente de forma autoincremental
	private Integer id;
	
	@Column(name = "nombre")
	@NotNull
	@Size(min = 1, max = 20)
	private String nombre;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo", foreignKey = @ForeignKey(name = "fk_tipo"))
	@JsonIgnoreProperties(value = {"animal","hibernateLazyInitializer", "handler"}, allowSetters = true)
	@NotNull
	private Tipo tipo;
	
	@Column(name = "edad")
	@Min(value=0)
	private int edad;
	
	@Column(name = "descripcion")
	@Size(min = 1, max = 1000)
	@NotNull
	private String descripcion;
	
	@Column(name = "foto")
	private String foto;
	
	
	public Animal() {
		
	}
	
	public Animal(Integer id, String nombre, Tipo tipo, int edad, String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.edad = edad;
		this.descripcion = descripcion;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
