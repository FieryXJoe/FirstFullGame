
public class LevelExit extends CollidableObject
{
	float[] vertices = {
			getCoords()[0], getCoords()[1],
			getCoords()[0], getCoords()[1]+0.1f,
			getCoords()[0]+0.05f, getCoords()[1]+0.1f,
			getCoords()[0]+0.05f, getCoords()[1]
					};
	float[] colors = {
			.1f,.1f,.1f,1f,
			.2f,.2f,.2f,1f,
			.2f,.2f,.2f,1f,
			.2f,.2f,.2f,1f
	};
	public LevelExit(float x, float y, int vaoID, int vboID, int vbocID, int vboiID)
	{
		super(x, y, vaoID, vboID, vbocID, vboiID);
		setColor(colors);
		setVertices(vertices);
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
