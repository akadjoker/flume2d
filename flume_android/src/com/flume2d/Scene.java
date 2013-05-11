package com.flume2d;

import java.util.*;



import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.graphics.Graphic;
import com.flume2d.math.Vector2;


public abstract class Scene implements Disposable
{
	
	public Vector2 camera = new Vector2();
	
	public Scene()
	{
		
		
	}
	
	abstract public void OnLoad();
	abstract public void OnRender(SpriteBatch spriteBatch);
	
	
	
	@Override
	public void dispose()
	{
		removeAll();

		
	}
	
	public Entity add(Entity e)
	{
		if (e.getWorld() != null)
			return e;
		mAdd.add(e);
		e.setScene(this);
		return e;
		
		
	}
	

	public Entity remove(Entity e) {
		if (e.getWorld() != this)
			return e;
		mRemove.add(e);
		e.setWorld(null);
		return e;
	}
	public void removeAll() {
		Entity e = mUpdateFirst;
		while (e != null) {
			mRemove.add(e);
			Graphic g = e.getGraphic();
			if (g != null) 
			{
				g.dispose();
			}
			e.setWorld(null);
			e = e.mUpdateNext;
		}
	}


	public void addList(List<Entity> entities) {
		for (Entity e : entities) {
			add(e);
		}
	}


	public void addList(Entity... entities) {
		for (Entity e : entities) {
			add(e);
		}
	}

	public void removeList(List<Entity> entities) {
		for (Entity e : entities) {
			remove(e);
		}
	}

	public void removeList(Entity... entities) {
		for (Entity e : entities) {
			remove(e);
		}
	}
	public Entity addGraphic(Graphic graphic)
	{
		return addGraphic(graphic, 0, 0);
	}
	
	public Entity addGraphic(Graphic graphic, int x, int y)
	{
		Entity e = new Entity(x, y, graphic);
		add(e);
		return e;
	}
	
	
	

	public void update()
	{
				
		// update the entities
				Entity e = mUpdateFirst;
				while (e != null) {
					if (e.active) 
					{
						e.update();
					}
					Graphic g = e.getGraphic();
					if (g != null)
						g.update();
					e = e.mUpdateNext;
				}
	}
	
	public void render(SpriteBatch spriteBatch)
	{
		synchronized (mLayerList) {
			Entity e;
			int i = mLayerList.size();
			while (i-- > 0) {
				e = mRenderLast.get(mLayerList.get(i));
				while (e != null) {
					if (e.visible)
					{
						matrix.setToTranslation(e.x - camera.x, e.y - camera.y, 0);
						spriteBatch.setTransformMatrix(matrix);
						e.render(spriteBatch);
					}
					e = e.mRenderPrev;
				}
			}
		}

	}

//***
	public int getCount() {
		return mCount;
	}
	public boolean bringForward(Entity e) {
		if (e.getWorld() != this || e.mRenderPrev == null)
			return false;
		// pull from list
		e.mRenderPrev.mRenderNext = e.mRenderNext;
		if (e.mRenderNext != null)
			e.mRenderNext.mRenderPrev = e.mRenderPrev;
		else
			mRenderLast.put(e.getLayer(), e.mRenderPrev);
		// shift towards the front
		e.mRenderNext = e.mRenderPrev;
		e.mRenderPrev = e.mRenderPrev.mRenderPrev;
		e.mRenderNext.mRenderPrev = e;
		if (e.mRenderPrev != null)
			e.mRenderPrev.mRenderNext = e;
		else
			mRenderFirst.put(e.getLayer(), e);
		return true;
	}
	public boolean sendBackward(Entity e) {
		if (e.getWorld() != this || e.mRenderNext == null)
			return false;
		// pull from list
		e.mRenderNext.mRenderPrev = e.mRenderPrev;
		if (e.mRenderPrev != null)
			e.mRenderPrev.mRenderNext = e.mRenderNext;
		else
			mRenderFirst.put(e.getLayer(), e.mRenderNext);
		// shift towards the back
		e.mRenderPrev = e.mRenderNext;
		e.mRenderNext = e.mRenderNext.mRenderNext;
		e.mRenderPrev.mRenderNext = e;
		if (e.mRenderNext != null)
			e.mRenderNext.mRenderPrev = e;
		else
			mRenderLast.put(e.getLayer(), e);
		return true;
	}

	public int typeCount(String type) {
		Integer i = mTypeCount.get(type);
		if (i == null)
			return 0;
		else
			return i;
	}


