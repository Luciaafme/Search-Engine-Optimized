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
			System.out.println("Metadatos almacenados correctamente para el libro: " + metadata.getBookID());
		} else {
			System.err.println("Metadatos nulos, no se pudieron almacenar.");
		}
	}

	@Override
	public void printAllMetadata() {
		if (metadataMap.isEmpty()) {
			System.out.println("El mapa de metadatos está vacío.");
		} else {
			System.out.println("Contenido del mapa de metadatos:");
			for (String key : metadataMap.keySet()) {
				System.out.println(metadataMap.get(key));
			}
		}
	}

}
