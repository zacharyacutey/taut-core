package com.taut.game.models.items;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taut.game.objects.FolderApplication;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InteractionItemGenerator implements FolderApplication {
	public static InteractionItem createInteractionItem(String json) {
    	ObjectMapper mapper = new ObjectMapper();
    	
	    InteractionItem interactionItem = null;
	    
		try {
			interactionItem = mapper.readValue(json, InteractionItem.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    	
	    return interactionItem;		

	}

	@Override
	public InteractionItem readFromFile(Path path) {
		InteractionItem interactionItem = null;
		
		try {
			interactionItem = createInteractionItem(new String(Files.readAllBytes(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return interactionItem;
	}
}
