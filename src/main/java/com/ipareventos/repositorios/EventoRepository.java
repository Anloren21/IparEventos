package com.ipareventos.repositorios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipareventos.entidades.Evento;
import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface EventoRepository extends JpaRepository<Evento, Long> {

	List<Evento> findByFechaAfterOrderByFechaAsc(LocalDateTime fecha);

	List<Evento> findDistinctByCategorias_IdAndFechaAfterOrderByFechaAsc(Long categoriaId, LocalDateTime fecha);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Evento> findOneById(Long id);
}