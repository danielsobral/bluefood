package com.daniel.bluefood.domain.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daniel.bluefood.domain.cliente.Cliente;
import com.daniel.bluefood.domain.cliente.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public void saveCliente(Cliente cliente) throws ValidationException {
		
		if(!validateEmail(cliente.getEmail(), cliente.getId())) {
			throw new ValidationException("E-mail já cadastrado");
		}
		
		if (cliente.getId() != null) {
			
			Cliente clienteDB = clienteRepository.findById(cliente.getId()).orElseThrow();
			cliente.setSenha(clienteDB.getSenha());
			
		} else {
			cliente.ecncryptPassword();
		}
		
		clienteRepository.save(cliente);
	}
	
	public boolean validateEmail(String email, Integer id) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if (cliente != null) {
			if (id == null) {
				return false;
			}
			
			if (!cliente.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
}
