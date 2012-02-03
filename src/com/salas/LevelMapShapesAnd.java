package com.salas;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.Debug;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class LevelMapShapesAnd {
	
	private LevelModel level;
	private Scene scene;
	private LevelManager lman;
	private Engine engine;
	private FixedStepPhysicsWorld worldBox2d;

	
	LevelMapShapesAnd(WorldAnd world) {
		level = world.currLevel;
		scene = ((WorldSpritesAnd) world.sprites).scene;
		lman = world.levelMgr;
		engine = world.engine;
		worldBox2d = ((WorldBodiesAnd)world.bodies).worldBox2d;
	}
	
	public void createWalls() {
		
		float height = level.getBottomRightPos().y;
		float width = level.getBottomRightPos().x;
		
		// Create the shapes forming each of the walls
		final Shape bottomOuter = new Rectangle(0, height - 2, width, 2);
		final Shape topOuter = new Rectangle(0, 0, width, 2);
		final Shape leftOuter = new Rectangle(0, 0, 2, height);
		final Shape rightOuter = new Rectangle(width - 2, 0, 2, height);

		// Create the Bodys which give the walls substance
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(worldBox2d, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(worldBox2d, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(worldBox2d, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(worldBox2d, rightOuter, BodyType.StaticBody, wallFixtureDef);

		// Attach them to the scene
		scene.attachChild(bottomOuter);
		scene.attachChild(topOuter);
		scene.attachChild(leftOuter);
		scene.attachChild(rightOuter);
	}

	public void createTiledBackground() {
		int columns = level.gridColumns;
			int rows = level.gridRows;
			for (int row = 0; row < rows; row ++) {
				for (int col=0; col < columns; col ++) {
					TileModel tile = level.getTile(new TCoord(row, col));
					LevelMapSprite tileSprite = new LevelMapSprite(level, tile);
					scene.attachChild(tileSprite);
				}
			}
		}

	public void setBackgroundColor() {
		scene.setBackground(new ColorBackground(0, 0.5f, 0));		
	}

//
// A decoration is a bit of art that augments the level's display. 
// Go through each DecorationModel, locat it's art and a copy of it in a tile for each 'placement' location.
//
	public void createDecorations() {
		for (DecorationModel dm : level.decorations().values()) {
			// Each decoration is placed in zero or more places
			for (PlacementModel pm : dm.getPlacements()) {
				TextureRegion decoRegion = lman.decoRegionsMap.get(dm.getName());
				TPos spritePos = level.newTposFromCoords(pm.coord.row, pm.coord.col);
				spritePos.x += pm.xOffset * level.coordScaleFactor;
				spritePos.y += pm.yOffset * level.coordScaleFactor;
				Sprite decoSprite = new Sprite(spritePos.x, spritePos.y, dm.getWidth(), dm.getHeight(), decoRegion);
				scene.attachChild(decoSprite);
			}
		}
	}
	

/**
 * Create the level sprites using an optional filename level.tmxFilename.
 *  
 * A TMX file is created with the tool Tiled (although other tools exist) and
 * describes both the graphic files that represent the level as well as meta data for tiles. The assumped format is that 
 * there are two layers defined in the tmx file. One layer is for non-map graphics, and the other layer has the images
 * which denote the roads over which the vehicles travel. The tiles in the latter layer each have a single property called "map"
 * whose value is one or more of NSEW, indicating for that tile, which directions are blocked. The method reads in the TMX file
 * and catches all the tiles with the "map" property and use them to assemble the map tiles
 * @param carGameActivity
 */
	public void createTMXTiles(CarGameActivity carGameActivity) {
		TMXTiledMap tmxTiledMap = null;
		try {
			final TMXLoader tmxLoader = new TMXLoader(carGameActivity, 
													  engine.getTextureManager(), 
													  TextureOptions.BILINEAR_PREMULTIPLYALPHA, 
													  new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					if(pTMXTileProperties.get(0).getName().equalsIgnoreCase("map"))
					{
						int row = pTMXTile.getTileRow();
						int col =  pTMXTile.getTileColumn();
						String spec = pTMXTileProperties.get(0).getValue();
						LevelModelFactory.tileFromSpecString(level, row, col, spec);
					}
				}
			});
			tmxTiledMap = tmxLoader.loadFromAsset(carGameActivity, level.tmxFileName);
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}
// Attach each layer defined in the tmx file
		for (TMXLayer t : tmxTiledMap.getTMXLayers()) {
			scene.attachChild(t);
		}
	}
}
