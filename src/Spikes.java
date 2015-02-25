public class Spikes extends CollidableObject
{
	/**
	 * Collisions are currently checked using rectangles 
	 * instead of adapting for any shape I will simply
	 * use a rectangular approximation until I get onto
	 * more complex shapes in which case I will possibly
	 * allow for multiple bounding boxes but will keep them
	 * rectangular
	 */
	float[] boundingBox = {
			getCoords()[0] , getCoords()[1],
			getCoords()[0] , getCoords()[1] + 0.0625f/3,
			getCoords()[0] + 0.0625f , getCoords()[1] + 0.0625f/3,
			getCoords()[0] + 0.0625f , getCoords()[1]
	};
	float[] vertices = {
			getCoords()[0] , getCoords()[1],
			getCoords()[0] + 0.0625f/8 , getCoords()[1] + 0.0625f/3,
			getCoords()[0] + 0.0625f/4 , getCoords()[1],
			
			getCoords()[0] + 0.0625f/4 , getCoords()[1],
			getCoords()[0] + 3*0.0625f/8 , getCoords()[1] + 0.0625f/3,
			getCoords()[0] + 0.0625f/2 , getCoords()[1],
			
			getCoords()[0] + 0.0625f/2 , getCoords()[1],
			getCoords()[0] + 5*0.0625f/8 , getCoords()[1] + 0.0625f/3,
			getCoords()[0] + 3*0.0625f/4 , getCoords()[1],
			
			getCoords()[0] + 3*0.0625f/4 , getCoords()[1],
			getCoords()[0] + 7*0.0625f/8 , getCoords()[1] + 0.0625f/3,
			getCoords()[0] + 0.0625f , getCoords()[1],
	};
	float[] colors = {
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f,
	};
	public Spikes(float x, float y, int vaoID, int vboID, int vbocID, int vboiID) 
	{
		super(x, y, vaoID, vboID, vbocID, vboiID);
		setVertices(boundingBox);
		setColor(colors);
	}
	@Override
	public void doRender(Main m)
	{
		for(int i = 0; i < 4; i++)
		{
			float[] tempVertices = new float[]{
					vertices[i * 6], vertices[i * 6 + 1],
					vertices[i * 6 + 2], vertices[i * 6 + 3],
					vertices[i * 6 + 4], vertices[i * 6 + 5],
			};
			m.renderAsTriangles(tempVertices, getColor(), getVAO(), getVBO(), getVBOC(), getVBOI(), 3);
		}
	}
	@Override
	public void act(Main m)
	{
	}
}
