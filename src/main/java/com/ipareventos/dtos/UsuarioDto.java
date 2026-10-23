package com.ipareventos.dtos;

import com.ipareventos.entidades.Rol;

public record UsuarioDto(Long id, String nombre, String email, Rol rol) {
}