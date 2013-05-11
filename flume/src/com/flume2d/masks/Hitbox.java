package com.flume2d.masks;


import com.flume2d.math.*;

public class Hitbox extends Mask {

    protected int mWidth, mHeight;
    protected int mX, mY;
    
    public Hitbox() {
        this(1, 1, 0, 0);
    }
    
    public Hitbox(int width) {
        this(width, 1, 0, 0);
    }
    
    public Hitbox(int width, int height) {
        this(width, height, 0, 0);
    }
    
    public Hitbox(int width, int height, int x) {
        this(width, height, x, 0);
    }
    
    public void assignTo(com.flume2d.Entity parent) 
    {
        this.parent = parent;
        update();
    }
    
    /**
     * Constructor.
     * @param   width       Width of the hitbox.
     * @param   height      Height of the hitbox.
     * @param   x           X offset of the hitbox.
     * @param   y           Y offset of the hitbox.
     */
    public Hitbox(int width, int height, int x, int y) {
        super();
        mWidth = width;
        mHeight = height;
        mX = x;
        mY = y;
        
      
    }
    
    public void setHitbox(int width, int height, int originX, int originY)
    {
    	
        mWidth = width;
        mHeight = height;
        mX = originX;
        mY = originY;
    }

    /** @private Collides against an Entity. */
    private boolean collideMask(Mask other) {
        return parent.x + mX + mWidth > other.parent.x - other.parent.originX
            && parent.y + mY + mHeight > other.parent.y - other.parent.originY
            && parent.x + mX < other.parent.x - other.parent.originX + other.parent.width
            && parent.y + mY < other.parent.y - other.parent.originY + other.parent.height;
    }

    /** @private Collides against a Hitbox. */
    private boolean collideHitbox(Hitbox other)
    {
        return parent.x + mX + mWidth > other.parent.x + other.mX
            && parent.y + mY + mHeight > other.parent.y + other.mY
            && parent.x + mX < other.parent.x + other.mX + other.mWidth
            && parent.y + mY < other.parent.y + other.mY + other.mHeight;
    }

    private void checkUpdate() {

            update();
    }
    
    public int getX() {
        return mX;
    }
    
    public void setX(int x) {
        if (x == mX) 
            return;
        mX = x;
        checkUpdate();
    }
    
    public int getY() {
        return mY;
    }
    
    public void setY(int y) {
        if (y == mY) 
            return;
        mY = y;
        checkUpdate();
    }
    
    public int getWidth() {
        return mWidth;
    }
    
    public void setWidth(int width) {
        if (width == mWidth) 
            return;
        mWidth = width;
        checkUpdate();
    }
    public int getHeight() {
        return mHeight;
    }
    
    public void setHeight(int height) {
        if (height == mHeight) 
            return;
        mHeight = height;
        checkUpdate();
    }
    

    protected void update() {
     if (parent != null)
     {
            parent.originX = -mX;
            parent.originY = -mY;
            parent.width = mWidth;
            parent.height = mHeight;
        }
    }

	@Override
	public boolean overlaps(Mask mask) {
	
		return collideMask(mask);
	}

	@Override
	public Vector2 collide(Mask mask) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean collideAt(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}
