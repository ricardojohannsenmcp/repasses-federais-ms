package br.com.cutrimpereira.repasses.modelo;

import lombok.Data;

@Data
public class Form {
	
	
	//"01/2020"
	private String mesAnoInicio;
	
	private String mesAnoFim;
	
	//"Administração Pública Municipal"
	//"Fundo Público"
	private String tipoFavorecido;
	
	//153173   FNDE
	private String unidadeGestora;
	
	

}
