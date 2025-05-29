package game.util.audio;

import java.util.ArrayList;

import ddf.minim.AudioSample;
import ddf.minim.Minim;

import game.util.Material;

public class AudioHelper {
	private static AudioHelper instance;
	private static ArrayList<AudioSample> samples;
	private static Minim minim;
	
	public AudioHelper(Minim minim) {
		instance = this;
		AudioHelper.minim = minim;
	}
	
	
	private void loadBasicAudio() {
		
	}
	
	
	
	public static AudioHelper get() {
		return instance;
	}



	public static Minim getMinim() {
		return minim;
	}
	
	
	
	public static AudioSample getPhysicsSound(Material material1, Material material2) {
		switch(material1) {
			case PLASTIC:
				
			
		}
		return null;
		
	}
}
