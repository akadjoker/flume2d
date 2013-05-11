package com.flume2d;


import java.util.*;



import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.graphics.Graphic;
import com.flume2d.masks.*;
import com.flume2d.math.*;

public class Entity implements Disposable
{
	
	public boolean active = true;
	 /**
	   * If the Entity should render.
	   */
	  public boolean visible = true;
	  
	  /**
	   * If the Entity should respond to collision checks.
	   */
	  public boolean collidable = true;
	//  public float x = 0;
	//  public float y = 0;

	  public int x = 0;
	  public int y = 0;
	  
	    public int width;
	    public int height;
	    public int originX;
	    public int originY;
	
	public Entity()
	{
		this(0, 0, null, null);
	}
	
	public Entity(int x, int y)
	{
		this(x, y, null, null);
	}
	
	public Entity(int x, int y, Graphic graphic)
	{
		this(x, y, graphic, null);
	}

	public Entity(int x, int y, Graphic graphic, Mask mask)
	{
		this.x = x;
		this.y = y;
		setGraphic(graphic);
		
		 if (mask != null) 
		 {
	            mMask = mask;
	        }
	        HITBOX.assignTo(this);
	        
		this.layer = 0;
		
	}
    public String toString() {
    	return String.format("%s: %s xy<%d, %d> origin<%d, %d> wh<%d, %d>", getClass().getName(), mType, x, y, originX, originY, width, height);
    }
    
	public int getLayer() { return mLayer; }
	public void setLayer(int value) {
		if (mLayer == value) 
			return;
		if (!mAdded) {
			mLayer = value;
			return;
		}
		mWorld.removeRender(this);
		mLayer = value;
		mWorld.addRender(this);
	}
	

	public Entity collide(String type, int x, int y) 
	{
		
		
		
		if (mWorld == null) 
			return null;

		Entity e = mWorld.mTypeFirst.get(type);
		if (!collidable || e == null) 
			return null;
		
		

		mX = this.x; mY = this.y;
		this.x = x; this.y = y;

		if (mMask == null) 
		{
			while (e != null) 
			{
				if (x - originX + width > e.x - e.originX
				&& y - originY + height > e.y - e.originY
				&& x - originX < e.x - e.originX + e.width
				&& y - originY < e.y - e.originY + e.height
				&& e.collidable && e != this)
				{
					if (e.mMask == null || e.mMask.overlaps(HITBOX))
					{
						this.x = (int)mX; 
						this.y = (int)mY;
						return e;
					}
				}
				e = e.mTypeNext;
			}
			this.x = (int)mX;
			this.y = (int)mY;
			return null;
		}

		while (e != null) 
		{
			if (x - originX + width > e.x - e.originX
			&& y - originY + height > e.y - e.originY
			&& x - originX < e.x - e.originX + e.width
			&& y - originY < e.y - e.originY + e.height
			&& e.collidable && e != this) {
				if (mMask.overlaps(e.mMask != null ? e.mMask : e.HITBOX))
				{
					this.x = (int)mX; this.y = (int)mY;
					return e;
				}
			}
			e = e.mTypeNext;
		}
		this.x = (int)mX; 
		this.y = (int)mY;
		return null;
	}
	public Entity collideTypes(Vector<String> types, int x, int y) {
		if (mWorld == null)
			return null;
		Entity e;
		for (String type : types) {
			if ((e = collide(type, x, y)) != null) 
				return e;
		}
		return null;
	}
	public Entity collideWith(Entity e, int x, int y) {
		mX = this.x; mY = this.y;
		this.x = x; this.y = y;

		if (x - originX + width > e.x - e.originX
		&& y - originY + height > e.y - e.originY
		&& x - originX < e.x - e.originX + e.width
		&& y - originY < e.y - e.originY + e.height
		&& collidable && e.collidable)
		{
			if (mMask == null) {
				if (e.mMask == null || e.mMask.overlaps(HITBOX)) {
					this.x = (int)mX; this.y = (int)mY;
					return e;
				}
				this.x = (int)mX; this.y = (int)mY;
				return null;
			}
			if (mMask.overlaps(e.mMask != null ? e.mMask : e.HITBOX))
			{
				this.x = (int)mX; this.y = (int)mY;
				return e;
			}
		}
		this.x = (int)mX; this.y = (int)mY;
		return null;
	}
	public void collideInto(String type, int x, int y, Vector<Entity> array) {
		if (mWorld == null) 
			return;

		Entity e = mWorld.mTypeFirst.get(type);
		if (!collidable || e == null) 
			return;

		mX = this.x; mY = this.y;
		this.x = x; this.y = y;

		if (mMask == null){
			while (e != null){
				if (x - originX + width > e.x - e.originX
				&& y - originY + height > e.y - e.originY
				&& x - originX < e.x - e.originX + e.width
				&& y - originY < e.y - e.originY + e.height
				&& e.collidable && e != this)
				{
					if (e.mMask == null || e.mMask.overlaps(HITBOX))
						array.add(e);
				}
				e = e.mTypeNext;
			}
			this.x = (int)mX; this.y = (int)mY;
			return;
		}

		while (e != null) {
			if (x - originX + width > e.x - e.originX
			&& y - originY + height > e.y - e.originY
			&& x - originX < e.x - e.originX + e.width
			&& y - originY < e.y - e.originY + e.height
			&& e.collidable && e != this)
			{
				if (mMask.overlaps(e.mMask != null ? e.mMask : e.HITBOX)) 
					array.add(e);
			}
			e = e.mTypeNext;
		}
		this.x = (int)mX; this.y = (int)mY;
		return;
	}
	public void collideTypesInto(Vector<String> types, int x, int y, Vector<Entity> array) {
		if (mWorld == null) 
			return;
		for (String type : types) {
			collideInto(type, x, y, array);
		}
	}
	
