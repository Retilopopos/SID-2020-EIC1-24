public class Alerta {

	private Medi��oSensor medi��o;
	private Double limite;
	private String desc;

	public Alerta(Medi��oSensor medi��o, Double limite, String desc) {
		this.medi��o = medi��o;
		this.limite = limite;
		this.desc = desc;
	}

	public Double getLimite() {
		return limite;
	}

	public String getDesc() {
		return desc;
	}

	public String toQuery() {
		return "INSERT INTO `alerta`(`DataHoraMedicao`, `TipoSensor`, `ValorMedicao`, `Limite`, `Descricao`, `Controlo`, `Extra`) VALUES (\""
				+ medi��o.getDataHora() + "\", \"" + medi��o.getTipo() + "\", " + medi��o.getValor() + ", "
				+ this.getLimite() + ", \"" + this.getDesc() + "\", 0, \"\")";
	}

}
