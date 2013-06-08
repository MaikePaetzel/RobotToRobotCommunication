/**
 * Diese Klasse bestimmt, ob ein gegebener Signalwert zu einer Spracheingabe gehört.
 */

package com.bachelorthesis.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class Energiewertbestimmung {
	
	private double average;
	private List<Double> buffer;
	private Filter filter;
	private int analyseWert; //dient nur für die Analyse
	
	public Energiewertbestimmung() {
		average = 0;
		analyseWert = 0;
		filter = new Filter();
	}

	/**
	 * Diese Methode bestimmt, ob es sich bei dem letzten Wert im Buffer
	 * um Sprache handelt
	 * @return boolean Sprache
	 */
	public boolean voiceDetected() {
		double last_signal = buffer.get(buffer.size()-1);
		analyseWert--;
		boolean sprachSignal = Math.abs(last_signal) > average*2 && Math.abs(last_signal) > 600;
		sprachSignal = filter.addElementAndComputeFilter(sprachSignal);
		
		todo_analyse(last_signal, sprachSignal);
		return sprachSignal;
	}

	/**
	 * Diese Methode dient nur den Python-Analysen
	 * @param last_signal
	 * @param x
	 */
	private void todo_analyse(double last_signal, boolean x) {
		if(analyseWert >= 0)
		{
		  try{
			  // Create file 
			  FileWriter fstream = new FileWriter("resources/Output/out.txt", true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(last_signal + ";" + x + ";" + average + "\n");
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		}
		else
		{
			analyseWert++;
		}
	}
	
	/**
	 * Hier wird der durchschnittliche Signalwert im Buffer berechnet
	 * @param buffer
	 */
	public void computeContinuousAverage(List<Double> buffer) {
		double summe=0;
		this.buffer = buffer;
		analyseWert++;
		
		for(Double d : buffer)
		{
			summe += Math.abs(d);
		}
		average =  summe/buffer.size();
		
	}
}
