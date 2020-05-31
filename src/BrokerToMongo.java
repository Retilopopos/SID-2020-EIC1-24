import java.io.FileInputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BrokerToMongo implements MqttCallback {

	MqttClient mqttclient;
	static MongoClient mongoClient;
	static MongoDatabase mongoDatabase;
	static MongoCollection<Document> mongoReadingsCollection;
	static MongoCollection<Document> mongoErrorCollection;
	static String cloud_server = new String();
	static String cloud_topic = new String();
	static String mongo_host = new String();
	static String mongo_database = new String();
	static String mongo_readings_collection = new String();
	static String mongo_error_collection = new String();

	public static void main(String[] args) {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("cloudToMongo.ini"));
			cloud_server = p.getProperty("cloud_server");
			cloud_topic = p.getProperty("cloud_topic");
			mongo_host = p.getProperty("mongo_host");
			mongo_database = p.getProperty("mongo_database");
			mongo_readings_collection = p.getProperty("mongo_readings_collection");
			mongo_error_collection = p.getProperty("mongo_error_collection");

		} catch (Exception e) {
			System.out.println("Error reading CloudToMongo.ini file " + e);
			JOptionPane.showMessageDialog(null, "The CloudToMongo.ini file wasn't found.", "CloudToMongo",
					JOptionPane.ERROR_MESSAGE);
		}
		new BrokerToMongo().connectCloud();
		new BrokerToMongo().connectMongo();
	}

	public void connectCloud() {
		int i;
		try {
			i = new Random().nextInt(100000);
			mqttclient = new MqttClient(cloud_server, "CloudToMongo_" + String.valueOf(i) + "_" + cloud_topic);
			mqttclient.connect();
			mqttclient.setCallback(this);
			mqttclient.subscribe(cloud_topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void connectMongo() {
		mongoClient = MongoClients.create(mongo_host);
		mongoDatabase = mongoClient.getDatabase(mongo_database);
		mongoReadingsCollection = mongoDatabase.getCollection(mongo_readings_collection);
		mongoClient = MongoClients.create(mongo_host);
		mongoDatabase = mongoClient.getDatabase(mongo_database);
		mongoErrorCollection = mongoDatabase.getCollection(mongo_error_collection);
	}

	@Override
	public void messageArrived(String topic, MqttMessage c) throws Exception {
		try {
			Document json = Document.parse(c.toString().replace("\", ", "\",\"").replace("\"\"", "\""));
			
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormatter= new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormatter= new SimpleDateFormat("HH:");
			String time = json.getString("tim");
			String[] timeArray = time.split(":");
			json.replace("tim", timeFormatter.format(date) + timeArray[1] + ":" + timeArray[2]);
			json.replace("dat", dateFormatter.format(date));
			System.out.println(json.toString());

			mongoReadingsCollection.insertOne(json);
		} catch (Exception e) {
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());

			try {
				Document obj = new Document("erro", c.toString().replace("{", "").replace("}", "").replace("\"", "").replace(":", " ")) ;
				obj.put("timestamp", formatter.format(date));
				mongoErrorCollection.insertOne(obj);
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
	}

	@Override
	public void connectionLost(Throwable cause) {
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

}