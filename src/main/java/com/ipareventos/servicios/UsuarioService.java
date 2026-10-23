package com.ipareventos.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.ipareventos.dtos.InscripcionDto;
import com.ipareventos.entidades.Evento;
import com.ipareventos.entidades.Inscripcion;
import com.ipareventos.entidades.Usuario;
import com.ipareventos.excepciones.InscripcionException;
import com.ipareventos.mapeadores.InscripcionMapper;
import com.ipareventos.repositorios.EventoRepository;
import com.ipareventos.repositorios.InscripcionRepository;
import com.ipareventos.repositorios.UsuarioRepository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final EventoRepository eventoRepository;
	private final InscripcionRepository inscripcionRepository;
	private final InscripcionMapper inscripcionMapper;

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public InscripcionDto inscribirse(@NotNull @Positive Long idEvento, @Min(1) @Max(4) int plazas) {

		Usuario usuario = obtenerUsuarioActual();

		Evento evento = eventoRepository.findOneById(idEvento)
				.orElseThrow(() -> new InscripcionException("El evento no existe"));

		LocalDateTime ahora = LocalDateTime.now();

		if (!evento.getFecha().isAfter(ahora)) {
			throw new InscripcionException("No puedes inscribirte en un evento ya iniciado");
		}

		int plazasReservadas = inscripcionRepository.findByEvento_Id(idEvento).stream().mapToInt(Inscripcion::getPlazas)
				.sum();

		int plazasDisponibles = evento.getAforo() - plazasReservadas;

		if (plazas > plazasDisponibles) {
			throw new InscripcionException("No hay plazas suficientes. Disponibles: " + plazasDisponibles);
		}

		Inscripcion inscripcion = Inscripcion.builder().usuario(usuario).evento(evento).plazas(plazas)
				.fechaInscripcion(ahora).build();

		Inscripcion guardada = inscripcionRepository.save(inscripcion);

		return inscripcionMapper.toDto(guardada);
	}

	public List<InscripcionDto> misInscripciones() {

		Usuario usuario = obtenerUsuarioActual();

		List<Inscripcion> inscripciones = inscripcionRepository.findByUsuario_Id(usuario.getId());

		return inscripcionMapper.toDtoList(inscripciones);
	}

	@Transactional
	public void cancelar(@NotNull @Positive Long idInscripcion) {

		Usuario usuario = obtenerUsuarioActual();

		Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
				.orElseThrow(() -> new InscripcionException("La inscripción no existe"));

		if (!inscripcion.getUsuario().getId().equals(usuario.getId())) {

			throw new InscripcionException("Solo puedes cancelar tus propias inscripciones");
		}

		inscripcionRepository.delete(inscripcion);
	}

	private Usuario obtenerUsuarioActual() {

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();

		if (autenticacion == null || !autenticacion.isAuthenticated()
				|| autenticacion instanceof AnonymousAuthenticationToken) {

			throw new InscripcionException("Debes iniciar sesión");
		}

		return usuarioRepository.findByEmail(autenticacion.getName())
				.orElseThrow(() -> new InscripcionException("El usuario autenticado no existe en la base de datos"));
	}
}