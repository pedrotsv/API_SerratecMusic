package org.serratec.serramusic.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "perfil")
@Schema(description = "Perfil do usuário com dados pessoais")
public class Perfil {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_perfil")
	@Schema(description = "ID único do perfil", example = "1")
	private Long id;

	@Column(nullable = false)
	@NotBlank(message = "Telefone obrigatório.")
	@Schema(description = "Telefone do usuário", example = "21987654321")
	private String telefone;

	@Column(name = "data_nascimento", nullable = false)
	@NotNull(message = "Data de nascimento obrigatória.")
	@Schema(description = "Data de nascimento", example = "1990-01-15")
	private LocalDate dataNascimento;

	@OneToOne(mappedBy = "perfil")
	@JsonBackReference
	@Schema(hidden = true)
	private Usuario usuario;

	public Perfil() {
	}

	public Perfil(Long id, String telefone, LocalDate dataNascimento) {
		this.id = id;
		this.telefone = telefone;
		this.dataNascimento = dataNascimento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}