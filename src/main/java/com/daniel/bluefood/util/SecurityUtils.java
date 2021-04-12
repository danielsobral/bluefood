package com.daniel.bluefood.util;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.daniel.bluefood.domain.cliente.Cliente;
import com.daniel.bluefood.domain.restaurante.Restaurante;
import com.daniel.bluefood.infrastructure.web.security.LoggedUser;

public class SecurityUtils {

	public static LoggedUser loggedUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication instanceof AnonymousAuthenticationToken) {
			
			return null;
		}
		
		return (LoggedUser) authentication.getPrincipal();
	}
	
	public static Cliente loggedCliente() {
		
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			
			throw new IllegalStateException("N�o existe um usu�rio logado");
		}
		
		if (!(loggedUser.getUsuario() instanceof Cliente)) {
			
			throw new IllegalStateException("O usu�rio n�o � um cliente");
		}
		
		return (Cliente) loggedUser.getUsuario();
	}
	
	public static Restaurante loggedRestaurante() {
		
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			
			throw new IllegalStateException("N�o existe um usu�rio logado");
		}
		
		if (!(loggedUser.getUsuario() instanceof Restaurante)) {
			
			throw new IllegalStateException("O usu�rio n�o � um Restaurante");
		}
		
		return (Restaurante) loggedUser.getUsuario();
	}
}
