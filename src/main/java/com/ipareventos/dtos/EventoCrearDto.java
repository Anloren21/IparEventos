package com.ipareventos.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EventoCrearDto(

		@NotBlank(message = "El título es obligatorio") @Size(max = 50, message = "El título admite hasta 50 caracteres") String titulo,

		@Size(max = 500, message = "La descripción admite hasta 500 caracteres") String descripcion,

		@NotNull(message = "La fecha es obligatoria") LocalDateTime fecha,

		@Min(value = 1, message = "El aforo debe ser al menos 1") int aforo,

		@NotNull(message = "El precio es obligatorio") @DecimalMin(value = "0.00", message = "El precio no puede ser negativo") BigDecimal precio,

		@NotNull(message = "La selección de categorías es obligatoria") Set<@NotNull @Positive Long> categoriaIds

) {
}