/*
Cenário de Negócio:
Todo dia útil por volta das 6 horas da manhã um colaborador da retaguarda do Sicredi recebe e organiza as informações de 
contas para enviar ao Banco Central. Todas agencias e cooperativas enviam arquivos Excel à Retaguarda. Hoje o Sicredi 
já possiu mais de 4 milhões de contas ativas.
Esse usuário da retaguarda exporta manualmente os dados em um arquivo CSV para ser enviada para a Receita Federal, 
antes as 10:00 da manhã na abertura das agências.

Requisito:
Usar o "serviço da receita" (fake) para processamento automático do arquivo.

Funcionalidade:
0. Criar uma aplicação SprintBoot standalone. Exemplo: java -jar SincronizacaoReceita <input-file>
1 .Processa um arquivo CSV de entrada com o formato abaixo.
2. Envia a atualização para a Receita através do serviço (SIMULADO pela classe ReceitaService).
3. Retorna um arquivo com o resultado do envio da atualização da Receita. Mesmo formato adicionando o resultado em uma 
nova coluna.


Formato CSV:
agencia;conta;saldo;status
0101;12225-6;100,00;A
0101;12226-8;3200,50;A
3202;40011-1;-35,12;I
3202;54001-2;0,00;P
3202;00321-2;34500,00;B
...

*/
package com.sicredi.receita.sincronizacaoreceita;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.sicredi.receita.models.ReceitaModel;
import com.sicredi.receita.service.ReceitaService;

public class SincronizacaoReceita {
	
	//variaveis staticas com local de leitura e armazenamento dos arquivos
	private static final String CSV_FILE_WRITER = "C:/temp/receitaGerado.csv" ;
	private static final String CSV_FILE_READER = "C:/temp/receita.csv";

	public static void main(String[] args) throws RuntimeException, InterruptedException, IOException, CsvException {
		
		try {
	        // cria o objeto file reader.
	        FileReader fileReader = new FileReader(CSV_FILE_READER);
	 
	        // cria o csvParser para separar colunas vindas com virgula do arquivo csv
	        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
	 
	        // cria o objeto csvReader com os parametros fileReader e parser
	        CSVReader csvReader = new CSVReaderBuilder(fileReader)
	                                  .withCSVParser(parser)
	                                  .build();
	 
	        // carrega o conteudo do arquivo em uma lista de strings
	        List<String[]> conteudoLido = csvReader.readAll();
	 
	        // Popula os campos do model
	        List<ReceitaModel> receitas = new ArrayList<>();
	        for (String[] linha : conteudoLido) {
				if(!linha[2].contains("saldo")) {
					Double saldo = Double.parseDouble(linha[2].replaceAll(",","."));
					receitas.add(new ReceitaModel(linha[0],linha[1],saldo,linha[3],null));
				}
	        	
	        }
	        
	        //cria uma lista de strings para exportar para o arquivo csv que sera gerado
	        List<String[]> listaString = new ArrayList<String[]>();		
	        
	        //constroi e inclui o cabecalho a lista
			String[] cabecalho = {"agencia","conta", "saldo", "status", "resultado"};
			listaString.add(cabecalho);
			
			//variavel auxiliar 
			String contaSemTraco = "";
			
			for (ReceitaModel receita : receitas) {
				//completar com um zero a esquerda, pode vim do arquivo csv sem o zero.
				if(receita.getAgencia().length() == 3) {
					receita.setAgencia(String.format ("%04d", Integer.parseInt(receita.getAgencia())));
				}
				
				//retirar o caracter "-" da conta
				if(receita.getConta().contains("-")) {
					contaSemTraco =  receita.getConta().replaceAll("[^0-9.]", "");
				}
				
				//setando o resultado de acordo com os campos
				ReceitaService receitaService = new ReceitaService();
				receita.setResultado(receitaService.atualizarConta(receita.getAgencia(), contaSemTraco,receita.getSaldo(), receita.getStatus()));	
//				System.out.println("Agencia="+receita.getAgencia()+ "Conta=" +receita.getConta()+ "Saldo=" +receita.getSaldo()+
//						"Status=" +receita.getStatus()+ "Resultado=" +receita.getResultado());
				
				//formatar saldo com duas casas apos virgula
				String saldoFormatadoComDuasCasas = "";
				if(String.valueOf(receita.getSaldo()).contains(".")) {
					DecimalFormat df = new DecimalFormat("0.00"); 
					saldoFormatadoComDuasCasas = df.format(receita.getSaldo());
				}
				//adicionando campos da linha a lista de geracao do arquivo de saida
				String[] item = {receita.getAgencia(), receita.getConta(), saldoFormatadoComDuasCasas, receita.getStatus(),receita.getResultado().toString()};			
				listaString.add(item);
			}
			
			
			//gerar arquivo atualizado csv
			//criando objeto fileWriter com o parametro do caminho a ser gravado o arquivo
			FileWriter fileWriter = new FileWriter(new File(CSV_FILE_WRITER));
			
			//criando csvWriter com o caracter ; separando os campos
			CSVWriter csvWriter = new CSVWriter(fileWriter, ';',
												CSVWriter.NO_QUOTE_CHARACTER,
												CSVWriter.DEFAULT_ESCAPE_CHARACTER,
												CSVWriter.DEFAULT_LINE_END);			
			
			//incluido lista de strings ao csvWriter
			csvWriter.writeAll(listaString);
			
			//fechando cenexoes
			csvWriter.close();
			fileWriter.close();
			
			JOptionPane.showMessageDialog(null, "ARQUIVO GERADO COM SUCESSO, VÁ ATE C:/TEMP E VEJA O ARQUIVO COM RESULTADO!!!!");
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }

	}

}
