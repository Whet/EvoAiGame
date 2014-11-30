/*
 * 
 */
package utils;


// TODO: Auto-generated Javadoc
// Mathematical functions in addition to Java's Math ones
/**
 * The Class Maths.
 */
public class Maths {
	
	// gets distance between two positions
	/**
	 * Gets the distance.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the distance
	 */
	public static double getDistance(double[] pos1, double[] pos2){
		return Math.sqrt( Math.pow( (pos1[0] - pos2[0]) ,2) + Math.pow( (pos1[1] - pos2[1]) ,2) );
	}
	
	/**
	 * Gets the distance.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the distance
	 */
	public static double getDistance(int[] pos1, int[] pos2) {
		return Math.sqrt( Math.pow( (pos1[0] - pos2[0]) ,2) + Math.pow( (pos1[1] - pos2[1]) ,2) );
	}
	
	/**
	 * Gets the distance.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the distance
	 */
	public static double getDistance(int[] pos1, double[] pos2) {
		return Math.sqrt( Math.pow( (pos1[0] - pos2[0]) ,2) + Math.pow( (pos1[1] - pos2[1]) ,2) );
	}
	
	/**
	 * Gets the distance.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @return the distance
	 */
	public static double getDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt( Math.pow( (x1 - x2) ,2) + Math.pow( (y1 - y2) ,2) );
	}
	
	// gets angle between two positions in radians
	/**
	 * Gets the rads.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the rads
	 */
	public static double getRads(double[] pos1, double[] pos2){
		return Math.atan2(pos2[1] - pos1[1], pos2[0] - pos1[0]);
	}
	
	/**
	 * Gets the rads.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the rads
	 */
	public static double getRads(double[] pos1, int[] pos2) {
		return Math.atan2(pos2[1] - pos1[1], pos2[0] - pos1[0]);
	}
	
	/**
	 * Gets the rads.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the rads
	 */
	public static double getRads(int[] pos1, double[] pos2) {
		return Math.atan2(pos2[1] - pos1[1], pos2[0] - pos1[0]);
	}
	
	/**
	 * Gets the rads.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the rads
	 */
	public static double getRads(int[] pos1, int[] pos2) {
		return Math.atan2(pos2[1] - pos1[1], pos2[0] - pos1[0]);
	}
	
	/**
	 * Gets the rads.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @return the rads
	 */
	public static double getRads(double x1, double y1, double x2, double y2){
		return Math.atan2(y2 - y1, x2 - x1);
	}
	
	// gets angle between two positions in radians
	/**
	 * Gets the degrees.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the degrees
	 */
	public static double getDegrees(double[] pos1, double[] pos2){
		return Math.toDegrees(getRads(pos1,pos2));
	}
	
	/**
	 * Gets the degrees.
	 *
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * @return the degrees
	 */
	public static double getDegrees(double[] pos1, int[] pos2) {
		return Math.toDegrees(getRads(pos1,pos2));
	}
	
	/**
	 * Gets the degrees.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @return the degrees
	 */
	public static double getDegrees(double x1, double y1, double x2, double y2){
		return Math.toDegrees(getRads(x1,y1,x2,y2));
	}
	
	public static double getDegrees(int[] pos1, int[] pos2) {
		return Math.toDegrees(getRads(pos1, pos2));
	}
	
	// gets the difference in two angles in degrees
	/**
	 * Angle difference.
	 *
	 * @param angle1 the angle1
	 * @param angle2 the angle2
	 * @return the double
	 */
	public static double angleDifference(double angle1, double angle2){

		double difference = Math.abs(angle1 - angle2) % 360;

		if(difference > 180){
			difference = 360 - difference;
		}

	    return difference;
	}
	
	public static double angleDifferenceOneWay(double angle1, double angle2){

		double difference = Math.abs(angle1 - angle2) % 360;

	    return difference;
	}
	
	// Randomly returns a positive or negative int
	/**
	 * POM.
	 *
	 * @return the int
	 */
	public static int POM(){
		
		if(Math.random() > 0.5){
			return 1;
		}
		else{
			return -1;
		}
	}
	
	// Crops a value to be between two values if it exceeds the boundaries
	/**
	 * Crop.
	 *
	 * @param input the input
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @return the double
	 */
	public static double crop(double input, double lowerBound, double upperBound){
		if(input > upperBound){
			input = upperBound;
		}
		if(input < lowerBound){
			input = lowerBound;
		}
		return input;
	}
	
	// returns boolean if a value is within a range of target value
	/**
	 * Estimate.
	 *
	 * @param trueValue the true value
	 * @param targetValue the target value
	 * @param errorMargin the error margin
	 * @return true, if successful
	 */
	public static boolean estimate(double trueValue, double targetValue, double errorMargin){
		if(targetValue + errorMargin >= trueValue && targetValue - errorMargin <= trueValue){
			return true;
		}
		return false;
	}
	
	// makes a value positive
	/**
	 * Mod.
	 *
	 * @param number the number
	 * @return the double
	 */
	public static double mod(double number){
		return Math.sqrt(number * number);
	}
	
	// return if object is in array
	/**
	 * Contains.
	 *
	 * @param object the object
	 * @param array the array
	 * @return true, if successful
	 */
	public static boolean contains(Object object, Object[] array){
		for(int i = 0; i < array.length; i++){
			if(array[i] == object){
				return true;
			}
		}
		return false;
	}

}
