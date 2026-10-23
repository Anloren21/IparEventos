package com.ipareventos.mapeadores;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.ipareventos.dtos.InscripcionDto;
import com.ipareventos.entidades.Inscripcion;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface InscripcionMapper {

	@Mapping(target = "eventoId", source = "evento.id")
	@Mapping(target = "tituloEvento", source = "evento.titulo")
	@Mapping(target = "fechaEvento", source = "evento.fecha")
	InscripcionDto toDto(Inscripcion inscripcion);

	List<InscripcionDto> toDtoList(List<Inscripcion> inscripciones);
}
