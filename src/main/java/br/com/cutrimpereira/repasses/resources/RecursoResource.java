package br.com.cutrimpereira.repasses.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cutrimpereira.repasses.modelo.Form;
import br.com.cutrimpereira.repasses.modelo.Recurso;
import br.com.cutrimpereira.repasses.service.RecursoService;

@RestController
@RequestMapping("/recursos")
public class RecursoResource {


	@Autowired
	private RecursoRepository recursoRepository;
	
	@Autowired
	private RecursoService recursoService;
	
	@GetMapping
	private ResponseEntity<List<Recurso>> lista(){
		
		List<Recurso> recursos =  recursoRepository.findAll();
		return ResponseEntity.ok(recursos);
	}
	
	@ResponseStatus(value=HttpStatus.OK)
	@PostMapping
	public void importar(@RequestBody Form form) {
		
		recursoService.importarDados(form);
		
	}


}
