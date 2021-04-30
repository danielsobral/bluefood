package com.daniel.bluefood.domain.application.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daniel.bluefood.domain.cliente.Cliente;
import com.daniel.bluefood.domain.cliente.ClienteRepository;
import com.daniel.bluefood.domain.restaurante.Restaurante;
import com.daniel.bluefood.domain.restaurante.RestauranteComparator;
import com.daniel.bluefood.domain.restaurante.RestauranteRepository;
import com.daniel.bluefood.domain.restaurante.SearchFilter;
import com.daniel.bluefood.domain.restaurante.SearchFilter.SearchType;
import com.daniel.bluefood.util.SecurityUtils;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private ImageService imageservice;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Transactional
	public void saveRestaurante(Restaurante restaurante) throws ValidationException {

		if (!validateEmail(restaurante.getEmail(), restaurante.getId())) {
			throw new ValidationException("E-mail j� cadastrado");
		}

		if (restaurante.getId() != null) {

			Restaurante restauranteDB = restauranteRepository.findById(restaurante.getId()).orElseThrow();
			restaurante.setSenha(restauranteDB.getSenha());

		} else {
			restaurante.ecncryptPassword();
			restaurante = restauranteRepository.save(restaurante);
			restaurante.setLogotipoFileName();
			imageservice.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
		}

	}

	public boolean validateEmail(String email, Integer id) {

		Cliente cliente = clienteRepository.findByEmail(email);
		
		if (cliente != null) {
			
			return false;
		}
		
		Restaurante restaurante = restauranteRepository.findByEmail(email);

		if (restaurante != null) {
			if (id == null) {
				return false;
			}

			if (!restaurante.getId().equals(id)) {
				return false;
			}
		}

		return true;
	}
	
	public List<Restaurante> serach(SearchFilter filter){
		List<Restaurante> restaurantes;
		
		if (filter.getSearchType() == SearchType.Texto) {
			
			restaurantes = restauranteRepository.findByNomeIgnoreCaseContaining(filter.getTexto());
		
		} else if (filter.getSearchType() == SearchType.Categoria) {
			
			restaurantes = restauranteRepository.findByCategorias_Id(filter.getCategoriaId());
		} else {
			
			throw new IllegalStateException("O tipo de buscar " + filter.getSearchType() + " n�o � suportado");
		}
		
		Iterator<Restaurante> it = restaurantes.iterator();
		
		while (it.hasNext()) {
			Restaurante restaurante = it.next();
			double taxaEntrega = restaurante.getTaxaEntrega().doubleValue();
			
			if (filter.isEntregaGratis() && taxaEntrega > 0 || 
					!filter.isEntregaGratis() && taxaEntrega == 0) {
				it.remove();
			}
		}
		
		RestauranteComparator comparator = new RestauranteComparator(filter, SecurityUtils.loggedCliente().getCep());
		restaurantes.sort(comparator);
		
		return restaurantes;
	}

}
