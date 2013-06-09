/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:09 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;

import com.bachelorthesis.infrastructure.ReadXML;
import org.xml.sax.SAXException;

/**
 * Diese Klasse liest die XML-Datei ein und stellt die Referenzdaten daraus zur Verfügung.
 * Außerdem kann eine Textdatei mit der Zuordnung des Dateinamens zum gesprochenen Input
 * (sowohl für die Referenzdaten als auch für die Analysedaten) eingelesen werden.
 */
public class Load {
	
	private Map<String,Vector<double[]>> referenzdaten;
	private Map<String, String> contentfolder = new HashMap<String,String>();
	
	public Load(StartUpFilePathConfiguration config) throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException {		
		ReadXML reader = new ReadXML(config);
		referenzdaten = reader.getContent();
		
		loadFilenameToContentMap(config.getPlaintext());
	}
	
	/**
	 * Diese Methode liest an einem gegebenen Pfad die Datei "plaintext.txt" ein
	 * und gibt eine Map mit der Zuordnung Dateiname zu Sprachinput zurück.
	 * @param path Pfad zur Plaintext-Datei
	 * @return Map mit Zuordnung Dateiname - Sprachinput
	 * @throws IOException
	 */
	public Map<String, String> loadFilenameToContentMap(String path) throws IOException {
		
	    BufferedReader fr2 = new BufferedReader(new FileReader(new File(path)));   
	    //Wird solange ausgeführt, wie es noch einzulesende Zeilen gibt.
	    boolean doing = true;
	    while(doing)
	    {
	    	//eine neue Zeile wird eingelesen
	    	String string = fr2.readLine();
	    	if(string != null) //wenn die Zeile nicht leer ist
	    	{
	    		//Die Zeile wird nach dem ";" aufgesplittet. Davor steht der Name des Mitarbeiters, dahinter der Ordner
	    		//Achtung: Wir gehen hier davon aus, dass die Namen immer in der gleichen Schreibweise wie im Wochenbericht auftauchen
		    	String[] splitted = string.split(";");
		    	String dateiname = splitted[0].trim();
		    	String content = splitted[1].trim();
		    	//Zuordnung wird intern gespeichert.
		    	contentfolder.put(dateiname, content);
	    	}else{ //Ende des Dokuments erreicht
	    		doing = false;
	    	}
	    }  
	    fr2.close(); //Wir schließen die eingelesene Datei
	    
	    return contentfolder;
	}

	/**
	 * Diese Methode gibt den Referendatensatz aus der XML-Datei zurück.
	 * @return Referenzdatensatz 
	 */
	public Map<String,Vector<double[]>> getInput()
	{
		return referenzdaten;
	}


	/**
	 * Diese Methode gibt  eine Map mit der Zuordnung Dateiname zu Sprachinput zurück.
	 * @return  Map mit Zuordnung Dateiname - Sprachinput
	*/
	public Map<String,String> getContentfolder()
	{
		return contentfolder;
	}


}
