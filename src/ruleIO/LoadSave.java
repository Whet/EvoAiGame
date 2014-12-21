package ruleIO;

import gameworld.WorldMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import ai.Ai;
import ai.Rule;

public class LoadSave {

	public static void saveAi(File file, Ai ai) {
		try {
			List<Rule> rules = new ArrayList<Rule>();
			for(Rule rule:ai.getRules()) {
				rules.add(rule.copy());
			}
		    ObjectOutput output = new ObjectOutputStream(new FileOutputStream(file));
		    output.writeObject(rules);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Ai loadAi(File file, Ai ai, WorldMap map) {
		try {
			InputStream buffer = new BufferedInputStream(new FileInputStream(file));
			ObjectInput input = new ObjectInputStream (buffer);
			List<Rule> rules = (List<Rule>)input.readObject();
			return new Ai(map, rules);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}