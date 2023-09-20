package com.cines.pueblo.validate;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.cines.pueblo.model.EntradaDTO;

@Component
public class EntradaDTOValidate  implements Validator{

	
	@Override
	public boolean supports(Class<?> clazz) {
		return EntradaDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "ent_cine", "Falta cine");
		ValidationUtils.rejectIfEmpty(errors, "ent_fila", "Falta fila");
		ValidationUtils.rejectIfEmpty(errors, "ent_numero", "Falta numero");
		EntradaDTO e = (EntradaDTO) target;
		if (e.getEnt_numero()<=0 || e.getEnt_numero()>100 )
			errors.rejectValue("ent_numero", "Valor de numero erroneo");
	}

}
