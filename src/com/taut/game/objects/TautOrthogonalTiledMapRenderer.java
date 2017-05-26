package com.taut.game.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TautOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer {

	public TautOrthogonalTiledMapRenderer (TiledMap map) {
		super(map);
	}

	public TautOrthogonalTiledMapRenderer (TiledMap map, Batch batch) {
		super(map, batch);
	}

	public TautOrthogonalTiledMapRenderer (TiledMap map, float unitScale) {
		super(map, unitScale);
	}

	public TautOrthogonalTiledMapRenderer (TiledMap map, float unitScale, Batch batch) {
		super(map, unitScale, batch);
	}
	
	@Override
	public void render () {
		beginRender();
		for (MapLayer layer : map.getLayers()) {
			if (layer.isVisible()) {
				if (layer instanceof TiledMapTileLayer) {
					renderTileLayer((TiledMapTileLayer)layer);
				} else if (layer instanceof TiledMapImageLayer) {
					renderImageLayer((TiledMapImageLayer)layer);
				} else {
					renderObjects(layer);
				}
			}
		}
		endRender();
	}
}
