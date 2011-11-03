package com.salas;

import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import android.graphics.Color;
import android.graphics.Typeface;

public class DashTextBox {
	static private Font font;
	static private BitmapTextureAtlas fontTexture;
	public ChangeableText textBox;

	public static void loadResources(CommonActivity com) {
		fontTexture = new BitmapTextureAtlas(256, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(fontTexture, Typeface.create(Typeface.DEFAULT,
				Typeface.BOLD), 20, true, Color.WHITE);
		
		com.engine.getTextureManager().loadTexture(fontTexture);
		com.getFontManager().loadFont(font);
	}

	public void createBox(int xPos, int yPos) {
		textBox = new ChangeableText(xPos, yPos, font, "", 60);
	}
}
