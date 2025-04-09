package control;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastServer {
    private HazelcastInstance hazelcastInstance;

    public HazelcastServer() {
        try {

            String hostIp = System.getenv("HOST_IP");
            String remoteIp = System.getenv("REMOTE_IP");
/*
            String hostIp = "10.26.14.213";
            String remoteIp = "10.26.14.214:5701";

 */

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
