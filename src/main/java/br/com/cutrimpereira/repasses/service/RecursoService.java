package br.com.cutrimpereira.repasses.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cutrimpereira.repasses.modelo.Form;
import br.com.cutrimpereira.repasses.modelo.Recurso;
import br.com.cutrimpereira.repasses.resources.RecursoRepository;

@Service
public class RecursoService {
	
	
	
	
	@Autowired
	private RecursoRepository recursoRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${KEY_API_DADOS}")
	private String key;


	public void importarDados(Form form){
        int pagina =  1;
        while(true) {
        	try {
				Recurso[] recursos = getRecursos(pagina,form);
				if(recursos != null) {
					save(recursos);
					pagina =  pagina + 1;
				}else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}	
        }
	}

	private Recurso[] getRecursos(Integer pagina,Form form){
		try {
			MultiValueMap<String, String> params =  new LinkedMultiValueMap<String, String>();
			if(form.getMesAnoInicio() != null && !form.getMesAnoInicio().isEmpty()) {
				params.add("mesAnoInicio", form.getMesAnoInicio());
			}
			
       if(form.getMesAnoFim() != null && !form.getMesAnoFim().isEmpty()) {
				
				params.add("mesAnoFim", form.getMesAnoFim());
			}
       if(form.getTipoFavorecido() != null && !form.getTipoFavorecido().isEmpty()) {
			params.add("tipoFavorecido", form.getTipoFavorecido());
		}
       if(form.getUnidadeGestora() != null && !form.getUnidadeGestora().isEmpty()) {
			params.add("unidadeGestora", form.getUnidadeGestora());
		}
		params.add("uf", "MA");
		params.add("pagina", String.valueOf(pagina));
			HttpHeaders headers = new HttpHeaders();
			headers.add("chave-api-dados", key);
			headers.add("Accept", "*/*");
			UriComponents builder = UriComponentsBuilder.fromHttpUrl("http://www.portaltransparencia.gov.br/api-de-dados/despesas/recursos-recebidos")
					.queryParams(params).build(false);
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
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public void save(Recurso[] recs) {
		List<Recurso> arrayToList = CollectionUtils.arrayToList(recs);
		recursoRepository.saveAll(arrayToList);
	}

}
