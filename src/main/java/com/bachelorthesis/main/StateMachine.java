/**
 * Diese Klasse ist die Hauptklasse des Projekts.
 * Hier ist das Zustandssystem realisiert, dass die Worte voneinander trennt und diese
 * danach auswertet.
 */

package com.bachelorthesis.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class StateMachine {
	
	private int nextState;
	private Energiewertbestimmung energy;
	private StreamBuffer buff;
	private Sprachauswertung auswertung;
	private List<String> ausgabe;
	public static long start_time;
	private StartUpFilePathConfiguration config;
	
	public StateMachine(StartUpFilePathConfiguration config) throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException {	
		energy = new Energiewertbestimmung();
		buff = new StreamBuffer(energy, config.getTestaufnahme());
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
		deleteFiles();
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
		buff.receiveNewData();
		while(!energy.voiceDetected())
		{
			buff.receiveNewData();
		}
		buff.markNewStartingPoint(); 
		//Mögliche Spracheingabe wurde erkannt, wechsle State.
		nextState = 2;
		return;
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
			buff.receiveNewData();
			if(buff.getLastIndex()-buff.getStartIndex() > 10)
			{
				//Anfang der Spracheingabe bestätigt, wechsle in Zustand 3.
				nextState = 3;
				return;
			}
		}
		buff.resetPoints();
		//Keine Spracheingabe erkannt, wechsle zurück zu Zustand 1.
		nextState = 1;
		return;
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
			buff.receiveNewData();
		}
		buff.markNewEndPoint(); 
		// Mögliches Sprachende wurde detektiert, wechsle in Zustand 4.
		nextState = 4;
		return;
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
				buff.receiveNewData(); 
				if(buff.getLastIndex()-buff.getEndIndex() > config.getWordPause() && (buff.getEndIndex() - buff.getStartIndex() > 500))
				{
					//Spracheingabe bestätigt. Starte Analyse.
					double[] temp;
					temp = buff.getInterestingData();
					String match = auswertung.getMatch(temp);
										
					ausgabe.add(match);
					
					System.out.print(" " +match);		
					writeToFile(buff.getStartIndex(), buff.getEndIndex(), buff.getAlreadyDeletedSamplesFromBuffer());
					
					buff.resetPoints();
					nextState = 1;
					return;	
				}
				//Hier ist die Spracheingabe zu kurz für ein Wort, alles wird verworfen.
				else if(buff.getLastIndex()-buff.getEndIndex() > config.getWordPause() && (buff.getEndIndex() - buff.getStartIndex()) <= 500)
				{
					buff.resetPoints();
					nextState = 1;
					return;
				}
			}
			//Hier wurde das Ende des Audioinputs erkannt
			catch(IOException io)
			{
				// Wenn noch eine Spracheingabe markiert ist, wird diese analysiert
				// Im Anschluss wird das Programm beendet.
				if(buff.getEndIndex() != -1)
				{
					double[] temp;
					temp = buff.getInterestingData();
					String match = auswertung.getMatch(temp);
					ausgabe.add(match);
					System.out.print(match);		
					writeToFile(buff.getStartIndex(), buff.getEndIndex(), buff.getAlreadyDeletedSamplesFromBuffer());
					buff.resetPoints();
				}
				throw new ProgramEndedException();
				
			}	
		}
		buff.resetEndPoint();
		// Endpunkt der Spracheingabe wurde nicht bestätigt, Rückkehr zu Zustand 3.
		nextState = 3;
		return;
	}

	/**
	 * Hier wird zu Analysezwecken die Region der interessanten Daten ins Array geschrieben
	 * @param start_interesting
	 * @param end_interesting
	 * @param globalCounter
	 */
	private void writeToFile(int start_interesting, int end_interesting, int globalCounter) {
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("resources/Output/out2.txt", true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write((start_interesting+globalCounter) + ";" + (end_interesting+globalCounter) + "\n");
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
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
	 * Hilfsmethode, die zu Anfang alle noch vorhandenen Analysedateien des letzten
	 * Durchgangs überschreibt.
	 */
	private void deleteFiles()
	{
			File f = new File("resources/Output");
			File[] fileArray = f.listFiles(new FilenameFilter() {         
	            public boolean accept(File dir, String name) {
	                return name.endsWith("txt");
	            }
	        });
			for(File x: fileArray)
			{
				x.delete();
			}
	}	
	
	/**
	 * Gibt die für die Satzanalyse benötigte Zeit aus.
	 * @return Zeitdifferenz
	 */
	public long getZeitDiff()
	{
		return buff.gettZeitDiff();
	}
}
