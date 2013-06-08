package com.bachelorthesis.infrastructure;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.bachelorthesis.main.Config;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestXMLProcessing {

    @Test
    public void testResourceLoading(){
  
    }

    @Ignore
	@Test
	public void test() throws UnsupportedAudioFileException, IOException, ParserConfigurationException, SAXException, TransformerException {
		CreateXMLFile ref = null; //new Create_Referenzdata();
		ref.writeXML();
		ReadXML reader = new ReadXML(null);
		Map<String,Vector<double[]>> referenzen = reader.getContent();
		System.out.println(referenzen.keySet());
		System.out.println(ref.referenzen.keySet());
		assertTrue(ref.referenzen != referenzen);
		assertTrue(gleich(referenzen, ref.referenzen));
		
	}
	
	private boolean gleich(Map<String,Vector<double[]>> map1, Map<String,Vector<double[]>> map2)
	{
		for(String s: map1.keySet())
		{
			assertTrue(map2.containsKey(s));
			Vector<double[]> viki1 = map1.get(s);
			Vector<double[]> viki2 = map2.get(s);
			assertEquals(viki1.size(), viki2.size());
			for(int i = 0; i < viki1.size(); i++)
			{
				double[] d1 = viki1.get(i);
				double[] d2 = viki2.get(i);
				assertEquals(d1.length, d2.length);
				for(int j = 0; j<d1.length; j++)
				{
					assertEquals(d1[j], d2[j], 0.0);
					if(d1[j] != d2[j])
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}

}
