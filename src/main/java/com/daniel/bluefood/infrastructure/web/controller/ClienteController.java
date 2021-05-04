package com.daniel.bluefood.infrastructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daniel.bluefood.domain.application.service.ClienteService;
import com.daniel.bluefood.domain.application.service.RestauranteService;
import com.daniel.bluefood.domain.application.service.ValidationException;
import com.daniel.bluefood.domain.cliente.Cliente;
import com.daniel.bluefood.domain.cliente.ClienteRepository;
import com.daniel.bluefood.domain.restaurante.CategoriaRestaurante;
import com.daniel.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import com.daniel.bluefood.domain.restaurante.ItemCardapio;
import com.daniel.bluefood.domain.restaurante.ItemCardapioRepository;
import com.daniel.bluefood.domain.restaurante.Restaurante;
import com.daniel.bluefood.domain.restaurante.RestauranteRepository;
import com.daniel.bluefood.domain.restaurante.SearchFilter;
import com.daniel.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;

	@Autowired
	private RestauranteService restauranteService;

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;

	@GetMapping(path = "/home")
	public String home(Model model) {

		List<CategoriaRestaurante> categorias = categoriaRestauranteRepository.findAll(Sort.by("nome"));

		model.addAttribute("categorias", categorias);
		model.addAttribute("searchFilter", new SearchFilter());

		return "cliente-home";
	}

	@GetMapping(path = "/edit")
	public String edit(Model model) {

		Integer clienteId = SecurityUtils.loggedCliente().getId();

		Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();

		model.addAttribute("cliente", cliente);

		ControllerHelper.isEditMode(model, true);

		return "cliente-cadastro";
	}

	@PostMapping(path = "/save")
	public String save(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model) {

		if (!errors.hasErrors()) {
			try {
				clienteService.saveCliente(cliente);
				model.addAttribute("msg", "Dados cadastrados com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		ControllerHelper.isEditMode(model, true);

		return "cliente-cadastro";
	}

	@GetMapping(path = "/search")
	public String search(@ModelAttribute("searchFilter") SearchFilter filter,
			@RequestParam(value = "cmd", required = false) String command, Model model) {

		filter.processFilter(command);

		List<Restaurante> restaurantes = restauranteService.serach(filter);

		model.addAttribute("restaurantes", restaurantes);

		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);

		model.addAttribute("searchFilter", filter);
		model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());

		return "cliente-busca";
	}

	@GetMapping(path = "/restaurante")
	public String viewRestaurante(
			@RequestParam("restauranteId") Integer restauranteId,
			@RequestParam(value = "categoria", required = false) String categoria,
			Model model) {

		Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();
		model.addAttribute("restaurante", restaurante);
		model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());
		
		List<String> categorias = itemCardapioRepository.findCategorias(restauranteId);
		model.addAttribute("categorias", categorias);
		
		List<ItemCardapio> itensCardapioDestaque;
		List<ItemCardapio> itensCardapioNaoDestaque;
		
		if (categoria == null) {
			
			itensCardapioDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, true);
			itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, false);
			
		} else {
			
			itensCardapioDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, true, categoria);
			itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, false, categoria);
		}
		
		model.addAttribute("itensCardapioDestaque", itensCardapioDestaque);
		model.addAttribute("itensCardapioNaoDestaque", itensCardapioNaoDestaque);
		model.addAttribute("categoriaSelecionada", categoria);
		
		return "cliente-restaurante";
	}
}
