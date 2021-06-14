package com.hector.spring.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hector.spring.dto.Mensaje;
import com.hector.spring.entity.Animal;
import com.hector.spring.entity.Tipo;
import com.hector.spring.service.IAnimalService;
import com.hector.spring.service.ITipoService;

@CrossOrigin(origins = { "http://localhost:4200" }) // conexion con el frontend
@RestController
@RequestMapping("/app") // generar url
public class AnimalRestController {
	
	private final Logger LOG = LoggerFactory.getLogger(AnimalRestController.class);

	@Autowired
	private IAnimalService animalService; // la llamada a esta interfaz busca la primera clase que la implementa ->
											// AnimalServiceImpl
	@Autowired
	private ITipoService tipoService;

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/animals")
	public List<Animal> index() {
		return animalService.findAll(); // el controlador llama al servicio y este al dao que a su vez es un repositorio
										// con los metodos CRUD de JPA
		
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/animals/tipos")
	public List<Tipo> indexTipos() {
		return tipoService.findAll();
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/animals/page/{page}")
	public Page<Animal> indexPage(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 8);
		return animalService.findAll(pageable);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/animals/{id_tipo}/page/{page}")
	public Page<Animal> indexByTipoPage(@PathVariable Integer page, @PathVariable Integer id_tipo) {
		Pageable pageable = PageRequest.of(page, 8);
		return animalService.findByTipo(id_tipo, pageable);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/animals/{id}")
	public ResponseEntity<?> show(@PathVariable Integer id) {
		
		Map<String, Object> response = new HashMap<>();
		Animal animal = null;
		
		try {
			animal = animalService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		
		if(animal == null) {
			response.put("mensaje", "El animal con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND); 
		}
		
		return new ResponseEntity<Animal>(animal, HttpStatus.OK); 
	}

	
	@PostMapping("/animals")
	@ResponseStatus(HttpStatus.CREATED) //201
	public ResponseEntity<?> create(@RequestBody Animal animal, BindingResult result) { // requestbody -> objeto json
		Animal newAnimal = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
				.stream()
				.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
				.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			newAnimal = animalService.save(animal);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El animal ha sido creado con éxito");
		response.put("animal", newAnimal);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/animals/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@Valid @RequestBody Animal animal, BindingResult result, @PathVariable Integer id) {
		Animal animalActual = animalService.findById(id);
		Animal animalUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
				.stream()
				.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
				.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(animalActual == null) {
			response.put("mensaje", new Mensaje("Error: no se pudo editar, el animal con el ID: ".concat(id.toString().concat(" no existe en la base de datos."))));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			animalActual.setNombre(animal.getNombre());
			animalActual.setTipo(animal.getTipo());
			animalActual.setEdad(animal.getEdad());
			animalActual.setDescripcion(animal.getDescripcion());
			
			animalUpdated = animalService.save(animalActual);
			
		}catch(DataAccessException e) {
			response.put("mensaje", new Mensaje("Error al actualizar el animal en la base de datos."));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", new Mensaje("El animal ha sido actualizado con éxito."));
		response.put("animal", animalUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED); 
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/animals/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) //204
	public void delete(@PathVariable Integer id) {
		Animal animal = animalService.findById(id);
		String previousFileName = animal.getFoto();
		if(previousFileName != null && previousFileName.length() > 0) { //si el animal ya tiene una foto anterior
			Path previousPath = Paths.get("uploads").resolve(previousFileName).toAbsolutePath(); //coge la ruta
			File previousFile = previousPath.toFile(); //lo convierte a un File
			if(previousFile.exists() && previousFile.canRead()) { //si existe
				previousFile.delete(); //lo elimina
			}
		}
		animalService.delete(id);
	}
	
	
	@PostMapping("/animals/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile file, @RequestParam("id") Integer id) {
		Map<String, Object> response = new HashMap<>();
		
		Animal animal = animalService.findById(id);
		
		if(!file.isEmpty()) {
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", ""); //UUID genera un identificador aleatorio para que no se repita el nombre de la imagen al subir; replace quita los espacios en blanco
			Path path = Paths.get("uploads").resolve(fileName).toAbsolutePath(); //ruta completa de la imagen
			LOG.info(path.toString());
			try {
				Files.copy(file.getInputStream(), path);
			} catch (IOException e) {
				response.put("mensaje", new Mensaje("Error al subir la imagen: " + fileName));
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String previousFileName = animal.getFoto();
			if(previousFileName != null && previousFileName.length() > 0) { //si el animal ya tiene una foto anterior
				Path previousPath = Paths.get("uploads").resolve(previousFileName).toAbsolutePath(); //coge la ruta
				File previousFile = previousPath.toFile(); //lo convierte a un File
				if(previousFile.exists() && previousFile.canRead()) { //si existe
					previousFile.delete(); //lo elimina
				}
			}
			animal.setFoto(fileName);
			animalService.save(animal);
			response.put("animal", animal);
			response.put("mensaje", new Mensaje("Has subido correctamente la imagen: " + fileName));
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/uploads/img/{imgName:.+}")
	public ResponseEntity<Resource> seeImage(@PathVariable String imgName) {
		Path path = Paths.get("uploads").resolve(imgName).toAbsolutePath();
		Resource resource = null;
		LOG.info(path.toString());
		System.err.println("e");
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			System.err.println("e1");
			e.printStackTrace();
		}
		
		if(!resource.exists() && resource.isReadable()) {
			System.err.println("e2");
			throw new RuntimeException("Error, no se pudo cargar la imagen: " + imgName);
		}
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		System.err.println("e3");
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

}
