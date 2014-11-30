package gameworld;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class MapLoader {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		imageToMap(ImageIO.read(new FileInputStream(new File(args[0]))), new File(args[1]));
	}
	
	public static void imageToMap(BufferedImage image, File file) throws FileNotFoundException {
		
		PrintWriter writer = new PrintWriter(file);
		
		writer.println("this.worldBlocks = new WorldStates["+image.getWidth()+"]["+image.getHeight()+"];");
		
		writer.println("for (int i = 0; i < worldBlocks.length; i++) {");
		writer.println("for (int j = 0; j < worldBlocks[i].length; j++) {");
		writer.println("worldBlocks[i][j] = WorldStates.EMPTY;");
		writer.println("}}");
		
		for(int i = 0; i < image.getWidth(); i++) {
			for(int j = 0; j < image.getHeight(); j++) {
				
				String worldState = "";
				
				if(Math.abs(image.getRGB(i, j) - Color.black.getRGB()) < 100) {
					worldState = "WorldStates.OCCUPIED";
				}
//				else {
//					worldState = "WorldStates.EMPTY";
//				}
				
				if(!worldState.isEmpty())
					writer.println("worldBlocks["+i+"]["+j+"] = "+worldState+";");
			}
		}
		writer.close();
	}
	
}
