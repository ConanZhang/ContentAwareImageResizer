/**
 * 
 */
package seam_carving;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @author conanz
 *
 */
public class SeamCarving extends Component
{
    BufferedImage imgActual;
    BufferedImage reference;
    boolean changeImageCalled;//boolean hack to only call changeImage() function once
    int counterHorizontal;
    int counterVertical;
    int height;
    int width;
    String inputFile;
    String outFile;


    /**CONSTRUCTOR**/
    public SeamCarving(String inputFileParameter,String outputFileParameter, int widthParameter, int heightParameter) 
    {
       height = heightParameter;
       width = widthParameter;
       inputFile = inputFileParameter;
       outFile = outputFileParameter;
       //Assign values to class member variables
       changeImageCalled = false;
       
       try 
       {
//           loadImage();//call to bring user to a "select file" window
           //Assign a specified image file
           imgActual = ImageIO.read( new File(inputFile) );
           reference = imgActual;
//           img2 = ImageIO.read( new File("Balloons.jpg") );
       } 
       catch (IOException e) 
       {
           //File doesn't exist where specified, don't crash program and print warning
           System.out.println("File not found");
       }
    
    }
    
    /**FUNCTION TO DRAW IMAGE ONTO JFRAME**/
    public void paint(Graphics g) 
    {
        if(counterHorizontal < height)
        {

                seamHorizontalCarve(reference);
                counterHorizontal++;

        }
        
        if(counterVertical < width)
        {

                seamVerticalCarve(reference);
                counterVertical++;

        }
        //draw image 2 (altered image)
                
        repaint();//paint image every frame

        g.drawImage(reference, 0, 0, null);
        if(counterHorizontal == height && counterVertical == width)
        {
              try
              {
                  File outputfile = new File(outFile);
                  ImageIO.write(reference, "jpg", outputfile);
              }
              catch(IOException d)
              {
                  //File doesn't exist where specified, don't crash program and print warning
                  System.out.println("File not found");
              }
        }
    }
    
    public void seamHorizontalCarve(BufferedImage img)
    {
        BufferedImage gradImage = GradientCalculation.calculateGradient(img);
        
        /**VERTICAL SEAM**/
 
        //set matrix size
        int rows = gradImage.getWidth();
        int cols = gradImage.getHeight();
        
        /**--------------------------------COST--------------------------------**/
        //create a matrix of matrix size that will contain pixel cost (integers)
        int cost[][] = new int[rows][cols];
    
        // fill matrix for every row
        for(int row = 0; row < rows; row++)
        {
            //fill matrix for every column
            for(int col = 0; col < cols; col++)
            {
                cost[row][col] = (new Color(gradImage.getRGB(row, col)).getRed() ) ;//randomize cost values
            }
        }
        
        /**----------------------------END COST--------------------------------**/
    
        /**--------------------------TOTAL COST--------------------------------**/
        
        //create matrix for total cost
        int totalCost[][] = new int[rows][cols];
        
        //starting row of totalCost shall be equal to cost
        for(int col = 0; col < cols; col++)
        {
            totalCost[0][col] = cost[0][col];
        }
        
        //!!!SKIP FIRST ROW!!!; calculate total cost values based on cost values
        for(int row = 1; row < rows;row++)
        {
            //!!!SPECIAL EDGE CASE FIRST COLUMN!!! total cost will be cost plus the minimum of the two values above it
            //-------------------------------------------directly above------diagonal above to the right
            totalCost[row][0] = cost[row][0] + Math.min(totalCost[row-1][0], totalCost[row-1][1]);
            
            //!!!SPECIAL EDGE CASE LAST COLUMN!!! total cost will be cost plus the minimum of the two values above it
            //-----------------------------------------------------------directly above----------diagonal above to the left
            totalCost[row][cols-1] = cost[row][cols - 1] + Math.min(totalCost[row-1][cols-1], totalCost[row-1][cols-2]);
            
            //values in between first and last
            for(int col = 1; col< cols -1; col++)
            {
                //total cost will be cost plus the minimum of three values
                //calculate minimum of "diagonal above to the left" and "diagonal above to the right" first then that minimum to "directly above"
                totalCost[row][col] = cost[row][col] + 
                        Math.min(totalCost[row-1][col], //directly above
                                 Math.min(totalCost[row-1][col-1], //diagonal above  to the left
                                          totalCost[row-1][col+1]) );//diagonal above to the right
            }
            
        }
        
        /**----------------------END TOTAL COST--------------------------------**/
        
        /**--------------------------------PATH--------------------------------**/
    
        //create array for path, size is only as long as matrix rows
        int path[] = new int[rows];
        
        //variable to start search for lowest total cost pixel's index on bottom row
        int minCostIndex = 0;
        
        for(int col = 1; col < cols; col++)
        {
            //if next element is less than the current minimum
            if(totalCost[rows-1][col] < totalCost[rows-1][minCostIndex])
            {
                minCostIndex = col;//set new minimum's index
            }
        }
                    
        //reference variable that will START at the lowest total cost pixel's index, but will be changed
        int column = minCostIndex;
        
        //!!!STARTS AT LOWEST TOTAL COST PIXEL'S INDEX ON BOTTOM ROW!!!; begin on bottom row going up
        for(int row = rows-1; row >= 0; row--)
        {
            int tempCol = column;//temporary reference variable that starts on the lowest cost pixel
            
            //if not last column, check if value to the right is less than current
            if(column < cols - 1 && totalCost[row][column+1] < totalCost[row][column])
            {
                tempCol = column + 1;//set current to right value
            }
            
            //if not first column, check if value to the left is less than current
            if(column > 0 && totalCost[row][column-1] < totalCost[row][tempCol])
            {
                tempCol = column - 1;//set current to left value
            }
            
            //otherwise remain on col
            column = tempCol;//set col to what tempCol has become after finding the minimum
            
            path[row] = column;//set value in array
        }
        
        /**----------------------------END PATH--------------------------------**/
        
        /**----------------------------MAKE NEW IMAGE BASED ON SEAM--------------------------------**/
        
        //Make a new image of the same size but one less column that is a matrix of RGB integers
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight()-1, BufferedImage.TYPE_INT_RGB);
        
