package org.serratec.serramusic.controller;

import java.util.ArrayList;
import java.util.List;

import org.serratec.serramusic.domain.Artista;
import org.serratec.serramusic.domain.Musica;
import org.serratec.serramusic.repository.ArtistaRepository;
import org.serratec.serramusic.repository.MusicaRepository;
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
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/artista")
@Tag(name = "Artistas", description = "Gerenciamento de artistas musicais")
public class ArtistaController {

	@Autowired
	private ArtistaRepository artistaRepository;

	@Autowired
	private MusicaRepository musicaRepository;

	@GetMapping
	@Operation(summary = "Listar todos os artistas", description = "Retorna lista completa de artistas cadastrados")
	public List<Artista> listarTodos() {
		return artistaRepository.findAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar artista por ID", description = "Retorna um artista específico pelo ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Artista encontrado"),
			@ApiResponse(responseCode = "404", description = "Artista não encontrado") })
	public ResponseEntity<Artista> buscarPorId(@PathVariable Long id) {
		return artistaRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar novo artista", description = "Cadastra um novo artista no sistema")
	@ApiResponse(responseCode = "201", description = "Artista criado com sucesso")
	public Artista criar(@Valid @RequestBody Artista artista) {
		return artistaRepository.save(artista);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar artista", description = "Atualiza dados de um artista existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Artista atualizado"),
			@ApiResponse(responseCode = "404", description = "Artista não encontrado") })
	public ResponseEntity<Artista> atualizar(@PathVariable Long id, @Valid @RequestBody Artista artistaAtualizado) {
		return artistaRepository.findById(id).map(artista -> {
			artista.setNome(artistaAtualizado.getNome());
			artista.setNacionalidade(artistaAtualizado.getNacionalidade());
			return ResponseEntity.ok(artistaRepository.save(artista));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Deletar artista", description = "Remove um artista e suas músicas associadas")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Artista deletado"),
			@ApiResponse(responseCode = "404", description = "Artista não encontrado") })
	public void deletar(@PathVariable Long id) {
		var opt = artistaRepository.findById(id);
		if (opt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista não encontrado");
		}

		Artista artista = opt.get();
		List<Musica> musicas = new ArrayList<>(artista.getMusicas());

		for (Musica m : musicas) {
			m.getArtistas().remove(artista);
			if (m.getArtistas().isEmpty()) {
				musicaRepository.delete(m);
			} else {
				musicaRepository.save(m);
			}
		}

		artistaRepository.delete(artista);
	}
}