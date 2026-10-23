package com.ipareventos.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipareventos.entidades.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

	List<Inscripcion> findByUsuario_Id(Long usuarioId);

	List<Inscripcion> findByEvento_Id(Long eventoId);
}