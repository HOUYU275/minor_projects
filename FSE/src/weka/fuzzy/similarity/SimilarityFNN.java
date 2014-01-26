/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    EuclideanDistance.java
 *    Copyright (C) 1999-2007 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.fuzzy.similarity;

import weka.core.*;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;

/**
 <!-- globalinfo-start -->
 *  Similarity measure: Fuzzy NN
 *
 * 
 <!-- globalinfo-end -->
 *

 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre> -D
 *  Turns off the normalization of attribute 
 *  values in distance calculation.</pre>
 * 
 * <pre> -R &lt;col1,col2-col4,...&gt;
 *  Specifies list of columns to used in the calculation of the 
 *  distance. 'first' and 'last' are valid indices.
 *  (default: first-last)</pre>
 * 
 * <pre> -V
 *  Invert matching sense of column indices.</pre>
 *  
 * <pre> -T
 *  determines the t-norm to use </pre>
 *  
 * <pre> -C
 *  determines the cutoff value to use </pre>
 * 
 <!-- options-end --> 
 *
 */
public class SimilarityFNN
extends Similarity
implements Cloneable, TechnicalInformationHandler {

	/** for serialization. */
	private static final long serialVersionUID = 1068606253458807903L;



	/**
	 * Constructs Similarity based Distance object, Instances must be still set.
	 */
	public SimilarityFNN() {
		super();
	}

	/**
	 * Constructs an Similarity based Distance object and automatically initializes the
	 * ranges.
	 * 
	 * @param data 	the instances the distance function should work on
	 */
	public SimilarityFNN(Instances data) {
		super(data);
	}

	/**
	 * Returns a string describing this object.
	 * 
	 * @return 		a description of the evaluator suitable for
	 * 			displaying in the explorer/experimenter gui
	 */
	public String globalInfo() {
		return 
		"The similarity measure is: " 
		+ "Math.pow((first - second),2).";
	}

	public String toString() {
		return "Math.pow((first - second),2).";
	}

	/**
	 * Returns an instance of a TechnicalInformation object, containing 
	 * detailed information about the technical background of this class,
	 * e.g., paper reference or book this class is based on.
	 * 
	 * @return 		the technical information about this class
	 */
	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation 	result;

		result = new TechnicalInformation(Type.MISC);
		//to be filled in
		return result;
	}


	/**
	 * similarity function calculates similarity between 2 attribute values
	 * 
	 * @param index   the index of the attribute for which the similarity is calculated
	 * @param first 	the value of the attribute of the first instance
	 * @param second 	the value of the attribute of the second instance
	 * 
	 * @return 	a measurement of similarity
	 * 
	 * 
	 */
	public double similarity(int index, double first, double second){

		return Math.pow((first - second),2);
	}

//	Interval-valued version
	public double[] similarity(int index, double first, double second, double param){
		double[] ret = new double[2];

		ret[0] = param*Math.pow((first - second),2);
		ret[1] = Math.pow((first - second),2);

		return ret;
	}


}
