import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.flume2d.Engine;



public class testegame {
	
	
	public static void main (String[] argv) 
	{
	
		new JoglApplication(new Engine(new world()), "My First Triangle", 640, 600, false);

		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}
}
