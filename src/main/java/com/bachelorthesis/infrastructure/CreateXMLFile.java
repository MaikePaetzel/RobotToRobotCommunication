/**
 * Mit Hilfe dieser Klasse lässt sich unter Angabe eines Pfades zu einem Ordner
 * mit Referenzdaten eine XML-Datei erzeugen.
 */

package com.bachelorthesis.infrastructure;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.bachelorthesis.main.StartUpFilePathConfiguration;

public class CreateXMLFile {
	
	public Map<String,Vector<double[]>> referenzen;
	WriteXML xml;
	
	/**
	 * Mainmethode. Parameter sind zuerst der Pfad zum Referenzdatenordner und dann der Dateiname
	 * für die zu erstellende XML-Datei.
	 * @param args Konsolenargumente
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException, TransformerException {
		if(args.length != 2)
		{
			System.out.println("Bitte geben Sie einen gültigen Parameter ein.");
			System.exit(0);
		}
		
		String pathToFile = args[0];
		String xmlName = args[1];
		
		CreateXMLFile ref = new CreateXMLFile(pathToFile, xmlName);
		ref.writeXML();
		System.out.println("XML-Datei " + xmlName + " wurde erfolfgreich angelegt.");
	}
	
	public CreateXMLFile(String pathToFile, String xmlName) throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException {
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setDictionary(pathToFile);
		config.setXml(xmlName);
		
		ReadReferenceFromAudioFile grfp = new ReadReferenceFromAudioFile(config);
		referenzen = grfp.generateReferenceMap();
		xml = new WriteXML(config);
	}
	
	/**
	 * Diese Methode schreibt nacheinander alle Mel-Cepstren aus allen Referenzfiles
	 * in die XML-Datei.
	 * @throws TransformerException
	 */
	public void writeXML() throws TransformerException
	{
		for(String s : referenzen.keySet())
		{
			xml.appendDoc(referenzen.get(s), s);
		}
		xml.closeDoc();
	}
	

}
