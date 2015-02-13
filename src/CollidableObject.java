import java.awt.Color;
import java.util.Arrays;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public abstract class CollidableObject 
{
	private float xCoord, yCoord, originalX, originalY;
	private String texLoc;
	private int vaoID, vboID, vbocID, vboiID;
	private float[] vertices, colors;
	public CollidableObject(float x, float y, int vaoID, int vboID, int vbocID, int vboiID)
	{
		xCoord = x;
		yCoord = y;
		originalX = x;
		originalY = y;
		this.vaoID = vaoID;
		this.vboID = vboID;
		this.vbocID = vbocID;
		this.vboiID = vboiID;
	}
	public int getVAO()
	{
		return vaoID;
	}
	public int getVBO()
	{
		return vboID;
	}
	public int getVBOC()
	{
		return vbocID;
	}
	public int getVBOI()
	{
		return vboiID;
	}
	public void setX(float x)
	{
		xCoord = x;
	}
	public void setY(float y)
	{
		yCoord = y;
	}
	public String getTextureLocation()
	{
		return texLoc;
	}
	public float[] getCoords()
	{
		float[] coords = {xCoord , yCoord};
		return coords;
	}
	public void setColor(float r, float g, float b, float a)
	{
		colors = new float[] {
				r,g,b,a,
				r,g,b,a,
				r,g,b,a,
				r,g,b,a
		};
	}
	public void setColor(float[] colors)
	{
		this.colors = colors;
	}
	public float[] getColor()
	{
		return colors;
	}
	public void setVertices(float[] v)
	{
		vertices = v;
	}
	public float[] getVertices()
	{
		return vertices;
	}
	public boolean checkForCollision(CollidableObject c)
	{
		float[] tempVertices = c.getVertices();
		float[] vertices = getVertices();
		float maxXTemp = Math.max(tempVertices[0], tempVertices[4]);
		float minXTemp = Math.min(tempVertices[0], tempVertices[4]);
		float maxYTemp = Math.max(tempVertices[1], tempVertices[5]);
		float minYTemp = Math.min(tempVertices[1], tempVertices[5]);
		float maxX = Math.max(vertices[0], vertices[4]);
		float minX = Math.min(vertices[0], vertices[4]);
		float maxY = Math.max(vertices[1], vertices[5]);
		float minY = Math.min(vertices[1], vertices[5]);
		if(maxXTemp > maxX)
			if(minXTemp < maxX)
				if(maxYTemp > maxY)
					if(minYTemp < maxY)
						return true;
					else return false;
				else if(maxYTemp > minY)
					return true;
				else return false;
			else return false;
		else if(maxXTemp > minX)
			if(maxYTemp > maxY)
				if(minYTemp < maxY)
					return true;
				else return false;
			else if(maxYTemp > minY)
				return true;
			else return false;
		else return false;
	}
	public boolean checkForContact(CollidableObject c)
	{
		float[] tempVertices = c.getVertices();
		float[] vertices = getVertices();
		float maxXTemp = Math.max(tempVertices[0], tempVertices[4]);
		float minXTemp = Math.min(tempVertices[0], tempVertices[4]);
		float maxYTemp = Math.max(tempVertices[1], tempVertices[5]);
		float minYTemp = Math.min(tempVertices[1], tempVertices[5]);
		float maxX = Math.max(vertices[0], vertices[4]);
		float minX = Math.min(vertices[0], vertices[4]);
		float maxY = Math.max(vertices[1], vertices[5]);
		float minY = Math.min(vertices[1], vertices[5]);
		if(Math.abs(minX - maxXTemp) < 0.001 || Math.abs(maxY - minYTemp) < 0.001|| Math.abs(maxX - minXTemp) < 0.001 || Math.abs(minY - maxYTemp) < 0.001)
			return true;
		return false;
	}
	public void reset()
	{
		setX(originalX);
		setY(originalY);
	}
	public static CollidableObject createObjectFromString(float x, float y, int id, String s, Main m)
	{
		switch(id)
		{
			case 1: 
				if(s.contains("[") && s.contains("]"))
				{
					float xOffset = Float.parseFloat((s.substring(s.indexOf("[") + 1, s.indexOf(";", s.indexOf("[")))));
					float yOffset = Float.parseFloat(s.substring(s.indexOf(";", s.indexOf("[")) + 1,s.indexOf("]")));
					return new SmallBlock(x + xOffset, y + yOffset, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers());
				}
				else
					return new SmallBlock(x, y, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers());
			case 2:
				if(s.contains("[") && s.contains("]"))
				{
					float xOffset = Float.parseFloat((s.substring(s.indexOf("[") + 1, s.indexOf(";", s.indexOf("[")))));
					float yOffset = Float.parseFloat(s.substring(s.indexOf(";", s.indexOf("[")) + 1,s.indexOf("]")));
					return new LevelExit(x + xOffset, y + yOffset, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers());
				}
				else
					return new LevelExit(x, y, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers());
			case 3:
				if(s.contains("[") && s.contains("]"))
				{
					float xOffset = Float.parseFloat((s.substring(s.indexOf("[") + 1, s.indexOf(";", s.indexOf("[")))));
					float yOffset = Float.parseFloat(s.substring(s.indexOf(";", s.indexOf("[")) + 1,s.indexOf("]")));
					return new MovingPlatform(x + xOffset, y + yOffset, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers());
				}
				else
					return new MovingPlatform(x, y, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers());
			case 4: 
				if(s.contains("[") && s.contains("]"))
				{
					float xOffset = Float.parseFloat((s.substring(s.indexOf("[") + 1, s.indexOf(";", s.indexOf("[")))));
					float yOffset = Float.parseFloat(s.substring(s.indexOf(";", s.indexOf("[")) + 1,s.indexOf("]")));
					return new Spikes(x + xOffset, y + yOffset, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers());
				}
				else
					return new Spikes(x, y, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers());
			case 5:
				float maxY, minY;
				if(s.contains("(") && s.contains(")"))
				{
					String temp = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
					minY = y - Float.parseFloat(temp.split(";")[0]);
					maxY = y + Float.parseFloat(temp.split(";")[1]);
				}
				else
				{
					maxY = y + .5f;
					minY = y;
				}
				if(s.contains("[") && s.contains("]"))
				{
					float xOffset = Float.parseFloat((s.substring(s.indexOf("[") + 1, s.indexOf(";", s.indexOf("[")))));
					float yOffset = Float.parseFloat(s.substring(s.indexOf(";", s.indexOf("[")) + 1,s.indexOf("]")));
					return new VerticalMovingPlatform(x + xOffset, y + yOffset, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers(), maxY, minY);
				}
				else
					return new VerticalMovingPlatform(x, y, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers(), maxY, minY);
		}
		return null;
	}
	public abstract void doRender(Main m);
	public abstract void act(Main m);
}