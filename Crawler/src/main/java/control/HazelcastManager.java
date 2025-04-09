package control;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class HazelcastManager {
	private HazelcastInstance hazelcastInstance;

	public HazelcastManager() {
		try {

			String hostIp = System.getenv("HOST_IP");
			String remoteIp = System.getenv("REMOTE_IP");


			if (hostIp == null || remoteIp == null) {
				throw new RuntimeException("HOST_IP and REMOTE_IP environment variables must be set");
			}


			Config config = new Config();

			NetworkConfig networkConfig = config.getNetworkConfig();
			networkConfig.setPort(5701);
			networkConfig.setPublicAddress(hostIp);

			networkConfig.getJoin().getTcpIpConfig()
					.setEnabled(true)
					.addMember(remoteIp);


			networkConfig.getJoin().getMulticastConfig().setEnabled(false);

			this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);

			System.out.println("Hazelcast Server started successfully");

		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize Hazelcast server", e);
		}
	}

	public HazelcastInstance getHazelcastInstance() {
		return this.hazelcastInstance;
	}

	public void shutdown() {
		if (hazelcastInstance != null) {
			hazelcastInstance.shutdown();
			System.out.println("Hazelcast Server shut down");
		}
	}
}