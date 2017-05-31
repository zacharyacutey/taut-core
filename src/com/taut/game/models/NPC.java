package com.taut.game.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Math.toIntExact;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NPC { 
    String name;
    String imageName;
    String dialogue;
    int spriteId;
    
    // placement
    int screenId;
    int[] coords = new int[2];
 
    // quests
    List<Quest> quests = new ArrayList<>();
    
    public void readAllNPCAssets() {
    	try (Stream<Path> paths = Files.walk(Paths.get("assets/NPC/"))) {
    	    paths.forEach(path -> {
				try {
					readJSON(new String(Files.readAllBytes(path)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public void readJSON(String json) {
    	JSONParser parser = new JSONParser();
    	
	    try {
	    	JSONObject obj = (JSONObject) parser.parse(json);
	    	System.out.println(obj);
	    	
	    	// populating the NPC info without any quest info
	    	String name = (String) obj.get("name");
	    	this.name = name;
	    	
	    	String imageName = (String) obj.get("imageName");
	    	this.imageName = imageName;
	    	
	    	String dialogue = (String) obj.get("dialogue");
	    	this.dialogue = dialogue;
	    	
	    	long spriteId = (long) obj.get("spriteID");
	    	this.spriteId = toIntExact(spriteId);
	    	
	    	// placement
	    	JSONObject placement = (JSONObject) obj.get("placement");
	    	
	    	long screenId = (long) placement.get("screenID");
	    	this.screenId = toIntExact(screenId);
	    	
	    	// convert JSONArray to normal array
	    	JSONArray coordsInJSONArray = (JSONArray) placement.get("coordsOnScreen");
	    	int[] coords = {(int) coordsInJSONArray.get(0), (int) coordsInJSONArray.get(1)};
	    	this.coords = coords;
	  
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDialogue() {
		return dialogue;
	}

	public void setDialogue(String dialogue) {
		this.dialogue = dialogue;
	}

	public int getSpriteId() {
		return spriteId;
	}

	public void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public int[] getCoords() {
		return coords;
	}

	public void setCoords(int[] coords) {
		this.coords = coords;
	}

	public List<Quest> getQuests() {
		return quests;
	}

	public void setQuests(List<Quest> quests) {
		this.quests = quests;
	}
}