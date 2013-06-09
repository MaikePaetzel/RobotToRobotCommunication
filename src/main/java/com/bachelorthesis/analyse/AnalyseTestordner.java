/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:14 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.analyse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.bachelorthesis.main.Load;
import com.bachelorthesis.main.ProgramEndedException;
import com.bachelorthesis.main.StartUpFilePathConfiguration;
import com.bachelorthesis.main.StateMachine;

/**
 * Diese Klasse führt eine Sprachanalyse mit einem Ordner von Testdaten durch.
 */
public class AnalyseTestordner {
	
	private Map<String, Integer> erfolg;
	private List<Integer> position;
	private Map<String, Integer> nummerncounter;
	private int wortcounter;
	private float abweichung;
	private int misserfolg;
	private StartUpFilePathConfiguration config;
	
	public AnalyseTestordner(StartUpFilePathConfiguration config) {
		wortcounter = 0;
		abweichung = 0;
		nummerncounter = new HashMap<String,Integer>();
		position = new ArrayList<Integer>();
		erfolg  = new HashMap<String,Integer>();
		misserfolg = 0;
		this.config = config;
	}


	public void initializeArrays() {
		fillPosition();
		fillNummern();
		fillErfolg();
	}
	
	/**
	 * Diese Methode analysiert einen Ordner mit Datensätzen auf Erkennungsrate 
	 * einzelner Ziffern, der Satzposition und der durchschnittlichen Abweichung
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void analyse() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		File[] filearray = getAllFiles();
		for(File f : filearray)
		{
			Load loader = new Load(config);
			Map<String,String> contentfolder1 = loader.loadFilenameToContentMap(config.getTestordnerPath() + "/plaintext.txt");
			String satz = contentfolder1.get(f.getName());
			System.out.println("Starte Satzanalyse");
			System.out.println("Soll-Satz: " + satz);
			String file = config.getTestordner() + f.getName();
			config.setTestaufnahme(file);
			StateMachine machine = new StateMachine(config);
			try{
				machine.running();
			}
			catch(ProgramEndedException ignored){}

			List<String> ist = machine.getAusgabe();
			System.out.println("");
			System.out.println("Satzauswertung hat " + machine.getZeitDiff()/1000 + " Sekunden gedauert.");
			System.out.println("---------------------------------------------------------");
			
			String[] worte = satz.split("\\s+");
			
			//Hier wird geprüft, ob der Satz in korrekt viele Teile segmentiert wurde
			if(ist.size() == 6)
			{
				wortcounter += 1;
				for (int i = 0; i < ist.size(); i++)
				{
					String is_word = ist.get(i);
					String soll_word = worte[i];
					int bisher = nummerncounter.get(soll_word);
					nummerncounter.put(soll_word, bisher+1);
					
					
					if(is_word.equals(soll_word))
					{
						addiereWerte(is_word, i);
					}
					else
					{
						misserfolg++;
						berechneDurchschnittlicheAbweichung(is_word, soll_word);
					}
				}
			}
			else
			{
				int bisher = nummerncounter.get("Illegal");
				nummerncounter.put("Illegal", bisher+1);
				System.out.println("Achtung - Falch segmentiert wurde: " + satz);
			}
			
			
		}
		
		generiereAusgabe();
	}
	
	/**
	 * Diese Methode generiert die Ausgabe, die in die Auswertungsdatei geschrieben wird.
	 */
	private void generiereAusgabe() {
		writeToFile("----------------------------");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss ");
        Date currentTime = new Date();
	    writeToFile(formatter.format(currentTime)); 
		
		for(int i = 0; i < position.size(); i++)
		{
			double prozent = position.get(i)/(double)wortcounter*100;
			writeToFile("Position " + i + ": " + prozent);
		}
		double prozent;
		for(String entry: erfolg.keySet())
		{
			if(erfolg.get(entry) != 0)
			{
				prozent = erfolg.get(entry) /(double)nummerncounter.get(entry)*100;
			}
			else
            {
				prozent = 100.0;
            }
            writeToFile(entry + ": " + prozent);
		
		}
		writeToFile("Es wurden " + nummerncounter.get("Illegal") + " Sätze falsch segmentiert.");
		writeToFile("Die durchschnittliche Abweichung in der Distanz betrug " + (abweichung/misserfolg) + " Meter.");
		
	}
	
	/**
	 * Dies ist eine Hilfsmethode, die einen gegebenen String in eine Datei schreibt.
	 * @param s Text, der in die Datei geschrieben werden soll
	 */
	private void writeToFile(String s) {
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("resources/Testergebnis/ausgabe.txt", true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(s + "\n");
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			    System.err.println("Error: " + e.getMessage());
			  }
		
	}

	/**
	 * Hilfsmethode, die das Array für die Positionsanalyse füllt.
	 */
	private void fillPosition()
	{
		for(int i = 0; i < 6; i++)
		{
			position.add(0);
		}
	}
	
	/**
	 * Hilfsmethode, die die Map für die Ziffernanalyse füllt.
	 */
	private void fillNummern()
	{
        for(int i=0; i <= 9; i++){
            nummerncounter.put(i+".", 0);
        }
		nummerncounter.put("Distance.", 0);
		nummerncounter.put("Meter.", 0);
		nummerncounter.put("Point.", 0);
		nummerncounter.put("Illegal",0);
	}

	/**
	 * Diese Methode füllt die Map, dass für jede Ziffer die erfolgreichen Erkennungen
	 * speichert
	 */
	private void fillErfolg()
	{
        for(int i=0; i <= 9; i++){
            erfolg.put(i+".", 0);
        }
        erfolg.put("Distance.", 0);
        erfolg.put("Meter.", 0);
        erfolg.put("Point.", 0);
	}
	
	/**
	 * Hilfsmethode, die im Positionsarray und in der Erfolgsmap den Inhalt an der
	 * korrekten Position um eins hochzählt.
	 * @param is_word key, der hochgezählt werden soll
	 * @param i Position im Array, die hochgezählt werden soll
	 */
	private void addiereWerte(String is_word, int i) {
		int bisher = erfolg.get(is_word);
		erfolg.put(is_word, bisher+1);
		bisher = position.get(i);
		position.set(i, bisher+1);
		
	}

	/**
	 * Diese Methode berechnet die durchschnittliche Abweichung der falsch erkannten
	 * Sätze
	 * @param is_word erkanntes Wort
	 * @param soll_word Wort, das hätte erkannt werden sollen
	 */
	private void berechneDurchschnittlicheAbweichung(String is_word, String soll_word) {
			float ist = getMeter(is_word);
			float soll = getMeter(soll_word);
			abweichung += Math.abs(soll-ist);
	}

	/** 
	 * Gibt einen gegebenen String als float zurück
	 * @param input 
	 * @return input als float
	 */
	private float getMeter(String input)
	{
		String[] zwischenspeicher = input.split(" ");
    	String meter = zwischenspeicher[1].trim();
    	try{
    	    return Float.parseFloat(meter);
    	}
    	catch(NumberFormatException e){
    		return 0;
    	}
	}
	
	/**
	 * Gibt alle Files mit der Dateiendung "wav" zurück.
	 * @return Filearray
	 */
	private File[] getAllFiles()
	{
		File f = new File(config.getTestordnerPath());
		File[] fileArray = f.listFiles(new FilenameFilter() {         
	            public boolean accept(File dir, String name) {
	                return name.endsWith("wav");
	            }
	        });
		return fileArray;
	}
}
