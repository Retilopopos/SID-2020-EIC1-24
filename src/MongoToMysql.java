import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TimeZone;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;

public class MongoToMysql {

	Queue<MediçãoSensor> waitingList = new LinkedList<MediçãoSensor>();

	String mongo_host = "mongodb://localhost:26017,localhost:25017,localhost:23017";
	String mongo_database = "Museu";
	String mongo_collection = "DadosSensores";

	String mysql_host = "localhost";
	String mysql_database = "g24_origem";
	String mysql_user = "root";
	String mysql_password = "";

	static MongoClient mongoClient;
	static MongoDatabase mongoDatabase;
	static MongoCollection<Document> mongoCollection;

	Double oldTmp = -1.0;
	Double oldHum = -1.0;
	Double oldLum = -1.0;
	Double oldMov = -1.0;
	boolean alertaLimiteUltrapassadoTemperatura = false;
	boolean alertaPerigoTemperatura = false;
	boolean alertaLimiteUltrapassadoDeHumidade = false;
	boolean alertaPerigoDeHumidade = false;

	//Variáveis de Sistema
	Double limiteTemperatura;
	Double temperaturaPerigo;
	
	Double limiteHumidade;
	Double humidadePerigo;
	
	Double variacaoValorErro;
	
	int maximoErrosPermitidos;
	
	//Counters
	int nErrosConsecutivosTemperatura = 0;
	int nErrosConsecutivosDeHumidade = 0;

	private Connection mysqlConn;
	private Statement mysqlSt;
	private CallableStatement mysqlSP;
	private Statement erroData;

