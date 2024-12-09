package control.interfaces;

import model.Metadata;

import java.io.File;

public interface MetadataSerializerManager {

	void serialize(File filePath, Metadata metadata);

}
