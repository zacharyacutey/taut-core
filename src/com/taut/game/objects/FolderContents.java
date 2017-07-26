package com.taut.game.objects;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Iterator;

/**
 * @author Garrett
 * Operations on every file in a folder
 */
public class FolderContents {
	String pathName;
	
	public FolderContents(String pathName) {
		this.pathName = pathName;
	}
	
	/**
	 * @param application Applies FolderApplication to all files in folder
	 * @param mutatedFileStorage Stores mutated file info in mutatedFileStorage
	 */
	public <T> void generateAndStore(FolderApplication application, List<T> mutatedFileStorage) {
		String modifiedPathName = generateFolderPath();
		
		// read through all JSON files in the specified folder and store info in the mutatedFileStorage
		try {
			//Files.walk(Paths.get(modifiedPathName))
			// .filter(Files::isRegularFile)
			// .forEach(path -> {
			//	mutatedFileStorage.add((T) application.readFromFile(path));
			// });
			Iterator<Path> it = Files.walkFileTree(Paths.get(modifiedPathName)).iterator();
			Path path = null;
			while(it.hasNext()) {
				path = it.next();
				if(isRegularFile(path)) {
					mutatedFileStorage.add((T) application.readFromFile(path));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String generateFolderPath() {
		// for some reason, different OSs create different folders when it compiles
		File npcFolder = new File("bin");
		if (npcFolder.exists() && npcFolder.isDirectory()) {
			return "./bin/" + this.pathName;
		} else {
			return "./" + this.pathName;
		}
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
}
