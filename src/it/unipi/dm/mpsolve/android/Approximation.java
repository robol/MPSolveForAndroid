package it.unipi.dm.mpsolve.android;

/**
 * @brief Wrapper around mps_approximation that allows the Java interface
 * to manipulate the approximations. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 */
public class Approximation {
	
	/**
	 * @brief A String holding a human-readable representation of the Approximation. 
	 * 
	 * This approximation will have a number of digits as required by the digits
	 * parameter set in the PolynomialSolver. 
	 */
	public String valueRepresentation;
	
	/**
	 * @brief A String holding a human-readable representation of the inclusion radius
	 * for this Approximation. 
	 */
	public String radiusRepresentation;
	
	/**
	 * @brief double version of the Approximation. It is provided to offer a convenient
	 * access to a (possibly) lower precision version of the value.
	 *  
	 * This field holds the real part of the Approximation. 
	 */
	public double realValue;
	
	/**
	 * @brief double version of the Approximation. It is provided to offer a convenient
	 * access to a (possibly) lower precision version of the value.
	 *  
	 * This field holds the imaginary part of the Approximation. 
	 */
	public double imagValue;
	
	/**
	 * @brief double version of the inclusion radius for the Approximation. 
	 */
	public double radius;
	
	/**
	 * @brief A String representation of the status of the approximaition. In the current
	 * version of MPSolve this can be one of "Isolated", "Approximated", "Clustered", 
	 * "Approximated in a cluster". 
	 */
	public String status;
	
}
