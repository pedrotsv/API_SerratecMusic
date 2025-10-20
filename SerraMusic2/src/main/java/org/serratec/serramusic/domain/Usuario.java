package org.serratec.serramusic.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario")
@Schema(description = "Representa um usuário do sistema")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	@Schema(description = "ID único do usuário", example = "1")
	private Long id;

	@Column(name = "nome_usuario", nullable = false, length = 50)
	@NotBlank(message = "Nome obrigatório.")
	@Size(max = 50, message = "Máximo 50 caracteres.")
	@Schema(description = "Nome do usuário", example = "João Silva")
	private String nome;

	@Column(name = "email_usuario", nullable = false, unique = true)
	@NotBlank(message = "Email obrigatório.")
	@Email(message = "Formato do email inválido.")
	@Schema(description = "Email do usuário", example = "joao@email.com")
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_perfil")
	@Valid
	@JsonManagedReference
	@Schema(description = "Perfil do usuário")
	private Perfil perfil;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@Schema(hidden = true)
	private List<Playlist> playlists = new ArrayList<>();

	public Usuario() {
	}

	public Usuario(Long id, String nome, String email) {
		this.id = id;
		this.nome = nome;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
		if (perfil != null) {
			perfil.setUsuario(this);
		}
	}

	public List<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}
}