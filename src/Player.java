import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.linux.XVisualInfo;

public class Player extends CollidableObject
{
	public boolean isOnGround, isOnCelieng, isOnWallLeft, isOnWallRight, walkRight, walkLeft;
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
	@Override
	public void doRender(Main m)
	{
		m.renderRectangleAsTriangles(vertices, getColor(), getVAO(), getVBO(), getVBOC(), getVBOI());
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
	}
	@Override
	public void act(Main m)
	{
		collidedObjects = new ArrayList<CollidableObject>();
		if(walkLeft)
			setXVelocity(-0.00027);
		else if(walkRight)
			setXVelocity(0.00027);
		else
			if(getXVelocity() > 0)
				setXVelocity(getXVelocity() - 0.000027);
			else if(getXVelocity() < 0)
				setXVelocity(getXVelocity() + 0.000027);
		double deltaX = velocityX*getDeltaT();
		double deltaY = velocityY*getDeltaT();
		float[] oldVertices = vertices;
		vertices[0] += (float)deltaX;
		vertices[1] += (float)deltaY;
		reAlignVertices();
		CollidableObject collidedObject = null;
		for(CollidableObject c : m.getObjList())
			if(checkForCollision(c))
			{
				if(c instanceof LevelExit)
					System.exit(0);
				collidedObjects.add(c);
				double tempDeltaX = 0, tempDeltaY = 0;
				collidedObject = c;
				float[] tempVerticies = collidedObject.getVertices();
				// TODO: Create method given velocity, distance to collision,
				// possibly both objects and sets new velocity and position
				if(getXVelocity() > 0)
					if(Math.max(vertices[0], vertices[4])>Math.min(tempVerticies[0], tempVerticies[4]))
						tempDeltaX = (Math.min(tempVerticies[0], tempVerticies[4])-Math.max(vertices[0], vertices[4]));
				if(getXVelocity() < 0)
					if(Math.min(vertices[0], vertices[4]) < Math.max(tempVerticies[0], tempVerticies[4]))
						tempDeltaX = (Math.max(tempVerticies[0], tempVerticies[4])-Math.min(vertices[0], vertices[4]));
				if(getYVelocity() > 0)
					if(Math.max(vertices[1], vertices[5])>Math.min(tempVerticies[1], tempVerticies[5]))
						tempDeltaY = (Math.min(tempVerticies[1], tempVerticies[5])-Math.max(vertices[1], vertices[5]));
				if(getYVelocity() < 0)
					if(Math.min(vertices[1], vertices[5])<Math.max(tempVerticies[1], tempVerticies[5]))
						tempDeltaY = (Math.max(tempVerticies[1], tempVerticies[5])-Math.min(vertices[1], vertices[5]));
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
				c.setColor(1f, 0f, 0f, 1f);
				vertices = oldVertices;
				vertices[0] += (float)deltaX;
				vertices[1] += (float)deltaY;
				reAlignVertices();
			}
		isOnGround = false;
		isOnCelieng = false;
		isOnWallRight = false;
		isOnWallLeft = false;
		for(CollidableObject c : m.getObjList())
			if(checkForContact(c))
			{
				float[] tempVertices = c.getVertices();
				if(Math.abs(Math.min(vertices[1], vertices[5])
						-Math.max(tempVertices[1], tempVertices[5]))<0.001f)
					isOnGround = true;
				else if(Math.abs(Math.max(vertices[1], vertices[5])
						-Math.min(tempVertices[1], tempVertices[5]))<0.001f)
					isOnCelieng = true;
				// TODO: MAKE DIS SHIT WORK
				else if(Math.abs(Math.max(vertices[0], vertices[4])
						-Math.min(tempVertices[0], tempVertices[4]))<0.001f)
				{
					isOnWallRight = true;
				}
				else if(Math.abs(Math.min(vertices[0], vertices[4])
						-Math.max(tempVertices[0], tempVertices[4]))<0.001f)
					isOnWallLeft = true;
			}
		setYVelocity(getYVelocity()-(0.0000009f * getDeltaT()));
		setVertices(vertices);
		System.out.println(getYVelocity());
		if(Math.random() > 0.9)
		{
			setColor((float)Math.random() * 1f, (float)Math.random() * 1f, (float)Math.random() * 1f, 1f);
		}
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
		return Math.sqrt(velocityX*velocityX+velocityY*velocityY);
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