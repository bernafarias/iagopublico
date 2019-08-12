package br.com.sgdw.service.scheduling;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.sgdw.service.UpdateServ;

@Component
public class UpdateTask {
	
	static final Logger log = Logger.getLogger(UpdateTask.class); 

	@Autowired
	private UpdateServ serv;
		
	/**
	 * "0 0 * * * *" = o topo de cada hora de cada dia.
	 * "* / 10 * * * * *" = a cada dez segundos.
	 * "0 5 * * * ?"= a cada 5 minutos
	 * "0 0 8-10 * * *" = 8, 9 e 10 horas de cada dia.
	 * "0 * 6,19 * * *" = 6:00 AM e 7:00 PM diariamente.
	 * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 e 10 horas diariamente.
	 * "0 0 9-17 * * MON-FRI" = na hora de nove a cinco dias da semana
	 * "0 0 0 25 12?" = Dia de Natal à meia-noite
	 * s m h d m dayweek
	 * seis campos separados por espaço: representando segundo, minuto, hora, dia, mês, dia da semana
	 * @throws ParseException
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public void atualizarBase() throws ParseException{	
		log.info("Executando job de atualização dos conjuntos de dados...");
		this.serv.atualizarDatasetsAuto();
	}
}
