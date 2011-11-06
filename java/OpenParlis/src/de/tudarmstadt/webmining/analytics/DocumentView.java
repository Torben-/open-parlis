package de.tudarmstadt.webmining.analytics;

import java.util.Map;

public interface DocumentView {
	public String getName();
	public double frequency(final String word);
	public Map<String, Double> asMap(); 
}
