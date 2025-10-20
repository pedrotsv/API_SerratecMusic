package org.serratec.serramusic.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table
@Schema(description = "Representa um artista musical")
public class Artista {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_artista")
	@Schema(description = "ID único do artista", example = "1")
	private Long id;

	@Column(name = "nome_artista", nullable = false, unique = true, length = 50)
	@NotBlank(message = "Nome obrigatório")
	@Size(max = 50, message = "Máximo de 50 caracteres.")
	@Schema(description = "Nome do artista", example = "Tom Jobim")
	private String nome;

	@Column(name = "nacionalidade_artista")
	@NotNull(message = "Nacionalidade obrigatória.")
	@Schema(description = "Nacionalidade do artista", example = "Brasileira")
	private String nacionalidade;

	@ManyToMany(mappedBy = "artistas")
	@JsonBackReference
	@Schema(hidden = true)
	private List<Musica> musicas = new ArrayList<>();

	public Artista() {
	}

	public Artista(Long id, String nome, String nacionalidade) {
		this.id = id;
		this.nome = nome;
		this.nacionalidade = nacionalidade;
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

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public List<Musica> getMusicas() {
		return musicas;
	}

	public void setMusicas(List<Musica> musicas) {
		this.musicas = musicas;
	}
}
