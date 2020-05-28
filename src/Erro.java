
public class Erro {

	private MediçãoSensor medição;
	private TipoErro tipo;

	public Erro(MediçãoSensor medição, String tipo) {
		this.medição = medição;
		this.tipo = TipoErro.valueOf(tipo);
	}

	public TipoErro getTipo() {
		return tipo;
	}

	public enum TipoErro {
		ERR01, ERR02, ERR03, ERR04, ERR06, ERR07, ERR08, ERRO9
	}

	public String toQuery() {
		return "INSERT INTO `erro`(`ValorMedição`, `TipoSensor`, `DataHoraErro`, `CódigoErro`) VALUES ("
				+ medição.getValor() + ", \"" + medição.getTipo() + "\", \"" + medição.getDataHora() + "\", \""
				+ this.getTipo() + "\")";
	}
	
	public String toQueryErroSintaxe() {
		return "INSERT INTO `erro`(`ValorMedição`, `TipoSensor`, `DataHoraErro`, `CódigoErro`) VALUES (\""
				+ medição.getValorErr() + "\", \"" + medição.getTipo() + "\", \"" + medição.getDataHora() + "\", \""
				+ this.getTipo() + "\")";
	}

}
