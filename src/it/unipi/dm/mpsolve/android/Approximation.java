package it.unipi.dm.mpsolve.android;

/**
 * @brief Wrapper around mps_approximation that allows the Java interface
 * to manipulate the approximations. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 */
public class Approximation {
	
	public String valueRepresentation;
	public String radiusRepresentation;
	
	public double realValue;
	public double imagValue;
	
	public double radius;
	
	public String status;
	
}
