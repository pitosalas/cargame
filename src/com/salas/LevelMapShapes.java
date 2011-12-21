package com.salas;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class LevelMapShapes {
	
	private LevelModel level;
	private Scene scene;
	private FixedStepPhysicsWorld world;
	private LevelManager lman;
	
	LevelMapShapes(GameContext gcontext) {
		level = gcontext.currLevel;
		scene = gcontext.scene;
		world = gcontext.world;
		lman = gcontext.levelMgr;
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
		PhysicsFactory.createBoxBody(world, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(world, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(world, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(world, rightOuter, BodyType.StaticBody, wallFixtureDef);

		// Attach them to the scene
		scene.attachChild(bottomOuter);
		scene.attachChild(topOuter);
		scene.attachChild(leftOuter);
		scene.attachChild(rightOuter);
	}

	public void createTiledBackground(int screenHeight, int screenWidth) {
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
}
