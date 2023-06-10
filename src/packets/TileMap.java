package packets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class TileMap implements Serializable{

	private static final long serialVersionUID = 1L;
	transient private BufferedReader br;
	private int rows;
	private int columns;
	private int layers;
	private int[][][] textureMap;
	private boolean[][] collisionMap;
	
	public TileMap(InputStream is) {
		br = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			line = br.readLine();
			String arguments[] = line.split(" ");
			this.rows = Integer.parseInt(arguments[0]); // first int is rows
			this.columns = Integer.parseInt(arguments[1]); //second is columns
			this.layers = Integer.parseInt(arguments[2]); // third is layers
			textureMap = new int[layers][rows][columns];
			collisionMap = new boolean[rows][columns];
			loadMap();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void loadMap() {
		try {
			//Generates texture map from file
			for(int layer=0; layer<layers; layer++) {
				for(int row = 0; row < rows; ++row) {
					String line = br.readLine();
					String numbers[] = line.split(" ");
					for(int col = 0; col < columns; col++) {
						textureMap[layer][row][col] = Integer.parseInt(numbers[col]);
					}
				}
			}
			//Generates collision map from file
			for(int row = 0; row < rows; ++row) {
				String line = br.readLine();
				String numbers[] = line.split(" ");
				for(int col = 0; col < columns; col++) {
					collisionMap[row][col] = getBoolean(Integer.parseInt(numbers[col]));
				}
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public boolean getBoolean(int i) {
		if(i==0) return false;
		else return true;
	}
	
	//Getters & Setters
	public int getRows() { return rows; }
	
	public int getColumns() { return columns; }
	
	public int getLayers() { return layers; }
	
	public int[][][] getTextureMap() { return textureMap; }
	
	public boolean[][] getCollisionMap() { return collisionMap; }
}
