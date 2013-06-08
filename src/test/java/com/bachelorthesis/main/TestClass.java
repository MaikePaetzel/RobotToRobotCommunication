package com.bachelorthesis.main;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;


import com.bachelorthesis.infrastructure.ReadReferenceFromAudioFile;
import com.bachelorthesis.main.Sprachauswertung;
import com.bachelorthesis.main.VirtualAudioStream;
import com.bachelorthesis.main.Config;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestClass {
	
	StartUpFilePathConfiguration config;
	
	@Ignore
	@Test
	public void testVectorDist() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
	Sprachauswertung sprache = new Sprachauswertung(null);
			
			Vector<double[]> viki = new Vector<double[]>();
			double[] d = new double[5];
			double[] d1 = new double[5];
			double[] d2 = new double[5];
			for (int i = 0; i < 5; i++)
			{
				d[i] = i;
				d1[i] = 0;
				d2[i] = i*100;
				
			}
			viki.add(d);
			viki.add(d1);
			viki.add(d2);
			
			assertEquals(0.0, sprache.getVectorDistance(viki, viki),0.0);
	}

    @Ignore
	@Test
	public void testDistanceMatrix() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		Sprachauswertung sprache = new Sprachauswertung(null);
		Map<String, double[]> input = getInput(config);
		Map<String, double[]> input2 = getInputTwo();
		assertEquals(13, input.size());
		double[][] matrix = new double[13][13];
		double sum;
		double sum1 =0;
		for(int i = 0; i < 13; i++){
			sum = 0;
			for(int j = 0; j < 13; j++)
			{
				//assertFalse(input.get(j) == null);
				
				Vector<double[]> victor1 = sprache.generateMelCepstrum(Sprachauswertung.fillData(input.get("data" + i + ".wav")));
				Vector<double[]> victor2 = sprache.generateMelCepstrum(Sprachauswertung.fillData(input.get("data" + j + ".wav")));
				double result = sprache.getVectorDistance(victor1, victor2);
				matrix[i][j] = Math.round(result*10)/10.0;
				sum += Math.round(result*10)/10.0;
			}
			System.out.println(sum/13);
			sum1 += sum/13;
		}
		printMatrix(matrix);
		System.out.println(sum1/13);
		
	}
	
	@Ignore
	@Test
	public void testDistanceMatrixWithOffset() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		Sprachauswertung sprache = new Sprachauswertung(null);
		Map<String, double[]> input = getInput(config);
		assertEquals(13, input.size());
		double[][] matrix = new double[13][13];
		for(int i = 0; i < 13; i++){
			for(int j = 0; j < 13; j++)
			{
				Vector<double[]> victor1 = sprache.generateMelCepstrum(sprache.fillData(input.get(i)));
				double[] temp = sprache.fillData(verschiebe(input.get(j)));
				
				Vector<double[]> victor2 = sprache.generateMelCepstrum(temp);
				double result = sprache.getVectorDistance(victor1, victor2);
				matrix[i][j] = Math.round(result*10)/10.0;
			}
		}
		printMatrix(matrix);
		
	}
	@Ignore
	@Test
	public void testDistanceMatrixOriginal() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException
	{
		StartUpFilePathConfiguration config = new StartUpFilePathConfiguration();
		config.setXml("/resources/referenzenESpeak.xml");
		config.setPlaintext("/resources/plaintext.txt");
		Load loader = new Load(config);
		Map<String, Vector<double[]>> map1= loader.getInput();
		Map<String, Vector<double[]>> map2= loader.getInput();
		Sprachauswertung sprache = new Sprachauswertung();

		vergleiche(map1,map2);
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
	
	private void vergleiche(Map<String, Vector<double[]>> referenzen1, Map<String, Vector<double[]>> referenzen2) throws UnsupportedAudioFileException, IOException {

		for(String s : referenzen1.keySet()){
			Vector<double[]> vec1 = referenzen1.get(s);
			Vector<double[]> vec2 = referenzen2.get(s);
			assertEquals(vec1.size(), vec2.size());
			for(int k = 0; k < vec1.size(); k++)
			{
				double[] d1 = vec1.get(k);
				double[] d2 = vec2.get(k);
				assertEquals(d1.length, d2.length);
				for(int j = 0; j<d1.length; j++)
				{
					assertEquals(d1[j], d2[j], 5);
				}
			}
		}
		
		
	}

	private double[] verschiebe(double[] d)
	{
		int l = 256;
		double[] temp = new double[d.length+l];
		System.arraycopy(d, 0, temp, l, d.length);
		return temp;
	}
	
	
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

	private Map<String, double[]> getInput(StartUpFilePathConfiguration config) throws UnsupportedAudioFileException, IOException
	{
		Map<String, double[]> result = new HashMap<String, double[]>();
		File[] filearray = getAllFiles(config.getDictionary());
		for(File f: filearray)
		{
			VirtualAudioStream virtuali = new VirtualAudioStream(f.getAbsolutePath(), Config.defaultWindowSize, true);
			result.put(f.getName(), virtuali.getFullStream());
		}

		
		return result;
		
	}
	
	private Map<String, double[]> getInputTwo() throws UnsupportedAudioFileException, IOException
	{
		Map<String, double[]> result = new HashMap<String, double[]>();
		File[] filearray = getAllFiles(null);
		for(File f: filearray)
		{
			VirtualAudioStream virtuali = new VirtualAudioStream(f.getAbsolutePath(), Config.defaultWindowSize, true);
			result.put(f.getName(), virtuali.getFullStream());
		}

		
		return result;
		
	}
	
	
	private static File[] getAllFiles(String path)
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
