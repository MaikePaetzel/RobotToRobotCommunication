/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:10 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.main;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse filtert die Sprachdetektion der Energiewertbestimmung, so dass Ausreißer
 * in den Signalwerten nicht betrachtet werden.
 */
public class Filter {
	
	
	private List<Integer> ungefiltert;
	
	public Filter() {
		ungefiltert = new ArrayList<Integer>();
	}
	
	/**
	 * Fügt ein Element der Filterliste hinzu und berechnet, ob es sich um
	 * einen Ausreißer handelt
	 * @param bool Voicedetektion der Energiewertbestimmung
	 * @return gefilterte Voicedetektion
	 */
	public boolean addElementAndComputeFilter(boolean bool)
	{
		int boolToInt = bool ? 1 : 0;
		ungefiltert.add(boolToInt);
		ensureLength();
		return computeFilter();
	}
	
	/**
	 * Diese Methode löscht den ältesten Wert aus der Filterliste
	 */
	private void ensureLength() {
		if(ungefiltert.size() > Config.LIST_LENGTH)
		{
			ungefiltert.remove(0);
		}
	}
	
	/**
	 * Hier wird gefiltert. Solange in der Filterliste zweimal der Wert true für
	 * die Sprachdetektion vorkommt, werden alle einkommenden Werte als true
	 * zurück gegeben. 
	 * @return gefilterte Voicedetektion
	 */
	private boolean computeFilter()
	{
		int sum = 0;
		for(int i = 0; i<ungefiltert.size(); i++)
		{
			sum += ungefiltert.get(i);
		}
		return sum >= 2;
	}	
}
