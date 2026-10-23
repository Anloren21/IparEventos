package com.ipareventos.excepciones;

public class EmailDuplicadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailDuplicadoException() {
		super("Ya existe un usuario con ese correo electrónico");
	}
}