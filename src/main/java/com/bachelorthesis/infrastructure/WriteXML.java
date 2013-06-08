/**
 * Diese Klasse schreibt eine XML-Datei aus einem Vektor.
 */

package com.bachelorthesis.infrastructure;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.bachelorthesis.main.StartUpFilePathConfiguration;

public class WriteXML {
		
		private DocumentBuilderFactory docFactory;
		private DocumentBuilder docBuilder;
		private Document doc;
		private Element rootElement;
		private StartUpFilePathConfiguration config;
		
		public WriteXML(StartUpFilePathConfiguration config) throws ParserConfigurationException, SAXException, IOException {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			rootElement = doc.createElement("root");
			doc.appendChild(rootElement);
			this.config = config;
		}
	 
		/**
		 * Diese Methode fügt an ein bestehendes Dokument die Mel-Cepstrum-Werte
		 * eines Vektors an.
		 * @param input
		 * @param datei
		 * @throws TransformerException
		 */
		public void appendDoc(Vector<double[]> input, String datei) throws TransformerException
		{
			Element dateiElement = doc.createElement("datei");
			rootElement.appendChild(dateiElement);
            dateiElement.setAttribute("name",datei);
			
            
			for(int i = 0; i<input.size(); i++)
			{
				Element arrayElement = doc.createElement("Array_"+i);
				dateiElement.appendChild(arrayElement);
				 
				for(int j=0; j<input.get(i).length; j++)
				{
					Element valueElement = doc.createElement("Value_"+j);
					arrayElement.appendChild(valueElement);
					valueElement.appendChild(doc.createTextNode(""+input.get(i)[j]));
				}
				
			}	
		}
		
		/**
		 * Hilfsmethode, die das geöffnete Dokument wieder korrekt schließt.
		 * @throws TransformerException
		 */
		public void closeDoc() throws TransformerException
		{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(config.getXML()));
			transformer.transform(source, result);
		}
}
