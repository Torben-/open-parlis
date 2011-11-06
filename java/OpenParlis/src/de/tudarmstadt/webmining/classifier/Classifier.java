package de.tudarmstadt.webmining.classifier;

import java.util.List;

public interface Classifier<L,V> {
	public L predict(List<V> example);
}
