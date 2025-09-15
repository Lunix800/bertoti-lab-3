package com.lanchonete.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class LanchoneteRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LanchoneteRestApiApplication.class, args);
	}

}

@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:5500"})
@RestController
@RequestMapping("/sanduiches") // Endpoint principal agora é /sanduiches
class SanduicheApiController {
	// A lista agora armazena objetos do tipo Sanduiche
	private final List<Sanduiche> sanduiches = new ArrayList<>();

	public SanduicheApiController() {
		// Populando a lista com alguns sanduíches iniciais
		sanduiches.addAll(List.of(
				new Sanduiche("X-Salada"),
				new Sanduiche("Misto Quente"),
				new Sanduiche("Bauru"),
				new Sanduiche("Frango com Catupiry")
		));
	}

	@GetMapping
	Iterable<Sanduiche> getSanduiches() {
		return sanduiches;
	}

	@GetMapping("/{id}")
	Optional<Sanduiche> getSanduichePorId(@PathVariable String id) {
		for (Sanduiche s : sanduiches) {
			if (s.getId().equals(id)) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}

	@PostMapping
	Sanduiche postSanduiche(@RequestBody Sanduiche sanduiche) {
		sanduiches.add(sanduiche);
		return sanduiche;
	}

	@PutMapping("/{id}")
	ResponseEntity<Sanduiche> putSanduiche(@PathVariable String id,
										   @RequestBody Sanduiche sanduiche) {
		int sanduicheIndex = -1;

		for (Sanduiche s : sanduiches) {
			if (s.getId().equals(id)) {
				sanduicheIndex = sanduiches.indexOf(s);
				sanduiches.set(sanduicheIndex, sanduiche);
			}
		}

		// A lógica de criar um novo se o ID não existir foi mantida
		return (sanduicheIndex == -1) ?
				new ResponseEntity<>(postSanduiche(sanduiche), HttpStatus.CREATED) :
				new ResponseEntity<>(sanduiche, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	void deleteSanduiche(@PathVariable String id) {
		sanduiches.removeIf(s -> s.getId().equals(id));
	}
}


class Sanduiche {
	private final String id;
	private String nome;

	public Sanduiche(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Sanduiche(String nome) {
		this(UUID.randomUUID().toString(), nome);
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}