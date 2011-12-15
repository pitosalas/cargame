package com.salas;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

// Manages the descriptors of the levels. Reads them in from json file
// and returns them as java GameLevel objects. Also manages resources that are needed for the levels.
public class LevelManager {
	LevelModel[] gameLevels;
	BitmapTextureAtlas decoAtlas;
	HashMap<String, TextureRegion> decoRegionsMap = new HashMap<String, TextureRegion>();

	public void loadLevelsFromAssets(Context c) {
        AssetManager am = c.getResources().getAssets();
        String assets[] = null;
        try {
            assets = am.list( "levels" );
            gameLevels = new LevelModel[assets.length];
            for (int i = 0; i < assets.length; i++) {
            	InputStream assetStream = am.open("levels/"+assets[i]);
            	InputStreamReader aFile = new InputStreamReader(assetStream);
            	gameLevels[i] = LevelModelFactory.constructFromJson(aFile);
            }
        } catch( IOException ex ) {
            Log.e( "CAR", "I/O Exception", ex );
        }
    }
    public LevelModel getLevel(int levelnumber) {
    	assert levelnumber >= 0 && levelnumber < gameLevels.length;
		return gameLevels[levelnumber];
	}

    public void loadDecorationResources(CommonActivity activity, GameContext context) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("tiles/");
		decoAtlas = new BitmapTextureAtlas(1024, 1024, TextureOptions.DEFAULT);
		for (int level = 0; level < gameLevels.length; level++) {
			LevelModel lm = gameLevels[level];
			for (DecorationModel dm : lm.decorations().values()) {
// Create a TextureRegion using the indicated file, and set the origin at the x and y in the file
// Where we can find the art for the decoration.
				TextureRegion region = 
						BitmapTextureAtlasTextureRegionFactory.createFromAsset(decoAtlas, 
																			   activity, 
																			   "decorations1.png", 
																			   (int) dm.getPosition().x, 
																			   (int) dm.getPosition().y);
				decoRegionsMap.put(dm.getName(), region);
			}
		}
		context.engine.getTextureManager().loadTextures(decoAtlas);
    }
}

