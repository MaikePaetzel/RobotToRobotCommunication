/**
 * Diese Klasse bestimmt, ob ein gegebener Signalwert zu einer Spracheingabe gehört.
 */

package com.bachelorthesis.main;

import java.util.List;

public class Energiewertbestimmung {
	
	private double average;
	private List<Double> buffer;
	private Filter filter;

	public Energiewertbestimmung() {
		average = 0;
		filter = new Filter();
	}

	/**
	 * Diese Methode bestimmt, ob es sich bei dem letzten Wert im Buffer
	 * um Sprache handelt
	 * @return boolean Sprache
	 */
	public boolean voiceDetected() {
		double last_signal = buffer.get(buffer.size()-1);
		boolean sprachSignal = Math.abs(last_signal) > average*2 && Math.abs(last_signal) > 600;
		sprachSignal = filter.addElementAndComputeFilter(sprachSignal);
		
		return sprachSignal;
	}

	
	/**
	 * Hier wird der durchschnittliche Signalwert im Buffer berechnet
	 * @param buffer
	 */
	public void computeContinuousAverage(List<Double> buffer) {
		double summe=0;
		this.buffer = buffer;

		for(Double d : buffer)
		{
			summe += Math.abs(d);
		}
		average =  summe/buffer.size();
		
	}
}
