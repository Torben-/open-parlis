package de.tudarmstadt.webmining.utils;

public class Stopwatch {
	private long startTime;
	private boolean run=false;
	
	public void start() {
		this.startTime = System.nanoTime();
		this.run = true;
	}
	
	public long split() {
		return this.run ? 
				System.nanoTime() - this.startTime : 0;
	}
	
	public long stop() {
		long result = split();
		this.run = false;
		
		return result;
	}
}
