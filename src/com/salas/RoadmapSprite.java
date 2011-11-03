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
public class RoadmapSprite extends TiledSprite {

	private static BitmapTextureAtlas tileTextureAtlas;
	private static TiledTextureRegion tilesTextureRegion;

/* 
 * Because there are many different kinds of tiles in the art work we give each one a BaseTileType. They are used in
 * different places as the background of the roadmap. We classiy each image according to where it could be used in the map. 
*/
	enum TileImageType {
		
		// All for directions open
		RoadBlockedZ(0, 0, "R", ""),
		
		// Blocked in one direction
		RoadBlockedS(0, 1, "R", "FTFF"), RoadBlockedW(0, 2, "R", "FFFT"), 
		RoadBlockedN(0, 3, "R", "TFFF"), RoadBlockedE(0, 4, "R", "FFTF"),
		
		// Blocked in two directions
		RoadBlockedNE(1, 0, "R", "TFTF"), RoadBlockedSW(1, 1, "R", "FTFT"), RoadBlockedWN(1, 2, "R", "TFFT"), 
		RoadBlockedES(1, 3, "R", "FTTF"), RoadBlockedNS(1, 4, "R", "TTFF"), RoadBlockedEW(1, 5, "R", "FFTT"),
		
		// Blocked in three directions
		RoadBlockedNES(2, 0, "R", "TTTF"), RoadBlockedNEW(2, 1, "R", "TFTT"), 
		RoadBlockedNSW(2, 2, "R", "TTFT"), RoadBlockedESW(2, 3, "R", "FTTT"),

		// Blocked in all directions (not a road really)
		RoadBlockedNESW(0, 5, "R", "TTTT"),
		
		// Land
		Land(2, 4, "L", "");

		public TCoord tileCoord;
		public String coding;
		public String type;

		private static int columns = 6;
		private static int rows = 3;

		TileImageType(int row, int column, String tp, String code) {
			tileCoord = new TCoord(row, column);
			coding = code;
			type = tp;
		}
	}

	// Create a sprite for the indicate Tile on the indicated Roadmap
	public RoadmapSprite(Roadmap rmap, Tile tile) {
		super(rmap.getSpriteTLPos(tile).x, rmap.getSpriteTLPos(tile).y, tilesTextureRegion.deepCopy());
		TileImageType ttype = getTileTypefromTile(tile); 
		this.setCurrentTileIndex(ttype.tileCoord.col, ttype.tileCoord.row);
		Log.v("SPRITE", "RM Sprite "+rmap.getSpriteTLPos(tile)+", type="+ttype.toString());
	}

	// Read the file with the images and load it into memory	
	public static void loadResources(CommonActivity ctx, Engine engine) {
		tileTextureAtlas = new BitmapTextureAtlas(2048, 1024, TextureOptions.DEFAULT);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("tiles/");
		tilesTextureRegion = BitmapTextureAtlasTextureRegionFactory
								.createTiledFromAsset(tileTextureAtlas, ctx, "streetmaps.png",
									  0, 0, TileImageType.columns, TileImageType.rows);
		engine.getTextureManager().loadTexture(tileTextureAtlas);

	}

	// Examine this Tile and determine which TileImageType we need to render for it in the background
	TileImageType getTileTypefromTile(Tile t) {
		boolean[] vec = t.getBlocked();
		boolean road = t.isRoad();
		int encode = encodeBlockedVector(vec);
		if (road) {
			switch (encode) {
				case 0: return TileImageType.RoadBlockedZ;
				case 1: return TileImageType.RoadBlockedN;
				case 2: return TileImageType.RoadBlockedS;
				case 3: return TileImageType.RoadBlockedNS;
				case 4: return TileImageType.RoadBlockedE;
				case 5: return TileImageType.RoadBlockedNE;
				case 6: return TileImageType.RoadBlockedES;
				case 7: return TileImageType.RoadBlockedNES;
				case 8: return TileImageType.RoadBlockedW;
				case 9: return TileImageType.RoadBlockedWN;
				case 10: return TileImageType.RoadBlockedSW;
				case 11: return TileImageType.RoadBlockedNSW;
				case 12: return TileImageType.RoadBlockedEW;
				case 13: return TileImageType.RoadBlockedNEW;
				case 14: return TileImageType.RoadBlockedESW;
				case 15: return TileImageType.RoadBlockedNESW;
			}
			return null;
		} else {
			return TileImageType.Land;
		}
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
