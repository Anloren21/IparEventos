package com.ipareventos.controladores.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ipareventos.dtos.EventoDetalleDto;
import com.ipareventos.dtos.EventoResumenDto;
import com.ipareventos.servicios.AnonimoService;

import org.springframework.http.ProblemDetail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.media.ArraySchema;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/eventos")
@Tag(name = "Eventos", description = "Consulta de eventos")
public class EventoRestController {

	private final AnonimoService anonimoService;

	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Eventos futuros ordenados por fecha. "
			+ "La lista puede estar vacía.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventoResumenDto.class)))),
			@ApiResponse(responseCode = "400", description = "Identificador de categoría inválido", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))) })

	@GetMapping
	@Operation(summary = "Lista eventos futuros, con categoría opcional")
	public List<EventoResumenDto> listadoEventos(@RequestParam(name = "categoria", required = false) Long categoriaId) {

		return anonimoService.listadoEventos(categoriaId);
	}

	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Detalle del evento con sus plazas disponibles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoDetalleDto.class))),
			@ApiResponse(responseCode = "400", description = "Identificador de evento inválido", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "404", description = "El evento no existe", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))) })

	@GetMapping("/{id}")
	@Operation(summary = "Consulta un evento y sus plazas disponibles")
	public EventoDetalleDto verEvento(@PathVariable("id") Long id) {

		return anonimoService.verEvento(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El evento no existe"));
	}
}