package com.ipareventos.controladores.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipareventos.dtos.UsuarioDto;
import com.ipareventos.dtos.UsuarioRegistroDto;
import com.ipareventos.servicios.AnonimoService;

import org.springframework.http.ProblemDetail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuarios", description = "Registro de usuarios")
public class UsuarioRestController {

	private final AnonimoService anonimoService;

	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Usuario registrado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class))),
			@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos. "
					+ "La propiedad errores contiene los mensajes por campo.", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
			@ApiResponse(responseCode = "409", description = "Correo electrónico ya registrado o conflicto de datos", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))) })

	@PostMapping("/registrar")
	@Operation(summary = "Registra un usuario con rol USUARIO")
	public ResponseEntity<UsuarioDto> registrar(@Valid @RequestBody UsuarioRegistroDto dto) {

		UsuarioDto usuario = anonimoService.registrarse(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}
}