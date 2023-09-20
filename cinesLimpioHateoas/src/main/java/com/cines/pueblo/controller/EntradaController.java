package com.cines.pueblo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import com.cines.pueblo.model.EntradaDTO;
import com.cines.pueblo.service.CineService;
import com.cines.pueblo.service.EntradaService;

@CrossOrigin
@RestController
@RequestMapping("/api/entrada")
public class EntradaController {

	@Autowired
	private EntradaService cDao;

	@Autowired
	private CineService cDaoCine;

	public static Link createLink(Entrada c) throws ControllerException {
		return EntradaController.createLink(c, "self");
	}

	public static Link createLink(Entrada c, String entrada) throws ControllerException {
		Link linkSelf;
		linkSelf = WebMvcLinkBuilder.linkTo(methodOn(EntradaController.class).leerUno(c.getId_entrada()))
				.withRel(entrada);
		return linkSelf;
	}

	public static EntityModel<Entrada> createEntradaResource(Entrada c) {

		EntityModel<Entrada> entityModel = EntityModel.of(c);
		try {
			entityModel.add(EntradaController.createLink(c));
			Cine cine = new Cine();
			cine.setId_cine(c.getEnt_cine());
			entityModel.add(CineController.createLink(cine,"ent_cine"));
		} catch (ControllerException e) {
			e.printStackTrace();
		} finally {
		}
		return entityModel;
	}

	@GetMapping("/{id}")
	public EntityModel<Entrada> leerUno(@PathVariable("id") Long id) throws ControllerException {
		String mensaje = "";
		if (id != null) {
			try {

				Optional<Entrada> entradaDB = (Optional<Entrada>) cDao.leerUno(id);

				if (entradaDB.isPresent()) {
					return EntradaController.createEntradaResource(entradaDB.get());
				} else {
					mensaje = "No existen datos";
				}
			} catch (NumberFormatException nfe) {
				mensaje = "Formato erroneo";
			}
		} else {
			mensaje = "Formato erroneo";
		}
		throw new ControllerException(mensaje);

	}

	@GetMapping({ "", "/" })
	public CollectionModel<EntityModel<Entrada>> leerTodos() throws ControllerException {

		List<Entrada> entradas = cDao.listAll();
		if (!entradas.isEmpty()) {
			List<EntityModel<Entrada>> entradasE = entradas.stream()
					.map(entrada -> EntradaController.createEntradaResource(entrada))
					.collect(Collectors.toList());
			return CollectionModel.of(entradasE, 
					linkTo(methodOn(EntradaController.class)).withSelfRel());
		} else {
			throw new ControllerException("No existen datos");
		}
	}

	@GetMapping("/leerporid/{idCliente}")
	public ResponseEntity<List<Entrada>> leerPorId(@PathVariable("idCliente") String id) throws ControllerException {

		List<Entrada> entradas = cDao.findByIdCliente(id);
		if (!entradas.isEmpty()) {
			return ResponseEntity.ok(entradas);
		} else {
			throw new ControllerException("No existen datos");

		}
	}

	@PostMapping
	public ResponseEntity<Entrada> alta(@RequestBody EntradaDTO c)
			throws DomainException, ControllerException, DAOException { // ID,NOMBRE,DESCRIPCION

//		throw new DomainException("Mensaje de pruebas");
		Entrada e = convertirDTO(c);
		e.setId_entrada(0l);

		System.out.println("En alta-" + e.toString());
		e = cDao.insert(e);
		if (e != null) {
			System.out.println("En alta dada-" + e.getId_entrada() + "/" + e.toString());
			cDaoCine.addEntrada(e);
			// throw new DomainException("Mensaje de pruebas");
			return ResponseEntity.ok(e);
		} else {
			throw new ControllerException("Error al hacer la insercion");
		}
	}

	@PutMapping
	public ResponseEntity<Map<String, Object>> modificacion(@RequestBody EntradaDTO c)
			throws ControllerException, DomainException, DAOException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Entrada e = convertirDTO(c);
		if (cDao.update(e)) {
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
				Optional<Entrada> entradaDB = cDao.leerUno(id);
				cDao.deleteById(entradaDB.get().getId_entrada());
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

	public Entrada convertirDTO(EntradaDTO d) throws ControllerException {

		Entrada e = new Entrada();
		if (Objects.isNull(d.getId_entrada())) {
			d.setId_entrada(0L);
		}
		e.setId_entrada(d.getId_entrada());
		e.setEnt_fila(d.getEnt_fila());
		e.setEnt_numero(d.getEnt_numero());
		e.setEnt_fecha_str(d.getEnt_fecha());
		e.setIdCliente(d.getIdCliente());
		Optional<Cine> cineDB = (Optional<Cine>) cDaoCine.leerUno(d.getId_cine());
		if (cineDB.isPresent()) {
			e.setEnt_cine(cineDB.get().getId_cine());
		} else {
			throw new ControllerException("Cine no existe - " + d.getId_cine());
		}
		return e;
	}

}
