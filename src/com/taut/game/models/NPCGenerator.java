package com.taut.game.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taut.game.objects.FolderApplication;

/**
 * @author 19smitgr
 * Class which accepts
 * json input and outputs
 * NPC classes based upon 
 * the json
 */

public class NPCGenerator implements FolderApplication {
	public static NPC createNPC(String json) {
    	ObjectMapper mapper = new ObjectMapper();
    	
	    NPC npc = null;
	    
		try {
			npc = mapper.readValue(json, NPC.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    	
	    return npc;		

	}

	@Override
	public NPC readFromFile(Path path) {
		NPC npc = null;
		
		try {
			npc = createNPC(new String(Files.readAllBytes(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return npc;
	}
}
