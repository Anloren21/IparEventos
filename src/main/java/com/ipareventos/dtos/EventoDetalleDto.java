package com.ipareventos.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record EventoDetalleDto(Long id, String titulo, String descripcion, LocalDateTime fecha, int aforo,
		BigDecimal precio, Set<CategoriaDto> categorias, int plazasDisponibles) {
}