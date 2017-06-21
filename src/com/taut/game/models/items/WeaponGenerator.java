package com.taut.game.models.items;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taut.game.objects.FolderApplication;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeaponGenerator implements FolderApplication {
	public static Weapon createWeapon(String json) {
    	ObjectMapper mapper = new ObjectMapper();
    	
	    Weapon weapon = null;
	    
		try {
			weapon = mapper.readValue(json, Weapon.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    	
	    return weapon;		

	}

	@Override
	public Weapon readFromFile(Path path) {
		Weapon weapon = null;
		
		try {
			weapon = createWeapon(new String(Files.readAllBytes(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return weapon;
	}
}
