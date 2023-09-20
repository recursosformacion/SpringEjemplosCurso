package com.cines.pueblo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cines.pueblo.model.Cine;
import com.cines.pueblo.model.Pelicula;



public interface IPelicula extends JpaRepository<Pelicula, Long>{

}
