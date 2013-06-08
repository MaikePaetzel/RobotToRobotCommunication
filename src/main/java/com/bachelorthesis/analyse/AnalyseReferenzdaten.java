/**
 * Diese Klasse analysiert von einem Referenzdatensatz entweder die Distanzen
 * gegenüber dem eigenen Datensatz oder gegenüber einem anderen Datensatz.
 */

package com.bachelorthesis.analyse;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.bachelorthesis.main.Load;
import com.bachelorthesis.main.Sprachauswertung;
import com.bachelorthesis.main.StartUpFilePathConfiguration;

public class AnalyseReferenzdaten {
	
	private StartUpFilePathConfiguration config;
	
	public AnalyseReferenzdaten(StartUpFilePathConfiguration config) {
		this.config = config;
	}
	
	/**
	 * Diese Methode gibt eine Matrix mit den Distanzwerten eines Referenzdatensatzes
	 * zu sich selbst aus.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void testDistanceMatrixAgainsSelf() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		Load loader = new Load(config);
		Map<String, Vector<double[]>> map1= loader.getInput();
		Map<String, Vector<double[]>> map2= loader.getInput();
		Sprachauswertung sprache = new Sprachauswertung();

		int i = 0;
		int j = 0;
		double[][] matrix = new double[14][14];
		for(String s: map1.keySet())
		{
			j = 0;
			Vector<double[]> victor1 = map1.get(s);
			for(String p: map2.keySet())
			{
				Vector<double[]> victor2 = map2.get(p);
				double result = sprache.getVectorDistance(victor1, victor2);
				matrix[i][j] = Math.round(result*10)/10.0;
				j++;
			}
			i++;
		}
		printMatrix(matrix);
		
	}
	
	/**
	 * Diese Methode gibt die Distanzwerte eines Referenzdatensatzes zu einem
	 * anderen Referenzdatensatz zurück.
	 * @param config1 Configuration des zweiten Referenzdatensatzes
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void testDistanceMatrixAgainstOther(StartUpFilePathConfiguration config1) throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		Load loader = new Load(config);
		Map<String, Vector<double[]>> map1= loader.getInput();
		
		Load loader1 = new Load(config1);
		Map<String, Vector<double[]>> map2= loader1.getInput();
		Sprachauswertung sprache = new Sprachauswertung();

		int i = 0;
		int j = 0;
		double[][] matrix = new double[14][14];
		for(String s: map1.keySet())
		{
			j = 0;
			Vector<double[]> victor1 = map1.get(s);
			for(String p: map2.keySet())
			{
				Vector<double[]> victor2 = map2.get(p);
				double result = sprache.getVectorDistance(victor1, victor2);
				matrix[i][j] = Math.round(result*10)/10.0;
				j++;
			}
			i++;
		}
		printMatrix(matrix);
		
	}
	
	/**
	 * Hilfsmethode, die eine Matrix auf der Konsole ausgibt.
	 * @param matrix Matrix, die ausgegeben werden soll
	 */
	private void printMatrix(double[][] matrix) {
		for(int i = 0; i < 13; i++){
			for(int j = 0; j < 13; j++)
			{
				System.out.print(matrix[i][j]);
				System.out.print("; ");
			}
			System.out.println();
		}
	}

}
