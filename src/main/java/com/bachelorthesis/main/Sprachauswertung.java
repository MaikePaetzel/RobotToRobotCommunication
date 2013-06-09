/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:07 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.main;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.bachelorthesis.lib.MFCC;

/**
 * Diese Klasse ist die eigentliche Analyse, die für eine gegebene Menge an
 * Sprachdaten ausgibt, welcher Text gesprochen wurde.
 */
public class Sprachauswertung {

	
	private Map<String,Vector<double[]>> referenzen;
	private Map<String, String> contentfolder;
	private double minDistance;
	private String valueOfMinDist;

	
	public Sprachauswertung(StartUpFilePathConfiguration config) throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException {
		Load loader = new Load(config);
		referenzen = loader.getInput();
		contentfolder = loader.getContentfolder();
		minDistance = Double.MAX_VALUE;
		valueOfMinDist = "";
	}
	
	public Sprachauswertung() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException {
	}
	
	/**
	 * Diese Methode füllt ein gegebenes Array mit Nullen auf, damit die
	 * Anforderung der hopSize erfüllt wird.
	 * @param d Eingabearray
	 * @return aufgefülltes Ausgabearray
	 */
	public static double[] fillData(double[] d)
	{
		int length = d.length;
		int hopSize = Config.defaultWindowSize/2;
		int rest = length % hopSize;
		int toAdd = hopSize - rest;
		length += toAdd;
		
		double[] temp = new double[length];
		System.arraycopy(d, 0, temp, 0, d.length);
		return temp;
	}
	
	/**
	 * Diese Methode berechnet für ein gegebenes Array den darin gesprochenen Text.
	 * Dabei wird jedoch nicht das komplette Array pro Analysedurchgang benutzt
	 * sondern die berechnete Länge der Spracheingabe wird aus dem Array mit verschiedenen
	 * möglichen Verschiebungen selektiert und über allen Verschiebungen das globale Minimum
	 * berechnet.
	 * @param d Eingabearray
	 * @return gesprochener Text im Eingabearray
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	public String getMatch(double[] d) throws UnsupportedAudioFileException, IOException
	{
		int length = d.length - 2*Config.OVERHEAD;
		int hopSize = Config.defaultWindowSize/2;
		int rest = length % hopSize;
		int toAdd = hopSize - rest;
		length += toAdd;
		
		minDistance = Integer.MAX_VALUE;
		valueOfMinDist = "";

		// Für jeden 32 wird das Minimum berechnet
		for(int i = 0; i < Config.OVERHEAD; i+=32)
		{
			double[] temp = new double[length];
			System.arraycopy(d, i, temp, 0, length);
			computeMin(temp);
		}

		//System.out.println(minDistance);
		return contentfolder.get(valueOfMinDist);
		
	}
	
	/**
	 * Diese Methode berechnet für ein gegebenes Array den besten dazu passenden
	 * Referenzdatensatz und prüft, ob dieser das globale Minimum bildet.
	 *
     * @param temp Eingabearray
     * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	private void computeMin(double[] temp) throws UnsupportedAudioFileException, IOException
	{
		Vector<double[]> toCheck = generateMelCepstrum(temp);
	
		for(String s : referenzen.keySet())
		{
			double distance = getVectorDistance(toCheck, referenzen.get(s));
	
			if(distance < minDistance)
			{
				minDistance = distance;
				valueOfMinDist = s;	
			}
		}
		
	}
	
	/**
	 * Ein gegebener Sampledatensatz wird hier in das Mel-Cepstrum umgerechnet
	 * @param d
	 * @return Vector im Mel-Cepstrum
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	public Vector<double[]> generateMelCepstrum(double[] d) throws UnsupportedAudioFileException, IOException{
		
		MFCC mel = new MFCC(Config.Sampling_Rate, Config.defaultWindowSize);
		double[][] temp =  mel.process(d);
		
		Vector<double[]> vector = new Vector<double[]>();
		
		for(double[] s : temp)
		{
			vector.add(s);
		}
		return vector;
	}

	/**
	 * Berechnet die Distanz zwischen zwei gegebenen Vektoren
	 * @param vektor1
	 * @param vektor2
	 * @return Vektordistanz als double
	 */
	public double getVectorDistance(Vector<double[]> vektor1, Vector<double[]> vektor2)
	{
		double distance = 0;
		int min = Math.min(vektor1.size(), vektor2.size());
	
		for(int i = 0; i<min ; ++i)
		{
			distance += getArrayDistance(vektor1.get(i), vektor2.get(i));	
		}
		return (Math.sqrt(distance)/min);
	}

	/**
	 * Berechnet die Distanz zwischen zwei gegebenen Arrays
	 * @param array1
	 * @param array2
	 * @return Arraydistanz als double
	 */
	private double getArrayDistance(double[] array1, double[] array2)
	{
		double distance = 0;
		
		for(int i = 0; i<array1.length; ++i)
		{
			distance += Math.pow(array1[i] - array2[i], 2);	
		}
		return distance;
	}

}
