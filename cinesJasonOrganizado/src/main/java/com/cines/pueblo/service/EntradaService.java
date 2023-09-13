package com.cines.pueblo.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cines.pueblo.exception.DAOException;
import com.cines.pueblo.exception.DomainException;
import com.cines.pueblo.model.Cine;
import com.cines.pueblo.model.Entrada;
import com.cines.pueblo.repository.IEntrada;
import com.cines.pueblo.service.interfaces.IServicio;
import com.cines.pueblo.service.util.Rutinas;

@Service
public class EntradaService implements IServicio<Entrada, Long> {

	@Autowired
	private IEntrada entradaRepository;

	@Override
	public boolean insert(Entrada entrada) {
		return entradaRepository.save(entrada) != null;
	}
	@Override
	public List<Entrada> listAll() {
		return  entradaRepository.findAll();
	}
	
	public List<Entrada> entradaPorIdCliente(String id){
		return entradaRepository.findByIdCliente(id);
	}

	@Override
	public boolean update(Entrada entrada) throws DomainException,DAOException {

		Optional<Entrada> entradaDBO = entradaRepository.findById(entrada.getId_entrada());
		if (entradaDBO.isEmpty()) {
			throw new DAOException("El registro ya no existe");
		}
		Entrada entradaDB = entradaDBO.get();

		entradaDB.setIdCliente(Rutinas.nuevoSiNoVacio(entradaDB.getIdCliente(), entrada.getIdCliente()));
		entradaDB.setEnt_numero(Rutinas.nuevoSiNoVacio(entradaDB.getEnt_numero(), entrada.getEnt_numero()));
		//entradaDB.setEnt_fecha(Rutinas.nuevoSiNoVacio(entradaDB.getEnt_fecha(), entrada.getEnt_fecha()));
		entradaDB.setEnt_cine((Cine)Rutinas.nuevoSiNoVacio(entradaDB.getEnt_cine(), entrada.getEnt_cine()));

		return entradaRepository.save(entradaDB) != null;
	}

	@Override
	public boolean deleteById(Long id_entrada) {
		 entradaRepository.deleteById(id_entrada);
		 return true;

	}

	@Override
	public Optional<Entrada> leerUno(Long id) {
		return entradaRepository.findById(id);		
	}
	
	public List<Entrada> findByIdCliente(String id){
		return entradaRepository.findByIdCliente(id);
	}

}
