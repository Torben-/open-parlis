package de.tudarmstadt.webmining.analytics;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.tudarmstadt.webmining.utils.collection.Bag;

public class Corpus {
	private static final double log2 = Math.log(2);

	public static enum ViewType {
		BOOLEAN, NORMALISED_TF, TF, TFIDF, SMART;
	}

	private class Boolean implements DocumentView {
		private final String name;
		private final long totalWords;

		@Override
		public Map<String, Double> asMap() {
			final Map<String, Double> result =
				new HashMap<String, Double>();
			final Bag<String> words =
				Corpus.this.documents.get(this.name);
			if(words==null) return Collections.emptyMap();
			for(String word : words.toSet())
				result.put(word, frequency(word));

			return result;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public double frequency(String word) {
			return Corpus.this.documents.get(this.name).contains(word) ? 1.0d : 0.0d;
		}

		public Boolean(final String name) {
			this.name = name;
			Bag<String> words = Corpus.this.documents.get(this.name);
			if(words==null) 
				this.totalWords = 0;
			else
				this.totalWords = words.size();
		}
	}
	

	private class NormalisedTF implements DocumentView {
		private final String name;
		private final long totalWords;

		@Override
		public Map<String, Double> asMap() {
			final Map<String, Double> result =
				new HashMap<String, Double>();
			final Bag<String> words =
				Corpus.this.documents.get(this.name);
			if(words==null) return Collections.emptyMap();
			for(String word : words.toSet())
				result.put(word, frequency(word));

			return result;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public double frequency(String word) {
			double tf = (double) Corpus.this.documents.get(this.name).frequency(word)/
			this.totalWords;

			return tf;
		}

		public NormalisedTF(final String name) {
			this.name = name;
			Bag<String> words = Corpus.this.documents.get(this.name);
			if(words==null) 
				this.totalWords = 0;
			else
				this.totalWords = words.size();
		}
	}

	private class TF implements DocumentView {
		private final String name;
		private final long totalWords;

		@Override
		public Map<String, Double> asMap() {
			final Map<String, Double> result =
				new HashMap<String, Double>();
			final Bag<String> words =
				Corpus.this.documents.get(this.name);
			if(words==null) return Collections.emptyMap();
			for(String word : words.toSet())
				result.put(word, frequency(word));

			return result;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public double frequency(String word) {
			double tf = (double) Corpus.this.documents.get(this.name).frequency(word);

			return tf;
		}

		public TF(final String name) {
			this.name = name;
			Bag<String> words = Corpus.this.documents.get(this.name);
			if(words==null) 
				this.totalWords = 0;
			else
				this.totalWords = words.size();
		}
	}

	private class TFIDF implements DocumentView {
		private final String name;
		private final long totalWords; 

		@Override
		public Map<String, Double> asMap() {
			final Map<String, Double> result =
				new HashMap<String, Double>();
			final Bag<String> words =
				Corpus.this.documents.get(this.name);
			if(words==null) return Collections.emptyMap();
			for(String word : words.toSet())
				result.put(word, frequency(word));

			return result;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public double frequency(String word) {
			double tf = (double) Corpus.this.documents.get(this.name).frequency(word)/
			this.totalWords;
			double idf = (double) Math.log((Corpus.this.documents.size()
					/ Corpus.this.corpusWords.frequency(word))) / log2;

			return tf * idf;
		}

		public TFIDF(final String name) {
			this.name = name;
			Bag<String> words = Corpus.this.documents.get(this.name);
			if(words==null) 
				this.totalWords = 0;
			else
				this.totalWords = words.size();
		}
	}

	private class SMART implements DocumentView {
		private final String name;
		private final long totalWords; 

		@Override
		public Map<String, Double> asMap() {
			final Map<String, Double> result =
				new HashMap<String, Double>();
			final Bag<String> words =
				Corpus.this.documents.get(this.name);
			if(words==null) return Collections.emptyMap();
			for(String word : words.toSet())
				result.put(word, frequency(word));

			return result;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public double frequency(String word) {
			int wordFrequency = Corpus.this.documents.get(this.name).frequency(word);
			double tf = (wordFrequency==0) ? 
					0 :
						(double) 1+Math.log(1+ Math.log(wordFrequency)/log2)/log2;
			double idf = (double) Math.log(((1+Corpus.this.documents.size())
					/ Corpus.this.corpusWords.frequency(word))) / log2;

			return tf * idf;
		}

		public SMART(final String name) {
			this.name = name;
			Bag<String> words = Corpus.this.documents.get(this.name);
			if(words==null) 
				this.totalWords = 0;
			else
				this.totalWords = words.size();
		}
	}


	private final Bag<String> corpusWords;
	private final Map<String, Bag<String>> documents =
		new HashMap<String, Bag<String>>();

	public void addDocument(final String name,
			final Bag<String> words) { 
		if(words==null) return;

		for(String word : words.toSet())
			this.corpusWords.add(word);

		this.documents.put(name, words);
	}

	public DocumentView getDocumentView(final String name, final ViewType type) {
		switch(type) {
		case BOOLEAN: return new Boolean(name);
		case TF: return new TF(name);
		case NORMALISED_TF: return new NormalisedTF(name);
		case TFIDF: return new TFIDF(name);
		case SMART: return new SMART(name);
		default: return null;
		}
	}

	public Set<String> getWords() {
		return corpusWords.toSet();  
	}

	public Corpus() {
		this.corpusWords = new Bag<String>();
	}

	public Corpus(final Comparator<String> comparator) {
		this.corpusWords = new Bag<String>(comparator);
	}
}
