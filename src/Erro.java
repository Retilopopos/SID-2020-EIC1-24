
public class Erro {
	
	String id;
	private Medi��oSensor medi��o;
	private TipoErro tipo;

	public Erro(Medi��oSensor medi��o, String tipo, String id) {
		this.medi��o = medi��o;
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
		return "INSERT INTO `erro`(`ValorMedi��o`, `TipoSensor`, `DataHoraErro`, `C�digoErro`, `Mongo_ID`) VALUES ("
				+ medi��o.getValor() + ", \"" + medi��o.getTipo() + "\", \"" + medi��o.getDataHora() + "\", \""
				+ this.getTipo() + "\", \"" +this.getId() + "\")";
	}
	
	public String toQueryErroSintaxe() {
		return "INSERT INTO `erro`(`ValorMedi��o`, `TipoSensor`, `DataHoraErro`, `C�digoErro`, `Mongo_ID`) VALUES (\""
				+ medi��o.getValorErr() + "\", \"" + medi��o.getTipo() + "\", \"" + medi��o.getDataHora() + "\", \""
				+ this.getTipo() + "\", \"" +this.getId() + "\")";
	}

}
