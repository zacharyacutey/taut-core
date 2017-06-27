package com.taut.game.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taut.game.models.quest.Quest;
import com.taut.game.objects.FolderApplication;

public class QuestGenerator implements FolderApplication {
	public static ObjectMapper mapper = new ObjectMapper();
	
	public static Quest createQuest(String json) {
	    Quest quest = null;
	    
		try {
			quest = mapper.readValue(json, Quest.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    	
	    return quest;		
	}

	@Override
	public Quest readFromFile(Path path) {
		Quest quest = null;
		
		try {
			quest = createQuest(new String(Files.readAllBytes(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return quest;
	}
}
