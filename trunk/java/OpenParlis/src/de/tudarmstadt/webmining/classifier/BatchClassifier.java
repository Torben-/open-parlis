package de.tudarmstadt.webmining.classifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BatchClassifier<L,V> implements Classifier<L, V>{
	private final Map<? extends List<V>, ? extends L> examples;
	
	protected Map<? extends List<V>, ? extends L> getExamples() {
		return this.examples;
	}
	
	public BatchClassifier(final Map<? extends List<V>, L> examples) {
		this.examples = 
			new HashMap<List<V>,L>(examples);
	}
}
