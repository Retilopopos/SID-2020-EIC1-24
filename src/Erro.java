
public class Erro {
	
	String id;
	private MediçãoSensor medição;
	private TipoErro tipo;

	public Erro(MediçãoSensor medição, String tipo, String id) {
		this.medição = medição;
		this.tipo = TipoErro.valueOf(tipo);
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public TipoErro getTipo() {
		return tipo;
	}

	public enum TipoErro {
		ERR01, ERR02, ERR03, ERR04, ERR06, ERR07, ERR08, ERRO9
	}

	public String toQuery() {
		return "INSERT INTO `erro`(`ValorMedição`, `TipoSensor`, `DataHoraErro`, `CódigoErro`, `Mongo_ID`) VALUES ("
				+ medição.getValor() + ", \"" + medição.getTipo() + "\", \"" + medição.getDataHora() + "\", \""
				+ this.getTipo() + "\", \"" +this.getId() + "\")";
	}
	
	public String toQueryErroSintaxe() {
		return "INSERT INTO `erro`(`ValorMedição`, `TipoSensor`, `DataHoraErro`, `CódigoErro`, `Mongo_ID`) VALUES (\""
				+ medição.getValorErr() + "\", \"" + medição.getTipo() + "\", \"" + medição.getDataHora() + "\", \""
				+ this.getTipo() + "\", \"" +this.getId() + "\")";
	}

}
