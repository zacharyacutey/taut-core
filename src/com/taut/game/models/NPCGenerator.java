package com.taut.game.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taut.game.models.quest.Quest;
import com.taut.game.objects.FolderApplication;

/**
 * @author 19smitgr
 * Class which accepts
 * json input and outputs
 * NPC classes based upon 
 * the json
 */

public class NPCGenerator implements FolderApplication {
	public static ObjectMapper mapper = new ObjectMapper();
	
	public static NPC createNPC(String json) {
	    NPC npc = null;
	    
		try {
			npc = mapper.readValue(json, NPC.class);
			npc.setTexture();
			npc.getQuests().forEach(quest -> {
				quest.getCompleteActions().fillCombinedActionsList();
			});

			// array is used for json mapping, but arraylist is more convenient
			npc.setQuests(npc.getQuests());
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
