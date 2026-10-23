package com.ipareventos.modelos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class UsuarioFormModel {

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "El correo es obligatorio")
	@Email(message = "Introduce un correo válido")
	private String email;

	@NotBlank(message = "La contraseña es obligatoria")
	@ToString.Exclude
	private String password;
}