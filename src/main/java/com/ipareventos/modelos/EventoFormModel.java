package com.ipareventos.modelos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventoFormModel {

	@NotBlank(message = "El título es obligatorio")
	@Size(max = 50, message = "Máximo 50 caracteres")
	private String titulo;

	@Size(max = 500, message = "Máximo 500 caracteres")
	private String descripcion;

	@NotNull(message = "La fecha es obligatoria")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime fecha;

	@NotNull(message = "El aforo es obligatorio")
	@Min(value = 1, message = "El aforo debe ser al menos 1")
	private Integer aforo;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
	private BigDecimal precio;

	@NotNull
	private Set<@NotNull @Positive Long> categoriaIds = new HashSet<>();
}