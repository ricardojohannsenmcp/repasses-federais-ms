package br.com.cutrimpereira.repasses.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


@Entity
@Table(schema="cgu",name="recursos_federais_recebidos")
@Data
public class Recurso {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer recursoId;

	private Integer anoMes;
	private String codigoFormatado;
	private String codigoOrgao;
	private String codigoOrgaoSuperior;
	private String codigoUG;
	private String municipioPessoa;
	private String nomeOrgao;
	private String nomeOrgaoSuperior;
	private String nomePessoa;
	private String nomeUG;
	private String siglaUFPessoa;
	private String tipoPessoa;
	private BigDecimal valor;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_importacao")
	private Date dataImportacao;
	
	
	@PrePersist
	public void prePersist() {
		this.dataImportacao =  new Date();
	}

}
