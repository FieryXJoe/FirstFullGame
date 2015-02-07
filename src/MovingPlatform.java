import java.util.ArrayList;
import java.util.Arrays;

public class MovingPlatform extends Player
{
	public ArrayList<CollidableObject> collidedObjects;
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
	public MovingPlatform(float x, float y, int vaoID, int vboID, int vbocID, int vboiID)
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
		setXVelocity(0.2);
	}
	@Override
	public void reset()
	{
		super.reset();
		setXVelocity(.2);
	}
	@Override
	public void doRender(Main m)
	{
		m.renderRectangleAsTriangles(getVertices(), getColor(), getVAO(), getVBO(), getVBOC(), getVBOI());
	}
	@Override
	public void act(Main m)
	{
		collidedObjects = new ArrayList<CollidableObject>();
		double deltaX = getXVelocity()*getDeltaT();
		float[] oldVertices = new float[vertices.length];
		for(int i = 0; i < vertices.length; i++)
			oldVertices[i] = vertices[i];
		vertices[0] += (float)deltaX;
		reAlignVertices();
		CollidableObject collidedObject = null;
		for(CollidableObject c : m.getObjList())
		{
			if(!c.equals(this) && !c.equals(m.getPlayer()) && checkForCollision(c))
			{
				collidedObjects.add(c);
				double tempDeltaX = 0;
				collidedObject = c;
				float[] tempVerticies = collidedObject.getVertices();
				if(getXVelocity() > 0)
				{
					if(Math.max(vertices[0], vertices[4])>Math.min(tempVerticies[0], tempVerticies[4]))
					{
						tempDeltaX = (Math.min(tempVerticies[0], tempVerticies[4])-Math.max(vertices[0], vertices[4]));
						setXVelocity(getXVelocity() * -1.0f);
					}
				}
				else if(getXVelocity() < 0)
					if(Math.min(vertices[0], vertices[4]) < Math.max(tempVerticies[0], tempVerticies[4]))
					{
						tempDeltaX = (Math.max(tempVerticies[0], tempVerticies[4])-Math.min(vertices[0], vertices[4]));
						setXVelocity(getXVelocity() * -1.0f);
					}
				if(tempDeltaX != 0)
				{
					deltaX = tempDeltaX;
					for(int i = 0; i < vertices.length; i++)
						vertices[i] = oldVertices[i];
					vertices[0] += (float)deltaX;
					reAlignVertices();
				}
			}
		}
		setVertices(vertices);
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