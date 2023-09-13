package com.cines.pueblo.exception;

@SuppressWarnings("serial")
public class ControllerException extends Exception {
	
	public ControllerException() {
	}

	public ControllerException(String mensaje) {
		super(mensaje);

	}
}
