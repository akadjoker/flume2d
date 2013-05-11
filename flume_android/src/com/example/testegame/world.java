package com.example.testegame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.flume2d.Engine;
import com.flume2d.Entity;
import com.flume2d.Scene;
import com.flume2d.graphics.Image;
import com.flume2d.graphics.Spritemap;


public class world extends Scene {

	private BitmapFont font;
	
public world()
{


	
}



@Override
public void OnLoad() {
	font = new BitmapFont(Gdx.files.internal("data/verdana39.fnt"), true);
	//System.out.printf("\n load scene");
	
	add(new player(new Image("data/badlogic.jpg")));
	add(new animation(new Spritemap("data/zombietxai.png",36,41)));
	
	//add(new Entity(20,50,new Image("data/badlogic.jpg")));
	
	
}

@Override
public void OnRender(SpriteBatch spriteBatch) {
	// TODO Auto-generated method stub

	font.setColor(Color.GREEN);
	
	
	font.draw(spriteBatch,"fps: " + Gdx.graphics.getFramesPerSecond(),10,20);
	font.draw(spriteBatch,"Delta: " +Gdx.graphics.getDeltaTime(),10,80);
	font.draw(spriteBatch,"Delta: " +Engine.MouseBut,10,80);
	font.draw(spriteBatch,"Delta: " +Engine.MouseCount,10,80);
	
	font.draw(spriteBatch, "Ola Mundo",10,380);
	font.draw(spriteBatch, "Djoker",Engine.width-100,Engine.height-100);
	font.draw(spriteBatch, "Djoker",Engine.width-100,10);
	font.draw(spriteBatch, "Djoker",10,Engine.height-100);

	
}




}
