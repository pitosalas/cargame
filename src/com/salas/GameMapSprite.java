package com.salas;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.util.Log;

// Sprites for the background of the Roadmap
//
public class GameMapSprite extends TiledSprite {

	private static BitmapTextureAtlas roadTilesTextureAtlas, parkTilesTextureAtlas;
	private static TiledTextureRegion roadTilesTextureRegion, parkTilesTextureRegion;

/* 
 * Because there are many different kinds of tiles in the art work we give each one a BaseTileType. They are used in
 * different places as the background of the roadmap. We classiy each image according to where it could be used in the map. 
*/
	enum TileImageType {
		// order NSEW
		// All for directions open
		BlockedZ(0, 0, ""),
		
		// Blocked in one direction
		BlockedS(0, 1, "FTFF"), BlockedW(0, 2, "FFFT"), 
		BlockedN(0, 3, "TFFF"), BlockedE(0, 4, "FFTF"),
		
		// Blocked in two directions
		BlockedNE(1, 0, "TFTF"), BlockedSW(1, 1, "FTFT"), BlockedWN(1, 2, "TFFT"), 
		BlockedES(1, 3, "FTTF"), BlockedNS(1, 4, "TTFF"), BlockedEW(1, 5, "FFTT"),
		
		// Blocked in three directions
		BlockedNES(2, 0, "TTTF"), BlockedNEW(2, 1, "TFTT"), 
		BlockedNSW(2, 2, "TTFT"), BlockedESW(2, 3, "FTTT"),

		// Blocked in all directions (not a road really)
		BlockedNESW(0, 5, "TTTT");
		
		// Grass in basic map (only used in early versions)
//		Grass(0, 5, "TTTT");
		
		public TCoord tileCoord;
		public String coding;

		private static int columns = 6;
		private static int rows = 3;

		TileImageType(int row, int column, String code) {
			tileCoord = new TCoord(row, column);
			coding = code;
		}
	}

	// Create a sprite for the indicate Tile on the indicated Roadmap
	public GameMapSprite(GameLevel rmap, Tile tile) {
		super(rmap.getSpriteTLPos(tile).x, rmap.getSpriteTLPos(tile).y, roadOrParkTextureRegion(tile).deepCopy());
		TileImageType ttype = getTileTypefromTile(tile); 
		this.setCurrentTileIndex(ttype.tileCoord.col, ttype.tileCoord.row);
		Log.v("SPRITE", "RM Sprite "+rmap.getSpriteTLPos(tile)+", type="+ttype.toString());
	}

	// Read the file with the images and load it into memory	
	public static void loadResources(CommonActivity ctx, Engine engine) {
		roadTilesTextureAtlas = new BitmapTextureAtlas(2048, 1024, TextureOptions.DEFAULT);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("tiles/");
		roadTilesTextureRegion = BitmapTextureAtlasTextureRegionFactory
								.createTiledFromAsset(roadTilesTextureAtlas, ctx, "roadtiles4.png",
									  0, 0, TileImageType.columns, TileImageType.rows);
		parkTilesTextureAtlas = new BitmapTextureAtlas(2048, 1024, TextureOptions.DEFAULT);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("tiles/");
		parkTilesTextureRegion = BitmapTextureAtlasTextureRegionFactory
								.createTiledFromAsset(parkTilesTextureAtlas, ctx, "parktiles4.png",
									  0, 0, TileImageType.columns, TileImageType.rows);

		engine.getTextureManager().loadTextures(roadTilesTextureAtlas, parkTilesTextureAtlas);

	}
	
//	Return one of the two texture regions depnding on whether this Tile is for road or park
	static private TiledTextureRegion roadOrParkTextureRegion(Tile t) {
		return t.isRoad() ? roadTilesTextureRegion : parkTilesTextureRegion;
//		return  roadTilesTextureRegion;
	}

	// Examine this Tile and determine which TileImageType we need to render for it in the background
	TileImageType getTileTypefromTile(Tile t) {
		boolean[] vec = t.getBlocked();
		int encode = encodeBlockedVector(vec);
//		if (!t.isRoad()) return TileImageType.Grass;
		switch (encode) {
			case 0: return TileImageType.BlockedZ;
			case 1: return TileImageType.BlockedN;
			case 2: return TileImageType.BlockedS;
			case 3: return TileImageType.BlockedNS;
			case 4: return TileImageType.BlockedE;
			case 5: return TileImageType.BlockedNE;
			case 6: return TileImageType.BlockedES;
			case 7: return TileImageType.BlockedNES;
			case 8: return TileImageType.BlockedW;
			case 9: return TileImageType.BlockedWN;
			case 10: return TileImageType.BlockedSW;
			case 11: return TileImageType.BlockedNSW;
			case 12: return TileImageType.BlockedEW;
			case 13: return TileImageType.BlockedNEW;
			case 14: return TileImageType.BlockedESW;
			case 15: return TileImageType.BlockedNESW;
		}
		return null;
	}

	private int encodeBlockedVector(boolean[] vec) {
		// order NSEW
		assert vec.length == 4;
		int result = 0;
		if (vec[0]) result += 1;
		if (vec[1]) result += 2;
		if (vec[2]) result += 4;
		if (vec[3]) result += 8;
		return result;
	}

}
