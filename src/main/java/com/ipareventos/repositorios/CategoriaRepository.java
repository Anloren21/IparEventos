package com.ipareventos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipareventos.entidades.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}