package de.tudarmstadt.webmining.utils;

import java.io.IOException;
import java.io.Reader;

public class HeadlessReader extends Reader {
	private final Reader base;
	private final boolean ready;
	
	@Override
	public void close() throws IOException {
		this.base.close();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return this.base.read(cbuf, off, len);
	}
	
	private void skipHeader() throws IOException {
		while(base.ready())
			if((char) base.read()=='\n')
				if(base.ready() && (char) base.read()=='\n') break;
	}
	
	public HeadlessReader(final Reader base) {
		this.base = base;
		try {
			skipHeader();
		} catch (IOException e) {
			this.ready = false;
			return;
		}
		this.ready = true;
	}

	@Override
	public boolean ready() throws IOException {
		return this.ready && this.base.ready();
	}

}
