package com.flume2d;





import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.flume2d.input.Input;
import com.flume2d.input.Touch;
import com.flume2d.math.Vector2;

/**
 * The game engine singleton classInputProcessor
 * @author matt.tuttle
 */
public class Engine implements ApplicationListener,InputProcessor
{
	public static int width;
	public static int height;
	public static float elapsed = 0;
	private SpriteBatch spriteBatch;
	public static int MouseX;
	public static int MouseY;
	
	public static int ArrMouseX[]=new int[10];
	public static int ArrMouseY[]=new int[10];
	
	private static boolean[] keys = new boolean[256];
	
	@Override
	public void create()
	{
		Engine.width = Gdx.graphics.getWidth();
		Engine.height = Gdx.graphics.getHeight();
	//	Gdx.input.setInputProcessor(new Input());
		  Gdx.input.setInputProcessor(this);
		
			Matrix4 projection = new Matrix4();
	        projection.setToOrtho(0, Engine.width, Engine.height, 0, -1, 1);
			spriteBatch = new SpriteBatch();
			spriteBatch.setProjectionMatrix(projection);
		//	resize(Engine.width, Engine.height);
			
			if (Engine.scene!=null)Engine.scene.OnLoad();
		
		running = true;		
	}
	
	public Engine(Scene newScene)
	{
		Engine.setScene(newScene);
	}

	@Override
	public void dispose()
	{
		if (scene != null)
			scene.dispose();
	}

	@Override
	public void pause()
	{
		running = false;
	}

	@Override
	public void render()
	{
		elapsed += Gdx.graphics.getDeltaTime();
		while(elapsed > frameRate)
		{
			
			if (newScene != null)
			{
				scene.dispose();
				scene = newScene;
				newScene = null;
			}
			
			scene.update();
		//	Input.update();
			elapsed -= frameRate;
		}
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 45.f, 1.0f);
		
		 scene.update();
		 scene.updateLists();
	     spriteBatch.begin();
		 scene.render(spriteBatch);
		 matrix.setToTranslation(0,0, 0);
		 spriteBatch.setTransformMatrix(matrix);
		 scene.OnRender(spriteBatch);
	     spriteBatch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		
		float aspectRatio = (float) width / (float) height;
		PerspectiveCamera camera = new PerspectiveCamera(64, Engine.width * aspectRatio, Engine.height);
		Matrix4 viewMatrix = new Matrix4();
	    viewMatrix.setToOrtho2D(0, 0,width, height);
		 
	   // projection.setToOrtho(0, Engine.width, Engine.height, 0, -1, 1);
	    
