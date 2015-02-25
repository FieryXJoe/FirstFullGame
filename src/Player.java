import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.system.linux.XVisualInfo;


public class Player extends CollidableObject
{
	public boolean isOnGround, isOnCelieng, isOnWallLeft, isOnWallRight, walkRight, walkLeft, isDead = false;
	public ArrayList<CollidableObject> collidedObjects;
	float[] vertices = {
			getCoords()[0], getCoords()[1],
			getCoords()[0], getCoords()[1]+0.1f,
			getCoords()[0]+0.05f, getCoords()[1]+0.1f,
			getCoords()[0]+0.05f, getCoords()[1]};
	float[] colors = {
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f
	};
	
	private double velocityX, velocityY;
	// private double direction = 0;
	public Player(float x, float y, int vaoID, int vboID, int vbocID, int vboiID)
	{
		super(x, y, vaoID, vboID, vbocID, vboiID);
		setVertices(vertices);
		setColor(colors);
	}
	public Player(float x, float y, int vaoID, int vboID, int vbocID, int vboiID, float[] color, float[] vertices)
	{
		super(x, y, vaoID, vboID, vbocID, vboiID);
		setVertices(vertices);
		setColor(colors);
	}
	@Override
	public void doRender(Main m)
	{
		m.renderAsTriangles(vertices, getColor(), getVAO(), getVBO(), getVBOC(), getVBOI(), 4);
	}
	@Override
	public void reset()
	{
		super.reset();
		setColor(new float[]{
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f
	});
		setVertices(new float[]{
				getCoords()[0], getCoords()[1],
				getCoords()[0], getCoords()[1]+0.1f,
				getCoords()[0]+0.05f, getCoords()[1]+0.1f,
				getCoords()[0]+0.05f, getCoords()[1]
	});
		vertices = getVertices();
		isDead = false;
	}
	@Override
	public void act(Main m)
	{
		collidedObjects = new ArrayList<CollidableObject>();
		double deltaX = getXVelocity()*getDeltaT();
		double deltaY = getYVelocity()*getDeltaT();
		float[] oldVertices = new float[vertices.length];
		for(int i = 0; i < vertices.length; i++)
			oldVertices[i] = vertices[i];
		vertices[0] += (float)deltaX;
		vertices[1] += (float)deltaY;
		reAlignVertices();
		CollidableObject collidedObject = null;
		for(CollidableObject c : m.getObjList())
			if(checkForCollision(c))
			{
				double tempDeltaX = 0, tempDeltaY = 0;
				if(c instanceof LevelExit)
				{
					m.loadNextLevel();
					break;
				}
				if(c instanceof Spikes)
				{
					isDead = true;
					walkLeft = false;
					walkRight = false;
					break;
				}
				collidedObjects.add(c);
				collidedObject = c;
				float[] tempVertices = collidedObject.getVertices();
				if(getXVelocity() > 0 && !(c instanceof  MovingPlatform))
					if(vertices[4] > tempVertices[0])
						tempDeltaX = (tempVertices[0]-vertices[4]);
				if(getXVelocity() < 0 && !(c instanceof  MovingPlatform))
					if(vertices[0] < tempVertices[4])
						tempDeltaX = (tempVertices[4]-vertices[0]);
				if(getYVelocity() > 0)
					if(vertices[5] > tempVertices[1])
						tempDeltaY = (tempVertices[1]-vertices[5]);
				if(getYVelocity() < 0)
					if(vertices[1] < tempVertices[5])
					{
						if(c instanceof MovingPlatform)
						{
							setXVelocity(((MovingPlatform)c).getXVelocity());
							tempDeltaX = getXVelocity()*getDeltaT();
						}
						tempDeltaY = tempVertices[5] - vertices[1];
					}
				if(tempDeltaX != 0 && tempDeltaY != 0)
					if(Math.abs(tempDeltaX) < Math.abs(tempDeltaY))
					{
						setXVelocity(0);
						deltaX = tempDeltaX;
					}
					else 
					{
						setYVelocity(0);
						deltaY = tempDeltaY;
					}
				else if(tempDeltaX != 0)
				{
					setXVelocity(0);
					deltaX = tempDeltaX;
				}
				else if(tempDeltaY != 0)
				{
					setYVelocity(0); 
					deltaY = tempDeltaY;
				}	
				for(int i = 0; i < vertices.length; i++)
					vertices[i] = oldVertices[i];
				vertices[0] += (float)deltaX;
				vertices[1] += (float)deltaY;
				reAlignVertices();
				c.setColor(1f, 0f, 0f, 1f);
			}
		isOnGround = false;
		isOnCelieng = false;
		isOnWallRight = false;
		isOnWallLeft = false;
		setVertices(vertices);
		for(CollidableObject c : m.getObjList())
			if(checkForContact(c))
			{
				float[] tempVertices = c.getVertices();
				if(Math.abs(vertices[1] - tempVertices[5]) < 0.001)
					isOnGround = true;
				else if(Math.abs(vertices[5] - tempVertices[1]) < 0.001)
					isOnCelieng = true;
				else if(Math.abs(vertices[4] - tempVertices[0]) < 0.001)
					isOnWallRight = true;
				else if(Math.abs(vertices[0] - tempVertices[4]) < 0.001)
					isOnWallLeft = true;
			}
		setYVelocity(getYVelocity()-(0.9f * getDeltaT()));
		if(Math.random() > 0.9)
		{
			setColor((float)Math.random() * 1f, (float)Math.random() * 1f, (float)Math.random() * 1f, 1f);
		}
		if(walkLeft)
			setXVelocity(-0.27);
		else if(walkRight)
			setXVelocity(0.27);
		else if(getXVelocity() != 0)
			if(Math.abs(getXVelocity()) < 0.0001)
				setXVelocity(0);
			else if(getXVelocity() > 0)
				setXVelocity(getXVelocity() - 0.027);
			else if(getXVelocity() < 0)
				setXVelocity(getXVelocity() + 0.027);
	}
	/**
	 * Ensures character retains shape based off of bottom left coordinate
	 */
	private void reAlignVertices()
	{
		vertices[2] = vertices[0];
		vertices[3] = vertices[1]+.1f;
		vertices[4] = vertices[2]+.05f;
		vertices[5] = vertices[3];
		vertices[6] = vertices[4];
		vertices[7] = vertices[1];
	}
	public double getSpeed()
	{
		return Math.sqrt(getXVelocity()*getXVelocity()+getYVelocity()*getYVelocity());
	}
	public double getDeltaT()
	{
		return Main.timeOfLastFrame;
	}
	public double getXVelocity()
	{
		return velocityX;
	}
	public double getYVelocity()
	{
		return velocityY;
	}
	public void setXVelocity(double x)
	{
		velocityX = x;
	}
	public void setYVelocity(double y)
	{
		velocityY = y;
	}
	public void setWalkingRight(boolean b)
	{
		walkRight = b;
	}
	public void setWalkingLeft(boolean b)
	{
		walkLeft = b;
	}
}