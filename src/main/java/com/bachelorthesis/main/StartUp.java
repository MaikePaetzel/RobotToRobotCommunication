/**
 * Diese Klasse bietet die Möglichkeit, über die Konsole verschiedene Analysen,
 * die in der Bachelorarbeit beschrieben wurden, selbst durchzuführen.
 */

package com.bachelorthesis.main;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.bachelorthesis.analyse.AnalyseReferenzdaten;
import com.bachelorthesis.analyse.AnalyseStimmPitches;
import com.bachelorthesis.analyse.AnalyseTestordner;

public class StartUp {

	/**
	 * Main-Methode, die über die Konsoleneingabe mit dem Parameter -s und einer Zahl
	 * gesteuert werden kann. Je nach Zahl wird eine bestimmte Methode aufgerufen.
	 * @param args Konsoleneingabe
	 */
	public static void main(String[] args) {
		if(args.length == 2 && args[0].equals("-s")){
			final String konsolenAusgabe = "Bitte geben Sie eine gültige Nummer ein.";
			try {
				int szenario = Integer.parseInt(args[1]);
				switch(szenario)
				{
					case 0: szenario0(); break;
					case 1: szenario1(); break;
					case 2: szenario2(); break;
					case 3: szenario3(); break;
					case 4: szenario4(); break;
					case 5: szenario5(); break;
					case 6: szenario6(); break;
					case 7: szenario7(); break;
					default: System.out.println(konsolenAusgabe);
				}
			}
			catch (NumberFormatException e) {
				System.out.println(konsolenAusgabe);
			} catch (IOException e) {
				System.out.println("In einem Szenario trat ein Fehler auf. Bitte überprüfen Sie, ob alle nötigen Dateien vorhanden sind.");
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				System.out.println("Es trat ein Problem beim Lesen eines Audiofiles auf.");
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				System.out.println("Es trat ein Problem beim Lesen des XML-File auf.");
				e.printStackTrace();
			} catch (SAXException e) {
				System.out.println("Es trat ein Problem beim Lesen des XML-File auf.");
				e.printStackTrace();
			}
		}
		// Hier wird eine Hilfe auf der Konsole ausgegeben.
		else{
			System.out.println("Bitte geben Sie einen gültigen Parameter ein. Sie haben folgende Optionen:");
			System.out.println("-s 0 analysiert eine einzelne Originalaufnahme auf ihren Sprachinhalt.");
			System.out.println("-s 1 analysiert einen Ordner mit Originalaufnahmen auf den Sprachinhalt.");
			System.out.println("-s 2 analysiert einen einzelne eSpeak-Datei auf ihren Sprachinhalt.");
			System.out.println("-s 3 analysiert einen Ordner mit eSpeak-Dateien auf den Sprachinhalt.");
			System.out.println("-s 4 analysiert einen Ordner mit verschiedenen Pitches darauf, welcher Roboter gesprochen hat.");
			System.out.println("-s 5 gibt eine Matrix mit den Distanzwerten des Referenzdatensatz mit eSpeak-Daten zurück.");
			System.out.println("-s 6 gibt eine Matrix mit den Distanzwerten des Referenzdatensatz mit Microphon-Daten zurück.");
			System.out.println("-s 7 gibt eine Matrix mit den Distanzwerten des Referenzdatensatz mit eSpeak-Daten gegenüber Microphondaten zurück.");
		}
	}

