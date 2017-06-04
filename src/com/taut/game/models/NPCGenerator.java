package com.taut.game.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class NPCGenerator {
	public NPC createNPC(String json) {
    	ObjectMapper mapper = new ObjectMapper();
    	
	    NPC npc = null;
	    
		try {
			npc = mapper.readValue(json, NPC.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    	
	    return npc;		

	}
	
	public ArrayList<NPC> generateAllNPCs() {
		NPCGenerator npcGenerator = new NPCGenerator(); 
		ArrayList<NPC> npcs = new ArrayList<>();
		
		// read through all JSON files in the NPC folder and make NPCs
		try {
			Files.walk(Paths.get("./bin/NPC/"))
			 .filter(Files::isRegularFile)
			 .forEach(path -> {
				try {
					NPC npc = npcGenerator.createNPC(new String(Files.readAllBytes(path)));
					npcs.add(npc);
				} catch (IOException e) {
					e.printStackTrace();
				}
			 });
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return npcs;
	}
}
