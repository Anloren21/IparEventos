package com.ipareventos.modelos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InscripcionFormModel {

	@NotNull(message = "Indica el número de plazas")
	@Min(value = 1, message = "Debes reservar al menos una plaza")
	@Max(value = 4, message = "Puedes reservar hasta cuatro plazas")
	private Integer plazas = 1;
}
