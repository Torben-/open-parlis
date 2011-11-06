package de.tudarmstadt.webmining.classifier;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class KeywordClassifier<L, V> extends BatchClassifier<L, V> {
	private final Iterable<L> keywords;
	private final List<String> words;
	private final L nullKeyword;
	
	@Override
	public L predict(List<V> example) {
		for(final L keyword : keywords) {
			int index = words.indexOf(keyword.toString());
			if(index!=-1) 
				if(example.get(index)!=null) return keyword;
		}
		return nullKeyword;
	}
	
	public KeywordClassifier(final Map<? extends List<V>, L> examples,
			final Iterable<L> keywords,
			final List<String> words,
			final L nullKeyword) {
		super(examples);
		this.nullKeyword = nullKeyword;
		this.keywords = keywords;
		this.words = words;
	}
}
