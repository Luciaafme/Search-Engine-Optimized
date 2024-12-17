package control;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HazelcastManager {
    private HazelcastInstance hazelcastInstance;

    public HazelcastManager() {
        try {
            // Cargar la configuración desde hazelcast.xml
            Config config = new XmlConfigBuilder(new FileInputStream("QueryEngine/src/main/resources/hazelcast.xml")).build();
            this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);
            System.out.println("Hazelcast instance inicializada con XML.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Hazelcast configuration file not found.", e);
        }
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    public void shutdown() {
        hazelcastInstance.shutdown();
        System.out.println("Hazelcast instance cerrada.");
    }
}
