public class Alerta {

	private MediçãoSensor medição;
	private Double limite;
	private String desc;

	public Alerta(MediçãoSensor medição, Double limite, String desc) {
		this.medição = medição;
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
				+ medição.getDataHora() + "\", \"" + medição.getTipo() + "\", " + medição.getValor() + ", "
				+ this.getLimite() + ", \"" + this.getDesc() + "\", 0, \"\")";
	}

}
