package com.ipareventos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ipareventos.entidades.Categoria;

@RepositoryRestResource(path = "categorias", collectionResourceRel = "categorias")
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}