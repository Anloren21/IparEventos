package com.ipareventos.mapeadores;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.ipareventos.dtos.UsuarioDto;
import com.ipareventos.dtos.UsuarioRegistroDto;
import com.ipareventos.entidades.Usuario;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UsuarioMapper {

	UsuarioDto toDto(Usuario usuario);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "rol", ignore = true)
	Usuario toEntity(UsuarioRegistroDto dto);
}