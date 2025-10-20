package org.serratec.serramusic.controller;

import java.util.List;

import org.serratec.serramusic.domain.Musica;
import org.serratec.serramusic.domain.Playlist;
import org.serratec.serramusic.domain.Usuario;
import org.serratec.serramusic.repository.MusicaRepository;
import org.serratec.serramusic.repository.PlaylistRepository;
import org.serratec.serramusic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/playlists")
@Tag(name = "Playlists", description = "Gerenciamento de playlists e músicas")
public class PlaylistController {

	@Autowired
	private PlaylistRepository playlistRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private MusicaRepository musicaRepository;

	@GetMapping
	@Operation(summary = "Listar todas as playlists", description = "Retorna lista completa de playlists cadastradas")
	public List<Playlist> listarTodas() {
		return playlistRepository.findAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar playlist por ID", description = "Retorna uma playlist específica pelo ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Playlist encontrada"),
			@ApiResponse(responseCode = "404", description = "Playlist não encontrada") })
	public ResponseEntity<Playlist> buscarPorId(@PathVariable Long id) {
		return playlistRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar nova playlist", description = "Cria uma playlist associada a um usuário")
	@ApiResponse(responseCode = "201", description = "Playlist criada com sucesso")
	public Playlist criar(@Valid @RequestBody Playlist playlist) {
		if (playlist.getUsuario() != null && playlist.getUsuario().getId() != null) {
			Usuario usuario = usuarioRepository.findById(playlist.getUsuario().getId())
					.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
			playlist.setUsuario(usuario);
		} else {
			throw new RuntimeException("Usuário deve ser informado ao criar uma playlist");
		}
		return playlistRepository.save(playlist);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar playlist", description = "Atualiza dados e músicas de uma playlist")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Playlist atualizada"),
			@ApiResponse(responseCode = "404", description = "Playlist não encontrada") })
	public ResponseEntity<Playlist> atualizar(@PathVariable Long id, @Valid @RequestBody Playlist playlistAtualizada) {
		return playlistRepository.findById(id).map(playlist -> {
			playlist.setNome(playlistAtualizada.getNome());
			playlist.setDescricao(playlistAtualizada.getDescricao());

			if (playlistAtualizada.getMusicas() != null) {
				playlist.getMusicas().clear();
				for (Musica m : playlistAtualizada.getMusicas()) {
					Musica musicaExistente = musicaRepository.findById(m.getId())
							.orElseThrow(() -> new RuntimeException("Música não encontrada com ID: " + m.getId()));
					playlist.getMusicas().add(musicaExistente);
				}
			}

			return ResponseEntity.ok(playlistRepository.save(playlist));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Deletar playlist", description = "Remove uma playlist do sistema")
	@ApiResponse(responseCode = "204", description = "Playlist deletada")
	public void deletar(@PathVariable Long id) {
		playlistRepository.deleteById(id);
	}
}