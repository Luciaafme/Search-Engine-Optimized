package control.word;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;
import control.Indexer;

public class CustomEntryListener<S, S1> implements EntryListener<String, String> {
	private final Indexer indexer;

	public CustomEntryListener(Indexer indexer) {
		this.indexer = indexer;
	}

	@Override
	public void entryAdded(EntryEvent<String, String> event) {
		System.out.println("listener activado");
		System.out.println("Libro: " + event.getKey());
		System.out.println("Contenido: " + event.getValue().substring(0, Math.min(event.getValue().length(), 100)) + "...");
		indexer.execute(event.getKey(), event.getValue());
	}

	@Override
	public void entryRemoved(EntryEvent<String, String> event) {
		System.out.println("Elemento eliminado: " + event.getKey());
	}

	@Override
	public void entryUpdated(EntryEvent<String, String> event) {
		System.out.println("Elemento actualizado: " + event.getKey() + " = " + event.getValue());
	}

	@Override
	public void entryEvicted(EntryEvent<String, String> event) {
		System.out.println("Elemento desalojado: " + event.getKey());
	}

	@Override
	public void mapCleared(MapEvent mapEvent) {

	}

	@Override
	public void mapEvicted(MapEvent mapEvent) {

	}
}