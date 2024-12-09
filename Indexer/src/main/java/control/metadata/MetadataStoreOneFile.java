package control.metadata;

import control.interfaces.MetadataSerializerManager;
import control.interfaces.MetadataStoreManager;
import model.Metadata;

import java.io.File;
import java.io.IOException;

public class MetadataStoreOneFile implements MetadataStoreManager {
	private final File datamartFilePath;
	private final MetadataSerializerManager metadataSerializer;

	public MetadataStoreOneFile(String generalDatamartDirectory, MetadataSerializerManager metadataSerializer) throws IOException {
		this.metadataSerializer = metadataSerializer;
		this.datamartFilePath = createMetadataDatamartDirectory(generalDatamartDirectory);
	}

	private File createMetadataDatamartDirectory(String generalDatamartDirectory) {
		File metadataDatamartDirectory = new File(generalDatamartDirectory, "metadata"); // create subfolder path inside datamart
		if (!metadataDatamartDirectory.exists()) {
			metadataDatamartDirectory.mkdirs();
		}
		return new File(metadataDatamartDirectory, "metadata"); // Create a file path inside the subfolder that was previously created
	}

	@Override
	public void update(Metadata new_metadata) throws IOException {

		metadataSerializer.serialize(this.datamartFilePath, new_metadata);
	}
}

