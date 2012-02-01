package com.salas;

import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import android.graphics.Color;
import android.graphics.Typeface;

public class TextBox {
	static private Font font;
	static private BitmapTextureAtlas fontTexture;
	public ChangeableText textBox;

	public static void loadResources(CommonActivity com) {
		fontTexture = new BitmapTextureAtlas(256, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(fontTexture, Typeface.create(Typeface.DEFAULT,
				Typeface.NORMAL), 16, true, Color.WHITE);
		
		com.engine.getTextureManager().loadTexture(fontTexture);
		com.getFontManager().loadFont(font);
	}

	public TextBox(int xPos, int yPos) {
		textBox = new ChangeableText(xPos, yPos, font, "", 60);
	}

	public void setText(String label) {
		textBox.setText(label, true);
	}
}
