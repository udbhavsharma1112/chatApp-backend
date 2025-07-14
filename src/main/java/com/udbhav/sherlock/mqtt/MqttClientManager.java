package com.udbhav.sherlock.mqtt;
import org.eclipse.paho.client.mqttv3.*;


public class MqttClientManager {
    private final String broker = "tcp://localhost:1883";
    private final String clientId;
    private MqttClient client;

    public MqttClientManager(String clientId) {
        this.clientId = clientId;
        try {
            this.client = new MqttClient(this.broker, this.clientId);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (client.isConnected()) client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttClient getClient() {
        return client;
    }
}