	public void moveBy(int x, int y) {
		moveBy(x,y, null, false);
	}
	/**
	 * Moves the Entity by the amount, retaining integer values for its x and y.
	 * @param	x			Horizontal offset.
	 * @param	y			Vertical offset.
	 * @param	solidType	An optional collision type to stop flush against upon collision.
	 * @param	sweep		If sweeping should be used (prevents fast-moving objects from going through solidType).
	 */
	public void moveBy(int x, int y, String solidType, boolean sweep) {
		mMoveX += x;
		mMoveY += y;
		x = Math.round(mMoveX);
		y = Math.round(mMoveY);
		mMoveX -= x;
		mMoveY -= y;
		if (solidType != null) {
			int sign;
			Entity e;
			if (x != 0) {
				if (collidable && (sweep || collide(solidType, this.x + x, this.y)  != null)) {
					sign = x > 0 ? 1 : -1;
					while (x != 0) {
						if ((e = collide(solidType, this.x + sign, this.y)) != null) {
							moveCollideX(e);
							break;
						} else {
							this.x += sign;
							x -= sign;
						}
					}
				}
				else 
					this.x += x;
			}
			if (y != 0) {
				if (collidable && (sweep || collide(solidType, this.x, this.y + y) != null)) {
					sign = y > 0 ? 1 : -1;
					while (y != 0) {
						if ((e = collide(solidType, this.x, this.y + sign)) != null) 
						{
							moveCollideY(e);
							break;
						} else {
							this.y += sign;
							y -= sign;
						}
					}
				}
				else 
					this.y += y;
			}
		} else {
			this.x += x;
			this.y += y;
		}
	}
	
	public void moveTo(int x, int y) {
		moveTo(x,y,null,false);
	}

	public void moveTo(int x, int y, String solidType, boolean sweep) {
		moveBy(x - this.x, y - this.y, solidType, sweep);
	}
	
	public void moveTowards(int x, int y, int amount) {
		moveTowards(x, y, amount, null, false);
	}

