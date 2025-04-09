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
}
