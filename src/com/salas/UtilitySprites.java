package com.salas;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class UtilitySprites {
	private static BitmapTextureAtlas iconsTexture;
	protected static TiledTextureRegion iconsTextureRegion;
	private static final int ICON_SIZE = 32;
	static private GameContext gameCtx;
	private static TiledSprite cursor;

	public static void loadResources(CommonActivity ctx, GameContext gamecontext) {
		gameCtx = gamecontext;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		iconsTexture = new BitmapTextureAtlas(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		iconsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(iconsTexture, ctx, "icons.png", 0, 0, 4, 4);
		gameCtx.engine.getTextureManager().loadTexture(iconsTexture);
	}
	
	public static TiledSprite cursorSprite(float x, float y) {
		if (cursor == null) {
			cursor = new TiledSprite(x, y, ICON_SIZE, ICON_SIZE, iconsTextureRegion.deepCopy());
			cursor.setCurrentTileIndex(0);
			gameCtx.scene.attachChild(cursor);			
		}
		cursor.setPosition(x, y);
		return cursor;
	}
	
	public static TiledSprite cursorSprite(Vector2 pos) {
		return cursorSprite(pos.x, pos.y);
	}
	
	public static TiledSprite plusIconSprite(float x, float y) {
		TiledSprite plus = new TiledSprite(x, y, ICON_SIZE, ICON_SIZE, iconsTextureRegion.deepCopy());
		plus.setCurrentTileIndex(1);
		plus.setPosition(x, y);
		return plus;
	}
	public static TiledSprite minusIconSprite(float x, float y) {
		TiledSprite minus = new TiledSprite(x, y, ICON_SIZE, ICON_SIZE, iconsTextureRegion.deepCopy());
		minus.setCurrentTileIndex(2);
		minus.setPosition(x, y);
		return minus;
	}

}
