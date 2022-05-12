package com.sicredi.receita.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReceitaModel {
	
	@Getter
	@Setter
	private String agencia;
	
	@Getter
	@Setter
	private String conta;
	
	@Getter
	@Setter
	private Double saldo;
	
	@Getter
	@Setter
	private String status;
	
	@Getter
	@Setter
	private Boolean resultado;
}
