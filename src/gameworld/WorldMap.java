package gameworld;

import java.awt.Point;
import java.awt.geom.AffineTransform;

import ai.Ai;

public class WorldMap {

	public static final int VIEW_SIZE = 15;
	public WorldStates[][] worldBlocks;

	private Point flag1, flag2;
	
	private Ai ai1, ai2;
	
	public WorldMap() {
		this.hardCodedBlocks();
		this.createBlocks();
	}
	
	public void setAi(Ai ai1, Ai ai2) {
		this.ai1 = ai1;
		this.ai2 = ai2;
	}

	private void createBlocks() {
		flag1 = new Point(24, 5);
		flag2 = new Point(24, 44);
		
		worldBlocks[flag1.x][flag1.y] = WorldStates.FLAG;
		worldBlocks[flag2.x][flag2.y] = WorldStates.FLAG;
	}

	public int[][] getGridView(Ai ai) {
		return getGridView(ai.getX(), ai.getY(), ai);
	}
	
	public int[][] getGridView(double aiX, double aiY, Ai ai) {

		int[][] worldView = new int[VIEW_SIZE][VIEW_SIZE];

		// Default spaces to occupied
		for (int i = 0; i < worldView.length; i++) {
			for (int j = 0; j < worldView[i].length; j++) {
				worldView[i][j] = WorldStates.OCCUPIED.getCode();
			}
		}

		int x, y;

		int xOffset = WorldMap.VIEW_SIZE / 2;

		for (int i = 0; i < WorldMap.VIEW_SIZE; i++) {
			for (int j = 0; j < WorldMap.VIEW_SIZE; j++) {

				x = (int) Math.round(i - xOffset);
				y = (int) Math.round(j);

				x += aiX;
				y += aiY;

				Point result = new Point();
				AffineTransform rotation = new AffineTransform();
				double angleInRadians = ai.getRotation() - Math.PI / 2;
				rotation.rotate(angleInRadians, aiX, aiY);
				rotation.transform(new Point(x, y), result);

				if (result.x >= 0 && result.x < worldBlocks.length && result.y >= 0 && result.y < worldBlocks[result.x].length) {
					worldView[i][j] = worldBlocks[result.x][result.y].getCode();
					
					if(ai1 != ai && Math.floor(ai1.getX()) == result.x && Math.floor(ai1.getY()) == result.y) {
						worldView[i][j] = WorldStates.ENEMY.getCode();
					}
					else if(ai2 != ai && Math.floor(ai2.getX()) == result.x && Math.floor(ai2.getY()) == result.y) {
						worldView[i][j] = WorldStates.ENEMY.getCode();
					}
				}
			}
		}

		return worldView;
	}

	public Point getStart1() {
		return this.flag1;
	}
	
	public Point getStart2() {
		return this.flag2;
	}

	public boolean isValid(double x, double y) {

		int xLoc = (int)Math.floor(x);
		int yLoc = (int)Math.floor(y);
		
		if(xLoc < 0 || xLoc >= this.worldBlocks.length || yLoc < 0 || yLoc >= this.worldBlocks[0].length ||
		  this.worldBlocks[xLoc][yLoc] == WorldStates.ENEMY || this.worldBlocks[xLoc][yLoc] == WorldStates.OCCUPIED)
			return false;
		
		return true;
	}
	
