package control;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class HazelcastClientManager {
	private final HazelcastInstance clientInstance;

	public HazelcastClientManager() {
		try {
			// Load client configuration
			InputStream configStream = getClass().getClassLoader().getResourceAsStream("indexer_client_hazelcast.xml");
			if (configStream == null) {
				throw new FileNotFoundException("indexer_client_hazelcast.xml not found in classpath");
			}

			ClientConfig clientConfig = new XmlClientConfigBuilder(configStream).build();
			this.clientInstance = HazelcastClient.newHazelcastClient(clientConfig);
			System.out.println("Hazelcast Client connected successfully");

		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize Hazelcast client", e);
		}
	}

	public HazelcastInstance getHazelcastInstance() {
		return clientInstance;
	}

	public void shutdown() {
		if (clientInstance != null) {
			clientInstance.shutdown();
			System.out.println("Hazelcast Client disconnected");
		}
	}
}