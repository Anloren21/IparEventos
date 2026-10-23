package com.ipareventos.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipareventos.entidades.*;
import com.ipareventos.repositorios.*;

@Service
public class DatosEjemploService {

	private final CategoriaRepository categoriaRepository;
	private final UsuarioRepository usuarioRepository;
	private final EventoRepository eventoRepository;
	private final InscripcionRepository inscripcionRepository;
	private final PasswordEncoder passwordEncoder;
	
	public DatosEjemploService(
	        CategoriaRepository categoriaRepository,
	        UsuarioRepository usuarioRepository,
	        EventoRepository eventoRepository,
	        InscripcionRepository inscripcionRepository,
	        PasswordEncoder passwordEncoder) {

	    this.categoriaRepository = categoriaRepository;
	    this.usuarioRepository = usuarioRepository;
	    this.eventoRepository = eventoRepository;
	    this.inscripcionRepository = inscripcionRepository;
	    this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void cargar() {

		// La carga inicial solo se realiza si las tablas estan vacias.
		if (categoriaRepository.count() > 0 || usuarioRepository.count() > 0 || eventoRepository.count() > 0
				|| inscripcionRepository.count() > 0) {
			return;
		}

		Categoria tecnologia = crearCategoria("Tecnología");
		Categoria cultura = crearCategoria("Cultura");
		Categoria deporte = crearCategoria("Deporte");

		crearUsuario("Administradora", "admin@ipareventos.test", "Admin123!", Rol.ADMINISTRADOR);

		Usuario ana = crearUsuario("Ana", "ana@ipareventos.test", "Ana12345!", Rol.USUARIO);

		Usuario luis = crearUsuario("Luis", "luis@ipareventos.test", "Luis12345!", Rol.USUARIO);

		LocalDateTime ahora = LocalDateTime.now();

		// Evento ya celebrado.
		crearEvento("Introducción a Java", "Taller de iniciación a la programación con Java.", ahora.minusDays(7), 30,
				"0.00", tecnologia);

		// Evento con 10 plazas y 9 reservadas.
		Evento casiCompleto = crearEvento("Taller de Spring Boot", "Desarrollo de aplicaciones web con Spring Boot.",
				ahora.plusDays(7), 10, "25.00", tecnologia);

		crearEvento("Exposición de fotografía", "Encuentro de fotografía y creatividad.", ahora.plusDays(10), 40,
				"5.00", cultura);

		crearEvento("Ruta de senderismo", "Ruta guiada para disfrutar de la naturaleza.", ahora.plusDays(14), 20,
				"10.00", deporte);

		crearEvento("Diseño y tecnología", "Aplicaciones de la tecnología en el diseño.", ahora.plusDays(20), 25,
				"15.00", tecnologia, cultura);

		crearEvento("Jornada deportiva", "Actividades deportivas para todos los niveles.", ahora.plusDays(30), 50,
				"0.00", deporte);

		// Cada inscripción cumple el límite de entre 1 y 4 plazas.
		crearInscripcion(ana, casiCompleto, 4, ahora);
		crearInscripcion(luis, casiCompleto, 4, ahora);
		crearInscripcion(ana, casiCompleto, 1, ahora);
	}

	private Categoria crearCategoria(String nombre) {
		return categoriaRepository.save(Categoria.builder().nombre(nombre).build());
	}

	private Usuario crearUsuario(String nombre, String email, String password, Rol rol) {

		return usuarioRepository.save(Usuario.builder().nombre(nombre).email(email)
				.password(passwordEncoder.encode(password)).rol(rol).build());
	}

	private Evento crearEvento(String titulo, String descripcion, LocalDateTime fecha, int aforo, String precio,
			Categoria... categorias) {

		return eventoRepository.save(Evento.builder().titulo(titulo).descripcion(descripcion).fecha(fecha).aforo(aforo)
				.precio(new BigDecimal(precio)).categorias(new HashSet<>(List.of(categorias))).build());
	}

	private void crearInscripcion(Usuario usuario, Evento evento, int plazas, LocalDateTime fecha) {

		Inscripcion inscripcion = Inscripcion.builder().usuario(usuario).evento(evento).plazas(plazas)
				.fechaInscripcion(fecha).build();

		evento.getInscripciones().add(inscripcion);
		inscripcionRepository.save(inscripcion);
	}
}