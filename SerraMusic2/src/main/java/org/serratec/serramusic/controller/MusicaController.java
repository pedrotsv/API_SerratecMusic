package org.serratec.serramusic.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/musica")
@Tag(name = "Músicas", description = "Gerenciamento de músicas")
public class MusicaController {

	@Autowired
	private MusicaRepository musicaRepository;

	@Autowired
	private ArtistaRepository artistaRepository;

	@GetMapping
	@Operation(summary = "Listar todas as músicas", description = "Retorna lista completa de músicas cadastradas")
	public ResponseEntity<List<Musica>> lista() {
		return ResponseEntity.ok(musicaRepository.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar música por ID", description = "Retorna uma música específica pelo ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Música encontrada"),
			@ApiResponse(responseCode = "404", description = "Música não encontrada") })
	public ResponseEntity<Musica> buscarPorId(@PathVariable Long id) {
		Optional<Musica> musica = musicaRepository.findById(id);
		if (musica.isPresent()) {
			return ResponseEntity.ok(musica.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar nova música", description = "Cadastra uma nova música com seus artistas")
	@ApiResponse(responseCode = "201", description = "Música criada com sucesso")
	public Musica criar(@Valid @RequestBody Musica musica) {
		List<Artista> artistasExistentes = new ArrayList<>();
		for (Artista a : musica.getArtistas()) {
			Artista artista = artistaRepository.findById(a.getId())
					.orElseThrow(() -> new RuntimeException("Artista não encontrado com o ID: " + a.getId()));
			artistasExistentes.add(artista);
		}
		musica.setArtistas(artistasExistentes);
		return musicaRepository.save(musica);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar música", description = "Atualiza dados de uma música existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Música atualizada"),
			@ApiResponse(responseCode = "404", description = "Música não encontrada") })
	public ResponseEntity<Musica> atualizar(@PathVariable Long id, @Valid @RequestBody Musica musica) {
		if (!musicaRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		musica.setId(id);
		musica = musicaRepository.save(musica);
		return ResponseEntity.ok(musica);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deletar música", description = "Remove uma música do sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Música deletada"),
			@ApiResponse(responseCode = "404", description = "Música não encontrada") })
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		if (!musicaRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		musicaRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}