package org.serratec.serramusic.controller;

import java.util.List;
import java.util.Optional;

import org.serratec.serramusic.domain.Perfil;
import org.serratec.serramusic.repository.PerfilRepository;
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
@RequestMapping("/perfis")
@Tag(name = "Perfis", description = "Gerenciamento de perfis de usuários")
public class PerfilController {

	@Autowired
	private PerfilRepository perfilRepository;

	@GetMapping
	@Operation(summary = "Listar todos os perfis", description = "Retorna lista completa de perfis cadastrados")
	public ResponseEntity<List<Perfil>> lista() {
		return ResponseEntity.ok(perfilRepository.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar perfil por ID", description = "Retorna um perfil específico pelo ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
			@ApiResponse(responseCode = "404", description = "Perfil não encontrado") })
	public ResponseEntity<Perfil> buscarPorId(@PathVariable Long id) {
		Optional<Perfil> perfil = perfilRepository.findById(id);
		if (perfil.isPresent()) {
			return ResponseEntity.ok(perfil.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar novo perfil", description = "Cadastra um novo perfil")
	@ApiResponse(responseCode = "201", description = "Perfil criado com sucesso")
	public Perfil criar(@Valid @RequestBody Perfil perfil) {
		return perfilRepository.save(perfil);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar perfil", description = "Atualiza dados de um perfil existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Perfil atualizado"),
			@ApiResponse(responseCode = "404", description = "Perfil não encontrado") })
	public ResponseEntity<Perfil> atualizar(@PathVariable Long id, @Valid @RequestBody Perfil perfilUpdate) {
		return perfilRepository.findById(id).map(existing -> {
			existing.setTelefone(perfilUpdate.getTelefone());
			existing.setDataNascimento(perfilUpdate.getDataNascimento());
			Perfil salvo = perfilRepository.save(existing);
			return ResponseEntity.ok(salvo);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deletar perfil", description = "Remove um perfil do sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Perfil deletado"),
			@ApiResponse(responseCode = "404", description = "Perfil não encontrado") })
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		if (!perfilRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		perfilRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
