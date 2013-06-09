/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:12 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.infrastructure;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.bachelorthesis.main.Config;
import com.bachelorthesis.main.Sprachauswertung;
import com.bachelorthesis.main.StartUpFilePathConfiguration;
import com.bachelorthesis.main.VirtualAudioStream;

/**
 * Diese Klasse ist eine Hilfsklasse für die Erstellung eines XML-Files.
 * Sie liest die Referenzdaten nicht aus einem XML-File sondern aus einem Ordner ein.
 */
public class ReadReferenceFromAudioFile {
	
	private StartUpFilePathConfiguration config;
	
	public ReadReferenceFromAudioFile(StartUpFilePathConfiguration config) {
		this.config = config;
	}
	
	/**
	 * Diese Methode ist die Hauptmethode, die sich aus einem Ordner alle Files holt,
	 * diese als Audiostream einliest, ein Mel-Cepstrum davon generiert und dieses
	 * dann als Map zurückgibt
	 * @return Map mit Mel-Cepstrum
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public Map<String, Vector<double[]>> generateReferenceMap() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		Sprachauswertung auswertung = new Sprachauswertung();
		Map<String, double[]> input = getInput();
		Map<String, Vector<double[]>> ergebnis = new HashMap<String, Vector<double[]>>();
		
		for(String s : input.keySet())
		{
			Vector<double[]> vector1 = auswertung.generateMelCepstrum(Sprachauswertung.fillData(input.get(s)));
			ergebnis.put(s, vector1);
		}
		
		return ergebnis;
	}
	
	/**
	 * Diese Methode liest alle Audiodateien in einem Ordner ein und gibt ein Doublearray
	 * der Signalwerte und den String des Dateinamens als Map zurück.
	 * @return Map mit Zuordnung Dateiname Signalinhalt
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	public Map<String, double[]> getInput() throws UnsupportedAudioFileException, IOException
	{
		Map<String, double[]> result = new HashMap<String, double[]>();
		File[] filearray = getAllFiles(config.getDictionary());
		for(File f: filearray)
		{
			VirtualAudioStream virtualAudioStream = new VirtualAudioStream(f.getAbsolutePath(), Config.defaultWindowSize, true);
			result.put(f.getName(), virtualAudioStream.getFullStream());
		}

		
		return result;
	}
	
	/**
	 * Hilfsmethode, die alle Dateien in einem Ordner zurückgibt, die die Endung .wav haben.
	 * @param path Pfad zum Ordner
	 * @return Filearray
	 */
	private File[] getAllFiles(String path)
	{
		File f = new File(path);
		File[] fileArray = f.listFiles(new FilenameFilter() {
	            public boolean accept(File dir, String name) {
	                return name.endsWith("wav");
	            }
	        });
		return fileArray;
	}
}
