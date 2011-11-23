package com.salas;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class LevelManager {
	GameLevel[] gameMaps;

	public void loadLevelsFromAssets(Context c) {
        AssetManager am = c.getResources().getAssets();
        String assets[] = null;
        try {
            assets = am.list( "levels" );
            gameMaps = new GameLevel[assets.length];
            for (int i = 0; i < assets.length; i++) {
            	InputStream assetStream = am.open("levels/"+assets[i]);
            	InputStreamReader aFile = new InputStreamReader(assetStream);
            	gameMaps[i] = GameLevelFactory.constructFromJson(aFile);
            }
        } catch( IOException ex ) {
            Log.e( "CAR", "I/O Exception", ex );
        }
    }
    public GameLevel getRoadMap(int levelnumber) {
		return gameMaps[levelnumber];
	}

}
