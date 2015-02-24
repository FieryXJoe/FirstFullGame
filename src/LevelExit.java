
public class LevelExit extends CollidableObject
{
	float[] boundingBox = {
			getCoords()[0] , getCoords()[1],
			getCoords()[0] , getCoords()[1] + 0.05653f * 2,
			getCoords()[0] + 0.06097f * 2 , getCoords()[1] + 0.05653f * 2,
			getCoords()[0] + 0.06097f * 2 , getCoords()[1]
	};
	float[] vertices = {
			getCoords()[0] + 0.01288f * 2, getCoords()[1],
			getCoords()[0] + 0.00153f * 2, getCoords()[1] + 0.03459f * 2,
			getCoords()[0] + 0.03125f * 2, getCoords()[1] + 0.05653f * 2,
			getCoords()[0] + 0.06097f * 2, getCoords()[1] + 0.03494f * 2,
			getCoords()[0] + 0.04962f * 2, getCoords()[1]
					};
	float[] colors = {
			.4f, .1f, .1f, 1f,
			.4f, .2f, .2f, 1f,
			.4f, .3f, .3f, 1f,
			.4f, .4f, .4f, 1f,
			.4f,.25f,.25f, 1f,
	};
	public LevelExit(float x, float y, int vaoID, int vboID, int vbocID, int vboiID)
	{
		super(x, y, vaoID, vboID, vbocID, vboiID);
		setColor(colors);
		setVertices(boundingBox);
	}
	@Override
	public void doRender(Main m)
	{
		//m.renderRectangleAsTriangles(vertices, getColor(), getVAO(), getVBO(), getVBOC(), getVBOI());
		m.renderPentagonAsTriangles(vertices, getColor(), getVAO(), getVBO(), getVBOC(), getVBOI());
	}
	@Override
	public void reset()
	{	
		super.reset();
		setColor(new float[]{
				.1f,.1f,.1f,1f,
				.2f,.2f,.2f,1f,
				.2f,.2f,.2f,1f,
				.2f,.2f,.2f,1f
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
	}
}
