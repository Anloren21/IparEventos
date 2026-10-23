package com.ipareventos.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRegistroDto(

		@NotBlank(message = "El nombre es obligatorio") String nombre,

		@NotBlank(message = "El correo es obligatorio") @Email(message = "Introduce un correo válido") String email,

		@NotBlank(message = "La contraseña es obligatoria") String password

) {
	@Override
	public String toString() {
		return "UsuarioRegistroDto[datos de registro omitidos]";
	}
}