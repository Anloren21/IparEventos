package com.ipareventos.mapeadores;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.ipareventos.dtos.CategoriaDto;
import com.ipareventos.entidades.Categoria;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CategoriaMapper {

	CategoriaDto toDto(Categoria categoria);

	List<CategoriaDto> toDtoList(List<Categoria> categorias);
}