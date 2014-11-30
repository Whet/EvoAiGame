/*
 * 
 */
package utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphicsFunctions.
 */
public class GraphicsFunctions {
	
	//private static final BasicStroke LINE_STROKE = new BasicStroke(1.5f);
	/**
	 * The Constant LINE_GAP.
	 */
	private static final int LINE_GAP = 40;
	
	/**
	 * The Constant LINE_LENGTH.
	 */
	private static final int LINE_LENGTH = 40;
	
	//private static final BasicStroke ARROW_STROKE = new BasicStroke(1.5f);
	/**
	 * The Constant ARROW_ANGLE.
	 */
	private static final double ARROW_ANGLE = Math.PI * 1.2;
	
	/**
	 * The Constant ARROW_LENGTH.
	 */
	private static final int ARROW_LENGTH = 8;
	
	/**
	 * The Constant ARROW_GAP.
	 */
	private static final int ARROW_GAP = 1;
	
	/**
	 * The arrow gap count.
	 */
	private static int arrowGapCount = 0;
	
	/**
	 * The reached.
	 */
	private static boolean reached = false;
	
	/**
	 * The angle.
	 */
	private static double angle = 0;
	// like the point of a pencil, where the line is being drawn
	/**
	 * The line pos.
	 */
	private static double[] linePos = new double[2];
	
	/**
	 * Draw arrow line.
	 *
	 * @param drawShape the draw shape
	 * @param start the start
	 * @param end the end
	 * @param colour the colour
	 * @param alpha the alpha
	 */
	public static void drawArrowLine(Graphics2D drawShape, double[] start, double[] end, Color colour, Float alpha){
		drawArrowLine(drawShape,start[0],start[1],end[0],end[1],colour,alpha);
	}
	
	/**
	 * Draw arrow line.
	 *
	 * @param drawShape the draw shape
	 * @param startX the start x
	 * @param startY the start y
	 * @param endX the end x
	 * @param endY the end y
	 * @param colour the colour
	 * @param alpha the alpha
	 */
	public static void drawArrowLine(Graphics2D drawShape, double startX, double startY, double endX, double endY, Color colour, Float alpha){
		
		// set colour
		drawShape.setColor(colour);
		
		// set arrow alpha
		drawShape.setComposite(makeComposite(alpha));
		
		// reset draw vars
		arrowGapCount = 0;
		
		reached = false;
		
		angle = Maths.getRads(startX,startY, endX,endY);
		
		linePos[0] = startX;
		linePos[1] = startY;
		
		while(reached == false){
			
			// If drawing a line won't overshoot the target
			if(Maths.getDistance(linePos[0],linePos[1],endX,endY) > LINE_LENGTH){
				
				// Draw Line to the next position along to target
				drawShape.drawLine((int)(linePos[0]),
								   (int)(linePos[1]),
								   (int)(linePos[0] + Math.cos(angle) * LINE_LENGTH),
								   (int)(linePos[1] + Math.sin(angle) * LINE_LENGTH));
				
				// Update line position
				linePos[0] += Math.cos(angle) * LINE_LENGTH;
				linePos[1] += Math.sin(angle) * LINE_LENGTH;
				
				// Draw arrow if at correct position
				if(arrowGapCount == 0){
					drawArrow(drawShape);
				}
				
			}
			// draw line to reach target exactly
			else{
				
				// Draw Line to the ai target
				drawShape.drawLine((int) (linePos[0]),
								   (int) (linePos[1]),
								   (int) (endX),
								   (int) (endY));

				reached = true;
				
				linePos[0] = endX;
				linePos[1] = endY;
				
				drawArrow(drawShape);
				
				// reset alpha
				drawShape.setComposite(makeComposite(1f));
				return;
			}
			// if a gap can be fit in without overshooting target
			if(Maths.getDistance(linePos[0],linePos[1], endX, endY) > LINE_GAP){
				linePos[0] += Math.cos(angle) * LINE_LENGTH;
				linePos[1] += Math.sin(angle) * LINE_LENGTH;
				
				arrowGapCount++;
				if(arrowGapCount > ARROW_GAP){
					arrowGapCount = 0;
				}
			}
			else{
				reached = true;
				
				linePos[0] = endX;
				linePos[1] = endY;
				
				drawArrow(drawShape);
				
				// reset alpha
				drawShape.setComposite(makeComposite(1f));
				return;
			}
		}
		
		
	}
	
	// draws the arrow segment of a dashed line
	/**
	 * Draw arrow.
	 *
	 * @param drawShape the draw shape
	 */
	private static void drawArrow(Graphics2D drawShape){
		
	//	drawShape.setStroke(ARROW_STROKE);
		drawShape.drawLine((int) (linePos[0]),
						   (int) (linePos[1]),
						   (int)(linePos[0] + Math.cos(angle - ARROW_ANGLE) * ARROW_LENGTH), 
						   (int)(linePos[1] + Math.sin(angle - ARROW_ANGLE) * ARROW_LENGTH));
		
		drawShape.drawLine((int) (linePos[0]),
				   		   (int) (linePos[1]),
						   (int)(linePos[0] + Math.cos(angle + ARROW_ANGLE) * ARROW_LENGTH),
						   (int)(linePos[1] + Math.sin(angle + ARROW_ANGLE) * ARROW_LENGTH));
	}
	
	// http://www.informit.com/articles/article.aspx?p=26349&seqNum=5
	/**
	 * Make composite.
	 *
	 * @param alpha the alpha
	 * @return the alpha composite
	 */
	public static AlphaComposite makeComposite(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return(AlphaComposite.getInstance(type, alpha));
	}
	
}
