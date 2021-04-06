package com.daniel.bluefood.infrastructure.web.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import com.daniel.bluefood.domain.restaurante.CategoriaRestaurante;
import com.daniel.bluefood.domain.restaurante.CategoriaRestauranteRepository;

public class ControllerHelper {

	public static void isEditMode(Model model, boolean isEditMode) {
		model.addAttribute("editMode", isEditMode);
	}
	
	public static void addCategoriasToRequest(CategoriaRestauranteRepository repository, Model model) {
		
		List<CategoriaRestaurante> categorias = repository.findAll(Sort.by("nome"));
		model.addAttribute("categorias", categorias);
	}
}
