import static org.lwjgl.opengl.GL11.*;

public class SmallBlock extends CollidableObject
{
	float[] vertices = {
			getCoords()[0] , getCoords()[1],
			getCoords()[0] , getCoords()[1] + (0.0625f * 4)/3,
			getCoords()[0] + 0.0625f , getCoords()[1] + (0.0625f * 4)/3,
			getCoords()[0] + 0.0625f, getCoords()[1]
	};
	float[] colors = {
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f,
			1f,1f,1f,1f
	};
	public SmallBlock(float x, float y, int vaoID, int vboID, int vbocID, int vboiID) 
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
			getCoords()[0] , getCoords()[1],
			getCoords()[0] , getCoords()[1] + (0.0625f * 4)/3,
			getCoords()[0] + 0.0625f , getCoords()[1] + (0.0625f * 4)/3,
			getCoords()[0] + 0.0625f, getCoords()[1]
	});
	}
	@Override
	public void act(Main m) 
	{
	}
}
