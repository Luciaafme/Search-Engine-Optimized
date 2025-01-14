package control.metadata;


import com.hazelcast.core.IMap;
import control.interfaces.MetadataStoreManager;
import model.Metadata;

public class MetadataStoreMap implements MetadataStoreManager {

	private final IMap<String, Metadata> metadataMap;

	public MetadataStoreMap(IMap<String, Metadata> metadataMap) {
		this.metadataMap = metadataMap;

	}

	@Override
	public void update(Metadata metadata) {
		if (metadata != null) {
			metadataMap.put(metadata.getBookID(), metadata);
			System.out.println("Metadata stored correctly in the book: " + metadata.getBookID());
		} else {
			System.err.println("Metadata NULL, couldn't be stored.");
		}
	}

	@Override
	public void printAllMetadata() {
		if (metadataMap.isEmpty()) {
			System.out.println("Metadata map is empty");
		} else {
			System.out.println("Content of the Metadata map: ");
			for (String key : metadataMap.keySet()) {
				System.out.println(metadataMap.get(key));
			}
		}
	}

}
