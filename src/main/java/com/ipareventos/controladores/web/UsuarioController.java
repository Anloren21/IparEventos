package com.ipareventos.controladores.web;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ipareventos.excepciones.InscripcionException;
import com.ipareventos.modelos.InscripcionFormModel;
import com.ipareventos.servicios.AnonimoService;
import com.ipareventos.servicios.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;
	private final AnonimoService anonimoService;

	@GetMapping("/inscripciones")
	public String misInscripciones(Model model) {

		model.addAttribute("inscripciones", usuarioService.misInscripciones());

		return "inscripciones";
	}

	@PostMapping("/evento/{id}/inscripciones")
	public String inscribirse(@PathVariable("id") Long id,
			@Valid @ModelAttribute("inscripcionFormModel") InscripcionFormModel formulario, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return mostrarEvento(id, model);
		}

		try {
			usuarioService.inscribirse(id, formulario.getPlazas());
		} catch (InscripcionException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());

			return "redirect:/evento/" + id;
		}

		redirectAttributes.addFlashAttribute("alerta", "Tu inscripción se ha realizado correctamente.");

		return "redirect:/inscripciones";
	}

	@PostMapping("/inscripciones/{id}/cancelar")
	public String cancelarInscripcion(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

		try {
			usuarioService.cancelarInscripcion(id);

			redirectAttributes.addFlashAttribute("alerta", "La inscripción se ha cancelado correctamente.");
		} catch (InscripcionException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}

		return "redirect:/inscripciones";
	}

	private String mostrarEvento(Long id, Model model) {

		var evento = anonimoService.verEvento(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El evento no existe"));

		model.addAttribute("evento", evento);

		model.addAttribute("eventoFuturo", evento.fecha().isAfter(LocalDateTime.now()));

		return "evento";
	}
}