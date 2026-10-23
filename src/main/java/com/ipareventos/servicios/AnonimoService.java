package com.ipareventos.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.ipareventos.dtos.CategoriaDto;
import com.ipareventos.dtos.EventoDetalleDto;
import com.ipareventos.dtos.EventoResumenDto;
import com.ipareventos.dtos.UsuarioDto;
import com.ipareventos.dtos.UsuarioRegistroDto;
import com.ipareventos.entidades.Evento;
import com.ipareventos.entidades.Inscripcion;
import com.ipareventos.entidades.Rol;
import com.ipareventos.entidades.Usuario;
import com.ipareventos.excepciones.EmailDuplicadoException;
import com.ipareventos.mapeadores.CategoriaMapper;
import com.ipareventos.mapeadores.EventoMapper;
import com.ipareventos.mapeadores.UsuarioMapper;
import com.ipareventos.repositorios.CategoriaRepository;
import com.ipareventos.repositorios.EventoRepository;
import com.ipareventos.repositorios.UsuarioRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnonimoService {

	private final CategoriaRepository categoriaRepository;
	private final EventoRepository eventoRepository;
	private final UsuarioRepository usuarioRepository;

	private final CategoriaMapper categoriaMapper;
	private final EventoMapper eventoMapper;
	private final UsuarioMapper usuarioMapper;

	private final PasswordEncoder passwordEncoder;

	public List<CategoriaDto> listarCategorias() {
		return categoriaMapper.toDtoList(categoriaRepository.findAll());
	}

	public List<EventoResumenDto> listarEventos(@Positive Long categoriaId) {

		LocalDateTime ahora = LocalDateTime.now();

		List<Evento> eventos;

		if (categoriaId == null) {
			eventos = eventoRepository.findByFechaAfterOrderByFechaAsc(ahora);
		} else {
			eventos = eventoRepository.findDistinctByCategorias_IdAndFechaAfterOrderByFechaAsc(categoriaId, ahora);
		}

		return eventoMapper.toResumenDtoList(eventos);
	}

	public Optional<EventoDetalleDto> verEvento(@NotNull @Positive Long id) {

		return eventoRepository.findById(id).map(evento -> {

			int plazasReservadas = evento.getInscripciones().stream().mapToInt(Inscripcion::getPlazas).sum();

			int plazasDisponibles = evento.getAforo() - plazasReservadas;

			return eventoMapper.toDetalleDto(evento, plazasDisponibles);
		});
	}

	@Transactional
	public UsuarioDto registrar(@NotNull @Valid UsuarioRegistroDto dto) {

		if (usuarioRepository.existsByEmail(dto.email())) {
			throw new EmailDuplicadoException();
		}

		Usuario usuario = usuarioMapper.toEntity(dto);

		usuario.setPassword(passwordEncoder.encode(dto.password()));

		usuario.setRol(Rol.USUARIO);

		Usuario guardado = usuarioRepository.save(usuario);

		return usuarioMapper.toDto(guardado);
	}
}