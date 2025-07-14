package com.udbhav.sherlock.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttSubscriber {
    private final MqttClient client;
    
    public MqttSubscriber(MqttClient client){
        this.client = client;
    }
    public void subscribe(String topic){
        try{
            client.subscribe(topic);
            System.out.println("Subscribed to topic: " + topic);
        }catch(MqttException e){
            e.printStackTrace();
        }
    }
}