	 //spriteBatch.setProjectionMatrix(viewMatrix);
	}

	@Override
	public void resume()
	{
		running = true;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public static void setScene(Scene newScene)
	{
		if (Engine.scene == null)
			Engine.scene = newScene;
		else
			Engine.newScene = newScene;
	}
	 public static double distance(int x1, int y1) {
	        return distance(x1,y1,0,0);
	    }
	
    public static double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
	
	 public static double distanceRects(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
	        if (x1 < x2 + w2 && x2 < x1 + w1)
	        {
	            if (y1 < y2 + h2 && y2 < y1 + h1) 
	                return 0;
	            if (y1 > y2) 
	                return y1 - (y2 + h2);
	            return y2 - (y1 + h1);
	        }
	        if (y1 < y2 + h2 && y2 < y1 + h1)
	        {
	            if (x1 > x2) 
	                return x1 - (x2 + w2);
	            return x2 - (x1 + w1);
	        }
	        if (x1 > x2)
	        {
	            if (y1 > y2) 
	                return distance(x1, y1, (x2 + w2), (y2 + h2));
	            return distance(x1, y1 + h1, x2 + w2, y2);
	        }
	        if (y1 > y2) 
	            return distance(x1 + w1, y1, x2, y2 + h2);
	        return distance(x1 + w1, y1 + h1, x2, y2);
	    }

	/** @private Calculates the squared distance between two rectangles. */
	private static double squareRects(int x1, int y1, int w1, int h1, int x2,
			int y2, int w2, int h2) {
		if (x1 < x2 + w2 && x2 < x1 + w1) {
			if (y1 < y2 + h2 && y2 < y1 + h1)
				return 0;
			if (y1 > y2)
				return (y1 - (y2 + h2)) * (y1 - (y2 + h2));
			return (y2 - (y1 + h1)) * (y2 - (y1 + h1));
		}
		if (y1 < y2 + h2 && y2 < y1 + h1) {
			if (x1 > x2)
				return (x1 - (x2 + w2)) * (x1 - (x2 + w2));
			return (x2 - (x1 + w1)) * (x2 - (x1 + w1));
		}
		if (x1 > x2) {
			if (y1 > y2)
				return squarePoints(x1, y1, (x2 + w2), (y2 + h2));
			return squarePoints(x1, y1 + h1, x2 + w2, y2);
		}
		if (y1 > y2)
			return squarePoints(x1 + w1, y1, x2, y2 + h2);
		return squarePoints(x1 + w1, y1 + h1, x2, y2);
	}

	/** @private Calculates the squared distance between two points. */
	private static double squarePoints(int x1, int y1, int x2, int y2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}

	/** @private Calculates the squared distance between a rectangle and a point. */
	private static double squarePointRect(int px, int py, int rx, int ry,
			int rw, int rh) {
		if (px >= rx && px <= rx + rw) {
			if (py >= ry && py <= ry + rh)
				return 0;
			if (py > ry)
				return (py - (ry + rh)) * (py - (ry + rh));
			return (ry - py) * (ry - py);
		}
		if (py >= ry && py <= ry + rh) {
			if (px > rx)
				return (px - (rx + rw)) * (px - (rx + rw));
			return (rx - px) * (rx - px);
		}
		if (px > rx) {
			if (py > ry)
				return squarePoints(px, py, rx + rw, ry + rh);
			return squarePoints(px, py, rx + rw, ry);
		}
		if (py > ry)
			return squarePoints(px, py, rx, ry + rh);
		return squarePoints(px, py, rx, ry);
	}
	
    public static int sign(float value)
    {
        return value < 0 ? -1 : (value > 0 ? 1 : 0);
    }
    
    public static int sign(int value)
    {
        return value < 0 ? -1 : (value > 0 ? 1 : 0);
    }
    public static float approach(float value, float target, float amount) {
        return value < target ? (target < value + amount ? target : value + amount) : (target > value - amount ? target : value - amount);
    }
    public static float  lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
    public static void stepTowards(Entity object, int x, int y, float distance) 
    {
        point.x = x - object.x;
        point.y = y - object.y;
        double len = point.length();
        if (len <= distance)
        {
            object.x = x;
            object.y = y;
            return;
        }
        float deltax = (float)(point.x / len) * distance;
        float deltay = (float)(point.y / len) * distance;
        object.x = object.x + sign(deltax) * Math.round(Math.abs(deltax));
        object.y = object.y + sign(deltay) * Math.round(Math.abs(deltay));
    }
    public static void anchorTo(Entity object, Entity anchor, float distance)
    {
        point.x = object.x - anchor.x;
        point.y = object.y - anchor.y;
        double len = point.length();
        if (len > distance) {
            point.x /= len;
            point.y /= len;
        }
        object.x = anchor.x + (int)point.x;
        object.y = anchor.y + (int)point.y;
    }
    public static double angle(int x1, int y1, int x2, int y2) {
        return angle((float)x1,(float)y1,(float)x2,(float)y2);
    }
    
    public static double angle(float x1, float y1, float x2, float y2) {
        double a = Math.atan2(y2 - y1, x2 - x1) * DEG;
        return a < 0 ? a + 360 : a;
    }
    public static void angleXY(Entity object, double angle) {
        angleXY(object, angle,1,0,0);
    }
    public static void angleXY(Entity object, double angle, double length) {
        angleXY(object, angle, length, 0, 0);
    }
    public static void angleXY(Entity object, double angle, double length, int x) {
        angleXY(object, angle, length, x, 0);
    }
    public static void angleXY(Entity object, double angle, double length, int x, int y) {
        angle *= RAD;
        object.x = (int)(Math.cos(angle) * length + x);
        object.y = (int)(Math.sin(angle) * length + y);
    }
    
    public static void rotateAround(Entity object, Entity anchor, double angle) {
        rotateAround(object, anchor, angle, true);
    }
    
    public static void rotateAround(Entity object, Entity anchor, double angle, boolean relative) {
        if (relative) 
            angle += Engine.angle(anchor.x, anchor.y, object.x, object.y);
        Engine.angleXY(object, angle, Engine.distance(anchor.x, anchor.y, object.x, object.y), anchor.x, anchor.y);
    }
    
 
    public static double angleDiff(double a, double b) {
        double diff = b- a;

        while (diff > 180) { diff -= 360; }
        while (diff <= -180) { diff += 360; }

        return diff;
    }
    
    
    public static final float DEG = (float)(-180.0 / Math.PI);
    public static final float RAD = (float)(Math.PI / -180.0);

	private Matrix4 matrix = new Matrix4();
	private float frameRate = 1.0f / 60.0f;
	private boolean running;
	private static Scene scene;
	private static Scene newScene;
	public static final Vector2 point = new Vector2();
    public static final Vector2 point2 = new Vector2();
    public static final Vector2 zero = new Vector2();



	@Override
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		x = x * Engine.width / Gdx.graphics.getWidth();
		y = y * Engine.height / Gdx.graphics.getHeight();
		MouseX=x;
		MouseY=y;
		ArrMouseX[pointer]=x;
		ArrMouseX[pointer]=y;
		
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		x = x * Engine.width / Gdx.graphics.getWidth();
		y = y * Engine.height / Gdx.graphics.getHeight();
		MouseX=x;
		MouseY=y;
		ArrMouseX[pointer]=x;
		ArrMouseX[pointer]=y;
		
		
		return true;
	}

	@Override
	public boolean touchMoved(int x, int y)
	{
		x = x * Engine.width / Gdx.graphics.getWidth();
		y = y * Engine.height / Gdx.graphics.getHeight();
		MouseX=x;
		MouseY=y;

		
	
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		
		return true;
	}

}
