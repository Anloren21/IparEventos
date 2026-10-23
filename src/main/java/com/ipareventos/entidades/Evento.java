package com.ipareventos.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "eventos")
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@NotBlank
	@Size(max = 50)
	@Column(nullable = false, length = 50)
	private String titulo;

	@Size(max = 500)
	@Column(length = 500)
	private String descripcion;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime fecha;

	@Min(1)
	@Column(nullable = false)
	private int aforo;

	@NotNull
	@DecimalMin("0.00")
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;

	@Builder.Default
	@ToString.Exclude
	@ManyToMany
	@JoinTable(name = "eventos_categorias", joinColumns = @JoinColumn(name = "evento_id"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
	private Set<Categoria> categorias = new HashSet<>();

	@Builder.Default
	@ToString.Exclude
	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Inscripcion> inscripciones = new ArrayList<>();
}