import java.sql.Timestamp;
import java.util.Arrays;

public class MediçãoSensor {

	private Double valor;
	private String valorErr;
	private TipoSensor tipo;
	private String dataHora;

	@SuppressWarnings("deprecation")
	MediçãoSensor(Double medição, String tipo, String data, String hora) {
		this.valor = medição;
		this.tipo = TipoSensor.valueOf(TipoSensor.class, tipo);
		int[] dataOg = Arrays.stream(data.split("/")).mapToInt(Integer::parseInt).toArray();
		int[] horaOg = Arrays.stream(hora.split(":")).mapToInt(Integer::parseInt).toArray();
		this.dataHora = new Timestamp(dataOg[2] - 1900, dataOg[1] - 1, dataOg[0], horaOg[0], horaOg[1], horaOg[2], 0)
				.toString().replace(".0", "");
	}
	@SuppressWarnings("deprecation")
	MediçãoSensor(String medição, String tipo, String data, String hora) {
		this.valorErr = medição;
		this.tipo = TipoSensor.valueOf(TipoSensor.class, tipo);
		int[] dataOg = Arrays.stream(data.split("/")).mapToInt(Integer::parseInt).toArray();
		int[] horaOg = Arrays.stream(hora.split(":")).mapToInt(Integer::parseInt).toArray();
		this.dataHora = new Timestamp(dataOg[2] - 1900, dataOg[1] - 1, dataOg[0], horaOg[0], horaOg[1], horaOg[2], 0)
				.toString().replace(".0", "");
	}

	public double getValor() {
		return valor;
	}
	
	public String getValorErr() {
		return valorErr != null? valorErr.substring(0, 9):"";
	}
	
	public TipoSensor getTipo() {
		return tipo;
	}

	public String getDataHora() {
		return dataHora;
	}

	public enum TipoSensor {
		hum, tmp, lum, mov
	}
	
	public String toQuerySP() {
		return "call inserirMedições(" +  this.getValor() + ",\"" +  this.getTipo() + "\",\"" + this.getDataHora() + "\")";
	}
	
	public String toString() {
        return "(" + this.getValor() + ", " + this.getTipo() + ", " + this.getDataHora() + ")";
    }
}
