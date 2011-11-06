package de.tudarmstadt.webmining.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A pretty simple word tokenizer that uses whitespace characters as
 * separator. Punctuation characters will be cut off at the beginning and
 * the end of each token.
 * 
 * Use the iterator to traverse the recognized tokens.
 * 
 * Note: This implementation will also create "empty tokens" which have to be
 * filtered by the caller.
 */
public class WordTokenizer implements Iterable<String> {
	/*
	 * The iterator over the tokens which contains the actual 
	 * implementation of this tokenizer.
	 */
	private static class WordIterator implements Iterator<String> {
		private final BufferedReader reader;  
		
		/* Punctuation characters. For performance reasons 
		 * converted to a hash set
		 */
		private static char[] IGNORE_CHAR_ARRAY =
			new char[] {'$','.', ':', ';', ',', '\"', '!','(',')','_','?','\'','[',']','{','}','*','<','>'};
		private static final Set<Character> IGNORE_CHARS =
			new HashSet<Character>();
		static {
			for(char character : IGNORE_CHAR_ARRAY)
				IGNORE_CHARS.add(Character.valueOf(character));
		}
		
		/*
		 * Cut off the punctuation characters before and after the
		 * token.
		 */
		private String trimIgnoreChars(StringBuilder tokenBuilder) {
			int lastCharacterIdx = tokenBuilder.length()-1;
			while(tokenBuilder.length()>0 && 
					IGNORE_CHARS.contains(Character.valueOf(tokenBuilder.charAt(lastCharacterIdx)))) {
				tokenBuilder.deleteCharAt(lastCharacterIdx);
				lastCharacterIdx--;
			}
		
			while(tokenBuilder.length()>0 && 
					IGNORE_CHARS.contains(Character.valueOf(tokenBuilder.charAt(0))))
				tokenBuilder.deleteCharAt(0);
			
			return tokenBuilder.toString();
		}

		/** {@inheritDoc} */
		@Override
		public boolean hasNext() {
			try {
				return reader.ready();
			} catch (IOException e) {
				return false;
			}
		}

		/** {@inheritDoc} */
		@Override
		public String next() {
			StringBuilder builder = new StringBuilder(); 
			try {
				char character;
				// Add all non-whitespace characters to the builder
				while(!Character.isWhitespace(character = (char) reader.read())) 
						builder.append(character); 
				
				return trimIgnoreChars(builder);
			} catch (IOException e) {
				throw new NoSuchElementException();
			}
			
		}

		/** {@inheritDoc} */
		@Override
		public void remove() {
			// Not implemented in this class!
			throw new UnsupportedOperationException();
		}
		
		/** {@inheritDoc} */
		public WordIterator(final BufferedReader reader) {
			this.reader = reader;
		}
		
	}
	
	private final Iterator<String> iterator;
	
	/**
	 * Creates a new tokenizer over the given stream.
	 * @param is input stream to be tokenized
	 */
	public WordTokenizer(final InputStream is) {
		this(new BufferedReader(new InputStreamReader(is)));
	}
	
	/**
	 * Creates a new tokenizer over the given buffered reader.
	 * @param reader buffered reader to be tokenized.
	 */
	public WordTokenizer(final BufferedReader reader) {
		this.iterator = new WordIterator(reader);
	}
	
	/** {@inheritDoc} */
	@Override
	public Iterator<String> iterator() {	
		return this.iterator;
	}

}
