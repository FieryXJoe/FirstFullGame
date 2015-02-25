import java.util.ArrayList;
import java.util.Arrays;

public class VerticalMovingPlatform extends Player
{
	public ArrayList<CollidableObject> collidedObjects;
	private float maxY, minY;
	float[] vertices = {
			getCoords()[0], getCoords()[1],
			getCoords()[0], getCoords()[1]+(0.0625f * 4)/3,
			getCoords()[0]+0.125f, getCoords()[1]+(0.0625f * 4)/3,
			getCoords()[0]+0.125f, getCoords()[1]};
	float[] colors = {
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f
	};
	public VerticalMovingPlatform(float x, float y, int vaoID, int vboID, int vbocID, int vboiID, float maxY, float minY)
	{
		super(x, y, vaoID, vboID, vbocID, vboiID,new float[] {
				1f,1f,1f,1f,
				1f,1f,1f,1f,
				1f,1f,1f,1f,
				1f,1f,1f,1f
		}, new float[] {
				x, y,
				x, y+(0.0625f * 4)/3,
				x+0.125f, y+(0.0625f * 4)/3,
				x+0.125f, y
		});
		this.maxY = maxY;
		this.minY = minY;
		setYVelocity(0.2);
	}
	@Override
	public void reset()
	{
		super.reset();
		setYVelocity(.2);
	}
	@Override
	public void doRender(Main m)
	{
		m.renderAsTriangles(getVertices(), getColor(), getVAO(), getVBO(), getVBOC(), getVBOI(), 4);
	}
	@Override
	public void act(Main m)	
	{
		collidedObjects = new ArrayList<CollidableObject>();
		double deltaY = getYVelocity()*getDeltaT();
		float[] oldVertices = new float[vertices.length];
		for(int i = 0; i < vertices.length; i++)
			oldVertices[i] = vertices[i];
		vertices[1] += (float)deltaY;
		reAlignVertices();
		CollidableObject collidedObject = null;
		for(CollidableObject c : m.getObjList())
			deltaY = objectCollisionCheck(c, collidedObject, deltaY, oldVertices);
		/**
		 * Two ghost objects which are not rendered and only this platform
		 * interacts with, they set the upper and lower limit for the platform's movement
		 */
		deltaY = objectCollisionCheck(new SmallBlock(vertices[0], maxY, getVAO(), getVBO(), getVBOC(), getVBOI()), collidedObject, deltaY, oldVertices);
		deltaY = objectCollisionCheck(new SmallBlock(vertices[0], minY - (0.0625f * 4)/3, getVAO(), getVBO(), getVBOC(), getVBOI()), collidedObject, deltaY, oldVertices);
		setVertices(vertices);
	}
	private double objectCollisionCheck(CollidableObject c, CollidableObject collidedObject, double deltaY, float[] oldVertices)
	{
		if(!c.equals(this) && checkForCollision(c))
		{
			collidedObjects.add(c);
			double tempDeltaY = 0;
			collidedObject = c;
			float[] tempVerticies = collidedObject.getVertices();
			if(getYVelocity() > 0)
			{
				if(Math.max(vertices[1], vertices[5])>Math.min(tempVerticies[1], tempVerticies[5]))
				{
					tempDeltaY = (Math.min(tempVerticies[1], tempVerticies[5])-Math.max(vertices[1], vertices[5]));
					setYVelocity(getYVelocity() * -1.0f);
				}
			}
			else if(getYVelocity() < 0)
				if(Math.min(vertices[1], vertices[5]) < Math.max(tempVerticies[1], tempVerticies[5]))
				{
					tempDeltaY = (Math.max(tempVerticies[1], tempVerticies[5])-Math.min(vertices[1], vertices[5]));
					setYVelocity(getYVelocity() * -1.0f);
				}
			if(tempDeltaY != 0)
			{
				deltaY = tempDeltaY;
				for(int i = 0; i < vertices.length; i++)
					vertices[i] = oldVertices[i];
				vertices[1] += (float)deltaY;
				reAlignVertices();
			}
		}
		return deltaY;
	}
	private void reAlignVertices()
	{
		vertices[2] = vertices[0];
		vertices[3] = vertices[1]+(0.0625f * 4)/3;
		vertices[4] = vertices[2]+0.125f;
		vertices[5] = vertices[3];
		vertices[6] = vertices[4];
		vertices[7] = vertices[1];
	}
}