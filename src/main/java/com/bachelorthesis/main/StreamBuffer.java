/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:02 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Diese Klasse ist der Buffer unserer Audiodaten, der eine gewisse Menge bereits
 * eingelesener Samples speichert und in diesen interessante Punkte für eine
 * Spracheingabe markieren kann.
 *
 */
public class StreamBuffer {
	

	private List<Double> buffer;
	private int start_interesting;
	private int end_interesting;
	private VirtualAudioStream virtualAudioStream;
	private Energiewertbestimmung energie;
	private long diff;
	
	public StreamBuffer(Energiewertbestimmung energy, String path) throws UnsupportedAudioFileException, IOException {
		buffer = new ArrayList<Double>();
		start_interesting = -1;
		end_interesting = -1;
		virtualAudioStream = new VirtualAudioStream(path, Config.defaultWindowSize, true);
		energie = energy;
		diff = 0;
	}
	

	/**
	 * Diese Methode holt sich eine Anzahl neuer Samples aus dem Audiostream
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void receiveNewData() throws IllegalArgumentException, IOException
	{
		double[] temp = virtualAudioStream.getNextSamples(8);
		// Hier wird geprüft, ob überhaupt noch Audiodaten vorhanden sind
		if(temp != null)
		{
			for(double w : temp)
			{
				buffer.add(w);
			}
			ensureBufferLength();
			energie.computeContinuousAverage(buffer);
		}
		else{
			long end_time = System.currentTimeMillis();
			diff = end_time - StateMachine.start_time;
			throw new IOException();
		}
	}


	/**
	 * Hier werden so viele Daten aus dem Buffer gelöscht, bis die definierte
	 * Bufergröße eingehalten wird.
	 * Ausnahme ist, wenn bereits ein Startpunkt für eine Spracheingabe gesetzt wurde.
	 * Dann kann der Buffer auch mehr Samples vorhalten.
	 */
	private void ensureBufferLength() {
		if(buffer.size() > Config.BUFFERSIZE)
		{
			if(start_interesting == -1)
			{
				while(buffer.size() > Config.BUFFERSIZE)
				{
					buffer.remove(0);
				}
			}
		}
	}
	
	/**
	 * Mit dieser Methode wird der Startpunkt für eine Spracheingabe gesetzt
	 */
	public void markNewStartingPoint()
	{
		start_interesting = buffer.size()-1;
	}
	
	/**
	 * Mit dieser Methode wird der Endpunkt für eine Spracheingabe gesetzt
	 */
	public void markNewEndPoint()
	{
		end_interesting = buffer.size()-1;
	}
	
	/**
	 * Diese Methode setzt den Startpunkt und den Endpunkt für eine Spracheingabe
	 * auf den Ursprungswert zurück.
	 */
	public void resetPoints()
	{
		start_interesting = -1;
		end_interesting = -1;
	}
	
	/** 
	 * Diese Methode setzt den Endpunkt einer Spracheingabe auf den Ursprungswert zurück.
	 */
	public void resetEndPoint()
	{
		end_interesting = -1;
	}
	
	/**
	 * Diese Methode gibt ein Doublearray zurück. In diesem sind die Sprachdaten 
	 * zwischen dem markierten Anfangs- und Endpunkt plus dem Overhead gespeichert.
	 * @return doubleArray der Sprachdaten zur Analyse
	 */
	public double[] getInterestingData()
	{
		int length = end_interesting-start_interesting+2*Config.OVERHEAD;

		double[] temp = new double[length];
		for(int i = 0; i<temp.length; i++)
		{		
			if(Config.OVERHEAD < start_interesting)
			{
			temp[i] = buffer.get(i+start_interesting-Config.OVERHEAD);
			}
			else
			{
				temp[i] = buffer.get(i);
			}
		}
		return temp;
	}
	
	/**
	 * Diese Methode gibt den Startindex der Spracheingabe zurück
	 * @return Startindex der Spracheingabe
	 */
	public int getStartIndex()
	{
		return start_interesting;
	}
	
	/**
	 * Diese Methode gibt den Endindex der Spracheingabe zurück.
	 * @return Endindex der Spracheingabe
	 */
	public int getEndIndex()
	{
		return end_interesting;
	}
	
	/**
	 * Diese Methode gibt den letzten Index im Buffer zurück.
	 * @return letzter Index im Buffer
	 */
	public int getLastIndex()
	{
		return buffer.size()-1;
	}

    /**
	 * Diese Methode gibt die Zeitdifferenz der Satzauswertung zurück
	 * @return Zeitdifferenz
	 */
	public long getZeitDiff()
	{
		return diff;
	}


}
