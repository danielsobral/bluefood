package com.daniel.bluefood.domain.cliente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.daniel.bluefood.domain.usuario.Usuario;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {

	@NotBlank(message = "O CPF não pode ser vazio")
	@Pattern(regexp = "[0-9]{11}", message = "O valor do CPF é invalido")
	@Column(length = 11, nullable = false)
	String cpf;
	
	@NotBlank(message = "o CEP não pode ser vazio")
	@Pattern(regexp = "[0-9]{8}", message = "O valor digitado esta incorreto")
	@Column(length = 8, nullable = false)
	String cep;
}
