/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:03 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.main;

import org.xml.sax.SAXException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse ist die Hauptklasse des Projekts.
 * Hier ist das Zustandssystem realisiert, dass die Worte voneinander trennt und diese
 * danach auswertet.
 */
public class StateMachine {
	
	private int nextState;
	private Energiewertbestimmung energy;
	private StreamBuffer buffer;
	private Sprachauswertung auswertung;
	private List<String> ausgabe;
	public static long start_time;
	private StartUpFilePathConfiguration config;
	
	public StateMachine(StartUpFilePathConfiguration config) throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException {	
		energy = new Energiewertbestimmung();
		buffer = new StreamBuffer(energy, config.getTestaufnahme());
		auswertung = new Sprachauswertung(config);
		ausgabe =  new ArrayList<String>(); 
    	start_time = System.currentTimeMillis();
    	nextState = 1;
    	this.config = config;
	}
	
	/**
	 * Hauptmethode, die die Zustandsübergänge realisiert.
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws ProgramEndedException 
	 */
	public void running() throws IllegalArgumentException, IOException, UnsupportedAudioFileException, ProgramEndedException
	{
		System.out.print("Ist-Satz:");
		
		while(true){
			try{
				switch(nextState)
				{
				case 1: s1();
						break;
				case 2: s2();
						break;
				case 3: s3();
						break;
				case 4: s4();
						break;
				}	
			}
			catch(IOException e)
			{
				throw new ProgramEndedException();
			}
		}		
	}
	
	/**
	 * Zustand 1: In diesem Zustand wurde noch keine Sprache detektiert.
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private void s1() throws IllegalArgumentException, IOException
	{
		buffer.receiveNewData();
		while(!energy.voiceDetected())
		{
			buffer.receiveNewData();
		}
		buffer.markNewStartingPoint();
		//Mögliche Spracheingabe wurde erkannt, wechsle State.
		nextState = 2;
	}
	
	/**
	 * Zustand 2: Hier wird geprüft, ob die mögliche Spracheingabe lang genug
	 * war und damit als Sprache bestätigt oder wieder verworfen wird.
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private void s2() throws IllegalArgumentException, IOException
	{
		while(energy.voiceDetected())
		{
			buffer.receiveNewData();
			if(buffer.getLastIndex()- buffer.getStartIndex() > 10)
			{
				//Anfang der Spracheingabe bestätigt, wechsle in Zustand 3.
				nextState = 3;
				return;
			}
		}
		buffer.resetPoints();
		//Keine Spracheingabe erkannt, wechsle zurück zu Zustand 1.
		nextState = 1;
	}
	
	/**
	 * Zustand 3: Sprache wurde detektiert, sucht nach Ende der Spracheingabe.
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private void s3() throws IllegalArgumentException, IOException
	{
		while(energy.voiceDetected())
		{
			buffer.receiveNewData();
		}
		buffer.markNewEndPoint();
		// Mögliches Sprachende wurde detektiert, wechsle in Zustand 4.
		nextState = 4;
	}
	
	/**
	 * Zustand 4: Überprüfen des Sprachendes.
	 * Wurde eine komplette Spracheingabe bestätigt, wird die Analyse gestartet.
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws ProgramEndedException 
	 */
	private void s4() throws IllegalArgumentException, IOException, UnsupportedAudioFileException, ProgramEndedException
	{
		while(!energy.voiceDetected())
		{
			try{
				buffer.receiveNewData();
				if(buffer.getLastIndex()- buffer.getEndIndex() > config.getWordPause() && (buffer.getEndIndex() - buffer.getStartIndex() > 500))
				{
					//Spracheingabe bestätigt. Starte Analyse.
					double[] temp;
					temp = buffer.getInterestingData();
					String match = auswertung.getMatch(temp);
										
					ausgabe.add(match);
					
					System.out.print(" " +match);		

					buffer.resetPoints();
					nextState = 1;
					return;	
				}
				//Hier ist die Spracheingabe zu kurz für ein Wort, alles wird verworfen.
				else if(buffer.getLastIndex()- buffer.getEndIndex() > config.getWordPause() && (buffer.getEndIndex() - buffer.getStartIndex()) <= 500)
				{
					buffer.resetPoints();
					nextState = 1;
					return;
				}
			}
			//Hier wurde das Ende des Audioinputs erkannt
			catch(IOException io)
			{
				// Wenn noch eine Spracheingabe markiert ist, wird diese analysiert
				// Im Anschluss wird das Programm beendet.
				if(buffer.getEndIndex() != -1)
				{
					double[] temp;
					temp = buffer.getInterestingData();
					String match = auswertung.getMatch(temp);
					ausgabe.add(match);
					System.out.print(" " + match);		
					buffer.resetPoints();
				}
				throw new ProgramEndedException();
				
			}	
		}
		buffer.resetEndPoint();
		// Endpunkt der Spracheingabe wurde nicht bestätigt, Rückkehr zu Zustand 3.
		nextState = 3;
	}
	
	/**
	 * Zu weiteren Analysen wird die berechnete Sprachausgabe zurückgegeben
	 * @return Ausgabe
	 */
	public List<String> getAusgabe()
	{
		return ausgabe;
	}
	
	/**
	 * Gibt die für die Satzanalyse benötigte Zeit aus.
	 * @return Zeitdifferenz
	 */
	public long getZeitDiff()
	{
		return buffer.getZeitDiff();
	}
}
