package com.cines.pueblo.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cines.pueblo.exception.ControllerException;
import com.cines.pueblo.exception.DAOException;
import com.cines.pueblo.exception.DomainException;
import com.cines.pueblo.model.Cine;
import com.cines.pueblo.model.Entrada;
import com.cines.pueblo.service.CineService;

@CrossOrigin
@RestController
@RequestMapping("/api/cine")
public class CineController {

	@Autowired
	private CineService cDao;

	public static Link createLink(Cine c) throws ControllerException {
		return CineController.createLink( c, "self");
	}
	public static Link createLink(Cine c, String target) throws ControllerException {

		Link linkSelf;
		linkSelf = linkTo(methodOn(CineController.class).leerUno(c.getId_cine())).withRel(target);
		return linkSelf;
	}

	private static EntityModel<Cine> createCineResource(Cine c) {

		EntityModel<Cine> entityModel = EntityModel.of(c);
		try {
			entityModel.add(CineController.createLink(c));
			List<Long> lista = c.getCi_lista();
			for (int a=0;a<lista.size();a++) {
				Entrada e = new Entrada();
				e.setId_entrada(lista.get(a));
				try {
					entityModel.add(EntradaController.createLink(e,"entradas"));
				} catch (ControllerException e1) {
					e1.printStackTrace();
				}
			}

		} catch (ControllerException e) {
			e.printStackTrace();
		} finally {
		}
		return entityModel;
	}

	@GetMapping("/{id}")
	public EntityModel<Cine> leerUno(@PathVariable("id") Long id) throws ControllerException {
		Cine cine = cDao.leerUno(id).orElseThrow(() -> new ControllerException("Cine no existe"));

		return CineController.createCineResource(cine);

	}

	@GetMapping({ "", "/" })
	public CollectionModel<EntityModel<Cine>> leerTodos() throws ControllerException {
		List<Cine> cines = cDao.listAll();
		List<EntityModel<Cine>> cinesE = cines.stream().map(cine -> CineController.createCineResource(cine))
				.collect(Collectors.toList());

		return CollectionModel.of(cinesE, linkTo(methodOn(CineController.class)).withSelfRel());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> alta(@RequestBody Cine c) throws DomainException, ControllerException { // ID,NOMBRE,DESCRIPCION

//		throw new DomainException("Mensaje de pruebas");
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		c.setId_cine(0);
		c = cDao.insert(c);
		if (c != null) {
			System.out.println("En alta-" + c.toString());
			map.put("status", 1);
			map.put("message", "Registro salvado");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			throw new ControllerException("Error al hacer la insercion");
		}
	}

	@PutMapping
	public ResponseEntity<Map<String, Object>> modificacion(@RequestBody Cine c)
			throws ControllerException, DomainException, DAOException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (cDao.update(c)) {
			map.put("status", 1);
			map.put("message", "Error al actualizar");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			throw new ControllerException("Error al hacer la modificacion");

		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(@PathVariable("id") String ids) throws ControllerException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (ids != null) {
			try {
				long id = Long.parseLong(ids);
				Optional<Cine> cineDB = cDao.leerUno(id);
				cDao.deleteById(cineDB.get().getId_cine());
				map.put("status", 1);
				map.put("message", "Registro borrado");
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			} catch (Exception ex) {
				throw new ControllerException("Error al borrar");

			}
		}
		throw new ControllerException("No existe registro al borrar");
	}

	@GetMapping("/error")
	public ResponseEntity<Map<String, Object>> error() throws DomainException, ControllerException {

		throw new DomainException("Mensaje de pruebas");
	}

}
