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

import com.daniel.bluefood.domain.application.service.ClienteService;
import com.daniel.bluefood.domain.application.service.RestauranteService;
import com.daniel.bluefood.domain.application.service.ValidationException;
import com.daniel.bluefood.domain.cliente.Cliente;
import com.daniel.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import com.daniel.bluefood.domain.restaurante.Restaurante;

@Controller
@RequestMapping(path = "/public")
public class PublicController {
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;

	@GetMapping(path = "/cliente/new")
	public String newCliente(Model model) {
		
		model.addAttribute("cliente", new Cliente());
		ControllerHelper.isEditMode(model, false);
		return "cliente-cadastro";
	}
	
	@GetMapping(path = "/restaurante/new")
	public String newRestaurante(Model model) {
		
		model.addAttribute("restaurante", new Restaurante());
		ControllerHelper.isEditMode(model, false);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-cadastro";
	}
	
	@PostMapping(path = "/cliente/save")
	public String saveCliente(
			@ModelAttribute("cliente") @Valid Cliente cliente, 
			Errors errors, 
			Model model) {
		
		if (!errors.hasErrors()) {
			try {
				clienteService.saveCliente(cliente);
				model.addAttribute("msg", "Dados cadastrados com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		ControllerHelper.isEditMode(model, false);
		return "cliente-cadastro";
	}
	
	@PostMapping(path = "/restaurante/save")
	public String saveRestaurante(
			@ModelAttribute("restaurante") @Valid Restaurante restaurante, 
			Errors errors, 
			Model model) {
		
		if (!errors.hasErrors()) {
			try {
				restauranteService.saveRestaurante(restaurante);
				model.addAttribute("msg", "Dados cadastrados com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		ControllerHelper.isEditMode(model, false);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-cadastro";
	}
}
