package com.example.testegame;

import com.flume2d.*;
import com.flume2d.graphics.Image;
import com.flume2d.masks.AABB;
import com.flume2d.masks.Circle;

public class player extends Entity {

	private Image image;
	public player(Image img)
	{
	super(0,50,img);

	image=img;
	image.scale=0.5f;
	image.originX=0.5f;
	image.originY=0.5f;
	
	setHitbox(120, 120);
		
	setType("player");
		
		
	}

	@Override public void update()
	{
	//	super.update();
		//image.angle++;
	//	x++;
		//moveBy(1,0,"animation",true);
		//moveBy(1,0);
		Engine.stepTowards(this, 0,0, 1.1f);
		if (x>Engine.width)x=0;
	
		
		Entity col=collide("animation",x,y);
		//Entity col=overlaps("animation");
		
		if (col!=null)
		{
			col.y=col.y+1;
			//col.dispose();
		}
		
		
	}
	
	
}
