package com.ipareventos.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record EventoResumenDto(Long id, String titulo, LocalDateTime fecha, BigDecimal precio,
		Set<CategoriaDto> categorias) {
}