	public void getSistemParameters() {
		try {
			String selectData = "SELECT LimiteTemperatura, LimiteHumidade, TemperaturaPerigo, HumidadePerigo, VariaçãoValorErro, MaximoErrosPermitidos FROM sistema";
			erroData = mysqlConn.createStatement();
			ResultSet rs = erroData.executeQuery(selectData);
			while(rs.next()) {
				limiteTemperatura = rs.getDouble("LimiteTemperatura");
				temperaturaPerigo = rs.getDouble("TemperaturaPerigo");
				limiteHumidade = rs.getDouble("LimiteHumidade");
				humidadePerigo = rs.getDouble("HumidadePerigo");
				variacaoValorErro = rs.getDouble("VariaçãoValorErro");
				maximoErrosPermitidos = rs.getInt("MaximoErrosPermitidos");				
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void connectToMysql() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection(
					"jdbc:mysql://localhost/g24_origem?serverTimezone=" + TimeZone.getDefault().getID(), "root", "");
			mysqlSt = mysqlConn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connectToMongo() {
		mongoClient = MongoClients.create(mongo_host);
		mongoDatabase = mongoClient.getDatabase(mongo_database);
		mongoCollection = mongoDatabase.getCollection(mongo_collection);
	}

	@SuppressWarnings("unused")
	private void watchForInserts() {

		MongoCursor<ChangeStreamDocument<Document>> cursor = mongoCollection.watch().iterator();
		while (cursor.hasNext()) {
			Document result = cursor.next().getFullDocument();
			System.out.println(result);
			Double newTmp = -1.0;
			Double newHum = -1.0;
			Double newLum = -1.0;
			Double newMov = -1.0;

			String dat = result.getString("dat");
			String tim = result.getString("tim");
			ObjectId objId = result.getObjectId("_id");
			String id = objId.toString();

			MediçãoSensor tmp = null;
			MediçãoSensor hum = null;
			MediçãoSensor lum = null;
			MediçãoSensor mov = null;

			try {
				newTmp = Double.parseDouble(result.getString("tmp"));
			} catch (Exception e) {
				try {
					String newTmpErr = result.getString("tmp");
					tmp = new MediçãoSensor(newTmpErr, "tmp", dat, tim, id);
					mysqlSt.executeUpdate(new Erro(tmp, "ERR01", id).toQueryErroSintaxe());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			try {
				newHum = Double.parseDouble(result.getString("hum"));
			} catch (Exception e) {
				try {
					String newHumErr = result.getString("hum");
					hum = new MediçãoSensor(newHumErr, "hum", dat, tim, id);
					mysqlSt.executeUpdate(new Erro(hum, "ERR02", id).toQueryErroSintaxe());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			try {
				newMov = Double.parseDouble(result.getString("mov"));
			} catch (Exception e) {
				try {
					String newMovErr = result.getString("mov");
					mov = new MediçãoSensor(newMovErr, "mov", dat, tim, id);
					mysqlSt.executeUpdate(new Erro(mov, "ERR03", id).toQueryErroSintaxe());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			try {
				newLum = Double.parseDouble(result.getString("cell"));
			} catch (Exception e) {
				try {
					String newLumErr = result.getString("lum");
					lum = new MediçãoSensor(newLumErr, "lum", dat, tim, id);
					mysqlSt.executeUpdate(new Erro(lum, "ERR04", id).toQueryErroSintaxe());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

			if (newTmp != -1.0) {
				tmp = new MediçãoSensor(newTmp, "tmp", dat, tim, id);

				Double variacao = Math.abs(1 - (Math.max(oldTmp, newTmp) / Math.min(oldTmp, newTmp)));
				if ((oldTmp == -1.0 || variacao < variacaoValorErro)|| nErrosConsecutivosTemperatura >= maximoErrosPermitidos) {

					System.out.println(tmp);
					// Enviar erros do buffer para a tabela Erros
					if (nErrosConsecutivosTemperatura < maximoErrosPermitidos) {
						while (!waitingList.isEmpty()) {
							try {
								mysqlSt.executeUpdate(new Erro(waitingList.remove(), "ERR05", id).toQuery());
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					} else {
						while (!waitingList.isEmpty()) {
							try {
								mysqlSP = mysqlConn.prepareCall(waitingList.remove().toQuerySP());
								mysqlSP.execute();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
					// Leitura correta, envio para tabela MedicaoSensor
					try {
						mysqlSP = mysqlConn.prepareCall(tmp.toQuerySP());
						mysqlSP.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					nErrosConsecutivosTemperatura = 0;
					oldTmp = newTmp;

				} else {
					// Controlo de erros
					waitingList.add(tmp);
					nErrosConsecutivosTemperatura++;
				}

			}
			if (newHum != -1.0) {
				hum = new MediçãoSensor(newHum, "hum", dat, tim, id);

				Double variacao = Math.abs(1 - (Math.max(oldHum, newHum) / Math.min(oldHum, newHum)));
				if ((oldHum == -1.0 || variacao < variacaoValorErro)
						|| nErrosConsecutivosDeHumidade >= maximoErrosPermitidos) {
					// Leitura correta, envio para tabela MedicaoSensor
					System.out.println(hum);
					// Enviar erros do buffer para a tabela Erros
					if (nErrosConsecutivosDeHumidade < maximoErrosPermitidos) {
						while (!waitingList.isEmpty()) {
							try {
								mysqlSt.executeUpdate(new Erro(waitingList.remove(), "ERR06", id).toQuery());
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					} else {
						while (!waitingList.isEmpty()) {
							try {
								mysqlSP = mysqlConn.prepareCall(waitingList.remove().toQuerySP());
								mysqlSP.execute();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
					try {
						mysqlSP = mysqlConn.prepareCall(hum.toQuerySP());
						mysqlSP.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					nErrosConsecutivosDeHumidade = 0;
					oldHum = newHum;

				} else {
					// Controlo de erros
					waitingList.add(hum);
					nErrosConsecutivosDeHumidade++;
				}
			}

			if (newMov == 0.0 || newMov == 1.0) {
				mov = new MediçãoSensor(newMov, "mov", dat, tim, id);
				try {
					mysqlSP = mysqlConn.prepareCall(mov.toQuerySP());
					mysqlSP.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				mov = new MediçãoSensor(newMov, "mov", dat, tim, id);
				try {
					mysqlSt.executeUpdate(new Erro(mov, "ERR03", id).toQuery());
					mysqlSP.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (newLum >= 0.0) {
				lum = new MediçãoSensor(newLum, "lum", dat, tim, id);
				try {
					mysqlSP = mysqlConn.prepareCall(lum.toQuerySP());
					mysqlSP.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (newLum != -1.0){
				lum = new MediçãoSensor(newLum, "lum", dat, tim, id);
				try {
					mysqlSt.executeUpdate(new Erro(lum, "ERR04", id).toQuery());
					mysqlSP.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void start() {
		connectToMongo();
		connectToMysql();
		getSistemParameters();
		watchForInserts();
	}

	public static void main(String[] args) {
		new MongoToMysql().start();
	}

}
