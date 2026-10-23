package com.ipareventos.mapeadores;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.ipareventos.dtos.EventoCrearDto;
import com.ipareventos.dtos.EventoDetalleDto;
import com.ipareventos.dtos.EventoResumenDto;
import com.ipareventos.entidades.Evento;

@Mapper(componentModel = "spring", uses = CategoriaMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EventoMapper {

	EventoResumenDto toResumenDto(Evento evento);

	List<EventoResumenDto> toResumenDtoList(List<Evento> eventos);

	@Mapping(target = "plazasDisponibles", source = "plazasDisponibles")
	EventoDetalleDto toDetalleDto(Evento evento, int plazasDisponibles);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "categorias", ignore = true)
	@Mapping(target = "inscripciones", ignore = true)
	Evento toEntity(EventoCrearDto dto);
}