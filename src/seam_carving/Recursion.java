/**
 * 
 */
package seam_carving;

import java.util.Arrays;

/**
 * @author conanz
 *
 * Class for Written Assignment to test recursive algorithms.
 */
public class Recursion
{
    /**MAIN METHOD TO TEST FUNCTIONS**/
    public static void main(String args[])
    {
        //Create array of numbers and print
        double[] testArray = {1,2,6,0,2,8,-1,5.5,3,600};
        for(int i=0;i<testArray.length;i++)
        {
            System.out.println( testArray[i] );
        }
        
        //Test function and print
        double max = maximum(testArray);
        System.out.println("Maximum: " + max);
    }
    
    /**RECURSIVE FUNCTION TO RETURN MAXIMUM ELEMENT IN AN ARRAY**/
    static double maximum(double[] array)
    {
        int start = 0;//create variable for an index starting point (defaults to first number in current array)
        double begin = array[start];//have a beginning element to start with
        
        /**Compound Interest**/
        //create an array of the rest of the elements (excluding the first number of the current array)
        double[] arrayRest = Arrays.copyOfRange(array, start+1, array.length);        

        /**Base Case**/
        if(arrayRest.length == 0)
        {
            //the array has been cut up until there aren't any more elements to check
            return begin;
        }
        
        /**Recursive Call**/
        double max = maximum(arrayRest);
        
        //comparison to find actual maximum (creates a huge call stack of comparing begins with max's of arrayRest
        if(begin > max)
        {
            //the beginning element may be the actual maximum so compare it to what the rest of the array's "maximum" is
            return begin;
        }
        else
        {
            //if max is bigger, just return the max
            return max;
        }
    }
}
