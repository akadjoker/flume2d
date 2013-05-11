
import com.flume2d.Engine;
import com.flume2d.Entity;
import com.flume2d.graphics.Image;
import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;
import com.flume2d.masks.Circle;


public class animation extends Entity {

	private Spritemap image;
	
	public animation(Spritemap img)
	{
	
		
		
	super(200,100,img);
	


	
	image=img;
	
	image.add("walk", new int[]{0, 1, 2, 3, 4, 5}, 0.2f,true);
	image.play("walk",true);


	setType("animation");
	setHitbox(80,80);
	
	}

	@Override public void update()
	{
		super.update();

	//	Engine.stepTowards(this, 0,0, 1.1f);
		x=Engine.MouseX;
		y=Engine.MouseY;
		
		//y=y+0.2f;
		//image.currentAnim.frame++;
		
		
	}
}
