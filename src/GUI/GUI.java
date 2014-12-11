package GUI;

import gameworld.WorldMap;
import gameworld.WorldStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;

import utils.GraphicsFunctions;
import utils.Maths;
import ai.Ai;

public class GUI extends JFrame {

	private static final int DRAW_SCALE = 5;
	private static final int FOV_ARC_LENGTH = (int)(Ai.ATTACK_RANGE * DRAW_SCALE);
	
	private WorldMap map;
	final Ai ai1, ai2;
	
	private BufferStrategy bufferS;
	private Graphics2D dBuffer;
	
	public boolean drawGrid = false;

	private List<Bullet> bullets;
	
	public GUI(int generation, Ai ai1, Ai ai2, WorldMap map) {
		
		this.setTitle("BATTLE OF THE CHAMPIONS GENERATION " + generation);
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 32) {
					reset();
				}
			}
			
		});
		
		this.ai1 = ai1;
		this.ai2 = ai2;
		this.map = map;
		
		reset();
		
		map.setAi(ai1, ai2);
		
		final Timer aiTimer = new Timer(50, new ActionListener() {

			int timer = 0;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Ai ai1 = GUI.this.ai1;
				Ai ai2 = GUI.this.ai2;
				if(timer == 0) {
					ai1.applyRules(GUI.this.getWorldView(ai1));
					ai2.applyRules(GUI.this.getWorldView(ai2));
					timer = 2;
				}
				else {
					ai1.act();
					ai2.act();
					timer--;
				}
				
				// Update world
				double distance = Maths.getDistance(ai1.getX(), ai1.getY(), getMap().getStart2().x, getMap().getStart2().y);
				if(distance < 5 && !ai1.hasFlag()) {
					ai1.setHasFlag(true);
				}
				distance = Maths.getDistance(ai1.getX(), ai1.getY(), getMap().getStart1().x, getMap().getStart1().y);
				if(distance < 5 && ai1.hasFlag()) {
					ai1.setHasFlag(false);
				}
				
				if(ai1.isAttacking() && ai1.getRecharge() == 0)
					createBullet(ai1.getX() * DRAW_SCALE, ai1.getY() * DRAW_SCALE, ai1.getRotation());
				
				if(ai1.attack(ai2)) {
					ai2.hit(ai1.getDamage());
				}
				
				distance = Maths.getDistance(ai2.getX(), ai2.getY(), getMap().getStart1().x, getMap().getStart1().y);
				if(distance < 5 && !ai2.hasFlag()) {
					ai2.setHasFlag(true);
				}
				distance = Maths.getDistance(ai2.getX(), ai2.getY(), getMap().getStart2().x, getMap().getStart2().y);
				if(distance < 5 && ai2.hasFlag()) {
					ai2.setHasFlag(false);
				}
				
				if(ai2.isAttacking() && ai2.getRecharge() == 0)
					createBullet(ai2.getX() * DRAW_SCALE, ai2.getY() * DRAW_SCALE, ai2.getRotation());
				
				if(ai2.attack(ai1)) {
					ai1.hit(ai2.getDamage());
				}
			}
		});
		
		aiTimer.start();
		
		this.bullets = new ArrayList<Bullet>();
	}

	public void reset() {
		ai1.setSpawn(map.getStart1() , Math.PI / 2);
		ai1.respawn();
		
		ai2.setSpawn(map.getStart2(), -Math.PI/2);
		ai2.respawn();
	}
	
	protected WorldMap getMap() {
		return this.map;
	}

	protected int[][] getWorldView(Ai ai) {
		return map.getGridView(ai.getX(), ai.getY(), ai);
	}

	public void createBuffers() {
		this.createBufferStrategy(2);
		bufferS = this.getBufferStrategy();
	}

	@Override
	public void paint(Graphics g) {

		dBuffer = (Graphics2D) bufferS.getDrawGraphics();

		dBuffer.setColor(Color.white);
		dBuffer.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.setToTranslation(50, 50);
		dBuffer.setTransform(affineTransform);
		
		drawMap(dBuffer);

		drawAiView(dBuffer, ai1, Color.red);
		drawAiView(dBuffer, ai2, Color.blue);
		
		drawScores(dBuffer);
		
		drawBullets(dBuffer);
		
		bufferS.show();

		g.dispose();
		dBuffer.dispose();
		
		tickBullets();
	}

	private void drawBullets(Graphics g) {
		g.setColor(Color.white);
		
		Iterator<Bullet> iterator = this.bullets.iterator();
		
		while(iterator.hasNext()) {
			Bullet next = iterator.next();
			g.drawLine(next.location.x, next.location.y, next.location.x + next.xDelta, next.location.y + next.yDelta);
		}
		
	}

	private void drawScores(Graphics g) {
		
	}

	private void drawAiView(Graphics2D g, Ai ai, Color colour) {
		
		double xAi = ai.getX() * DRAW_SCALE;
		double yAi = ai.getY() * DRAW_SCALE;

		int x, y;

		if(drawGrid) {
			int spacing = DRAW_SCALE;
	
			double view = ai.getRotation() - Math.PI / 2;
			
			int[][] gridView = this.map.getGridView(xAi / DRAW_SCALE, yAi / DRAW_SCALE, ai);
			
			g.setComposite(GraphicsFunctions.makeComposite(0.5f));
			
			for (int i = 0; i < WorldMap.VIEW_SIZE; i++) {
				for (int j = 0; j < WorldMap.VIEW_SIZE; j++) {
	
					int xOffset = WorldMap.VIEW_SIZE / 2;
	
					x = (int) Math.round(i - xOffset);
					y = (int) Math.round(j);
	
					x *= spacing;
					y *= spacing;
	
					x += xAi;
					y += yAi;
	
					Point result = new Point();
					AffineTransform rotation = new AffineTransform();
					double angleInRadians = (view);
					rotation.rotate(angleInRadians, xAi, yAi);
					rotation.transform(new Point(x, y), result);
	
					if(gridView[i][j] == WorldStates.EMPTY.getCode())
						g.setColor(Color.white);
					else if(gridView[i][j] == WorldStates.FLAG.getCode())
						g.setColor(Color.green);
					else if(gridView[i][j] == WorldStates.ENEMY.getCode())
						g.setColor(Color.red);
					else
						g.setColor(Color.cyan);
					
					g.fillOval((int)(result.x - 5), (int)(result.y - 5), (int)10, (int)10);
	
				}
			}
			
			g.setComposite(GraphicsFunctions.makeComposite(1.0f));
		}
		
		g.setColor(colour);
		g.fillOval((int)xAi - 5, (int)yAi - 5, 10, 10);

		if(ai.hasFlag())
			drawFlag(g, ai, colour);
		
		if(ai.getRecharge() != 0) {
			g.setColor(Color.yellow);
		}
		
		drawFOV(g, ai);
		drawHealthbar(g, ai);
	}
	
	private void drawHealthbar(Graphics2D g, Ai ai) {
		g.setColor(Color.black);
		g.fillRect((int)ai.getX()*DRAW_SCALE - 10, (int)ai.getY()*DRAW_SCALE - 5, 20, 3);
		
		g.setColor(Color.green);
		if(ai.getHealth() > 0)
		g.fillRect((int)ai.getX()*DRAW_SCALE - 10, (int)ai.getY()*DRAW_SCALE - 5, 20 * (Ai.MAX_HEALTH / ai.getHealth()), 3);
	}

	private void drawFOV(Graphics2D g, Ai ai) {
		g.drawLine((int)ai.getX()*DRAW_SCALE, (int)ai.getY()*DRAW_SCALE,
				   (int)(ai.getX()*DRAW_SCALE + FOV_ARC_LENGTH * Math.cos(ai.getRotation() + Math.toRadians(Ai.ATTACK_FOV) * 0.5)),
				   (int)(ai.getY()*DRAW_SCALE + FOV_ARC_LENGTH * Math.sin(ai.getRotation() + Math.toRadians(Ai.ATTACK_FOV) * 0.5)));
		
		g.drawLine((int)ai.getX()*DRAW_SCALE, (int)ai.getY()*DRAW_SCALE,
				   (int)(ai.getX()*DRAW_SCALE + FOV_ARC_LENGTH * Math.cos(ai.getRotation() - Math.toRadians(Ai.ATTACK_FOV) * 0.5)),
				   (int)(ai.getY()*DRAW_SCALE + FOV_ARC_LENGTH * Math.sin(ai.getRotation() - Math.toRadians(Ai.ATTACK_FOV) * 0.5)));
	}
	
	private void drawFlag(Graphics2D g, Ai ai, Color flagColour) {
		g.setColor(flagColour);
		g.fillRect((int)ai.getX()*DRAW_SCALE, (int)ai.getY()*DRAW_SCALE, 1, 5);
		g.fillRect((int)ai.getX()*DRAW_SCALE, (int)ai.getY()*DRAW_SCALE - 5, 4, 4);
	}

	private void drawMap(Graphics2D g) {
		for (int i = 0; i < this.map.worldBlocks.length; i++) {
			for (int j = 0; j < map.worldBlocks[i].length; j++) {
				WorldStates worldState = map.worldBlocks[i][j];
				switch (worldState) {
				case EMPTY:
					g.setColor(Color.gray);
					break;
				case OCCUPIED:
					g.setColor(Color.black);
					break;
				case FLAG:
					g.setColor(Color.white);
					break;
				}
				g.fillRect(i * DRAW_SCALE - DRAW_SCALE / 2, j * DRAW_SCALE - DRAW_SCALE / 2, DRAW_SCALE, DRAW_SCALE);
			}
		}
		
		// Draw the flags
		g.setComposite(GraphicsFunctions.makeComposite(0.4f));
		g.setColor(Color.red);
		g.fillRect((int)(map.getStart1().x * DRAW_SCALE - (2.5 * DRAW_SCALE)), (int)(map.getStart1().y * DRAW_SCALE - (2.5 * DRAW_SCALE)), 5 * DRAW_SCALE, 5 * DRAW_SCALE);
		
		g.setColor(Color.blue);
		g.fillRect((int)(map.getStart2().x * DRAW_SCALE - (2.5 * DRAW_SCALE)), (int)(map.getStart2().y * DRAW_SCALE - (2.5 * DRAW_SCALE)), 5 * DRAW_SCALE, 5 * DRAW_SCALE);
		g.setComposite(GraphicsFunctions.makeComposite(1f));
	}
	
	private void createBullet(double x, double y, double rotation) {
		this.bullets.add(new Bullet(new Point((int)x, (int)y), rotation));
	}
	
	private void tickBullets() {
		Iterator<Bullet> iterator = this.bullets.iterator();
		
		while(iterator.hasNext()) {
			Bullet next = iterator.next();
			
			next.tick();
			if(next.life < 0)
				iterator.remove();
		}
	}
	
	private static class Bullet {
		
		private Point location;
		public double speed, life;
		private int xDelta, yDelta;
		
		public Bullet(Point location, Double angle) {
			this.speed = 15;
			this.life = 5;
			
			this.location = new Point(location);
			
			xDelta = (int) (Math.cos(angle) * speed);
			yDelta = (int) (Math.sin(angle) * speed);
			
		}
		
		public void tick() {
			this.location.translate(xDelta, yDelta);
			this.life--;
		}
		
	}

}
