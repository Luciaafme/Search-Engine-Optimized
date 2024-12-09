package control.interfaces;

import model.Metadata;

import java.io.IOException;

public interface MetadataStoreManager {
	void update(Metadata metadata) throws IOException;
}
