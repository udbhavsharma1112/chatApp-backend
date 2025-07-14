package com.udbhav.sherlock;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;

// import java.sql.Connection;

import org.checkerframework.checker.units.qual.h;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.jdbi.v3.core.Jdbi;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import com.udbhav.sherlock.queue.MessageQueuePublisher;
import com.udbhav.sherlock.queue.RabbitMQConsumerWorker;
import com.udbhav.sherlock.dao.AuthUserDao;
import com.udbhav.sherlock.dao.MessageDao;
import com.udbhav.sherlock.dao.UserChatDao;
import com.udbhav.sherlock.dao.UserDao;
import com.udbhav.sherlock.mapper.AuthUserMapper;
import com.udbhav.sherlock.mapper.MessageMapper;
import com.udbhav.sherlock.mapper.UserMapper;
import com.udbhav.sherlock.mqtt.ChatMessageListener;
import com.udbhav.sherlock.mqtt.MqttClientManager;
import com.udbhav.sherlock.mqtt.MqttMessageHandler;
import com.udbhav.sherlock.mqtt.MqttSubscriber;
import com.udbhav.sherlock.resources.AuthResource;
import com.udbhav.sherlock.resources.MessageResource;
import com.udbhav.sherlock.resources.UserChatHistoryResource;

public class SherlockApplication extends Application<SherlockConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SherlockApplication().run(args);
    }

    @Override
    public String getName() {
        return "sherlock";
    }

    @Override
    public void initialize(final Bootstrap<SherlockConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(SherlockConfiguration configuration, Environment environment) throws Exception {
        try {
            final JdbiFactory factory = new JdbiFactory();
            final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
            jdbi.registerRowMapper(new UserMapper());
            jdbi.registerRowMapper(new MessageMapper());
            jdbi.registerRowMapper(new AuthUserMapper());

            final UserDao userDao = jdbi.onDemand(UserDao.class);
            final AuthUserDao authUserDao = jdbi.onDemand(AuthUserDao.class);
            final UserChatDao userChatDao = jdbi.onDemand(UserChatDao.class);
            final MessageDao messageDao = jdbi.onDemand(MessageDao.class);
            environment.jersey().register(new AuthResource(authUserDao));

            environment.jersey().register(new UserChatHistoryResource(userChatDao, userDao));
            environment.jersey().register(new MessageResource(messageDao));


            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest"); // or your credentials

            Connection connection = connectionFactory.newConnection();
            MessageQueuePublisher publisher = new MessageQueuePublisher(connection);



            String clientId = "sherlock-backend-subscriber";
            MqttClientManager mqttManager = new MqttClientManager(clientId);
            mqttManager.connect();

            MqttClient client = mqttManager.getClient();
            client.setCallback(new MqttMessageHandler(new ChatMessageListener(messageDao, userChatDao, publisher)));
            MqttSubscriber subscriber = new MqttSubscriber(client);
            subscriber.subscribe("/saveMessages");

            new Thread(new RabbitMQConsumerWorker(connection, messageDao, userChatDao)).start(); // Start RabbitMQ consumer worker



// Store this in your app context or pass it to MQTT message handler



            System.out.println("✅ UserResource registered");
        } catch (Exception e) {
            System.err.println("❌ Exception during app startup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}




