package control.interfaces;

import model.Metadata;

import java.io.IOException;


public interface MetadataExtractorManager {
	Metadata getMetadata(String content, String identifier) throws IOException;
}
