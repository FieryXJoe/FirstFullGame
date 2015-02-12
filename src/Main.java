import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Main
{
	private static long    windowID;
	private static boolean running;
    private ShaderProgram shaderProgram;

	// The callbacks
	GLFWErrorCallback       errorCallback;
	GLFWKeyCallback         keyCallback;
	GLFWCursorPosCallback   cursorPosCallback;
	GLFWMouseButtonCallback mouseButtonCallback;
	GLFWScrollCallback      scrollCallback;
	
	private ArrayList<CollidableObject> objectList = new ArrayList<CollidableObject>();
	private Player p;

	public Main()
	{
		if (glfwInit() != GL_TRUE)
		{
			System.err.println("Error initializing GLFW");
			System.exit(1);
		}

		// Window Hints for OpenGL context
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		windowID = glfwCreateWindow(640, 480, "My GLFW Window", NULL, NULL);

		if (windowID == NULL)
		{
			System.err.println("Error creating a window");
			System.exit(1);
		}

		glfwMakeContextCurrent(windowID);
		GLContext.createFromCurrent();

		glfwSwapInterval(1);
	}

	public void init()
	{
		glfwSetWindowTitle(getWindowID(), "First Game");

        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("VertexShader.vs");
        shaderProgram.attachFragmentShader("FragmentShader.fs");
        shaderProgram.link();
        
        LevelLoader.loadLevel(this);
	}
	public void update(float delta)
	{
		p.act(this);
		for(CollidableObject c: objectList)
			c.act(this);
		
	}

	public void render(float delta)
	{
		glClear(GL_COLOR_BUFFER_BIT);
        
		shaderProgram.bind();
		
		for(CollidableObject obj: objectList)
			obj.doRender(this);
		p.doRender(this);

        ShaderProgram.unbind();
	}

	public void dispose()
	{
		shaderProgram.dispose();

        glBindVertexArray(0);
        for(CollidableObject c: objectList)
        	glDeleteVertexArrays(c.getVAO());

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for(CollidableObject c: objectList)
        	glDeleteBuffers(c.getVBO());
	}
	public static float timeOfLastFrame;
	public void start()
	{
		float now, last, delta;

		last = 0;

		errorCallback = Callbacks.errorCallbackPrint(System.err);
		keyCallback = GLFWKeyCallback(this::glfwKeyCallback);
		cursorPosCallback = GLFWCursorPosCallback(this::glfwCursorPosCallback);
		mouseButtonCallback = GLFWMouseButtonCallback(this::glfwMouseButtonCallback);
		scrollCallback = GLFWScrollCallback(this::glfwScrollCallback);

		glfwSetErrorCallback(errorCallback);
		glfwSetKeyCallback(windowID, keyCallback);
		glfwSetCursorPosCallback(windowID, cursorPosCallback);
		glfwSetMouseButtonCallback(windowID, mouseButtonCallback);
		glfwSetScrollCallback(windowID, scrollCallback);

		init();

		running = true;

		while (running && glfwWindowShouldClose(windowID) != GL_TRUE)
		{	
			now = (float) glfwGetTime();
			delta = now - last;
			if(!(delta < 1/60))
			{
				last = now;
				timeOfLastFrame = delta;
				update(delta);
				render(delta);

				glfwPollEvents();
				glfwSwapBuffers(windowID);
			}
		}
		dispose();

		keyCallback.release();
		cursorPosCallback.release();
		mouseButtonCallback.release();
		scrollCallback.release();
		errorCallback.release();

		glfwDestroyWindow(windowID);
		glfwTerminate();

		System.exit(0);
	}

	public void end()
	{
		running = false;
	}

	public void glfwKeyCallback(long window, int key, int scancode, int action, int mods)
	{
		if(key == GLFW_KEY_D && !p.isDead)
			if(action == GLFW_PRESS || action == GLFW_REPEAT)
				p.setWalkingRight(true);
			else
				p.setWalkingRight(false);
		if(key == GLFW_KEY_A && !p.isDead)
			if(action == GLFW_PRESS || action == GLFW_REPEAT)
				p.setWalkingLeft(true);
			else
				p.setWalkingLeft(false);
		if(key == GLFW_KEY_SPACE && !p.isDead)
			if(action != GLFW_RELEASE && p.isOnGround)
				p.setYVelocity(0.6);
		if(key == GLFW_KEY_R)
			if(action != GLFW_RELEASE)
			{
				p.reset();
				for(CollidableObject c : objectList)
					c.reset();
			}
		if(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE)
			end();
	}

	public void glfwCursorPosCallback(long window, double xpos, double ypos)
	{
	}

	public void glfwMouseButtonCallback(long window, int button, int action, int mods)
	{
	}

	public void glfwScrollCallback(long window, double xoffset, double yoffset)
	{
	}

	public static boolean isKeyPressed(int key)
	{
		return glfwGetKey(windowID, key) != GLFW_RELEASE;
	}

	public static boolean isMouseButtonPressed(int button)
	{
		return glfwGetMouseButton(windowID, button) != GLFW_RELEASE;
	}

	public static long getWindowID()
	{
		return windowID;
	}
	public ArrayList<CollidableObject> getObjList()
	{
		return objectList;
	}
	public void setObjList(ArrayList<CollidableObject> newObjList)
	{
		objectList = newObjList;
	}
	public void setPlayer(Player p)
	{
		this.p = p;
	}
	public Player getPlayer()
	{
		return p;
	}
	public void addToObjList(CollidableObject c)
	{
		objectList.add(c);
	}
	public void removeFromObjectList(CollidableObject c)
	{
		objectList.remove(c);
	}
	public boolean replaceObjInList(CollidableObject objToReplace, CollidableObject objToReplaceWith)
	{
		for(int i = 0; i < objectList.size(); i++)
			if(objectList.get(i).equals(objToReplace))
			{
				objectList.set(i, objToReplaceWith);
				return true;
			}
		return false;
	}
	public boolean swapObjectsInList(CollidableObject obj1, CollidableObject obj2)
	{
		int x = -1, y = -1;
		for(int i = 0; i < objectList.size(); i++)
			if(objectList.get(i).equals(obj1))
				x = i;
			else if(objectList.get(i).equals(obj2))
				y = i;
		if(x != -1 || y != -1)
		{
			objectList.set(y, obj1);
			objectList.set(x, obj2);
			return true;
		}
		return false;
	}
	public void bindVertices(float[] vertices, float[] colors, byte[] indices, int vaoID, int vboID, int vbocID, int vboiID)
	{
		glBindVertexArray(vaoID);
		
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indices.length);
        indicesBuffer.put(indices).flip();
        
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();
        
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorBuffer.put(colors).flip();
        
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER, vbocID);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        
        glBindVertexArray(0);
	}
	public void drawVertices(int mode, int vaoID, int vboiID, int count)
	{
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiID);
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
	}
	public void renderRectangleAsTriangles(float[] vertices, float[] colors, int vaoID, int vboID, int vbocID, int vboiID)
	{
        byte[] indices = {
                0, 1, 2,
                2, 3, 0
        };
        int indicesCount = indices.length;
		bindVertices(vertices, colors, indices, vaoID, vboID, vbocID, vboiID);
		drawVertices(GL_TRIANGLES, vaoID, vboiID, indicesCount);
	}
	public void renderTriangle(float[] vertices, float[] colors, int vaoID, int vboID, int vbocID, int vboiID)
	{
		byte[] indices = {0,1,2};
		int indicesCount = indices.length;
		bindVertices(vertices, colors, indices, vaoID, vboID, vbocID, vboiID);
		drawVertices(GL_TRIANGLES, vaoID, vboiID, indicesCount);
	}
	public void loadNextLevel()
	{
		LevelLoader.loadLevel(this);
	}
	public static void main(String[] args)
	{
		new Main().start();
	}
}