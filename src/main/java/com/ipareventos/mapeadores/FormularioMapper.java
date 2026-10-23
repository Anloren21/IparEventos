package com.ipareventos.mapeadores;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.ipareventos.dtos.EventoCrearDto;
import com.ipareventos.dtos.UsuarioRegistroDto;
import com.ipareventos.modelos.EventoFormModel;
import com.ipareventos.modelos.UsuarioFormModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FormularioMapper {

	UsuarioRegistroDto toRegistroDto(UsuarioFormModel formulario);

	EventoCrearDto toEventoCrearDto(EventoFormModel formulario);
}