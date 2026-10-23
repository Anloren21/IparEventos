package com.ipareventos.controladores.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ipareventos.servicios.AnonimoService;
import com.ipareventos.excepciones.EmailDuplicadoException;
import com.ipareventos.mapeadores.FormularioMapper;
import com.ipareventos.modelos.UsuarioFormModel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AnonimoController {

	private final AnonimoService anonimoService;
	private final FormularioMapper formularioMapper;

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

	@GetMapping("/registro")
	public String formularioRegistro(Model model) {

		model.addAttribute("usuarioFormModel", new UsuarioFormModel());

		return "registro";
	}

	@PostMapping("/registro")
	public String registrar(@Valid @ModelAttribute("usuarioFormModel") UsuarioFormModel usuarioFormModel,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "registro";
		}

		try {
			anonimoService.registrarse(formularioMapper.toRegistroDto(usuarioFormModel));
		} catch (EmailDuplicadoException ex) {
			bindingResult.rejectValue("email", "email.duplicado", ex.getMessage());

			return "registro";
		}

		redirectAttributes.addFlashAttribute("alerta", "Tu cuenta se ha creado correctamente.");

		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}