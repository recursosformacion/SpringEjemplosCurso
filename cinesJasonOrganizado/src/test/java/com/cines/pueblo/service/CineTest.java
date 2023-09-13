package com.cines.pueblo.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.cines.pueblo.model.Cine;
import com.cines.pueblo.service.CineService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CineTest {

	@Autowired
	private CineService cDao;
	
	@Test
	public void testGuarda() {
		Cine cine = new Cine();
		cine.setId_cine(0l);
		cine.setCi_nombre("Las pruebas");
		boolean guardado = cDao.insert(cine);
		assertTrue(guardado);
	}
	
}