	public int layerCount(int layer) {
		if (layer >= mLayerCount.size())
			return 0;
		Integer i = mLayerCount.get(layer);
		if (i == null)
			return 0;
		else
			return i;
	}


	public Entity getFirst() {
		return mUpdateFirst;
	}


	public int getLayers() {
		return mLayerList.size();
	}

	public Entity typeFirst(String type) {
		if (mUpdateFirst == null)
			return null;
		return mTypeFirst.get(type);
	}

	public Entity layerFirst(int layer) {
		if (mUpdateFirst == null)
			return null;
		return mRenderFirst.get(layer);
	}

	public boolean isAtFront(Entity e) {
		return e.mRenderPrev == null;
	}

	public boolean isAtBack(Entity e) {
		return e.mRenderNext == null;
	}
	
	public Entity layerLast(int layer) {

		if (mUpdateFirst == null || layer >= mRenderLast.size())
			return null;
		return mRenderLast.get(layer);
	}


	public Entity getFarthest() {
		if (mUpdateFirst == null || mLayerList.size() == 0)
			return null;
		return mRenderLast.get(mLayerList.lastElement());
	}


	public Entity getNearest() {
		if (mUpdateFirst == null || mLayerList.size() == 0)
			return null;
		return mRenderFirst.get(mLayerList.firstElement());
	}


	public int getLayerFarthest() {
		if (mUpdateFirst == null || mLayerList.size() == 0)
			return 0;
		return mLayerList.get(mLayerList.lastElement());
	}


	public int getLayerNearest() {
		if (mUpdateFirst == null || mLayerList.size() == 0)
			return 0;
		return mLayerList.get(0);
	}

	public int getUniqueTypes() {
		return mTypeCount.keySet().size();
	}

	public String[] getTypes() {
		Set<String> types = mTypeCount.keySet();
		String[] typesArray = new String[types.size()];
		Iterator<String> it = types.iterator();
		for (int i = 0; it.hasNext(); i++) {
			String type = it.next();
			typesArray[i] = type;
		}
		return typesArray;
	}
	
	public void getType(String type, Vector<Entity> into) {
		Entity e = mTypeFirst.get(type);
		while (e != null) {
			into.add(e);
			e = e.mTypeNext;
		}
	}
	
	public void getLayer(int layer, Vector<Entity> into) {
		if (layer >= mRenderLast.size())
			return;
		Entity e = mRenderLast.get(layer);
		while (e != null) {
			into.add(e);
			e = e.mUpdatePrev;
		}
	}
	public void getAll(Vector<Entity> into) {
		Entity e = mUpdateFirst;
		while (e != null) {
			into.add(e);
			e = e.mUpdateNext;
		}
	}
	public void updateLists() {
		synchronized (mLayerList) {
			// remove entities
			if (mRemove.size() > 0) {
				for (Entity e : mRemove) {

					if (e.mAdded != true && mAdd.indexOf(e) >= 0) {
						mAdd.remove(e);
						continue;
					}
					e.mAdded = false;

					e.removed();
					removeUpdate(e);
					removeRender(e);
					
				}
				mRemove.clear();
			}

			// add entities
			if (mAdd.size() > 0) {
				for (Entity e : mAdd) {
					e.mAdded = true;
					addUpdate(e);
					addRender(e);
					if (!"".equals(e.mType))
						addType(e);
					e.added();
				}
				mAdd.clear();
			}

			// sort the depth list
			if (mLayerSort) {
				if (mLayerList.size() > 1) {
					Collections.sort(mLayerList);
				}

				mLayerSort = false;
			}
		}
	}

	protected void addUpdate(Entity e) {
		// add to update list
		if (mUpdateFirst != null) {
			mUpdateFirst.mUpdatePrev = e;
			e.mUpdateNext = mUpdateFirst;
		} else
			e.mUpdateNext = null;
		e.mUpdatePrev = null;
		mUpdateFirst = e;
		mCount++;
	}

	/** @private Removes Entity from the update list. */
	protected void removeUpdate(Entity e) {
		// remove from the update list
		if (mUpdateFirst == e)
			mUpdateFirst = e.mUpdateNext;
		if (e.mUpdateNext != null)
			e.mUpdateNext.mUpdatePrev = e.mUpdatePrev;
		if (e.mUpdatePrev != null)
			e.mUpdatePrev.mUpdateNext = e.mUpdateNext;
		e.mUpdateNext = e.mUpdatePrev = null;

		mCount--;
	}

