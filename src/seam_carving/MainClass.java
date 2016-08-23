/**
 * 
 */
package seam_carving;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * @author conanz
 *
 * Main class to run seam carving onto a given picture.
 * 
 * !!!IMPORTANT!!!IMPORTANT !!!IMPORTANT!!!IMPORTANT !!!IMPORTANT!!!IMPORTANT !!!IMPORTANT!!!IMPORTANT !!!IMPORTANT!!!IMPORTANT !!!IMPORTANT!!!IMPORTANT !!!IMPORTANT!!!IMPORTANT!!!
 * Parameters are given through the command line where:
 * 1. args[0] = input file name
 *      e.g. FileName.jpg
 *      
 * 2. args[1] = output file name
 *      e.g. FileNameOutput.jpg
 *      
 * 3. args[2] = pixels to reduce WIDTH by
 *      e.g. 100
 * 
 * 4. args[3] = pixels to reduce HEIGHT by
 *      e.g. 100
 */
public class MainClass
{
    /**MAIN METHOD TO RUN SEAMCARVING**/
    public static void main(String args[])
    {
      //Create JFrame window to load class
        JFrame f = new JFrame("Load Image Sample");
        
        //Add exit button to JFrame
        f.addWindowListener( new WindowAdapter()
            {
                public void windowClosing(WindowEvent e) 
                {
                    System.exit(0);  
                }
            }
        );
        //Call seam carving class and enter parameters IN COMMAND LINE
        f.add(new SeamCarving(args[0], args[1], Integer.parseInt(args[2]),Integer.parseInt(args[3]) ) );
        f.pack();
        f.setVisible(true);
        
        //Set size of JFrame
        f.setSize(1500, 750);
    }
}
