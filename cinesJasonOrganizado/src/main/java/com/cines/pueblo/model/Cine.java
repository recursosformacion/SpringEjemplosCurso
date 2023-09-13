package com.cines.pueblo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cines.pueblo.service.util.Rutinas;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Cine")
public class Cine {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id_cine;

	@Column(nullable = false, length = 50)
	private String ci_nombre;

	@Column(nullable = true, length = 100)
	private String ci_calle;

	@Column(nullable = true, length = 100)
	private String ci_barrio;

	@Column(nullable = false)
	private int ci_capacidad;
	
	@Transient
	private List<Long> ci_listaNumer;
	
	//JsonIgnore
	//JsonBackReference
	@JsonManagedReference
	@OneToMany(mappedBy = "ent_cine", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Entrada> ci_lista;

	
	

	public Cine() {
		super();
	}

	public Cine(long id_cine, String ci_nombre, int ci_capacidad, List<Entrada> ci_lista) {
		super();
		this.id_cine = id_cine;
		this.ci_nombre = ci_nombre;
		this.ci_capacidad = ci_capacidad;
		setCi_lista(ci_lista);
	}

	public long getId_cine() {
		return id_cine;
	}

	public void setId_cine(long id_cine) {
		this.id_cine = id_cine;
	}

	public String getCi_nombre() {
		return ci_nombre;
	}

	public void setCi_nombre(String ci_nombre) {
		this.ci_nombre = ci_nombre;
	}

	public int getCi_capacidad() {
		return ci_capacidad;
	}

	public void setCi_capacidad(int ci_capacidad) {
		this.ci_capacidad = ci_capacidad;
	}

	@Override
	public String toString() {
		return "Cine [id_cine=" + id_cine + ", ci_nombre=" + ci_nombre + ", ci_capacidad=" + ci_capacidad + "]";
	}
	
	public String toJson() throws JsonProcessingException {
		
		String com="\"";
		String salida="";
		salida+=com+"id_cine"+com+":"+id_cine+",";
		salida+=com+"ci_nombre"+com+":"+ci_nombre+",";
		salida+=com+"ci_calle"+com+":"+ci_calle+",";
		salida+=com+"ci_barrio"+com+":"+ci_barrio+",";
		salida+=com+"ci_capacidad"+com+":"+ci_capacidad;
		return salida;
	}

	public String getCi_calle() {
		return ci_calle;
	}

	public void setCi_calle(String ci_calle) {
		this.ci_calle = ci_calle;
	}

	public String getCi_barrio() {
		return ci_barrio;
	}

	public void setCi_barrio(String ci_barrio) {
		this.ci_barrio = ci_barrio;
	}

	public List<Entrada> getCi_lista() {
		return ci_lista;
	}

	public void setCi_lista(List<Entrada> ci_lista) {
		if (Rutinas.isEmptyOrNull(ci_lista)) {
			ci_lista = new ArrayList<Entrada>();
		}
		this.ci_lista = ci_lista;
	}
	
	public List<Long> ci_listaNumer(List<Entrada> ci_lista) {
		System.out.println("entrando1");
		if (Rutinas.isEmptyOrNull(ci_lista)) {
			ci_lista = new ArrayList<Entrada>();
		}
		System.out.println("entrando");
		return ci_lista.stream().map(e->e.getId_entrada()).collect(Collectors.toList());
	}
}
