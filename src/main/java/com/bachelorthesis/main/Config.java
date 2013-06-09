/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:11 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.main;

/**
 * Configdatei für alle globalen Konstanten und Pfadangaben
 */
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
