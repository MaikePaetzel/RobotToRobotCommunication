package com.bachelorthesis.main;

import java.io.File;
import java.io.IOException;

public class StartUpFilePathConfiguration {

		//Pfad zur ausführbaren JAR-Datei
		public String basepath;
		
		//Referenzdatensatz
		private String dictionary = null;

		//Ordner mit Dateien, die analysiert werden sollen
		private String testordner = null;
		
		//XML-File mit Referenzdaten
		private String xml = null;
		
		private String plaintext = null;
		
		//Einzelne zu analysierende Datei
		private String testaufnahme = null;
		
		//Länge der Pause zwischen zwei Worten
		private int wordPause = Integer.MAX_VALUE;
		
		
		public StartUpFilePathConfiguration() throws IOException {
			File currentDirectory = new File(new File(".").getAbsolutePath());
			basepath = currentDirectory.getCanonicalPath();
		}
		
	
		/**
		 * Diese Methode gibt den kompletten Pfad zu den Referenzdaten zurück
		 * @return Pfad zum Dictionary
		 * */
		public String getDictionary()
		{
			return basepath + dictionary;
		}
		
		/**
		 * Setzt den Pfad zu den Referenzdatenordner
		 * @param dictionary
		 */
		public void setDictionary(String dictionary) {
			this.dictionary = dictionary;
		}

		/**
		 * Setzt den Pfad zum testenden Ordner
		 * @param testordner
		 */
		public void setTestordnerPath(String testordner) {
			this.testordner = testordner;
		}

		/**
		 * Setzt den Pfad zum XML-File
		 * @param xml
		 */
		public void setXml(String xml) {
			this.xml = xml;
		}
		
		/**
		 * Setzt den Pfad zu der Plaintextdatei
		 * @param plaintext
		 */
		public void setPlaintext(String plaintext) {
			this.plaintext = plaintext;
		}

		/**
		 * Setzt den Pfad zu einer einzelnen Testdatei
		 * @param testaufnahme
		 */
		public void setTestaufnahme(String testaufnahme) {
			this.testaufnahme = testaufnahme;
		}


		/**
		 * Diese Methode gibt den kompletten Pfad zum Ordner der zu analysierenden Daten zurück
		 * @return Pfad zum Analyseordner
		 */
		public String getTestordnerPath()
		{
			return basepath + testordner;
		}
		
		/**
		 * Diese Methode gibt den relativen Pfad zum Testordner ausgehend von der 
		 * Projektumgebung zurück
		 * @return relativer Pfad zum Analyseordner
		 */
		public String getTestordner()
		{
			return testordner + "/";
		}
		
		/**
		 * Diese Methode gibt den kompletten Pfad zum XML-File zurück
		 * @return Pfad zur XML-Datei
		 */
		public String getXML()
		{
			return basepath + xml;
		}
		
		/**
		 * Diese Methode gibt den kompletten Pfad zur plaintext.txt zurück
		 * @return Pfad zur plaintext.txt
		 */
		public String getPlaintext()
		{
			return basepath + plaintext;
		}
		
		/**
		 * Diese Methode gibt den kompletten Pfad zum Ordner mit den Testaufnahmen zurück.
		 * @return Pfad zum Ordner mit Testaufnahmen.
		 */
		public String getTestaufnahme()
		{
			return basepath + testaufnahme;
		}
		
		/**
		 * Diese Methode gibt die Pause zurück, die zwischen zwei erkannten
		 * Weten liegen muss.
		 * @return wordPause
		 */
		public int getWordPause()
		{
			return wordPause;
		}
		
		/** 
		 * Diese Methode setzt die Länge, die zwischen zwei Worten als Pause
		 * liegen muss
		 * @param val wordPause
		 */
		public void setWordPause(int val)
		{
			wordPause = val;
		}
		
		

}
