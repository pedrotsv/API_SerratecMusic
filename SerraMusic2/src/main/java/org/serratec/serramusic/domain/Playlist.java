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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "playlist")
@Schema(description = "Playlist de músicas do usuário")
public class Playlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_playlist")
	@Schema(description = "ID único da playlist", example = "1")
	private Long id;

	@Column(name = "nome_playlist", nullable = false, length = 50)
	@NotBlank(message = "Nome da playlist obrigatório.")
	@Size(max = 50, message = "Máximo de 50 caracteres.")
	@Schema(description = "Nome da playlist", example = "MPB Clássicos")
	private String nome;

	@Column(name = "descricao_playlist", length = 100)
	@Size(max = 100, message = "Máximo de 100 caracteres.")
	@Schema(description = "Descrição da playlist", example = "As melhores músicas da MPB")
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@JsonBackReference
	@Schema(description = "Usuário dono da playlist")
	private Usuario usuario;

	@ManyToMany
	@JoinTable(name = "playlist_musica", joinColumns = @JoinColumn(name = "playlist_id"), inverseJoinColumns = @JoinColumn(name = "musica_id"))
	@Schema(description = "Lista de músicas na playlist")
	private List<Musica> musicas = new ArrayList<>();

	public Playlist() {
	}

	public Playlist(Long id, String nome, String descricao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Musica> getMusicas() {
		return musicas;
	}

	public void setMusicas(List<Musica> musicas) {
		this.musicas = musicas;
	}
}