        //Loop through image
        for(int i = 0; i < img.getWidth(); i++)
        {
            //Up to less than the path
            for(int j = 0; j < path[i]; j++)
            {
                //copy
                newImage.setRGB(i,j, img.getRGB(i, j));
            }
            //After the path
            for(int j = path[i] + 1; j < img.getHeight(); j++)
            {
                //copy
                newImage.setRGB(i,j-1, img.getRGB(i, j));
            }
        }
        
        reference =  newImage;
    /**----------------------------MAKE NEW IMAGE END----------------------------**/

        
//            /**Print Values**/
//            //COST
//            System.out.println("|--COST--|");
//            
//            for(int row = 0; row < rows; row++)
//            {
//                for(int col = 0; col<cols; col++)
//                {
//                    System.out.print(" " + cost[row][col] + " ");//print all col values in one row separated by a space
//                }
//                System.out.println();//break to a new line to show matrix/table of values
//            }
//            
//            //TOTAL COST
//            System.out.println("|--TOTAL COST--|");
//            
//            for(int row = 0; row < rows; row++)
//            {
//                for(int col = 0; col<cols; col++)
//                {
//                    System.out.print(" " + totalCost[row][col] + " ");//print all col values in one row separated by a space
//                }
//                System.out.println();//break to a new line to show matrix/table of values
//            }
//            
//            //PATH
//            System.out.println("|--PATH--|");
//            
//            for(int row = 0; row < rows; row++)
//            {
//                System.out.println("    " + path[row] + "    ");
//            }
        }
       
    public void seamVerticalCarve(BufferedImage img)
    {
        BufferedImage gradImage = GradientCalculation.calculateGradient(img);

        /**HORIZONTAL SEAM**/
        //set matrix size
        int rows = gradImage.getWidth();
        int cols = gradImage.getHeight();
        
        /**--------------------------------COST--------------------------------**/
        //create a matrix of matrix size that will contain pixel cost (integers)
        int cost[][] = new int[rows][cols];

        // fill matrix for every row
        for(int row = 0; row < rows; row++)
        {
            //fill matrix for every column
            for(int col = 0; col < cols; col++)
            {
                cost[row][col] = (new Color(gradImage.getRGB(row, col)).getRed() );//randomize cost values
            }
        }
        
        /**----------------------------END COST--------------------------------**/

        /**--------------------------TOTAL COST--------------------------------**/
        
        //create matrix for total cost
        int totalCost[][] = new int[rows][cols];
        
        //starting col of totalCost shall be equal to cost
        for(int row = 0; row < rows; row++)
        {
            totalCost[row][0] = cost[row][0];
        }
        
        //!!!SKIP FIRST COL!!!; calculate total cost values based on cost values
        for(int col = 1; col < cols;col++)
        {
            //!!!SPECIAL EDGE CASE FIRST ROW!!! total cost will be cost plus the minimum of the two values to the left of it
            //--------------------------------------------directly left---------diagonal down
            totalCost[0][col] = cost[0][col] + Math.min(totalCost[0][col-1], totalCost[1][col-1]);
            
            //!!!SPECIAL EDGE CASE LAST ROW!!! total cost will be cost plus the minimum of the two values to the left of it
            //---------------------------------------------------------------directly left-------------diagonal up
            totalCost[rows-1][col] = cost[rows-1][col] + Math.min(totalCost[rows-1][col-1], totalCost[rows-2][col-1]);
            
            //values in between first and last
            for(int row = 1; row< rows -1; row++)
            {
                //total cost will be cost plus the minimum of three values
                //calculate minimum of "diagonal up" and "diagonal down" first then that minimum to "directly right"
                totalCost[row][col] = cost[row][col] + 
                        Math.min(totalCost[row][col-1], //directly left
                                 Math.min(totalCost[row-1][col-1], //diagonal up
                                          totalCost[row+1][col-1]) );//diagonal down
            }
            
        }
        
        /**----------------------END TOTAL COST--------------------------------**/
        
        /**--------------------------------PATH--------------------------------**/

        //create array for path, size is only as long as matrix columns
        int path[] = new int[cols];
        
      //variable to start search for lowest total cost pixel's row on right most column
        int minCostRow = 0;
        
        for(int row = 1; row < rows; row++)
        {
            //if next row's element is less than the current minimum
            if(totalCost[row][cols-1] < totalCost[minCostRow][cols-1])
            {
                minCostRow = row;//set new minimum's row
            }
        }
                    
        //reference variable that will START at the lowest total cost pixel's row, but will be changed
        int rowRef = minCostRow;
        
        //!!!STARTS AT LOWEST TOTAL COST PIXEL'S ROW!!!; begin on column going left
        for(int col = cols-1; col >= 0; col--)
        {
            int tempRow = rowRef;//temporary reference variable that starts on the first row
            
            //if not last row, check if value below is less than current
            if(rowRef < rows - 1 && totalCost[rowRef+1][col] < totalCost[rowRef][col])
            {
                tempRow = rowRef + 1;//set current to below value
            }
            
            //if not first row, check if value to the above is less than current
            if(rowRef > 0 && totalCost[rowRef-1][col] < totalCost[tempRow][col])
            {
                tempRow = rowRef - 1;//set current to above value
            }
            
            //otherwise remain on row
            rowRef = tempRow;//set rowRef to what tempRow has become after finding the minimum
            
            path[col] = rowRef;//set value in array
        }
        
        /**----------------------------END PATH--------------------------------**/

/**----------------------------MAKE NEW IMAGE BASED ON SEAM--------------------------------**/
        
//        //Make a new image of the same size but one less row that is a matrix of RGB integers
        BufferedImage newImage = new BufferedImage(img.getWidth()-1, img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //Loop through image
        for(int i = 0; i < img.getHeight(); i++)
        {
            //Up to less than the path
            for(int j = 0; j < path[i]; j++)
            {
                //copy
                newImage.setRGB(j,i, img.getRGB(j, i));
            }
            //After the path
            for(int j = path[i] + 1; j < img.getWidth(); j++)
            {
                //copy
                newImage.setRGB(j-1,i, img.getRGB(j, i));
            }
        }
        
        reference =  newImage;
    /**----------------------------MAKE NEW IMAGE END----------------------------**/
        /**Print Values**/
//        //COST
//        System.out.println("|--COST--|");
//        
//        for(int row = 0; row < rows; row++)
//        {
//            for(int col = 0; col<cols; col++)
//            {
//                System.out.print(" " + cost[row][col] + " ");//print all col values in one row separated by a space
//            }
//            System.out.println();//break to a new line to show matrix/table of values
//        }
//        
//        //TOTAL COST
//        System.out.println("|--TOTAL COST--|");
//        
//        for(int row = 0; row < rows; row++)
//        {
//            for(int col = 0; col<cols; col++)
//            {
//                System.out.print(" " + totalCost[row][col] + " ");//print all col values in one row separated by a space
//            }
//            System.out.println();//break to a new line to show matrix/table of values
//        }
//        
//        //PATH
//        System.out.println("|--PATH--|");
//        
//        for(int row = 0; row < cols; row++)
//        {
//            System.out.print(" " + path[row] + " ");
//        }
        
    }

    
}
