/**
 * Configdatei für alle globalen Konstanten und Pfadangaben
 */

package com.bachelorthesis.main;

public class Config {
	
	//Samplingrate der Audiodatei
	public static final int Sampling_Rate = 44100;
	
	//Anzahl der Samples, die zur Verschiebung zusätzlich im Buffer vorgehalten werden
	public static final int OVERHEAD = 2048;
	
	//Anzahl der Samples, die im Buffer gespeichert werden
	public static final int BUFFERSIZE = 6000;
	
	//Länge des Filters für die Datenglättung
	public static final int LIST_LENGTH = 10;
	
	//Größe der Behälter für das Mel-Cepstrum
	public static final int defaultWindowSize = 1024;
	
}
