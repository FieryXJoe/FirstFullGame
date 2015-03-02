import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static org.lwjgl.opengl.GL15.*;

import javax.imageio.ImageIO;

public class TextureHandler
{
	public byte[] loadTexture2d(String path)
	{
		 BufferedImage img;
		 try{
			 img = ImageIO.read(new File(path));
		 }catch(IOException e){
			 System.out.println("Texture at path: " + path + " is missing or damaged.");
			 return null;
		 }
		 byte[] pixelArray = convertPngToByteArray(img);
		 
		 return pixelArray;
	}
	public byte[] convertPngToByteArray(BufferedImage img)
	{
		byte[] b = new byte[img.getHeight() * img.getHeight() * 4];
		for(int i = 0; i < b.length; i += 4)
		{
				b[i] = (byte)((img.getRGB((i/4)%img.getWidth(), (i/4)/img.getWidth()) >> 24) % 255);
				b[i + 1] = (byte)((img.getRGB((i/4)%img.getWidth(), (i/4)/img.getWidth()) >> 16) % 255);
				b[i + 2] = (byte)((img.getRGB((i/4)%img.getWidth(), (i/4)/img.getWidth()) >> 8) % 255);
				b[i + 3] = (byte)(img.getRGB((i/4)%img.getWidth(), (i/4)/img.getWidth()) % 255);
		}
		return b;
	}
}