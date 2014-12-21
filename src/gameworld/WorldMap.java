package gameworld;

import java.awt.Point;
import java.awt.geom.AffineTransform;

import ai.Ai;

public class WorldMap {

	public static final int VIEW_SIZE = 20;
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
		worldBlocks[0][1] = WorldStates.OCCUPIED;
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
		worldBlocks[1][22] = WorldStates.OCCUPIED;
		worldBlocks[1][23] = WorldStates.OCCUPIED;
		worldBlocks[1][24] = WorldStates.OCCUPIED;
		worldBlocks[1][25] = WorldStates.OCCUPIED;
		worldBlocks[1][26] = WorldStates.OCCUPIED;
		worldBlocks[2][0] = WorldStates.OCCUPIED;
		worldBlocks[2][22] = WorldStates.OCCUPIED;
		worldBlocks[2][23] = WorldStates.OCCUPIED;
		worldBlocks[2][24] = WorldStates.OCCUPIED;
		worldBlocks[2][25] = WorldStates.OCCUPIED;
		worldBlocks[2][26] = WorldStates.OCCUPIED;
		worldBlocks[2][49] = WorldStates.OCCUPIED;
		worldBlocks[3][0] = WorldStates.OCCUPIED;
		worldBlocks[3][22] = WorldStates.OCCUPIED;
		worldBlocks[3][23] = WorldStates.OCCUPIED;
		worldBlocks[3][24] = WorldStates.OCCUPIED;
		worldBlocks[3][25] = WorldStates.OCCUPIED;
		worldBlocks[3][26] = WorldStates.OCCUPIED;
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
		worldBlocks[9][8] = WorldStates.OCCUPIED;
		worldBlocks[9][9] = WorldStates.OCCUPIED;
		worldBlocks[9][10] = WorldStates.OCCUPIED;
		worldBlocks[9][11] = WorldStates.OCCUPIED;
		worldBlocks[9][12] = WorldStates.OCCUPIED;
		worldBlocks[9][37] = WorldStates.OCCUPIED;
		worldBlocks[9][38] = WorldStates.OCCUPIED;
		worldBlocks[9][39] = WorldStates.OCCUPIED;
		worldBlocks[9][40] = WorldStates.OCCUPIED;
		worldBlocks[9][41] = WorldStates.OCCUPIED;
		worldBlocks[9][49] = WorldStates.OCCUPIED;
		worldBlocks[10][0] = WorldStates.OCCUPIED;
		worldBlocks[10][8] = WorldStates.OCCUPIED;
		worldBlocks[10][9] = WorldStates.OCCUPIED;
		worldBlocks[10][10] = WorldStates.OCCUPIED;
		worldBlocks[10][11] = WorldStates.OCCUPIED;
		worldBlocks[10][12] = WorldStates.OCCUPIED;
		worldBlocks[10][37] = WorldStates.OCCUPIED;
		worldBlocks[10][38] = WorldStates.OCCUPIED;
		worldBlocks[10][39] = WorldStates.OCCUPIED;
		worldBlocks[10][40] = WorldStates.OCCUPIED;
		worldBlocks[10][41] = WorldStates.OCCUPIED;
		worldBlocks[10][49] = WorldStates.OCCUPIED;
		worldBlocks[11][0] = WorldStates.OCCUPIED;
		worldBlocks[11][8] = WorldStates.OCCUPIED;
		worldBlocks[11][9] = WorldStates.OCCUPIED;
		worldBlocks[11][10] = WorldStates.OCCUPIED;
		worldBlocks[11][11] = WorldStates.OCCUPIED;
		worldBlocks[11][12] = WorldStates.OCCUPIED;
		worldBlocks[11][37] = WorldStates.OCCUPIED;
		worldBlocks[11][38] = WorldStates.OCCUPIED;
		worldBlocks[11][39] = WorldStates.OCCUPIED;
		worldBlocks[11][40] = WorldStates.OCCUPIED;
		worldBlocks[11][41] = WorldStates.OCCUPIED;
		worldBlocks[11][49] = WorldStates.OCCUPIED;
		worldBlocks[12][0] = WorldStates.OCCUPIED;
		worldBlocks[12][8] = WorldStates.OCCUPIED;
		worldBlocks[12][9] = WorldStates.OCCUPIED;
		worldBlocks[12][10] = WorldStates.OCCUPIED;
		worldBlocks[12][11] = WorldStates.OCCUPIED;
		worldBlocks[12][12] = WorldStates.OCCUPIED;
		worldBlocks[12][37] = WorldStates.OCCUPIED;
		worldBlocks[12][38] = WorldStates.OCCUPIED;
		worldBlocks[12][39] = WorldStates.OCCUPIED;
		worldBlocks[12][40] = WorldStates.OCCUPIED;
		worldBlocks[12][41] = WorldStates.OCCUPIED;
		worldBlocks[12][49] = WorldStates.OCCUPIED;
		worldBlocks[13][0] = WorldStates.OCCUPIED;
		worldBlocks[13][8] = WorldStates.OCCUPIED;
		worldBlocks[13][9] = WorldStates.OCCUPIED;
		worldBlocks[13][10] = WorldStates.OCCUPIED;
		worldBlocks[13][11] = WorldStates.OCCUPIED;
		worldBlocks[13][12] = WorldStates.OCCUPIED;
		worldBlocks[13][37] = WorldStates.OCCUPIED;
		worldBlocks[13][38] = WorldStates.OCCUPIED;
		worldBlocks[13][39] = WorldStates.OCCUPIED;
		worldBlocks[13][40] = WorldStates.OCCUPIED;
		worldBlocks[13][41] = WorldStates.OCCUPIED;
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
		worldBlocks[22][22] = WorldStates.OCCUPIED;
		worldBlocks[22][23] = WorldStates.OCCUPIED;
		worldBlocks[22][24] = WorldStates.OCCUPIED;
		worldBlocks[22][25] = WorldStates.OCCUPIED;
		worldBlocks[22][26] = WorldStates.OCCUPIED;
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
		worldBlocks[36][8] = WorldStates.OCCUPIED;
		worldBlocks[36][9] = WorldStates.OCCUPIED;
		worldBlocks[36][10] = WorldStates.OCCUPIED;
		worldBlocks[36][11] = WorldStates.OCCUPIED;
		worldBlocks[36][12] = WorldStates.OCCUPIED;
		worldBlocks[36][37] = WorldStates.OCCUPIED;
		worldBlocks[36][38] = WorldStates.OCCUPIED;
		worldBlocks[36][39] = WorldStates.OCCUPIED;
		worldBlocks[36][40] = WorldStates.OCCUPIED;
		worldBlocks[36][41] = WorldStates.OCCUPIED;
		worldBlocks[36][49] = WorldStates.OCCUPIED;
		worldBlocks[37][0] = WorldStates.OCCUPIED;
		worldBlocks[37][8] = WorldStates.OCCUPIED;
		worldBlocks[37][9] = WorldStates.OCCUPIED;
		worldBlocks[37][10] = WorldStates.OCCUPIED;
		worldBlocks[37][11] = WorldStates.OCCUPIED;
		worldBlocks[37][12] = WorldStates.OCCUPIED;
		worldBlocks[37][37] = WorldStates.OCCUPIED;
		worldBlocks[37][38] = WorldStates.OCCUPIED;
		worldBlocks[37][39] = WorldStates.OCCUPIED;
		worldBlocks[37][40] = WorldStates.OCCUPIED;
		worldBlocks[37][41] = WorldStates.OCCUPIED;
		worldBlocks[37][49] = WorldStates.OCCUPIED;
		worldBlocks[38][0] = WorldStates.OCCUPIED;
		worldBlocks[38][8] = WorldStates.OCCUPIED;
		worldBlocks[38][9] = WorldStates.OCCUPIED;
		worldBlocks[38][10] = WorldStates.OCCUPIED;
		worldBlocks[38][11] = WorldStates.OCCUPIED;
		worldBlocks[38][12] = WorldStates.OCCUPIED;
		worldBlocks[38][37] = WorldStates.OCCUPIED;
		worldBlocks[38][38] = WorldStates.OCCUPIED;
		worldBlocks[38][39] = WorldStates.OCCUPIED;
		worldBlocks[38][40] = WorldStates.OCCUPIED;
		worldBlocks[38][41] = WorldStates.OCCUPIED;
		worldBlocks[38][49] = WorldStates.OCCUPIED;
		worldBlocks[39][0] = WorldStates.OCCUPIED;
		worldBlocks[39][8] = WorldStates.OCCUPIED;
		worldBlocks[39][9] = WorldStates.OCCUPIED;
		worldBlocks[39][10] = WorldStates.OCCUPIED;
		worldBlocks[39][11] = WorldStates.OCCUPIED;
		worldBlocks[39][12] = WorldStates.OCCUPIED;
		worldBlocks[39][37] = WorldStates.OCCUPIED;
		worldBlocks[39][38] = WorldStates.OCCUPIED;
		worldBlocks[39][39] = WorldStates.OCCUPIED;
		worldBlocks[39][40] = WorldStates.OCCUPIED;
		worldBlocks[39][41] = WorldStates.OCCUPIED;
		worldBlocks[39][49] = WorldStates.OCCUPIED;
		worldBlocks[40][0] = WorldStates.OCCUPIED;
		worldBlocks[40][8] = WorldStates.OCCUPIED;
		worldBlocks[40][9] = WorldStates.OCCUPIED;
		worldBlocks[40][10] = WorldStates.OCCUPIED;
		worldBlocks[40][11] = WorldStates.OCCUPIED;
		worldBlocks[40][12] = WorldStates.OCCUPIED;
		worldBlocks[40][37] = WorldStates.OCCUPIED;
		worldBlocks[40][38] = WorldStates.OCCUPIED;
		worldBlocks[40][39] = WorldStates.OCCUPIED;
		worldBlocks[40][40] = WorldStates.OCCUPIED;
		worldBlocks[40][41] = WorldStates.OCCUPIED;
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
		worldBlocks[46][22] = WorldStates.OCCUPIED;
		worldBlocks[46][23] = WorldStates.OCCUPIED;
		worldBlocks[46][24] = WorldStates.OCCUPIED;
		worldBlocks[46][25] = WorldStates.OCCUPIED;
		worldBlocks[46][26] = WorldStates.OCCUPIED;
		worldBlocks[46][49] = WorldStates.OCCUPIED;
		worldBlocks[47][0] = WorldStates.OCCUPIED;
		worldBlocks[47][22] = WorldStates.OCCUPIED;
		worldBlocks[47][23] = WorldStates.OCCUPIED;
		worldBlocks[47][24] = WorldStates.OCCUPIED;
		worldBlocks[47][25] = WorldStates.OCCUPIED;
		worldBlocks[47][26] = WorldStates.OCCUPIED;
		worldBlocks[47][49] = WorldStates.OCCUPIED;
		worldBlocks[48][0] = WorldStates.OCCUPIED;
		worldBlocks[48][22] = WorldStates.OCCUPIED;
		worldBlocks[48][23] = WorldStates.OCCUPIED;
		worldBlocks[48][24] = WorldStates.OCCUPIED;
		worldBlocks[48][25] = WorldStates.OCCUPIED;
		worldBlocks[48][26] = WorldStates.OCCUPIED;
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
