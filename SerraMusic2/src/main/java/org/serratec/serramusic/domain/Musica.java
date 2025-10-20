package org.serratec.serramusic.domain;

import java.util.ArrayList;
import java.util.List;

import org.serratec.serramusic.enums.GeneroMusical;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "musica")
@Schema(description = "Representa uma música no sistema")
public class Musica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_musica")
	@Schema(description = "ID único da música", example = "1")
	private Long id;
	
	@Column(name = "titulo_musica", nullable = false, length = 50)
	@NotBlank(message = "Título obrigatório")
	@Size(max = 50, message = "Máximo de 50 caracteres.")
	@Schema(description = "Título da música", example = "Garota de Ipanema")
	private String titulo;
	
	@Column(name = "minutos_musica", nullable = false)
	@NotNull(message = "Minutagem obrigatória")
	@Schema(description = "Duração em minutos", example = "5")
	private Integer minutos;
	
	@Column(name = "genero_musica", nullable = false)
	@NotNull(message = "Gênero obrigatório.") // ✅ CORRIGIDO - Era @NotBlank
	@Enumerated(EnumType.STRING)
	@Schema(description = "Gênero musical", example = "SAMBA")
	private GeneroMusical genero;
	
	@ManyToMany
	@JoinTable(
		name = "musica_artista", 
		joinColumns = @JoinColumn(name = "id_musica"), 
		inverseJoinColumns = @JoinColumn(name = "id_artista")
	)
	@Schema(description = "Lista de artistas da música")
	private List<Artista> artistas = new ArrayList<>();
	
	@ManyToMany(mappedBy = "musicas")
	@JsonBackReference
	@Schema(hidden = true)
    private List<Playlist> playlists = new ArrayList<>();

	public Musica() {}

	public Musica(Long id, String titulo, Integer minutos, GeneroMusical genero) {
		this.id = id;
		this.titulo = titulo;
		this.minutos = minutos;
		this.genero = genero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getMinutos() {
		return minutos;
	}

	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}

	public GeneroMusical getGenero() {
		return genero;
	}

	public void setGenero(GeneroMusical genero) {
		this.genero = genero;
	}

	public List<Artista> getArtistas() {
		return artistas;
	}

	public void setArtistas(List<Artista> artistas) {
		this.artistas = artistas;
	}

	public List<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}
}