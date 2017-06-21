package com.taut.game.models.items;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taut.game.objects.FolderApplication;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumableGenerator implements FolderApplication {
	public static Consumable createConsumable(String json) {
    	ObjectMapper mapper = new ObjectMapper();
    	
	    Consumable consumable = null;
	    
		try {
			consumable = mapper.readValue(json, Consumable.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    	
	    return consumable;		

	}

	@Override
	public Consumable readFromFile(Path path) {
		Consumable consumable = null;
		
		try {
			consumable = createConsumable(new String(Files.readAllBytes(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return consumable;
	}
}
