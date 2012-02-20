package com.salas;

import static com.google.common.base.Preconditions.*;

import java.util.*;

import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class UtilitySprites {
	private static BitmapTextureAtlas iconsTexture;
	protected static TiledTextureRegion iconsTextureRegion;
	static private WorldAnd world;
	private static TiledSprite cursor;
	private static HashMap<String, TiledSprite>cursors = new HashMap<String, TiledSprite>();

	private static final int ICON_SIZE = 32;

   public static void loadResources(CommonActivity ctx, WorldAnd gamecontext) {
		world = gamecontext;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		iconsTexture = new BitmapTextureAtlas(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		iconsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(iconsTexture, ctx, "icons.png", 0, 0, 4, 4);
		world.engine.getTextureManager().loadTexture(iconsTexture);
	}
	
	public static void unloadResources() {
		cursor = null;
	}
	
	public static void setCursorSprite(String name, Vector2 position) {
	   TiledSprite newCursor;
      checkNotNull(name);
	   if ((newCursor = cursors.get(name)) == null) {
	      newCursor = new TiledSprite(position.x-ICON_SIZE/2, position.y-ICON_SIZE/2, ICON_SIZE, ICON_SIZE, iconsTextureRegion.deepCopy());
	      newCursor.setCurrentTileIndex(0);
         ((WorldSpritesAnd)world.sprites).scene.attachChild(newCursor);
         cursors.put(name, newCursor);
	   } else {
	      newCursor.setPosition(position.x, position.y);
	   }
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
