package com.ipareventos.controladores.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ipareventos.excepciones.EventoException;
import com.ipareventos.mapeadores.FormularioMapper;
import com.ipareventos.modelos.EventoFormModel;
import com.ipareventos.servicios.AdminService;
import com.ipareventos.servicios.AnonimoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	private final AnonimoService anonimoService;
	private final FormularioMapper formularioMapper;

	@GetMapping({ "", "/" })
	public String listado(Model model) {

		model.addAttribute("eventos", adminService.listarEventos());

		return "admin/index";
	}

	@GetMapping("/eventos/nuevo")
	public String nuevoEvento(Model model) {

		model.addAttribute("eventoFormModel", new EventoFormModel());

		cargarCategorias(model);

		return "admin/evento-form";
	}

	@PostMapping("/eventos")
	public String crearEvento(@Valid @ModelAttribute("eventoFormModel") EventoFormModel formulario,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			cargarCategorias(model);
			return "admin/evento-form";
		}

		try {
			adminService.crearEvento(formularioMapper.toEventoCrearDto(formulario));
		} catch (EventoException ex) {
			bindingResult.reject("evento.invalido", ex.getMessage());

			cargarCategorias(model);
			return "admin/evento-form";
		}

		redirectAttributes.addFlashAttribute("alerta", "El evento se ha creado correctamente.");

		return "redirect:/admin";
	}

	@PostMapping("/eventos/{id}/eliminar")
	public String eliminarEvento(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

		try {
			adminService.eliminarEvento(id);

			redirectAttributes.addFlashAttribute("alerta", "El evento se ha eliminado correctamente.");
		} catch (EventoException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}

		return "redirect:/admin";
	}

	private void cargarCategorias(Model model) {
		model.addAttribute("categorias", anonimoService.listarCategorias());
	}
}