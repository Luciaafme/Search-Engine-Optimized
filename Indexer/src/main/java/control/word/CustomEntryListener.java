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
		System.out.println("listener activated");
		System.out.println("Book: " + event.getKey());
		System.out.println("Contend: " + event.getValue().substring(0, Math.min(event.getValue().length(), 100)) + "...");
		indexer.execute(event.getKey(), event.getValue());
	}

	@Override
	public void entryRemoved(EntryEvent<String, String> event) {
		System.out.println("Element deleted: " + event.getKey());
	}

	@Override
	public void entryUpdated(EntryEvent<String, String> event) {
		System.out.println("Element updated: " + event.getKey() + " = " + event.getValue());
	}

	@Override
	public void entryEvicted(EntryEvent<String, String> event) {
		System.out.println("evicted element: " + event.getKey());
	}

	@Override
	public void mapCleared(MapEvent mapEvent) {

	}

	@Override
	public void mapEvicted(MapEvent mapEvent) {

	}
}