	/**
	 * Dieses Szenario analysiert eine einzelne Sounddatei mit original Mikrophondaten
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void szenario0() throws IOException, UnsupportedAudioFileException, ParserConfigurationException, SAXException {
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setTestaufnahme("/resources/Micro/Microphondaten/data52.wav");
		config.setXml("/resources/Micro/referenzenMicrophon.xml");
		config.setPlaintext("/resources/Micro/plaintext.txt");
		config.setWordPause(10000);
		StateMachine machine = new StateMachine(config);
		try {
			machine.running();
		} catch (ProgramEndedException e) {
			System.out.println("");
			System.out.println("Programm Ende.");
		}
	}

	/**
	 * Dieses Szenario analysiert einen Ordner mit originalen Microphondaten
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void szenario1() throws IOException, UnsupportedAudioFileException, ParserConfigurationException, SAXException {
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setTestordnerPath("/resources/Micro/Microphondaten");
		config.setXml("/resources/Micro/referenzenMicrophon.xml");
		config.setPlaintext("/resources/Micro/plaintext.txt");
		config.setWordPause(10000);
		AnalyseTestordner setup = new AnalyseTestordner(config);
		setup.initializeArrays();
		setup.analyse();
	}
	
	/**
	 * Dieses Szenario analysiert eine Datei mit erzeugten eSpeakdaten
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void szenario2() throws IOException, UnsupportedAudioFileException, ParserConfigurationException, SAXException {
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setTestaufnahme("/resources/eSpeak/eSpeakdaten/data373.wav");
		config.setXml("/resources/eSpeak/referenzenESpeak.xml");
		config.setPlaintext("/resources/eSpeak/plaintext.txt");
		config.setWordPause(5000);
		StateMachine machine = new StateMachine(config);
		try {
			machine.running();
		} catch (ProgramEndedException e) {
			System.out.println("");
			System.out.println("Programm Ende.");
		}
	}

	/** 
	 * Dieses Szenario analysiert einen Ordner mit erzeugten eSpeakdaten
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void szenario3() throws IOException, UnsupportedAudioFileException, ParserConfigurationException, SAXException {
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setTestordnerPath("/resources/eSpeak/eSpeakdaten");
		config.setXml("/resources/eSpeak/referenzenESpeak.xml");
		config.setPlaintext("/resources/eSpeak/plaintext.txt");
		config.setWordPause(5000);
		AnalyseTestordner setup = new AnalyseTestordner(config);
		setup.initializeArrays();
		setup.analyse();
	}
	
	/**
	 * Dieses Szenario analysiert verschiedene Stimm-Pitches darauf, ob sie
	 * auseinander gehalten werden können.
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void szenario4() throws IOException, UnsupportedAudioFileException, ParserConfigurationException, SAXException {
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setTestordnerPath("/resources/Pitch/RoboterErkennungsdaten");
		config.setXml("/resources/Pitch/referenzenRoboter.xml");
		config.setPlaintext("/resources/Pitch/plaintext_roboter.txt");
		config.setWordPause(5000);
		AnalyseStimmPitches roboter = new AnalyseStimmPitches(config);
		roboter.analyse();
	}
	
	/**
	 * Dieses Szenario erzeugt die Matrix mit eSpeak-Referenzdaten im Distanztest
	 * gegen den eigenen Datensatz.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void szenario5() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setXml("/resources/eSpeak/referenzenESpeak.xml");
		config.setPlaintext("/resources/eSeapk/plaintext.txt");
		AnalyseReferenzdaten analyse = new AnalyseReferenzdaten(config);
		analyse.testDistanceMatrixAgainsSelf();
	}
	
	/**
	 * Dieses Szenario erzeugt die Matrix mit Microphon-Referenzdaten im Distanztest
	 * gegen den eigenen Datensatz.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void szenario6() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setXml("/resources/Micro/referenzenMicrophon.xml");
		config.setPlaintext("/resources/Micro/plaintext.txt");
		AnalyseReferenzdaten analyse = new AnalyseReferenzdaten(config);
		analyse.testDistanceMatrixAgainsSelf();
	}
	
	/**
	 * Dieses Szenario erzeugt die Matrix mit eSpeak-Referenzdaten im Distanztest
	 * gegen Microphon-Referenzdaten.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static void szenario7() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setXml("/resources/Micro/urces/plaintext.txt");
		
		StartUpFilePathConfiguration config1 = new StartUpFilePathConfiguration();
		config1.setXml("/resources/eSpeak/referenzenESpeak.xml");
		config1.setPlaintext("/resources/eSpeak/plaintext.txt");
		
		AnalyseReferenzdaten analyse = new AnalyseReferenzdaten(config);
		analyse.testDistanceMatrixAgainstOther(config1);
	}

}
