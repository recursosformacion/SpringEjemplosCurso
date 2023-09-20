package com.cines.pueblo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cines.pueblo.model.Entrada;



public interface IEntrada extends JpaRepository<Entrada, Long>{

	List<Entrada> findByIdCliente(String idCliente);
}
