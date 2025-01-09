package control;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class HazelcastServer {
	private final HazelcastInstance hazelcastInstance;

	public HazelcastServer() {
		try {
			// Load server configuration
			InputStream configStream = getClass().getClassLoader().getResourceAsStream("query_server_hazelcast.xml");
			if (configStream == null) {
				throw new FileNotFoundException("query_server_hazelcast.xml not found in classpath");
			}

			Config config = new XmlConfigBuilder(configStream).build();
			this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);
			System.out.println("Hazelcast Server started successfully");

		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize Hazelcast server", e);
		}
	}

	public HazelcastInstance getHazelcastInstance() {
		return hazelcastInstance;
	}

	public void shutdown() {
		if (hazelcastInstance != null) {
			hazelcastInstance.shutdown();
			System.out.println("Hazelcast Server shut down");
		}
	}
}
