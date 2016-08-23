package seam_carving;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.*;
import javax.swing.*;
 
/**
 * @author conanz
 * 
 * A class simply used to calculate the gradient of a fu 
 */
public class GradientCalculation extends Component 
{
    /**Class Member Variables**/
    BufferedImage img;//Original image (for reference)
    BufferedImage img2;//Altered image
    File chosenFile;
    boolean changeImageCalled;//boolean hack to only call changeImage() function once
 
    /**MAIN METHOD TO RUN CLASS**/
    public static void main(String[] args) 
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
        f.add(new GradientCalculation() );//Call constructor
        f.pack();
        f.setVisible(true);
    }
    
    /**CONSTRUCTOR**/
    public GradientCalculation() 
    {
       //Assign values to class member variables
       changeImageCalled = false;
       
       try 
       {
//           loadImage();//call to bring user to a "select file" window
           //Assign a specified image file
           img = ImageIO.read( new File("Balloons.jpg") );
           img2 = ImageIO.read( new File("Balloons.jpg") );
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
        //hack check to only call changeImage() once
        if(changeImageCalled == false)
        {
            calculateGradient(img);
            changeImageCalled = true;
        }
        //draw image 2 (altered image)
        g.drawImage(img2, 0, 0, null);
        
//        repaint();//paint image every frame
    }
    
    /**FUNCTION TO ALTER IMAGE TO A GREYSCALE GRADIENT**/
    public static BufferedImage calculateGradient(BufferedImage img)
    {
        BufferedImage img2 = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);        
        
        //Scroll through every row of pixels
        for(int row = 0; row < (img.getWidth() );row++)
        {
            //Scroll through every column of pixels
            for(int col = 0; col < (img.getHeight() ) ; col++)
            {
              //four corners
                //top left
                if(row == 0 && col == 0)
                {
//                    System.out.println("top left called");

                    //Get colors for each surrounding pixel   
                    Color bottomPixel = new Color( img.getRGB(row,   col+1) );
                    Color rightPixel  = new Color( img.getRGB(row+1, col) );
                    Color thisPixel = new Color( img.getRGB(row, col) );
                    
                    //Get the average RGB for each surrounding pixel
                    int bottomPixelRGB = ( bottomPixel.getRed() + bottomPixel.getBlue() + bottomPixel.getGreen() ) / 3;
                    int rightPixelRGB  = ( rightPixel.getRed() + rightPixel.getBlue() + rightPixel.getGreen() ) / 3;
                    int thisPixelRGB  = ( thisPixel.getRed() + thisPixel.getBlue() + thisPixel.getGreen() ) / 3;

                    
                    //APPLY THIS FORUMA:
                    //grad = abs(im(x+1,y)-im(x-1,y)) + abs(im(x,y+1)-im(x,y-1))
                    int grad = (  Math.abs(rightPixelRGB - thisPixelRGB)  ) + (  Math.abs(bottomPixelRGB - thisPixelRGB)  );
                    
                    //Can only send down colors that are up to 255
                    grad = (int)( Math.sqrt(grad)*10);              
                  
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }
                //top right
                if(row == img.getWidth()-1 && col == 0)
                {
//                    System.out.println("top right called");

                  //Get colors for each surrounding pixel   
                    Color bottomPixel = new Color( img.getRGB(row,   col+1) );
                    Color leftPixel   = new Color( img.getRGB(row-1, col) );
                    Color thisPixel = new Color( img.getRGB(row, col) );

                    
                    //Get the average RGB for each surrounding pixel
                    int bottomPixelRGB = ( bottomPixel.getRed() + bottomPixel.getBlue() + bottomPixel.getGreen() ) / 3;
                    int leftPixelRGB  = ( leftPixel.getRed() + leftPixel.getBlue() + leftPixel.getGreen() ) / 3;
                    int thisPixelRGB  = ( thisPixel.getRed() + thisPixel.getBlue() + thisPixel.getGreen() ) / 3;
                    
                    //APPLY THIS FORUMA:
                    //grad = abs(im(x+1,y)-im(x-1,y)) + abs(im(x,y+1)-im(x,y-1))
                    int grad = (  Math.abs(leftPixelRGB - thisPixelRGB)  ) + (  Math.abs(bottomPixelRGB - thisPixelRGB)  );
                    
                    //Can only send down colors that are up to 255
                    grad = (int)( Math.sqrt(grad)*10);              
                  
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }
                //bottom right
                if(row == img.getWidth()-1 && col == img.getHeight()-1)
                {
//                    System.out.println("bottom right called");

                    //Get colors for each surrounding pixel   
                    Color topPixel = new Color( img.getRGB(row,   col-1) );
                    Color leftPixel   = new Color( img.getRGB(row-1, col) );
                    Color thisPixel = new Color( img.getRGB(row, col) );

                    
                    //Get the average RGB for each surrounding pixel
                    int topPixelRGB = ( topPixel.getRed() + topPixel.getBlue() + topPixel.getGreen() ) / 3;
                    int leftPixelRGB  = ( leftPixel.getRed() + leftPixel.getBlue() + leftPixel.getGreen() ) / 3;
                    int thisPixelRGB  = ( thisPixel.getRed() + thisPixel.getBlue() + thisPixel.getGreen() ) / 3;
                    
                    //APPLY THIS FORUMA:
                    //grad = abs(im(x+1,y)-im(x-1,y)) + abs(im(x,y+1)-im(x,y-1))
                    int grad = (  Math.abs(leftPixelRGB - thisPixelRGB)  ) + (  Math.abs(topPixelRGB - thisPixelRGB)  );
                    
                    //Can only send down colors that are up to 255
                    grad = (int)( Math.sqrt(grad)*10);              
                  
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }
                //bottom left
                if(row == 0 && col == img.getHeight()-1)
                {
//                    System.out.println("bottom left called");

                    //Get colors for each surrounding pixel   
                    Color topPixel = new Color( img.getRGB(row,   col-1) );
                    Color rightPixel   = new Color( img.getRGB(row+1, col) );
                    Color thisPixel = new Color( img.getRGB(row, col) );

                    
                    //Get the average RGB for each surrounding pixel
                    int topPixelRGB = ( topPixel.getRed() + topPixel.getBlue() + topPixel.getGreen() ) / 3;
                    int rightPixelRGB  = ( rightPixel.getRed() + rightPixel.getBlue() + rightPixel.getGreen() ) / 3;
                    int thisPixelRGB  = ( thisPixel.getRed() + thisPixel.getBlue() + thisPixel.getGreen() ) / 3;
                    
                    //APPLY THIS FORUMA:
                    //grad = abs(im(x+1,y)-im(x-1,y)) + abs(im(x,y+1)-im(x,y-1))
                    int grad = (  Math.abs(rightPixelRGB - thisPixelRGB)  ) + (  Math.abs(topPixelRGB - thisPixelRGB)  );
                    
                    //Can only send down colors that are up to 255
                    grad = (int)( Math.sqrt(grad)*10);              
                  
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }
                //left right columns
                if(row == 0 && (col != 0  && col != img.getHeight()-1)  )
                {
//                    System.out.println("left called");

                    //Get colors for each surrounding pixel   
                    Color topPixel    = new Color( img.getRGB(row,   col-1) );
                    Color bottomPixel = new Color( img.getRGB(row,   col+1) );
                    Color rightPixel  = new Color( img.getRGB(row+1, col) );
                    Color thisPixel = new Color( img.getRGB(row, col) );

                    
                    //Get the average RGB for each surrounding pixel
                    int topPixelRGB    = ( topPixel.getRed() + topPixel.getBlue() + topPixel.getGreen() ) / 3;
                    int bottomPixelRGB = ( bottomPixel.getRed() + bottomPixel.getBlue() + bottomPixel.getGreen() ) / 3;
                    int rightPixelRGB  = ( rightPixel.getRed() + rightPixel.getBlue() + rightPixel.getGreen() ) / 3;
                    int thisPixelRGB  = ( thisPixel.getRed() + thisPixel.getBlue() + thisPixel.getGreen() ) / 3;

                    
                    //APPLY THIS FORUMA:
                    //grad = abs(im(x+1,y)-im(x-1,y)) + abs(im(x,y+1)-im(x,y-1))
                    int grad = (  Math.abs(rightPixelRGB - thisPixelRGB)  ) + (  Math.abs(bottomPixelRGB - topPixelRGB)  );
                    
                    //Can only send down colors that are up to 255
                    grad = (int)( Math.sqrt(grad)*10);              
                  
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }
                if(row == img.getWidth()-1 && (col != 0  && col != img.getHeight()-1)  )
                {
//                    System.out.println("right called");

                    //Get colors for each surrounding pixel   
                    Color topPixel    = new Color( img.getRGB(row,   col-1) );
                    Color bottomPixel = new Color( img.getRGB(row,   col+1) );
                    Color leftPixel   = new Color( img.getRGB(row-1, col) );
                    Color thisPixel = new Color( img.getRGB(row, col) );

                    //Get the average RGB for each surrounding pixel
                    int topPixelRGB    = ( topPixel.getRed() + topPixel.getBlue() + topPixel.getGreen() ) / 3;
                    int bottomPixelRGB = ( bottomPixel.getRed() + bottomPixel.getBlue() + bottomPixel.getGreen() ) / 3;
                    int leftPixelRGB   = ( leftPixel.getRed() + leftPixel.getBlue() + leftPixel.getGreen() ) / 3;
                    int thisPixelRGB  = ( thisPixel.getRed() + thisPixel.getBlue() + thisPixel.getGreen() ) / 3;

                    //APPLY THIS FORUMA:
                    //grad = abs(im(x+1,y)-im(x-1,y)) + abs(im(x,y+1)-im(x,y-1))
                    int grad = (  Math.abs(leftPixelRGB - thisPixelRGB)  ) + (  Math.abs(bottomPixelRGB - topPixelRGB)  );
                    
                    //Can only send down colors that are up to 255
                    grad = (int)( Math.sqrt(grad)*10);             
                  
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }
                //top bottom
                if(col == 0 && (row != 0 && row != img.getWidth()-1)  )
                {
//                    System.out.println("top called");

                   //Get colors for each surrounding pixel   
                    Color bottomPixel = new Color( img.getRGB(row,   col+1) );
                    Color leftPixel   = new Color( img.getRGB(row-1, col) );
                    Color rightPixel  = new Color( img.getRGB(row+1, col) );
                    Color thisPixel = new Color( img.getRGB(row, col) );

                    //Get the average RGB for each surrounding pixel
                    int bottomPixelRGB = ( bottomPixel.getRed() + bottomPixel.getBlue() + bottomPixel.getGreen() ) / 3;
                    int leftPixelRGB   = ( leftPixel.getRed() + leftPixel.getBlue() + leftPixel.getGreen() ) / 3;
                    int rightPixelRGB  = ( rightPixel.getRed() + rightPixel.getBlue() + rightPixel.getGreen() ) / 3;
                    int thisPixelRGB  = ( thisPixel.getRed() + thisPixel.getBlue() + thisPixel.getGreen() ) / 3;

                    //APPLY THIS FORUMA:
                    //grad = abs(im(x+1,y)-im(x-1,y)) + abs(im(x,y+1)-im(x,y-1))
                    int grad = (  Math.abs(rightPixelRGB - leftPixelRGB)  ) + (  Math.abs(bottomPixelRGB - thisPixelRGB)  );
                    
                    //Can only send down colors that are up to 255
                    grad = (int)( Math.sqrt(grad)*10);              
                  
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }
                if(col == img.getHeight()-1 && (row != 0 && row != img.getWidth()-1)  )
                {
//                    System.out.println("bottom called");

                    //Get colors for each surrounding pixel   
                    Color topPixel    = new Color( img.getRGB(row,   col-1) );
                    Color leftPixel   = new Color( img.getRGB(row-1, col) );
                    Color rightPixel  = new Color( img.getRGB(row+1, col) );
                    Color thisPixel = new Color( img.getRGB(row, col) );

                    //Get the average RGB for each surrounding pixel
                    int topPixelRGB    = ( topPixel.getRed() + topPixel.getBlue() + topPixel.getGreen() ) / 3;
                    int leftPixelRGB   = ( leftPixel.getRed() + leftPixel.getBlue() + leftPixel.getGreen() ) / 3;
                    int rightPixelRGB  = ( rightPixel.getRed() + rightPixel.getBlue() + rightPixel.getGreen() ) / 3;
                    int thisPixelRGB  = ( thisPixel.getRed() + thisPixel.getBlue() + thisPixel.getGreen() ) / 3;

                    //APPLY THIS FORUMA:
                    //grad = abs(im(x+1,y)-im(x-1,y)) + abs(im(x,y+1)-im(x,y-1))
                    int grad = (  Math.abs(rightPixelRGB - leftPixelRGB)  ) + (  Math.abs(topPixelRGB - thisPixelRGB)  );
                    
                    //Can only send down colors that are up to 255
                    grad = (int)( Math.sqrt(grad)*10);             
                  
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }
                //else center
                else if (row != 0 && row != img.getWidth()-1 && col != 0 && col != img.getHeight()-1)
                {
                    /**Edge Detection**/
                    //Look at pixels' colors around current pixel
                    Color top = new Color(img.getRGB(row, col-1));
                    Color bottom = new Color(img.getRGB(row, col+1));
                    Color right = new Color(img.getRGB(row+1, col));
                    Color left = new Color(img.getRGB(row-1, col));
                    
                    //Separate colors by RGB values, but since we gray scaled it, each value is the same so we only need to take one (red)
                    int topRGB = (top.getRed() + top.getBlue() + top.getGreen() )/3;
                    int bottomRGB = (bottom.getRed() + bottom.getBlue() + bottom.getGreen() )/3;
                    int rightRGB = (right.getRed() + right.getBlue() + right.getGreen() )/3;
                    int leftRGB = (left.getRed() + left.getBlue() + left.getGreen() ) /3;
    
                    //Calculate gradient by values
                    int grad =  ( Math.abs( (rightRGB  - leftRGB ) ) + Math.abs( (bottomRGB - topRGB) ) );
                    
                    /**Grey Scale Gradient**/
                    //lower gradient value to make sure it's not too high (we only have 255 colors)
                    grad = (int)( Math.sqrt(grad)*10);
                    
                    //Create a new black/white color based on grad values
                    Color cGrad = new Color(grad,grad,grad);
                    int rgbGrad = cGrad.getRGB();//put the color into a reference
    
                    //Alter actual image
                    img2.setRGB(row, col, rgbGrad );
                }

            }
        }

        return img2;
    }
    
    /**FUNCTION TO BRING UP FILE SELECT WINDOW**/
    void loadImage()
    {
        //Create file chooser
        JFileChooser chooser= new JFileChooser();
        //When clicked
        int choice = chooser.showOpenDialog(this);

        if (choice != JFileChooser.APPROVE_OPTION) return;

        chosenFile = chooser.getSelectedFile();

    }
    
    /**FUNCTION TO SET SIZE OF IMAGE**/
    public Dimension getPreferredSize() 
    {
        if (img == null) 
        {
             return new Dimension(100,100);
        } 
        else 
        {
           return new Dimension(img.getWidth(null), img.getHeight(null));
        }
    }
 
    
}