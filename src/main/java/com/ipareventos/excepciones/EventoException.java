package com.ipareventos.excepciones;

public class EventoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EventoException(String mensaje) {
		super(mensaje);
	}
}