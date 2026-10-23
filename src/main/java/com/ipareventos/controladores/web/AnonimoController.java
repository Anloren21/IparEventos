package com.ipareventos.controladores.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ipareventos.servicios.AnonimoService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AnonimoController {

	private final AnonimoService anonimoService;

	@GetMapping("/")
	public String inicio(@RequestParam(name = "categoria", required = false) Long categoriaId, Model model) {

		model.addAttribute("eventos", anonimoService.listadoEventos(categoriaId));

		model.addAttribute("categorias", anonimoService.listarCategorias());

		model.addAttribute("categoriaSeleccionada", categoriaId);

		return "index";
	}

	@GetMapping("/evento/{id}")
	public String verEvento(@PathVariable("id") Long id, Model model) {

		var evento = anonimoService.verEvento(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El evento no existe"));

		model.addAttribute("evento", evento);

		model.addAttribute("eventoFuturo", evento.fecha().isAfter(java.time.LocalDateTime.now()));

		return "evento";
	}
}