package com.hector.spring.security.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;



@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(name= "uk_usuario_username", columnNames = {"nombreUsuario"}))
public class Usuario {
	//autoincremental
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	private String nombre;
	
	@NotBlank
	@Column(length = 20)
	private String nombreUsuario;
	
	@NotBlank
	private String password;
	
	@NotBlank
	@Email(message = "No es una direccion de email")
	private String email;
	
	//un usuario varios roles y un role para varios usuarios
	@NotNull
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id"), //usuario_id en la tabla 'usuario_rol' references id en la tabla usuario
	inverseJoinColumns = @JoinColumn(name = "rol_id")) //rol_id en la tabla 'usuario_rol' references id en la tabla rol
	private Set<Rol> roles = new HashSet<>();
	
	
	public Usuario(String nombre, String nombreUsuario, String password, String email) {
		this.nombre = nombre;
		this.nombreUsuario = nombreUsuario;
		this.password = password;
		this.email = email;
	}

	public Usuario() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}
	
	
	

}
