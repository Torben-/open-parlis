package de.tudarmstadt.webmining.utils;

public enum TimeUnit {
	NANO(1,"ns"), MICRO(1000,"µs"), 
	MILLI(1000000,"ms"), SECOND(1000000000l,"s"),
	MINUTE(60000000000l,"m"), HOUR(3600000000000l,"h");
	
	private final long nanos;
	private final String symbol;
	
	public long getNanos() {
		return nanos;
	}

	public String getSymbol() {
		return symbol;
	}

	private TimeUnit(final long nanos, final String symbol) {
		this.nanos = nanos;
		this.symbol = symbol;
	}
	
}
