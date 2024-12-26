package control;

import com.hazelcast.core.HazelcastInstance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;


public class HazelcastManager {
    private HazelcastInstance hazelcastInstance;

    public HazelcastManager() {
        try {
            // Cargar la configuración desde hazelcast-client.xml
            ClientConfig clientConfig = new XmlClientConfigBuilder(new FileInputStream("Indexer/src/main/resources/hazelcast.xml")).build();
            this.hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
            System.out.println("Cliente Hazelcast conectado al clúster.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Hazelcast client configuration file not found.", e);
        }
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    public void shutdown() {
        hazelcastInstance.shutdown();
        System.out.println("Cliente Hazelcast cerrado.");
    }
}