	public void moveTowards(int x, int y, int amount, String solidType, boolean sweep) 
	{
		mPoint.x = x - this.x;
		mPoint.y = y - this.y;
		double len = mPoint.length();
		mPoint.x /= len;
		mPoint.y /= len;
		moveBy((int)mPoint.x, (int)mPoint.y, solidType, sweep);
	}

	public void moveCollideX(Entity e) {

	}
	public void moveCollideY(Entity e) {

	}
    public void setHitbox(int width, int height)
    {
    	setHitbox(width, height, 0, 0);
    }
    
	/**
	 * Sets the Entity's hitbox properties.
	 * @param	width		Width of the hitbox.
	 * @param	height		Height of the hitbox.
	 * @param	originX		X origin of the hitbox.
	 * @param	originY		Y origin of the hitbox.
	 */
	public void setHitbox(int width, int height, int originX, int originY) {
		this.width = width;
		this.height = height;
		this.originX = originX;
		this.originY = originY;
	
		
	}

	/**
	 * Sets the origin of the Entity.
	 * @param	x		X origin.
	 * @param	y		Y origin.
	 */
	public void setOrigin(int x , int y) {
		originX = x;
		originY = y;
	}
	
	/**
	 * Center's the Entity's origin (half width & height).
	 */
	public void centerOrigin() {
		originX = width / 2;
		originY = height / 2;
	}
	
	public double distanceFrom(Entity e) {
		return distanceFrom(e, false);
	}
	public double distanceFrom(Entity e, boolean useHitboxes) {
		if (!useHitboxes) 
			return Math.sqrt((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y));
		return Engine.distanceRects(x - originX, y - originY, width, height, e.x - e.originX, e.y - e.originY, e.width, e.height);
	}


	
	public void update()
	{
	//	System.out.printf("\n update sprite");
	//	if (graphic != null && graphic.isActive()) graphic.update();
	}
	
	/**
	 * Draws the current graphic
	 * @param spriteBatch batching object to draw sprites
	 */
	public void render(SpriteBatch spriteBatch)
	{

		if (graphic == null) return;
		
		graphic.render(spriteBatch);
	
	}
	

	
	public void setMask(Hitbox mask)
	{
		if (mask == null) return;
		mask.parent = this;
		this.mMask = mask;
		mask.assignTo(this);
	}
	
	public void setGraphic(Graphic graphic)
	{
		if (graphic == null) return;
		this.graphic = graphic;
	}
	public Graphic getGraphic() { return graphic; }
	
	public void dispose()
	{
		if (graphic != null) graphic.dispose();
	}

	public void setWorld(Scene w) {
		mWorld = w;
	}
	public Scene getWorld() {
		return mWorld;
	}
	   protected void release() {
	    	
	    }
	public void setScene(Scene scene) { this.mWorld = scene; }
	public boolean hasScene() { return (mWorld != null); }
	
	public String getType() { return mType; }
	public void setType(String value) {
		if (mType.equals(value))
			return;
		if (!mAdded)
		{
			mType = value;
			return;
		}
		if (!"".equals(mType)) 
			mWorld.removeType(this);
		mType = value;
		if (!"".equals(value)) 
			mWorld.addType(this);
	}

	public void added() { }
	public void removed() { }
	
	public int layer;

	public Graphic graphic;

	
	private Scene mWorld;
    protected boolean mAdded;
    public String mType = "";
    public String mName;
    private int mLayer = 0;
    protected Entity mUpdatePrev;
    protected Entity mUpdateNext;
    protected Entity mRenderPrev;
    protected Entity mRenderNext;
    protected Entity mTypePrev;
    protected Entity mTypeNext;
    
	

	  
	  // Collision information.

        private final Hitbox HITBOX = new Hitbox();
	    private Mask mMask;
	    private float mX;
	    private float mY;
	    private float mMoveX;
	    private float mMoveY;
	  

	    private Vector2 mPoint = new Vector2();

	
}
