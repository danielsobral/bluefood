package com.daniel.bluefood.domain.usuario;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.daniel.bluefood.util.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public class Usuario implements Serializable {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@NotBlank(message = "O Nome n�o pode ser vazio")
	@Size(max = 80, message = "O nome � muito grande")
	String nome;
	
	@NotBlank(message = "O E-mail n�o pode ser vazio")
	@Size(max = 60, message = "O e-mail � muito grande")
	@Email
	String email;
	
	@NotBlank(message = "A senha n�o pode ser vazia")
	@Size(max = 80, message = "A senha � muito grande")
	String senha;
	
	@NotBlank(message = "O telefone n�o pode ser vazio")
	@Pattern(regexp = "[0-9]{10,11}", message = "O telefone possui formato invalido")
	@Column(length = 11, nullable = false)
	String telefone;
	
	public void ecncryptPassword() {
		this.senha = StringUtils.encrypt(this.senha);
	}
}
