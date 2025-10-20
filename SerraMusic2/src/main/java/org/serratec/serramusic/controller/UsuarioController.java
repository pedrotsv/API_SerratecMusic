package org.serratec.serramusic.controller;

import java.util.List;
import java.util.Optional;

import org.serratec.serramusic.domain.Perfil;
import org.serratec.serramusic.domain.Usuario;
import org.serratec.serramusic.repository.PerfilRepository;
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
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuários", description = "Gerenciamento de usuários e perfis")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private PlaylistRepository playlistRepository;

	@GetMapping
	@Operation(summary = "Listar todos os usuários", description = "Retorna lista completa de usuários cadastrados")
	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
	@ApiResponse(responseCode = "404", description = "Usuário não encontrado") })
	public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar novo usuário", description = "Cadastra um novo usuário com perfil aninhado")
	@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
	public Usuario criar(@Valid @RequestBody Usuario usuario) {
		Perfil perfil = usuario.getPerfil();
		if (perfil == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Perfil é obrigatório no cadastro do usuário.");
		}
		usuario.setPerfil(perfil);
		perfil.setUsuario(usuario);
		return usuarioRepository.save(usuario);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar usuário", description = "Atualiza dados de um usuário existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado") })
	public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario autualizarUsuario) {
		return usuarioRepository.findById(id).map(existing -> {
			existing.setNome(autualizarUsuario.getNome());
			existing.setEmail(autualizarUsuario.getEmail());
			if (autualizarUsuario.getPerfil()!= null) {
				Perfil novoPerfil = autualizarUsuario.getPerfil();
				novoPerfil.setUsuario(existing);
				existing.setPerfil(novoPerfil);
			}
			Usuario salvo = usuarioRepository.save(existing);
			return ResponseEntity.ok(salvo);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deletar usuário", description = "Remove um usuário e suas playlists/perfil associados")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Usuário deletado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado") })
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		Optional<Usuario> opt = usuarioRepository.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Usuario usuario = opt.get();

		if (usuario.getPlaylists()!= null && !usuario.getPlaylists().isEmpty()) {
			usuario.getPlaylists().forEach(playlist -> {
				if (playlist.getMusicas()!= null) {
					playlist.getMusicas().forEach(m -> m.getPlaylists().remove(playlist));
					playlist.getMusicas().clear();
				}
				playlistRepository.delete(playlist);
			});
		}

		if (usuario.getPerfil()!= null) {
			Perfil perfil = usuario.getPerfil();
			perfilRepository.delete(perfil);
		}

		usuarioRepository.delete(usuario);
		return ResponseEntity.noContent().build();
	}
}