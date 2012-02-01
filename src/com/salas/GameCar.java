package com.salas;


import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.salas.TileModel.TDir;

public class GameCar {
	private BitmapTextureAtlas carTexture;
	protected TiledTextureRegion carTextureRegion;
	protected TiledSprite sprite;
	private static final int CAR_WIDTH = 64;
	private static final int CAR_HEIGHT = 32;

	
	public void loadResources(CommonActivity ctx, Engine engine) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		carTexture = new BitmapTextureAtlas(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		carTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(carTexture, ctx, "carsfancy.png", 0, 0, 2, 4);
		engine.getTextureManager().loadTexture(carTexture);
	}
	
	public void createSprite() {
		sprite = new TiledSprite(0.0f, 0.0f, CAR_WIDTH, CAR_HEIGHT, carTextureRegion);
		sprite.setCurrentTileIndex(0);
	}
	
	TiledSprite sprite() {
		return sprite;
	}
	
	public void setPosAndRotation(TPos pos, TDir rot) {
		sprite.setPosition(pos.x, pos.y);
		sprite.setRotation(rot.toDegrees());
	}
	
	public void setRotation(float heading) {
		sprite.setRotation((float) (heading * 180 / Math.PI));
	}


}