	protected void addRender(Entity e) {
		Entity f = mRenderFirst.get(e.getLayer());

		if (f != null) {
			// Append entity to existing layer.
			e.mRenderNext = f;
			f.mRenderPrev = e;
			mLayerCount.put(e.getLayer(), mLayerCount.get(e.getLayer()) + 1);
		} else {
			// Create new layer with entity.
			mRenderLast.put(e.getLayer(), e);
			mRenderLast.put(e.getLayer(), e);
			mLayerList.add(e.getLayer());
			mLayerSort = true;
			e.mRenderNext = null;
			mLayerCount.put(e.getLayer(), 1);
		}
		mRenderFirst.put(e.getLayer(), e);
		e.mRenderPrev = null;
	}

	/** @private Removes Entity from the render list. */
	protected void removeRender(Entity e) {
		if (e.mRenderNext != null)
			e.mRenderNext.mRenderPrev = e.mRenderPrev;
		else
			mRenderLast.put(e.getLayer(), e.mRenderPrev);
		if (e.mRenderPrev != null)
			e.mRenderPrev.mRenderNext = e.mRenderNext;
		else {
			// Remove this entity from the layer.
			mRenderFirst.put(e.getLayer(), e.mRenderNext);
			if (e.mRenderNext == null) {
				// Remove the layer from the layer list if this was the last
				// entity.
				mLayerList.remove((Integer) e.getLayer());
			}
		}
		mLayerCount.put(e.getLayer(), mLayerCount.get(e.getLayer()) - 1);
		e.mRenderNext = e.mRenderPrev = null;
	}

	public boolean bringToFront(Entity e) {
		if (e.getWorld() != this || e.mRenderPrev == null)
			return false;
		// pull from list
		e.mRenderPrev.mRenderNext = e.mRenderNext;
		if (e.mRenderNext != null)
			e.mRenderNext.mRenderPrev = e.mRenderPrev;
		else
			mRenderFirst.put(e.getLayer(), e.mRenderPrev);
		// place at the start
		e.mRenderNext = mRenderFirst.get(e.getLayer());
		e.mRenderNext.mRenderPrev = e;
		mRenderFirst.put(e.getLayer(), e);
		e.mRenderPrev = null;
		return true;
	}

	protected void addType(Entity e) {
		// add to type list
		if (mTypeFirst.get(e.mType) != null) {
			mTypeFirst.get(e.mType).mTypePrev = e;
			e.mTypeNext = mTypeFirst.get(e.mType);
			mTypeCount.put(e.mType, (Integer) (mTypeCount.get(e.mType) + 1));
		} else {
			e.mTypeNext = null;
			mTypeCount.put(e.mType, 1);
		}
		e.mTypePrev = null;

		mTypeFirst.put(e.mType, e);
	}

	/** @private Removes Entity from the type list. */
	protected void removeType(Entity e) {
		// remove from the type list
		if (mTypeFirst.get(e.mType) == e)
			mTypeFirst.put(e.mType, e.mTypeNext);
		if (e.mTypeNext != null)
			e.mTypeNext.mTypePrev = e.mTypePrev;
		if (e.mTypePrev != null)
			e.mTypePrev.mTypeNext = e.mTypeNext;
		e.mTypeNext = e.mTypePrev = null;
		mTypeCount.put(e.mType, mTypeCount.get(e.mType) - 1);
	}

	private Matrix4 matrix = new Matrix4();
	//private SpriteBatch spriteBatch;
	
	// Adding and removal.
		private Vector<Entity> mAdd = new Vector<Entity>();
		private Vector<Entity> mRemove = new Vector<Entity>();

		// Update information.
		private Entity mUpdateFirst;
		private int mCount;

		// Render information.
		private boolean mLayerSort;
		Vector<Integer> mLayerList = new Vector<Integer>();
		Map<Integer, Integer> mLayerCount = new HashMap<Integer, Integer>();
		Map<Integer, Entity> mRenderFirst = new HashMap<Integer, Entity>();
		Map<Integer, Entity> mRenderLast = new HashMap<Integer, Entity>();
		/*
		 * private var mRenderFirst:Array = []; private var mRenderLast:Array = [];
		 * private var mLayerList:Array = []; private var mLayerCount:Array = [];
		 * private var mLayerSort:Boolean; private var mTempArray:Array = [];
		 */
		protected Map<String, Entity> mTypeFirst = new HashMap<String, Entity>();
		protected Map<String, Integer> mTypeCount = new HashMap<String, Integer>();

}