	private void hardCodedBlocks() {
		this.worldBlocks = new WorldStates[50][50];
		for (int i = 0; i < worldBlocks.length; i++) {
		for (int j = 0; j < worldBlocks[i].length; j++) {
		worldBlocks[i][j] = WorldStates.EMPTY;
		}}
		worldBlocks[0][0] = WorldStates.OCCUPIED;
		worldBlocks[0][2] = WorldStates.OCCUPIED;
		worldBlocks[0][3] = WorldStates.OCCUPIED;
		worldBlocks[0][4] = WorldStates.OCCUPIED;
		worldBlocks[0][5] = WorldStates.OCCUPIED;
		worldBlocks[0][6] = WorldStates.OCCUPIED;
		worldBlocks[0][7] = WorldStates.OCCUPIED;
		worldBlocks[0][8] = WorldStates.OCCUPIED;
		worldBlocks[0][9] = WorldStates.OCCUPIED;
		worldBlocks[0][10] = WorldStates.OCCUPIED;
		worldBlocks[0][11] = WorldStates.OCCUPIED;
		worldBlocks[0][12] = WorldStates.OCCUPIED;
		worldBlocks[0][13] = WorldStates.OCCUPIED;
		worldBlocks[0][14] = WorldStates.OCCUPIED;
		worldBlocks[0][15] = WorldStates.OCCUPIED;
		worldBlocks[0][16] = WorldStates.OCCUPIED;
		worldBlocks[0][17] = WorldStates.OCCUPIED;
		worldBlocks[0][18] = WorldStates.OCCUPIED;
		worldBlocks[0][19] = WorldStates.OCCUPIED;
		worldBlocks[0][20] = WorldStates.OCCUPIED;
		worldBlocks[0][21] = WorldStates.OCCUPIED;
		worldBlocks[0][22] = WorldStates.OCCUPIED;
		worldBlocks[0][23] = WorldStates.OCCUPIED;
		worldBlocks[0][24] = WorldStates.OCCUPIED;
		worldBlocks[0][25] = WorldStates.OCCUPIED;
		worldBlocks[0][26] = WorldStates.OCCUPIED;
		worldBlocks[0][27] = WorldStates.OCCUPIED;
		worldBlocks[0][28] = WorldStates.OCCUPIED;
		worldBlocks[0][29] = WorldStates.OCCUPIED;
		worldBlocks[0][30] = WorldStates.OCCUPIED;
		worldBlocks[0][31] = WorldStates.OCCUPIED;
		worldBlocks[0][32] = WorldStates.OCCUPIED;
		worldBlocks[0][33] = WorldStates.OCCUPIED;
		worldBlocks[0][34] = WorldStates.OCCUPIED;
		worldBlocks[0][35] = WorldStates.OCCUPIED;
		worldBlocks[0][36] = WorldStates.OCCUPIED;
		worldBlocks[0][37] = WorldStates.OCCUPIED;
		worldBlocks[0][38] = WorldStates.OCCUPIED;
		worldBlocks[0][39] = WorldStates.OCCUPIED;
		worldBlocks[0][40] = WorldStates.OCCUPIED;
		worldBlocks[0][41] = WorldStates.OCCUPIED;
		worldBlocks[0][42] = WorldStates.OCCUPIED;
		worldBlocks[0][43] = WorldStates.OCCUPIED;
		worldBlocks[0][44] = WorldStates.OCCUPIED;
		worldBlocks[0][45] = WorldStates.OCCUPIED;
		worldBlocks[0][46] = WorldStates.OCCUPIED;
		worldBlocks[0][47] = WorldStates.OCCUPIED;
		worldBlocks[0][48] = WorldStates.OCCUPIED;
		worldBlocks[0][49] = WorldStates.OCCUPIED;
		worldBlocks[1][0] = WorldStates.OCCUPIED;
		worldBlocks[1][49] = WorldStates.OCCUPIED;
		worldBlocks[2][0] = WorldStates.OCCUPIED;
		worldBlocks[2][49] = WorldStates.OCCUPIED;
		worldBlocks[3][0] = WorldStates.OCCUPIED;
		worldBlocks[3][49] = WorldStates.OCCUPIED;
		worldBlocks[4][0] = WorldStates.OCCUPIED;
		worldBlocks[4][49] = WorldStates.OCCUPIED;
		worldBlocks[5][0] = WorldStates.OCCUPIED;
		worldBlocks[5][49] = WorldStates.OCCUPIED;
		worldBlocks[6][0] = WorldStates.OCCUPIED;
		worldBlocks[6][49] = WorldStates.OCCUPIED;
		worldBlocks[7][0] = WorldStates.OCCUPIED;
		worldBlocks[7][49] = WorldStates.OCCUPIED;
		worldBlocks[8][0] = WorldStates.OCCUPIED;
		worldBlocks[8][49] = WorldStates.OCCUPIED;
		worldBlocks[9][0] = WorldStates.OCCUPIED;
		worldBlocks[9][49] = WorldStates.OCCUPIED;
		worldBlocks[10][0] = WorldStates.OCCUPIED;
		worldBlocks[10][49] = WorldStates.OCCUPIED;
		worldBlocks[11][0] = WorldStates.OCCUPIED;
		worldBlocks[11][49] = WorldStates.OCCUPIED;
		worldBlocks[12][0] = WorldStates.OCCUPIED;
		worldBlocks[12][49] = WorldStates.OCCUPIED;
		worldBlocks[13][0] = WorldStates.OCCUPIED;
		worldBlocks[13][49] = WorldStates.OCCUPIED;
		worldBlocks[14][0] = WorldStates.OCCUPIED;
		worldBlocks[14][49] = WorldStates.OCCUPIED;
		worldBlocks[15][0] = WorldStates.OCCUPIED;
		worldBlocks[15][49] = WorldStates.OCCUPIED;
		worldBlocks[16][0] = WorldStates.OCCUPIED;
		worldBlocks[16][49] = WorldStates.OCCUPIED;
		worldBlocks[17][0] = WorldStates.OCCUPIED;
		worldBlocks[17][49] = WorldStates.OCCUPIED;
		worldBlocks[18][0] = WorldStates.OCCUPIED;
		worldBlocks[18][49] = WorldStates.OCCUPIED;
		worldBlocks[19][0] = WorldStates.OCCUPIED;
		worldBlocks[19][49] = WorldStates.OCCUPIED;
		worldBlocks[20][0] = WorldStates.OCCUPIED;
		worldBlocks[20][49] = WorldStates.OCCUPIED;
		worldBlocks[21][0] = WorldStates.OCCUPIED;
		worldBlocks[21][49] = WorldStates.OCCUPIED;
		worldBlocks[22][0] = WorldStates.OCCUPIED;
		worldBlocks[22][49] = WorldStates.OCCUPIED;
		worldBlocks[23][0] = WorldStates.OCCUPIED;
		worldBlocks[23][22] = WorldStates.OCCUPIED;
		worldBlocks[23][23] = WorldStates.OCCUPIED;
		worldBlocks[23][24] = WorldStates.OCCUPIED;
		worldBlocks[23][25] = WorldStates.OCCUPIED;
		worldBlocks[23][26] = WorldStates.OCCUPIED;
		worldBlocks[23][49] = WorldStates.OCCUPIED;
		worldBlocks[24][0] = WorldStates.OCCUPIED;
		worldBlocks[24][22] = WorldStates.OCCUPIED;
		worldBlocks[24][23] = WorldStates.OCCUPIED;
		worldBlocks[24][24] = WorldStates.OCCUPIED;
		worldBlocks[24][25] = WorldStates.OCCUPIED;
		worldBlocks[24][26] = WorldStates.OCCUPIED;
		worldBlocks[24][49] = WorldStates.OCCUPIED;
		worldBlocks[25][0] = WorldStates.OCCUPIED;
		worldBlocks[25][22] = WorldStates.OCCUPIED;
		worldBlocks[25][23] = WorldStates.OCCUPIED;
		worldBlocks[25][24] = WorldStates.OCCUPIED;
		worldBlocks[25][25] = WorldStates.OCCUPIED;
		worldBlocks[25][26] = WorldStates.OCCUPIED;
		worldBlocks[25][49] = WorldStates.OCCUPIED;
		worldBlocks[26][0] = WorldStates.OCCUPIED;
		worldBlocks[26][22] = WorldStates.OCCUPIED;
		worldBlocks[26][23] = WorldStates.OCCUPIED;
		worldBlocks[26][24] = WorldStates.OCCUPIED;
		worldBlocks[26][25] = WorldStates.OCCUPIED;
		worldBlocks[26][26] = WorldStates.OCCUPIED;
		worldBlocks[26][49] = WorldStates.OCCUPIED;
		worldBlocks[27][0] = WorldStates.OCCUPIED;
		worldBlocks[27][22] = WorldStates.OCCUPIED;
		worldBlocks[27][23] = WorldStates.OCCUPIED;
		worldBlocks[27][24] = WorldStates.OCCUPIED;
		worldBlocks[27][25] = WorldStates.OCCUPIED;
		worldBlocks[27][26] = WorldStates.OCCUPIED;
		worldBlocks[27][49] = WorldStates.OCCUPIED;
		worldBlocks[28][0] = WorldStates.OCCUPIED;
		worldBlocks[28][49] = WorldStates.OCCUPIED;
		worldBlocks[29][0] = WorldStates.OCCUPIED;
		worldBlocks[29][49] = WorldStates.OCCUPIED;
		worldBlocks[30][0] = WorldStates.OCCUPIED;
		worldBlocks[30][49] = WorldStates.OCCUPIED;
		worldBlocks[31][0] = WorldStates.OCCUPIED;
		worldBlocks[31][49] = WorldStates.OCCUPIED;
		worldBlocks[32][0] = WorldStates.OCCUPIED;
		worldBlocks[32][49] = WorldStates.OCCUPIED;
		worldBlocks[33][0] = WorldStates.OCCUPIED;
		worldBlocks[33][49] = WorldStates.OCCUPIED;
		worldBlocks[34][0] = WorldStates.OCCUPIED;
		worldBlocks[34][49] = WorldStates.OCCUPIED;
		worldBlocks[35][0] = WorldStates.OCCUPIED;
		worldBlocks[35][49] = WorldStates.OCCUPIED;
		worldBlocks[36][0] = WorldStates.OCCUPIED;
		worldBlocks[36][49] = WorldStates.OCCUPIED;
		worldBlocks[37][0] = WorldStates.OCCUPIED;
		worldBlocks[37][49] = WorldStates.OCCUPIED;
		worldBlocks[38][0] = WorldStates.OCCUPIED;
		worldBlocks[38][49] = WorldStates.OCCUPIED;
		worldBlocks[39][0] = WorldStates.OCCUPIED;
		worldBlocks[39][49] = WorldStates.OCCUPIED;
		worldBlocks[40][0] = WorldStates.OCCUPIED;
		worldBlocks[40][49] = WorldStates.OCCUPIED;
		worldBlocks[41][0] = WorldStates.OCCUPIED;
		worldBlocks[41][49] = WorldStates.OCCUPIED;
		worldBlocks[42][0] = WorldStates.OCCUPIED;
		worldBlocks[42][49] = WorldStates.OCCUPIED;
		worldBlocks[43][0] = WorldStates.OCCUPIED;
		worldBlocks[43][49] = WorldStates.OCCUPIED;
		worldBlocks[44][0] = WorldStates.OCCUPIED;
		worldBlocks[44][49] = WorldStates.OCCUPIED;
		worldBlocks[45][0] = WorldStates.OCCUPIED;
		worldBlocks[45][49] = WorldStates.OCCUPIED;
		worldBlocks[46][0] = WorldStates.OCCUPIED;
		worldBlocks[46][49] = WorldStates.OCCUPIED;
		worldBlocks[47][0] = WorldStates.OCCUPIED;
		worldBlocks[47][49] = WorldStates.OCCUPIED;
		worldBlocks[48][0] = WorldStates.OCCUPIED;
		worldBlocks[48][49] = WorldStates.OCCUPIED;
		worldBlocks[49][0] = WorldStates.OCCUPIED;
		worldBlocks[49][1] = WorldStates.OCCUPIED;
		worldBlocks[49][2] = WorldStates.OCCUPIED;
		worldBlocks[49][3] = WorldStates.OCCUPIED;
		worldBlocks[49][4] = WorldStates.OCCUPIED;
		worldBlocks[49][5] = WorldStates.OCCUPIED;
		worldBlocks[49][6] = WorldStates.OCCUPIED;
		worldBlocks[49][7] = WorldStates.OCCUPIED;
		worldBlocks[49][8] = WorldStates.OCCUPIED;
		worldBlocks[49][9] = WorldStates.OCCUPIED;
		worldBlocks[49][10] = WorldStates.OCCUPIED;
		worldBlocks[49][11] = WorldStates.OCCUPIED;
		worldBlocks[49][12] = WorldStates.OCCUPIED;
		worldBlocks[49][13] = WorldStates.OCCUPIED;
		worldBlocks[49][14] = WorldStates.OCCUPIED;
		worldBlocks[49][15] = WorldStates.OCCUPIED;
		worldBlocks[49][16] = WorldStates.OCCUPIED;
		worldBlocks[49][17] = WorldStates.OCCUPIED;
		worldBlocks[49][18] = WorldStates.OCCUPIED;
		worldBlocks[49][19] = WorldStates.OCCUPIED;
		worldBlocks[49][20] = WorldStates.OCCUPIED;
		worldBlocks[49][21] = WorldStates.OCCUPIED;
		worldBlocks[49][22] = WorldStates.OCCUPIED;
		worldBlocks[49][23] = WorldStates.OCCUPIED;
		worldBlocks[49][24] = WorldStates.OCCUPIED;
		worldBlocks[49][25] = WorldStates.OCCUPIED;
		worldBlocks[49][26] = WorldStates.OCCUPIED;
		worldBlocks[49][27] = WorldStates.OCCUPIED;
		worldBlocks[49][28] = WorldStates.OCCUPIED;
		worldBlocks[49][29] = WorldStates.OCCUPIED;
		worldBlocks[49][30] = WorldStates.OCCUPIED;
		worldBlocks[49][31] = WorldStates.OCCUPIED;
		worldBlocks[49][32] = WorldStates.OCCUPIED;
		worldBlocks[49][33] = WorldStates.OCCUPIED;
		worldBlocks[49][34] = WorldStates.OCCUPIED;
		worldBlocks[49][35] = WorldStates.OCCUPIED;
		worldBlocks[49][36] = WorldStates.OCCUPIED;
		worldBlocks[49][37] = WorldStates.OCCUPIED;
		worldBlocks[49][38] = WorldStates.OCCUPIED;
		worldBlocks[49][39] = WorldStates.OCCUPIED;
		worldBlocks[49][40] = WorldStates.OCCUPIED;
		worldBlocks[49][41] = WorldStates.OCCUPIED;
		worldBlocks[49][42] = WorldStates.OCCUPIED;
		worldBlocks[49][43] = WorldStates.OCCUPIED;
		worldBlocks[49][44] = WorldStates.OCCUPIED;
		worldBlocks[49][45] = WorldStates.OCCUPIED;
		worldBlocks[49][46] = WorldStates.OCCUPIED;
		worldBlocks[49][47] = WorldStates.OCCUPIED;
		worldBlocks[49][48] = WorldStates.OCCUPIED;
		worldBlocks[49][49] = WorldStates.OCCUPIED;
	}

}
