import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SimulateSensor implements MqttCallback {
	static MqttClient mqttclient;
	static String cloud_server;
	static String cloud_topic;

	public static void main(final String[] array) {
		try {
			final Properties properties = new Properties();
			properties.load(new FileInputStream("SimulateSensor.ini"));
			SimulateSensor.cloud_server = properties.getProperty("cloud_server");
			SimulateSensor.cloud_topic = properties.getProperty("cloud_topic");
		} catch (Exception obj) {
			System.out.println("Error reading SimulateSensor.ini file " + obj);
			JOptionPane.showMessageDialog(null, "The SimulateSensor.ini file wasn't found.", "Mongo To Cloud", 0);
		}
		new SimulateSensor().connecCloud();
		new SimulateSensor().writeSensor();
	}

	public void connecCloud() {
		try {
			(SimulateSensor.mqttclient = new MqttClient(SimulateSensor.cloud_server,
					"SimulateSensor" + SimulateSensor.cloud_topic)).connect();
			SimulateSensor.mqttclient.setCallback((MqttCallback) this);
			SimulateSensor.mqttclient.subscribe(SimulateSensor.cloud_topic);
		} catch (MqttException ex) {
			ex.printStackTrace();
		}
	}

	public void writeSensor() {
		LocalDate.now();
		LocalTime.now();
		long t = System.currentTimeMillis();
		long end = t + 20000;
		while (true) {
//			double d = 18.0;
//			while (d < 50.0) {
//				final String string = "{\"tmp\":\"" + d + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
//						+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
//						+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
//						+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
//				d += 0.5;
//				try {
//					Thread.sleep(2000L);
//				} catch (InterruptedException ex) {
//				}
//				this.publishSensor(string);
//			}
//			int i = 1;
//			while (i < 10) {
//				final String string2 = "{\"tmp\":\"" + d + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
//						+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
//						+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
//						+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
//				++i;
//				try {
//					Thread.sleep(2000L);
//				} catch (InterruptedException ex2) {
//				}
//				this.publishSensor(string2);
//			}
//			while (d > 18.0) {
//				final String string3 = "{\"tmp\":\"" + d + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
//						+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
//						+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
//						+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
//				d -= 1.5;
//				try {
//					Thread.sleep(2000L);
//				} catch (InterruptedException ex3) {
//				}
//				this.publishSensor(string3);
//			}
			final String string4 = "{\"tmp\":\"" + 44.7 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex4) {
			}
			this.publishSensor(string4);
			final String string5 = "{\"tmp\":\"" + 45.0 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex5) {
			}
			this.publishSensor(string5);
			final String string6 = "{\"tmp\":\"" + 45.5 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex6) {	
			}
			this.publishSensor(string6);
			final String string7 = "{\"tmp\":\"" + 45.7 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex7) {
			}
			this.publishSensor(string7);
			final String string8 = "{\"tmp\":\"" + 46.8 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex8) {
			}
			this.publishSensor(string8);
			while(System.currentTimeMillis() < end) {
			final String string9 = "{\"tmp\":\"" + 47.4 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex9) {
			}
			this.publishSensor(string9);
			}
			final String string10 = "{\"tmp\":\"" + 45.0 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex10) {
			}
			this.publishSensor(string10);
			final String string11 = "{\"tmp\":\"" + 46.0 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex11) {
			}
			this.publishSensor(string11);
			final String string12 = "{\"tmp\":\"" + 45.2 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"x\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex12) {
			}
			this.publishSensor(string12);
			final String string13 = "{\"tmp\":\"" + 46.3 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + -20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex13) {
			}
			this.publishSensor(string13);
			final String string14 = "{\"tmp\":\"" + 48.4 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex14) {
			}
			this.publishSensor(string14);
			final String string15 = "{\"tmp\":\"" + 45.2 + "\",\"hum\":\"" + 35.0 + "\",\"dat\":\""
					+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\",\"tim\":\""
					+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\",\"cell\":\"" + 20
					+ "\",\"mov\":\"" + 0 + "\",\"sens\":\"eth\"}";
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException ex15) {
			}
			this.publishSensor(string15);
		}
	}

	public void publishSensor(final String s) {
		try {
			final MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload(s.getBytes());
			SimulateSensor.mqttclient.publish(SimulateSensor.cloud_topic, mqttMessage);
		} catch (MqttException ex) {
			ex.printStackTrace();
		}
	}

	public void connectionLost(final Throwable t) {
	}

	public void deliveryComplete(final IMqttDeliveryToken mqttDeliveryToken) {
	}

	public void messageArrived(final String s, final MqttMessage mqttMessage) {
	}

	static {
		SimulateSensor.cloud_server = new String();
		SimulateSensor.cloud_topic = new String();
	}
}