package com.daniel.bluefood.infrastructure.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.daniel.bluefood.domain.application.service.RestauranteService;
import com.daniel.bluefood.domain.application.service.ValidationException;
import com.daniel.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import com.daniel.bluefood.domain.restaurante.Restaurante;
import com.daniel.bluefood.domain.restaurante.RestauranteRepository;
import com.daniel.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/restaurante")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@GetMapping("/home")
	public String home() {
		return "restaurante-home";
	}
	
	@GetMapping(path = "/edit")
	public String edit(Model model) {

		Integer restauranteId = SecurityUtils.loggedRestaurante().getId();

		Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();

		model.addAttribute("restaurante", restaurante);

		ControllerHelper.isEditMode(model, true);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-cadastro";
	}
	
	@PostMapping(path = "/save")
	public String save(@ModelAttribute("restaurante") @Valid Restaurante restaurante, 
			Errors errors, 
			Model model) {

		if (!errors.hasErrors()) {
			try {
				restauranteService.saveRestaurante(restaurante);
				model.addAttribute("msg", "Restaurante cadastrados com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		ControllerHelper.isEditMode(model, true);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-cadastro";
	}
}

