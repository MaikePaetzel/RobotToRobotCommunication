/*
 * Created by Maike Paetzel, Natural Language Systems Division, Hamburg University, 6/7/13 11:01 PM.
 * This code is licensed under CC BY-NC-SA 3.0 DE
 * This code uses parts from http://mirlastfm.googlecode.com/svn/trunk/ which was licensed under Creative Commons
 */

package com.bachelorthesis.main;

import com.bachelorthesis.lib.AudioPreProcessor;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Diese Klasse simuliert einen Audiostream aus einer vollständigen Sounddatei
 */
public class VirtualAudioStream {

    private long sampleLength;
    private long frameLength;
    private int lastEnd;
	private double[] fullStream;
	
	public VirtualAudioStream(String u, int windowSize, boolean readComplete) throws UnsupportedAudioFileException, IOException {
		AudioInputStream audioInputStream;
		URL url = new URL("file://"+u);
		audioInputStream = AudioSystem.getAudioInputStream(url);
		frameLength = audioInputStream.getFrameLength();
        int channels = audioInputStream.getFormat().getChannels();
        int bytesPerSample = audioInputStream.getFormat().getSampleSizeInBits() / 8;
        int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
        int samplesPerFrame = bytesPerFrame / bytesPerSample;
		sampleLength = (frameLength / channels) * samplesPerFrame;
        float sampleRate = audioInputStream.getFormat().getSampleRate();
        AudioPreProcessor prepro = new AudioPreProcessor(audioInputStream, sampleRate);
		lastEnd = 0;

		// Verhalten, wenn die komplette Sounddatei gelesen werden soll
		if(readComplete)
		{
			assert ((int) sampleLength) == sampleLength;
			fullStream = new double[(int) sampleLength];
			prepro.append(fullStream, 0, (int) sampleLength);
		}
	}
	

	/**
	 * Diese Methode liefert eine bestimmte Anzahl Samples zurück.
	 * 
	 * @param anzahl Anzahl der Samples, die zurückgegeben werden sollen
	 * @return Array mit der gewünschten Anzahl an Samples
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public double[] getNextSamples(int anzahl) throws IllegalArgumentException, IOException
	{
		//Audiodatei ist vollständig eingelesen
		if(lastEnd >= sampleLength)
		{
			return null;
		}

		double[] newArray = fillWindow(lastEnd,anzahl);
		lastEnd += anzahl;
		
		return newArray;
	}
	
	/** 
	 * Befüllt ein Array mit einer Anzahl von Samples aus 
	 * den Originaldaten ab einem bestimmten Anfangswert
	 * @param begin Sample aus dem Gesamtdatensatz, ab dem befüllt werden soll
	 * @param size Anzahl an Samples
	 * @return befülltes Array
	 */
	public double[] fillWindow(int begin, int size)
	{
			double[] newArray = new double[size]; 
			
			for(int i = begin; i < begin+size; i++)
			{
				try
				{
					newArray[i-begin] = fullStream[i];
				}
				catch(IndexOutOfBoundsException e)
				{
					newArray[i-begin] = 0;
				}
			}
			return newArray;	
	}
	
	/**
	 * Gibt die komplette Audiodatei als Doublearray zurück
	 * @return DoubleArray aller Samples
	 */
	public double[] getFullStream() {
		return fullStream;
	}
}
