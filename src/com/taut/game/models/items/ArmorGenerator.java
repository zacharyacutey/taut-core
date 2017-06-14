package com.taut.game.models.items;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taut.game.objects.FolderApplication;

public class ArmorGenerator implements FolderApplication {
	public static Armor createArmor(String json) {
    	ObjectMapper mapper = new ObjectMapper();
    	
	    Armor armor = null;
	    
		try {
			armor = mapper.readValue(json, Armor.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    	
	    return armor;		

	}

	@Override
	public Armor readFromFile(Path path) {
		Armor armor = null;
		
		try {
			armor = createArmor(new String(Files.readAllBytes(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return armor;
	}
}
