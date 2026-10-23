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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuarios", description = "Registro de usuarios")
public class UsuarioRestController {

	private final AnonimoService anonimoService;

	@PostMapping("/registrar")
	@Operation(summary = "Registra un usuario con rol USUARIO")
	public ResponseEntity<UsuarioDto> registrar(@Valid @RequestBody UsuarioRegistroDto dto) {

		UsuarioDto usuario = anonimoService.registrarse(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}
}