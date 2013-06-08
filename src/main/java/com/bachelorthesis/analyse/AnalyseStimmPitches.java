/**
 * Diese Klasse führt eine Analyse durch, mit welchem Pitch ein eSpeakdatensatz
 * erzeugt wurde. Dies dient zur Erkennung, welcher Roboter gesprochen hat.
 */

package com.bachelorthesis.analyse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.bachelorthesis.main.Load;
import com.bachelorthesis.main.ProgramEndedException;
import com.bachelorthesis.main.StartUpFilePathConfiguration;
import com.bachelorthesis.main.StateMachine;

public class AnalyseStimmPitches {
	
	private StartUpFilePathConfiguration config;
	private int wortcounter;
	private int korrekt_erkannt;
	private int falsch_segmentiert;
	
	public AnalyseStimmPitches(StartUpFilePathConfiguration config) {
		this.config = config;
		wortcounter = 0;
		korrekt_erkannt = 0;
		falsch_segmentiert = 0;
	}
	
	/**
	 * Diese Methode führt die eigentliche Analyse aus. Dabei werden alle Files
	 * in einem Ordner durchgegangen und jedes Wort darauf überprüft, ob es als
	 * Präfix den richtigen Pitch gesetzt hat.
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
			Map<String,String>contentfolder1 = loader.loadFilenameToContentMap(config.getTestordnerPath() + "/plaintext.txt");
			String satz = contentfolder1.get(f.getName());
			String[] wort = satz.split(" ");
			String pitch = wort[0].split("-")[0];
			System.out.println("Starte Roboteranalyse");
			System.out.println("Soll-Pitch: " + pitch);
			String file = config.getTestordner() + f.getName();
			config.setTestaufnahme(file);
			StateMachine machine = new StateMachine(config);
			try{
				machine.running();
			}
			catch(ProgramEndedException e)
			{
			}
			List<String> ist = machine.getAusgabe();
			System.out.println("");
			System.out.println("---------------------------------------------------------");
			
			String[] worte = satz.split("\\s+");
			
			//Hier wird geprüft, ob der Satz in korrekt viele Teile segmentiert wurde
			if(ist.size() == 6)
			{
				for (int i = 0; i < ist.size(); i++)
				{
					wortcounter += 1;
					String is_word = ist.get(i);
					String is_pitch = is_word.split("-")[0];
					String soll_word = worte[i];
					String soll_pitch = soll_word.split("-")[0];

					if(is_pitch.equals(soll_pitch))
					{
						korrekt_erkannt++;
					}
				}
			}
			else
			{
				falsch_segmentiert++;
				System.out.println("Achtung - Falch segmentiert wurde: " + satz);
			}
			
			
		}
		
		writeToFile("----------------------------");
		 SimpleDateFormat formatter = new SimpleDateFormat(
	                "yyyy.MM.dd - HH:mm:ss ");
	        Date currentTime = new Date();
	    writeToFile(formatter.format(currentTime)); 
		writeToFile("Es wurden " + korrekt_erkannt/wortcounter + " Worte dem richtigen Roboter zugeordnet.");
		writeToFile("Es wurden " + falsch_segmentiert + " Sätze falsch segmentiert.");
	}
	
	/**
	 * Diese Methode ist eine Hilfsmethode, die alle Files in einem Ordner zurückgibt.
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
	
	

}
