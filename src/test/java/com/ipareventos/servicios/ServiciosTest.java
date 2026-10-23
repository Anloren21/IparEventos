package com.ipareventos.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.ipareventos.dtos.EventoCrearDto;
import com.ipareventos.dtos.EventoDetalleDto;
import com.ipareventos.dtos.InscripcionDto;
import com.ipareventos.dtos.UsuarioRegistroDto;
import com.ipareventos.entidades.Rol;
import com.ipareventos.excepciones.EmailDuplicadoException;
import com.ipareventos.excepciones.InscripcionException;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@Transactional
class ServiciosTest {

	@Autowired
	private AnonimoService anonimoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AdminService adminService;

	@BeforeEach
	void prepararAutenticacion() {
		autenticar("admin@ipareventos.test", "ADMINISTRADOR");
	}

	@AfterEach
	void limpiarAutenticacion() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void listadoPublicoIncluyeEventoFuturoYExcluyePasado() {

		EventoDetalleDto futuro = crearEvento(LocalDateTime.now().plusDays(2), 10);

		EventoDetalleDto pasado = crearEvento(LocalDateTime.now().minusDays(2), 10);

		var ids = anonimoService.listadoEventos(null).stream().map(dto -> dto.id()).toList();

		assertTrue(ids.contains(futuro.id()));
		assertFalse(ids.contains(pasado.id()));
	}

	@Test
	void reservarDescuentaPlazasYCancelarLasRecupera() {

		EventoDetalleDto evento = crearEvento(LocalDateTime.now().plusDays(2), 10);

		autenticar("ana@ipareventos.test", "USUARIO");

		InscripcionDto reserva = usuarioService.inscribirse(evento.id(), 3);

		assertEquals(7, anonimoService.verEvento(evento.id()).orElseThrow().plazasDisponibles());

		assertTrue(usuarioService.misInscripciones().stream().anyMatch(dto -> dto.id().equals(reserva.id())));

		usuarioService.cancelarInscripcion(reserva.id());

		assertEquals(10, anonimoService.verEvento(evento.id()).orElseThrow().plazasDisponibles());
	}

	@Test
	void rechazaReservaSinPlazasSuficientes() {

		EventoDetalleDto evento = crearEvento(LocalDateTime.now().plusDays(2), 1);

		autenticar("ana@ipareventos.test", "USUARIO");

		assertThrows(InscripcionException.class, () -> usuarioService.inscribirse(evento.id(), 2));
	}

	@Test
	void rechazaReservaEnEventoPasado() {

		EventoDetalleDto evento = crearEvento(LocalDateTime.now().minusDays(2), 10);

		autenticar("ana@ipareventos.test", "USUARIO");

		assertThrows(InscripcionException.class, () -> usuarioService.inscribirse(evento.id(), 1));
	}

	@Test
	void rechazaReservaEnEventoInexistente() {

		autenticar("ana@ipareventos.test", "USUARIO");

		assertThrows(InscripcionException.class, () -> usuarioService.inscribirse(Long.MAX_VALUE, 1));
	}

	@Test
	void impideCancelarInscripcionesDeOtraPersona() {

		EventoDetalleDto evento = crearEvento(LocalDateTime.now().plusDays(2), 10);

		autenticar("ana@ipareventos.test", "USUARIO");

		InscripcionDto reserva = usuarioService.inscribirse(evento.id(), 1);

		autenticar("luis@ipareventos.test", "USUARIO");

		assertThrows(InscripcionException.class, () -> usuarioService.cancelarInscripcion(reserva.id()));
	}

	@Test
	void usuarioNormalNoPuedeUsarServicioAdministrador() {

		autenticar("ana@ipareventos.test", "USUARIO");

		assertThrows(AccessDeniedException.class, () -> adminService.listarEventos());
	}

	@Test
	void registroAsignaRolUsuario() {

		var dto = new UsuarioRegistroDto("Prueba", "prueba-" + java.util.UUID.randomUUID() + "@ipareventos.test",
				"Prueba123!");

		var resultado = anonimoService.registrarse(dto);

		assertNotNull(resultado.id());
		assertEquals(dto.email(), resultado.email());
		assertEquals(Rol.USUARIO, resultado.rol());
	}

	@Test
	void rechazaCorreoDuplicado() {

		var dto = new UsuarioRegistroDto("Otra Ana", "ana@ipareventos.test", "Prueba123!");

		assertThrows(EmailDuplicadoException.class, () -> anonimoService.registrarse(dto));
	}

	@Test
	void validaLosDatosDeRegistroEnElServicio() {

		var dto = new UsuarioRegistroDto("", "correo-invalido", "");

		assertThrows(ConstraintViolationException.class, () -> anonimoService.registrarse(dto));
	}

	private EventoDetalleDto crearEvento(LocalDateTime fecha, int aforo) {

		return adminService.crearEvento(new EventoCrearDto("Evento de prueba", "Creado por una prueba automatizada",
				fecha, aforo, BigDecimal.ZERO, Set.of()));
	}

	private void autenticar(String email, String rol) {

		var autenticacion = new UsernamePasswordAuthenticationToken(email, null,
				List.of(new SimpleGrantedAuthority("ROLE_" + rol)));

		SecurityContextHolder.getContext().setAuthentication(autenticacion);
	}
}