package com.cines.pueblo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cines.pueblo.model.Cine;
import com.cines.pueblo.model.Programa;



public interface IPrograma extends JpaRepository<Programa, Long>{

}
