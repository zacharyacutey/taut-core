package com.taut.game.models.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Garrett
 * Honestly, I just thought it would be cool to have some flashes in the game
 */
public class FlashRenderer {
	ShapeRenderer shapeRenderer;
	Flash flash;
	
	public class Flash {
		public float timePassed = 0.0f;
		public float duration;
		public float maximumIntensity;
		
		public Flash(float duration, float maximumIntensity) {
			this.duration = duration;
			this.maximumIntensity = maximumIntensity;
		}
		
		public void flash(float delta, boolean recurring) {
			timePassed += delta;
			
			if (!(timePassed >= duration)) {
				int mapWidth = Gdx.graphics.getWidth();
				int mapHeight = Gdx.graphics.getHeight();
				
				float intensity = (float) getIntensity(delta, this);
				
			    Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
			    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
					shapeRenderer.setColor(new Color(255, 0, 0, (float) (intensity/10)));
		        	shapeRenderer.rect(0, 0, mapWidth, mapHeight);
		        shapeRenderer.end();
		        Gdx.gl.glDisable(GL20.GL_BLEND);
			} else {
				if (recurring) timePassed = 0.0f;
			}
		}
	}
	
	public FlashRenderer(ShapeRenderer shapeRenderer) {
		this.shapeRenderer = shapeRenderer;
	}
	
	public Flash createFlash(float duration, float maximumIntensity) {
		return new Flash(duration, maximumIntensity);
	}
	
	/**
	 * @param delta how much time has passed since flash started determines flash intensity at current time
	 */
	public double getIntensity(float delta, Flash flash){
		flash.timePassed += delta;
		
		float halfDuration = flash.duration/2;
		
		// curve of y = k/h^2 (x-h)^2 + k
		double intensity = -flash.maximumIntensity/Math.pow(halfDuration, 2)*Math.pow(flash.timePassed-halfDuration, 2)+flash.maximumIntensity;
		
		return intensity;
	}
}
