package br.com.sgdw.util.constantes;

public enum Frequency {

	POR_HORA(0),
	DIARIO(1),
	SEMANAL(2),
	MENSAL(3),
	SEMESTRAL(4),
	ANUAL(5),
	STATIC(6);
	
	private Integer valor;
	
	Frequency(Integer valor) {
		this.valor = valor;
	}
	
	public Integer valor(){
		return valor;
	}
	
	public static Frequency getFrequency(String f){
		
		Frequency frequency;
		
		switch(f){

		case "POR_HORA":
				frequency = POR_HORA;
				break;

		case "DIARIO":
				frequency = DIARIO;
				break;

		case "SEMANAL":
				frequency = SEMANAL;
				break;

		case "MENSAL":
				frequency = MENSAL;
				break;

		case "SEMESTRAL":
				frequency = SEMESTRAL;
				break;

		case "ANUAL":
				frequency = ANUAL;
				break;

		case "STATIC":
				frequency = STATIC;
				break;

		default:
				frequency = null;
				break;
		}
		return frequency;
	}
}
