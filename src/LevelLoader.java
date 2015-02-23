import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class LevelLoader
{
	private static String rawText;
	private static ArrayList<CollidableObject> objList = new ArrayList<CollidableObject>();
	private static int level = -1;
	private static String[] levelFiles = {
		"Level1.txt" , "Level2.txt" , "Level3.txt", "Level4.txt",
		"Level5.txt"
	};
	public LevelLoader(Main main){}
	public static void loadLevel(Main m)
	{
		String filePath = "";
		try{
			filePath = levelFiles[++level];
		}catch(IndexOutOfBoundsException e){
			System.out.println("Victory screen placeholder");
			m.end();
		}
		objList = new ArrayList<CollidableObject>();
        StringBuilder source = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderProgram.class.getClassLoader().getResourceAsStream(filePath)));
            String line;
            while ((line = reader.readLine()) != null)
            {
                source.append(line);
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.err.println("Error loading source code: " + filePath);
            e.printStackTrace();
        }
		rawText =  source.toString();
		String[] layers = rawText.split(":");
		float x = 0.0625f, y = (0.0625f * 4)/3;
		for(int i = 0; i < layers.length; i++)
		{
			String layer = layers[i];
			String[] objects = layer.split(",");
			for(int j = 0; j < objects.length; j++)
			{
				String object = objects[j];
				if(object.charAt(0) == 'p')
				{
					if(object.contains("[") && object.contains("]"))
					{
						float xOffset = Float.parseFloat((object.substring(object.indexOf("[") + 1, object.indexOf(";", object.indexOf("[")))));
						float yOffset = Float.parseFloat(object.substring(object.indexOf(";", object.indexOf("[")) + 1,object.indexOf("]")));
						m.setPlayer(new Player(-1f + (j * x * 1f) + xOffset, -1f + (i * y * 1f) + yOffset, glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers()));
					}
					else
						m.setPlayer(new Player(-1f + (j * x * 1f), -1f + (i * y * 1f), glGenVertexArrays(), glGenBuffers(), glGenBuffers(), glGenBuffers()));
				}
				else
				{
					int id = Integer.parseInt("" + object.charAt(0));
					if(!(id == 0))
					{
						object = object.substring(1);
						objList.add(CollidableObject.createObjectFromString(-1f + (j * x * 1f), -1f + (i * y * 1f), id, object, m));
					}
				}
			}
		}
		m.setObjList(objList);
	}
}
