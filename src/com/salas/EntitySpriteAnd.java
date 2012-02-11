package com.salas;


import org.anddev.andengine.entity.sprite.*;
import org.anddev.andengine.opengl.texture.*;
import org.anddev.andengine.opengl.texture.atlas.bitmap.*;
import org.anddev.andengine.opengl.texture.region.*;

public class EntitySpriteAnd extends EntitySprite {
	private BitmapTextureAtlas carTexture;
	protected TiledTextureRegion carTextureRegion;
	protected TiledSprite sprite;
	private static final int CAR_WIDTH = 64;
	private static final int CAR_HEIGHT = 32;

	@Override
	public void loadResources(World<WorldBodies, WorldSprites> modCtx) {
		WorldAnd ctx = (WorldAnd) modCtx;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		carTexture = new BitmapTextureAtlas(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		carTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(carTexture, ctx.aeCtx, "carsfancy.png", 0, 0, 2, 4);
		ctx.engine.getTextureManager().loadTexture(carTexture);
	}
	
	public void createSprite() {
		sprite = new TiledSprite(0.0f, 0.0f, CAR_WIDTH, CAR_HEIGHT, carTextureRegion);
		sprite.setCurrentTileIndex(0);
	}
	
	TiledSprite spriteAnd() {
		return sprite;
	}
	
	@Override
	public void setRotation(float heading) {
		sprite.setRotation((float) (heading * 180 / Math.PI));
	}

	@Override
	public void setPos(Vector2 pos) {
		sprite.setPosition(pos.x, pos.y);
	}
}
