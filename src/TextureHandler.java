import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static org.lwjgl.opengl.GL15.*;

import javax.imageio.ImageIO;

public class TextureHandler
{
	public boolean loadTexture2d(String path)
	{
		 BufferedImage img;
		 try{
			 img = ImageIO.read(new File(path));
		 }catch(IOException e){
			 System.out.println("Texture at path: " + path + " is missing or damaged.");
			 return false;
		 }
		 
		 return true;
	}
}