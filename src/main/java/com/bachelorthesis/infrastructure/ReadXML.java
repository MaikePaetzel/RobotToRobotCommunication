/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:12 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.infrastructure;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.bachelorthesis.main.StartUpFilePathConfiguration;

/**
 * Diese Klasse liest ein XML-File ein und schreibt das Ergebnis in eine
 * Map<String, Vector> zurück.
 */
public class ReadXML {

    private Document doc;
	
	public ReadXML(StartUpFilePathConfiguration config) throws ParserConfigurationException, SAXException, IOException {
        File file = new File(config.getXML());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();
	}

	/**
	 * Diese Methode liest eine XML-Datei ein und wandelt die Eingaben in
	 * eine Map um, welche zurückgegeben wird.
	 * @return Referenzdatenmap
	 */
	public Map<String,Vector<double[]>> getContent()
	{
		Map<String,Vector<double[]>> referenzdaten = new HashMap<String,Vector<double[]>>();
		NodeList dateien = doc.getElementsByTagName("datei");
		for (int i=0; i <dateien.getLength(); i++)
		{
			Node node = dateien.item(i);
			NodeList arrays = node.getChildNodes();
			Vector<double[]> vector = new Vector<double[]>();
			
			for (int j=0; j < arrays.getLength(); j++)
			{
				Node node2 = arrays.item(j);
				NodeList values = node2.getChildNodes();
				double[] signalparameter = new double[values.getLength()];
				
				for (int z=0; z < values.getLength(); z++)
				{
					Node node3 = values.item(z);
					signalparameter[z] = Double.parseDouble(node3.getTextContent());
				}
				
				vector.add(signalparameter);
			}
			
			NamedNodeMap attr = node.getAttributes();
		    Node nodeAttr = attr.getNamedItem("name");
		    String[] temp2 = nodeAttr.toString().split("\"");
			referenzdaten.put(temp2[1], vector);
		}

		return referenzdaten;
	}

	
}