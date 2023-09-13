package com.cines.pueblo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cines.pueblo.exception.DAOException;
import com.cines.pueblo.exception.DomainException;
import com.cines.pueblo.model.Cine;
import com.cines.pueblo.model.Entrada;
import com.cines.pueblo.repository.ICine;
import com.cines.pueblo.service.interfaces.IServicio;
import com.cines.pueblo.service.util.Rutinas;

@Service
public class CineService implements IServicio<Cine, Long> {

	@Autowired
	private ICine cineRepository;

	@Override
	public boolean insert(Cine cine) {
		List<Entrada> list_entradas = cine.getCi_lista();
		if (Rutinas.isEmptyOrNull(list_entradas)) {		
			list_entradas = new ArrayList<Entrada>();
		}
		cine.setCi_lista(list_entradas);
		return cineRepository.save(cine) != null;
	}

	@Override
	public List<Cine> listAll() {
		return cineRepository.findAll();
	}

	@Override
	public boolean update(Cine cine) throws DomainException, DAOException {

		Optional<Cine> cineDBO = cineRepository.findById(cine.getId_cine());
		if (cineDBO.isEmpty()) {
			throw new DAOException("El registro ya no existe");
		}
		Cine cineDB = cineDBO.get();
		if (Objects.nonNull(cine.getCi_nombre()) && !"".equalsIgnoreCase(cine.getCi_nombre())) {
			cineDB.setCi_nombre(cine.getCi_nombre());
		}

		if (Objects.nonNull(cine.getCi_capacidad())) {
			cineDB.setCi_capacidad(cine.getCi_capacidad());
		}

		return cineRepository.save(cineDB) != null;
	}

	@Override
	public boolean deleteById(Long id_cine) {
		cineRepository.deleteById(id_cine);
		return true;

	}

	@Override
	public Optional<Cine> leerUno(Long id) {
		return cineRepository.findById(id);

	}

	public boolean addEntrada(Entrada entrada) throws DomainException, DAOException {

		Optional<Cine> cineDBO = cineRepository.findById(entrada.getEnt_cine().getId_cine());
		if (cineDBO.isEmpty()) {
			throw new DAOException("El registro ya no existe");
		}
		Cine cineDB = cineDBO.get();
		List<Entrada> list_entradas = cineDB.getCi_lista();
		if (Rutinas.isEmptyOrNull(list_entradas)) {		
			list_entradas = new ArrayList<Entrada>();
		}
		list_entradas.add(entrada);
		cineDB.setCi_lista(list_entradas);

		return cineRepository.save(cineDB) != null;
	}

}
