package com.ipareventos.controladores.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ipareventos.servicios.AnonimoService;

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
}