package com.ipareventos.dtos;

import java.time.LocalDateTime;

public record InscripcionDto(Long id, Long eventoId, String tituloEvento, LocalDateTime fechaEvento, int plazas,
		LocalDateTime fechaInscripcion) {
}