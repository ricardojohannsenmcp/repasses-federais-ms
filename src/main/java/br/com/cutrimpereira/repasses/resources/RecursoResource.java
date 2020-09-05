package br.com.cutrimpereira.repasses.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cutrimpereira.repasses.modelo.Recurso;

@RestController
@RequestMapping("/recursos")
public class RecursoResource {

	@Autowired
	private RecursoRepository recursoRepository;



	@Autowired
	private RestTemplate restTemplate;








	@GetMapping("/fetch")
	public ResponseEntity<String> popula() throws IOException {


	
        int pagina =  1;
        
        
        while(true) {
        	
        	Recurso[] recursos = getRecursos(pagina);
        	
        	if(recursos != null) {
        		
        		save(recursos);
        		pagina =  pagina + 1;
        	}else {
        		break;
        	}
        	
        	
        }
		
		
		return ResponseEntity.ok("ok");
	}




	private Recurso[] getRecursos(Integer pagina){

		System.out.println("====== Pagina -> "+pagina);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("chave-api-dados", "");
			headers.add("Accept", "*/*");


			UriComponents builder = UriComponentsBuilder.fromHttpUrl("http://www.portaltransparencia.gov.br/api-de-dados/despesas/recursos-recebidos")
					.queryParam("mesAnoInicio", "01/2020")
					.queryParam("mesAnoFim", "09/2020")
					//.queryParam("nomeFavorecido", "Administração Pública Municipal")
					.queryParam("tipoFavorecido", "Fundo Público")
					.queryParam("uf", "MA")
					.queryParam("pagina", pagina).build(false);
			HttpEntity<?> request = new HttpEntity<>(headers);
			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,request,String.class);
			if(!response.hasBody()) {
				return null;
			}
			ObjectMapper mapper = new ObjectMapper();
			Recurso[] arr = mapper.readValue(response.getBody(), Recurso[].class);
			return arr;
		} catch (RestClientException | JsonProcessingException e) {
			
			throw new RuntimeException(e);
		}

	}







	@GetMapping
	public void importar() throws IOException {



		URL url = new URL("http://www.portaltransparencia.gov.br/api-de-dados/despesas/recursos-recebidos?mesAnoInicio=01%2F2020&mesAnoFim=09%2F2020&tipoFavorecido=Fundo%20P%C3%BAblico&uf=MA&pagina=1");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "*/*");
		con.setRequestProperty("chave-api-dados", "");

		BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream()));

		String inputLine;

		//save to this filename
		String fileName = "/home/ricardo/test.json";
		File file = new File(fileName);

		if (!file.exists()) {
			file.createNewFile();
		}

		//use FileWriter to write file
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		while ((inputLine = br.readLine()) != null) {
			bw.write(inputLine);
		}

		bw.close();
		br.close();

		System.out.println("Done");









	}

	@Transactional
	public void save(Recurso[] recs) {
		
		List<Recurso> arrayToList = CollectionUtils.arrayToList(recs);
		recursoRepository.saveAll(arrayToList);
	}



	//Administração Pública Municipal
	//Fundo Público
	//	headers.add("Accept", "*/*");
}
