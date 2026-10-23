package com.ipareventos.servicios;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.ipareventos.dtos.EventoCrearDto;
import com.ipareventos.dtos.EventoDetalleDto;
import com.ipareventos.dtos.EventoResumenDto;
import com.ipareventos.entidades.Categoria;
import com.ipareventos.entidades.Evento;
import com.ipareventos.excepciones.EventoException;
import com.ipareventos.mapeadores.EventoMapper;
import com.ipareventos.repositorios.CategoriaRepository;
import com.ipareventos.repositorios.EventoRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
@RolesAllowed("ADMINISTRADOR")
@Transactional(readOnly = true)
public class AdminService {

	private final EventoRepository eventoRepository;
	private final CategoriaRepository categoriaRepository;
	private final EventoMapper eventoMapper;

	public List<EventoResumenDto> listarEventos() {

		List<Evento> eventos = eventoRepository.findAll(Sort.by("fecha").ascending());

		return eventoMapper.toResumenDtoList(eventos);
	}

	@Transactional
	public EventoDetalleDto crearEvento(@NotNull @Valid EventoCrearDto dto) {

		List<Categoria> categorias = categoriaRepository.findAllById(dto.categoriaIds());

		if (categorias.size() != dto.categoriaIds().size()) {
			throw new EventoException("Alguna de las categorías seleccionadas no existe");
		}

		Evento evento = eventoMapper.toEntity(dto);

		evento.setCategorias(new HashSet<>(categorias));

		Evento guardado = eventoRepository.save(evento);

		return eventoMapper.toDetalleDto(guardado, guardado.getAforo());
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void eliminarEvento(@NotNull @Positive Long idEvento) {

		Evento evento = eventoRepository.findOneById(idEvento)
				.orElseThrow(() -> new EventoException("El evento no existe"));

		eventoRepository.delete(evento);
	}
}
