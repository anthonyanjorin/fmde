package org.upb.fmde.de.categories.concrete.tgraphs;

public class Timer {
	private long clock;
	
	public void tic(){
		clock = System.nanoTime();
	}
	
	public void toc(){
		System.out.println("Time elapsed: " + (System.nanoTime() - clock)/1000000000.0 + "s"); 
	}
}
