/*
    ImgArea is the class where all the image processing happens.
    FilterImage is the nested class that applies some filters using a Thread Pool.
    
    Copyright (C) 2019 Samantha Hernández Hernández

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

*/

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.Kernel;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.RecursiveAction;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

// CLASS IMGAREA IS THE ONE THAT MANIPULATES THE IMAGE
class ImgArea extends Canvas{
	// DECLARATION OF THE VARIABLES NEEDED
	Image orImg;
	BufferedImage orBufferedImage; // WILL ALWAYS KEEP THE ORIGINAL IMAGE
	BufferedImage bimg; // INTERACTS WITH THE RESIZE, BRIGHTNESS, ROTATE FILTERS
	BufferedImage bimg1; // INTERACTS WITH THREADPOOL FILTERS
	BufferedImage img; // IS WHERE THE ACTUAL IMAGE IS STORED
	float e;
	float radian;
	Dimension ds;
	int mX;
	int mY;
	int x;
	int y;
	static boolean imageLoaded;
	boolean actionSlided;
	boolean actionResized;
	boolean actionCompressed;
	boolean actionTransparent;
	boolean actionRotated;
	boolean actionDraw;
	boolean drawn;
	boolean edited;
	MediaTracker mt;
	static Color c;
	Color colorTextDraw;
	Robot rb;
	boolean dirHor;
	String imgFileName;
	String fontName;
	int fontSize;
	String textToDraw;
	int filter;
	  
public void paint(Graphics g){
	Graphics2D g2d=(Graphics2D)g; //create Graphics2D object   
	if(imageLoaded){ // DRAWS THE UPDATED IMAGE
		if(actionSlided || actionResized || actionTransparent || actionRotated || drawn ){
			x=mX-bimg.getWidth()/2;
		    y=mY-bimg.getHeight()/2;
		    g2d.translate(x,y);  
		    g2d.drawImage(bimg,0,0,null); // DRAWS THE UODATED IMAGE IN THE CANVAS 
		 }else{ // DRAWS THE SOURCE ORIGINAL IMAGE
		    x=mX-orBufferedImage.getWidth()/2;
		    y=mY-orBufferedImage.getHeight()/2;
		    g2d.translate(x,y); 
		    g2d.drawImage(orBufferedImage,0,0,null); // DRAWS ORIGINAL IMAGE
		 }
	}
	g2d.dispose(); // CLEANS
}

// SETS THE VALUE FOR C 
public void setColor(Color color){
   c=color;   
}

// SETS VALUE FOR IMGFILENAME
public void setImgFileName(String fname){
   imgFileName=fname;
}

//SETS VALJUE FOR E
public void setValue(float value){ 
	e=value;
}

// SETS VALUE
public void setActionSlided(boolean value ){ 
	actionSlided=value;
}

//SETS VALUE  
public void setActionResized(boolean value ){ 
	actionResized=value;
} 

//SETS VALUE  
public void setActionCompressed(boolean value ){ 
	actionCompressed=value;
}

//SETS VALUE 
public void setActionDraw(boolean value ){ 
	actionDraw=value;
}

// INITIALIZE VARIABLES
public void initialize(){
   imageLoaded=false; 
   actionSlided=false;
   actionResized=false;
   actionCompressed=false;
   actionTransparent=false;
   actionRotated=false;
   actionDraw=false;
   drawn=false;
   dirHor=false;
   edited=false;
   c=null;
   radian=0.0f;
   e=0.0f;
}

// RECALLS THE SOURCE IMAGE (WITH ANY FILTERS)
public void reset(){
   if(imageLoaded){
	   prepareImage(imgFileName);
	   repaint();
   }  
}

// METHOD THAT ROTATES THE IMAGE WUW :)
public void makeImageRotate(BufferedImage image,int w,int h){
   BufferedImage bi=(BufferedImage)createImage(w,h);
   Graphics2D  g2d=(Graphics2D)bi.createGraphics(); 
   radian=(float)Math.PI/2; // SETS ANGLE     
   g2d.translate(w/2,h/2); 
   g2d.rotate(radian); // ROTATES THE IMAGE
   g2d.translate(-h/2,-w/2); 
   g2d.drawImage(image,0,0,null); // DRAW THE ROTATED IMAGE 
   bimg=bi; // UPDATES THE VALUE OF THE IMAGE TO SEE IT IN THE CANVAS
   edited=true;
   g2d.dispose(); // CLEANS
}

// CALLS THE METHOD THAT ROTATES THE IMAGE
public void rotateImage(){
	BufferedImage bi;
    if(actionSlided || actionResized || actionTransparent || actionRotated || drawn || edited){
    	bi=bimg;    // ROTATES EDITED IMAGE 
    }else{
    	bi=orBufferedImage;  // ROTATES ORIGINAL IMAGE
    }
	makeImageRotate(bi,bi.getHeight(),bi.getWidth()); // CALLS THE METHOD THAT ROTATES THE IMAGE     
	actionRotated=true; 
    repaint(); 
}

// COMPRESS THE IMAGE METHOD
public  void makeCompression(File outFileName){
   try{
	   ImageWriter imgWriter =(ImageWriter) ImageIO.getImageWritersByFormatName("jpg").next();
	   ImageOutputStream imgOutStrm = ImageIO.createImageOutputStream(outFileName);
	   imgWriter.setOutput(imgOutStrm);  
	   IIOImage iioImg;
	   if(actionSlided || actionResized){ // COMPRESS THE EDITED
		   iioImg = new IIOImage(bimg, null,null);
	   }else{    
		   iioImg = new IIOImage(orBufferedImage, null,null); // COMPRESS THE ORIGINAL
	   }
	   ImageWriteParam jpgWriterParam = imgWriter.getDefaultWriteParam();
	   jpgWriterParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	   jpgWriterParam.setCompressionQuality(0.7f);
	   imgWriter.write(null, iioImg, jpgWriterParam);
	   // CLEANS
	   imgOutStrm.close();
	   imgWriter.dispose();
   }catch(Exception e){} 
}

// RESIZE METHOD 
public void resizeImage(int w,int h){
	BufferedImage bi=(BufferedImage)createImage(w,h);
    Graphics2D g2d=(Graphics2D)bi.createGraphics();
	edited=true;
    if(actionSlided || actionTransparent || actionRotated ||drawn) // RESIZE THE EDITED
    	g2d.drawImage(bimg,0,0,w,h,null);
    else
	    g2d.drawImage(orImg,0,0,w,h,null); // RESIZE THE ORIGINAL
	    bimg=bi;
	    g2d.dispose();
}

// PREPARES IMAGE TO BE DISPLAYED
public void prepareImage(String filename){
   initialize();
   try{
	   mt=new MediaTracker(this);    
	   orImg=Toolkit.getDefaultToolkit().getImage(filename); 
	   mt.addImage(orImg,0);
	   mt.waitForID(0); 
	   int width=orImg.getWidth(null); // GET IMAGE WIDTH
	   int height=orImg.getHeight(null); // GET IMAGE HEIGHT
	   orBufferedImage=createBufferedImageFromImage(orImg,width,height,false); // CREATES BUFFERED IMAGE
	   bimg = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);  // THIS BUFFEREDIMAGE WILL STORE THE IMG WITH FILTERS
	   imageLoaded=true; 
	   }catch(Exception e){System.exit(-1);}
}


//CHECK
// THIS METHOD APPLIES CONVOLUTION WITH JAVA LIBRARIES WITH A 3X3 KERNEL TO CHANGE THE BRIGHTNESS OF THE IMAGE
public void filterImage(){
	float[] elements = {0.0f, 1.0f, 0.0f, -1.0f,e,1.0f,0.0f,0.0f,0.0f}; 
	   Kernel kernel = new Kernel(3, 3, elements);  //create keynel object to encapsulate the elements array
	   ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null); //create ConvolveOp to encapsulate 
	   //the kernel
	   bimg= new BufferedImage(orBufferedImage.getWidth(),orBufferedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
	   cop.filter(orBufferedImage,bimg); //start filtering the image 
}

		  
// METHOD THAT MAKES A COPY OF A BUFFERED IMAGE
 public BufferedImage createBufferedImageFromImage(Image image, int width, int height, boolean tran){ 
	 BufferedImage dest ;
	 if(tran) 
		 dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	 else
		 dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   Graphics2D g2 = dest.createGraphics();
   g2.drawImage(image, 0, 0, null); // DRAWS IMAGE
   g2.dispose();  // CLEANS
   return dest; // RETURNS THE DEST IMAGE
}



// BUFFERED IMAGE TO FILE
 public void saveToFile(String filename){
	 String ftype=filename.substring(filename.lastIndexOf('.')+1);
	 try{
		 if(actionCompressed)
			 makeCompression(new File(filename)); // SAVES THE COMPRESSED IMAGE
		 else if(actionSlided || actionResized || actionTransparent || actionRotated || drawn)
			 ImageIO.write(bimg,ftype,new File(filename));
     }catch(IOException e){System.out.println("Error in saving the file");}
  }

 
 // REPAINTS
  public void act(BufferedImage im){
	  this.bimg = im;
	  drawn = true;
	  repaint();
	  }
	  
	  
  // CREATES THE CANVAS BASED ON THE SCREEN SIZE
  public ImgArea() {
  	try{
  	    rb=new Robot(); 
     }catch(AWTException e){}
     ds=getToolkit().getScreenSize();   
     mX=(int)ds.getWidth()/2; 
     mY=(int)ds.getHeight()/2;
  }	  
	
//CLASS WHERE THE FILTERS ARE APPLIED WITH THREADPOOLS :)  
//IT SPLITS THE IMAGE AND DIVIDES IT IN THREADS TO EXECUTE THE SAME TASK WITH DIFFERENT PARTS OF THE IMAGE AT THE SAME TIME
class FilterImage extends RecursiveAction {

	private static final long serialVersionUID = 1L;
    private int mStart;
    private int mLength;
    File file;
    BufferedImage img;
    BufferedImage dst;
    int [][] imgn;
  	int [][] nei;
  	int [][] neib;
  	int [][] neig;
  	int [][] neir;
  	int [][] red;
  	int [][] blue;
  	int [][] green;
  	int [][] alph;
  	
 // CLASS CONSTRUCTOR
  	public FilterImage (String src, String dst, int num) {
  	    try {
  	    	img = ImageIO.read(new File(src));
  	    }catch(Exception e) {
  	    	System.out.println(e.getMessage());
  	    }
  	    filter=num;
  	    int numRows = img.getWidth();
  	    int numCols = img.getHeight();
  	    imgn = new int [numRows][numCols];
  	  	nei = new int [numRows-2][numCols-2];
  	  	neib = new int [numRows-2][numCols-2];
  	  	neig = new int [numRows-2][numCols-2];
  	  	neir = new int [numRows-2][numCols-2];
  	  	red = new int [numRows][numCols];
  	  	blue = new int [numRows][numCols];
  	  	green = new int [numRows][numCols];
  	  	alph = new int [numRows][numCols];
  		mStart = 0;
  	    mLength =img.getWidth();
  	    this.dst = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB); 
  	    bimg=this.dst;
  	    act(bimg); // UPDATESX IMAGE IN CANVAS
  	}

  	//CLASS CONSTRUCTOR
  	public FilterImage (int num) {	
  		filter=num;
  		img=bimg;
  		mStart = 0;
  	    mLength =img.getWidth();
  	    int numRows = img.getWidth();
  	    int numCols = img.getHeight();
  	    imgn = new int [numRows][numCols];
  	  	nei = new int [numRows-2][numCols-2];
  	  	neib = new int [numRows-2][numCols-2];
  	  	neig = new int [numRows-2][numCols-2];
  	  	neir = new int [numRows-2][numCols-2];
  	  	red = new int [numRows][numCols];
  	  	blue = new int [numRows][numCols];
  	  	green = new int [numRows][numCols];
  	  	alph = new int [numRows][numCols];
  	    this.dst = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB); 
  	    bimg=this.dst;
  	    act(bimg);  	   
  	}

  	//CLASS CONSTRUCTOR
  	public FilterImage(BufferedImage src, int start, int length, BufferedImage dst) {
  	    img = src;
  	    mStart = start;
  	    int numRows = img.getWidth();
  	    int numCols = img.getHeight();
  	    imgn = new int [numRows][numCols];
  	  	nei = new int [numRows-2][numCols-2];
  	  	neib = new int [numRows-2][numCols-2];
  	  	neig = new int [numRows-2][numCols-2];
  	  	neir = new int [numRows-2][numCols-2];
  	  	red = new int [numRows][numCols];
  	  	blue = new int [numRows][numCols];
  	  	green = new int [numRows][numCols];
  	  	alph = new int [numRows][numCols];	
  	    mLength = length;
  	    this.dst = dst;
  	}

  	protected void computeDirectly() {
  		int heigth=img.getHeight();        	
  	    	if (filter==1) {
  	    		for (int i = mStart; i < mStart + mLength; i++) {
  		            for (int j = 0; j < heigth; j++) {
  		            	int rgb = img.getRGB(i, j);  // rgb contian all number coded within a single integer concatenaed as red/green/blue
  			            int red = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
  			            int green = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
  			            int blue = (rgb >> 16) & 0xFF; // >> Bitwise shifts 16 positions  & makes  uses  0000000111 with the number and eliminates the rest
  			            float L = (float) (0.2126 * (float) red + 0.7152 * (float) green + 0.0722 * (float) blue);
  			            int color;
  			            color = 153 * (int) L / 255;
  			            color = (color << 8) | 153 * (int) L / 255;
  			            color = (color << 8) | 153 * (int) L / 255;
  			            dst.setRGB(i, j, color); // sets the pixeles to specified color  (negative image)
  			            }
  		        }
  	       } else if (filter ==2) {
  	    	   for (int i = mStart; i < mStart + mLength; i++) {
  	    		   for (int j = 0; j < heigth; j++) {
  	    			   int rgb = img.getRGB(i, j);  // rgb contian all number coded within a single integer concatenaed as red/green/blue
  			            int red = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
  			            int green = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
  			            int blue = (rgb >> 16) & 0xFF; // >> Bitwise shifts 16 positions  & makes  uses  0000000111 with the number and eliminates the rest
  		                float L = (float) (0.2126 * (float) red + 0.7152 * (float) green + 0.0722 * (float) blue);
  		                int color;
  		                color = 234 * (int) L / 255;
  		                color = (color << 8) | 176 * (int) L / 255;
  		                color = (color << 8) | 3 * (int) L / 255;
  		                dst.setRGB(i, j, color); // sets the pixeles to specified color  (negative image) 
  		            }
  	    	   }		
  	       } else if (filter ==3) {
  	    	   for (int i = mStart; i < mStart + mLength; i++) {
  		            for (int j = 0; j < heigth; j++) {
  		            	int rgb = img.getRGB(i, j);  // rgb contian all number coded within a single integer concatenaed as red/green/blue
  		                int red = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
  		                int green = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
  		                int blue = (rgb >> 16) & 0xFF; // >> Bitwise shifts 16 positions  & makes  uses  0000000111 with the number and eliminates the rest
  		                float L = (float) (0.2126 * (float) red + 0.7152 * (float) green + 0.0722 * (float) blue);
  		                int color;
  		                color = 230 * (int) L / 255;
  		                color = (color << 8) | 200 * (int) L / 255;
  		                color = (color << 8) | 260 * (int) L / 255;	
  		                dst.setRGB(i, j, color); // sets the pixeles to specified color  (negative image) 	        		
  			         }
  	            }
  	       } else if (filter ==4) {
  	    	   for (int i = mStart; i < mStart + mLength; i++) {
  	    		   for (int j = 0; j < heigth; j++) {
  					   int rgb = img.getRGB(i, j);  // rgb contian all number coded within a single integer concatenaed as red/green/blue
  			    		int r = (rgb>>16)&0xff;
  			            int g = (rgb>>8)&0xff;
  			            int b = rgb & 0xff;
  			            dst.setRGB(i,j,r);
  				      }
  	    		}
  	       }else if (filter == 5) {	 
	    		for (int i = mStart; i < mStart + mLength; i++) {
		            for (int j = 0; j < heigth; j++) {
		              	int rgb = img.getRGB(i, j);  // rgb contian all number coded within a single integer concatenaed as red/green/blue
  		                int red = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
  		                int green = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
  		                int blue = (rgb >> 16) & 0xFF; // >> Bitwise shifts 16 positions  & makes  uses  0000000111 with the number and eliminates the rest
  		                float L = (float) (0.2126 * (float) red + 0.7152 * (float) green + 0.0722 * (float) blue);
  		                int color;
  		                color = 170 * (int) L / 255;
  		                color = (color << 8) | 200 * (int) L / 255;
  		                color = (color << 8) | 20 * (int) L / 255;	
  		                dst.setRGB(i, j, color); // sets the pixeles to specified color  (negative image) 	
		            }
	    		}
  	       } else if (filter==6) {
  	    	   // MATRIX B IS THE 3X3 KERNEL THAT IS USED 
	  	    	float [][] matrixB = {{(float) 1/9, (float) 1/9, (float) 1/9}, { (float)1/9, (float) 1/9,  (float)1/9}, {(float)1/9, (float)1/9, (float)1/9}};
	  	    	// FOR EACH PIXEL WE GET THE RED GREEN BLUE AND ALPHA CHANNEL VALUE (0 TO 255)
	  	    	for (int i = mStart; i < mStart + mLength; i++) {
	  	            for (int j = 0; j < heigth; j++) {
	  	            	int rgb = img.getRGB(i, j); 
	  	                red[i][j] = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
	  	                green[i][j] = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
	  	                blue[i][j] = (rgb >> 16) & 0xFF;
	  	                alph[i][j] = (rgb >> 24) & 0xFF;
	  	            }
  	            }
	  	    // ONCE WE HAVE THE CHANNELS WE APPLY THE CONVOLUTION TO EACH CHANNEL
  	    	for (int i = mStart+1; i < mStart+mLength-1; i++) {
  	            for (int j = 1; j < heigth-1; j++) {	                
  	                nei[i-1][j-1]=(int) Math.abs((alph[i-1][j-1]*matrixB[0][0] + alph[i-1][j]*matrixB[0][1] + alph[i-1][j+1]*matrixB[0][2] + alph[i][j-1]*matrixB[1][0] + alph[i][j]*matrixB[1][1] + alph[i][j+1]*matrixB[1][2] + alph[i+1][j-1]*matrixB[2][0] + alph[i+1][j]*matrixB[2][1] + alph[i+1][j+1]*matrixB[2][2])); 
  	                neig[i-1][j-1]=(int) Math.abs((green[i-1][j-1]*matrixB[0][0] + green[i-1][j]*matrixB[0][1] + green[i-1][j+1]*matrixB[0][2] + green[i][j-1]*matrixB[1][0] + green[i][j]*matrixB[1][1] + green[i][j+1]*matrixB[1][2] + green[i+1][j-1]*matrixB[2][0] + green[i+1][j]*matrixB[2][1] + green[i+1][j+1]*matrixB[2][2]));
  	                neir[i-1][j-1]=(int) Math.abs((red[i-1][j-1]*matrixB[0][0] + red[i-1][j]*matrixB[0][1] + red[i-1][j+1]*matrixB[0][2] + red[i][j-1]*matrixB[1][0] + red[i][j]*matrixB[1][1] + red[i][j+1]*matrixB[1][2] + red[i+1][j-1]*matrixB[2][0] + red[i+1][j]*matrixB[2][1] + red[i+1][j+1]*matrixB[2][2]));
  	                neib[i-1][j-1]=(int) Math.abs((blue[i-1][j-1]*matrixB[0][0] + blue[i-1][j]*matrixB[0][1] + blue[i-1][j+1]*matrixB[0][2] + blue[i][j-1]*matrixB[1][0] + blue[i][j]*matrixB[1][1] + blue[i][j+1]*matrixB[1][2] + blue[i+1][j-1]*matrixB[2][0] + blue[i+1][j]*matrixB[2][1] + blue[i+1][j+1]*matrixB[2][2]));
  	                int p = (nei[i-1][j-1]<<24) | (neib[i-1][j-1]<<16) | (neig[i-1][j-1]<<8) | neir[i-1][j-1];
  	                dst.setRGB(i-1, j-1, p);
  	            }
  	        } 
  	      } else if (filter==7) {
  	    	// MATRIX B IS THE 3X3 KERNEL THAT IS USED 
  	    	  int [][] matrixB = {{1, 0, -1}, {0, 0, 0}, {-1, 0, 1}};
  	    	// FOR EACH PIXEL WE GET THE RED GREEN BLUE AND ALPHA CHANNEL VALUE (0 TO 255)
  	    	  for (int i = mStart; i < mStart + mLength; i++) {
  	    		  for (int j = 0; j < heigth; j++) {
  	    			  int rgb = img.getRGB(i, j); 
	  	                red[i][j] = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
	  	                green[i][j] = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
	  	                blue[i][j] = (rgb >> 16) & 0xFF;
	  	                alph[i][j] = (rgb >> 24) & 0xFF;   	
  	    		  }
    		  }
  	    	// ONCE WE HAVE THE CHANNELS WE APPLY THE CONVOLUTION TO EACH CHANNEL
  	    	for (int i = mStart+1; i < mStart+mLength-1; i++) {
  	            for (int j = 1; j < heigth-1; j++) {	                
  	                nei[i-1][j-1]=(int) Math.abs((alph[i-1][j-1]*matrixB[0][0] + alph[i-1][j]*matrixB[0][1] + alph[i-1][j+1]*matrixB[0][2] + alph[i][j-1]*matrixB[1][0] + alph[i][j]*matrixB[1][1] + alph[i][j+1]*matrixB[1][2] + alph[i+1][j-1]*matrixB[2][0] + alph[i+1][j]*matrixB[2][1] + alph[i+1][j+1]*matrixB[2][2])); 
  	                neig[i-1][j-1]=(int) Math.abs((green[i-1][j-1]*matrixB[0][0] + green[i-1][j]*matrixB[0][1] + green[i-1][j+1]*matrixB[0][2] + green[i][j-1]*matrixB[1][0] + green[i][j]*matrixB[1][1] + green[i][j+1]*matrixB[1][2] + green[i+1][j-1]*matrixB[2][0] + green[i+1][j]*matrixB[2][1] + green[i+1][j+1]*matrixB[2][2]));
  	                neir[i-1][j-1]=(int) Math.abs((red[i-1][j-1]*matrixB[0][0] + red[i-1][j]*matrixB[0][1] + red[i-1][j+1]*matrixB[0][2] + red[i][j-1]*matrixB[1][0] + red[i][j]*matrixB[1][1] + red[i][j+1]*matrixB[1][2] + red[i+1][j-1]*matrixB[2][0] + red[i+1][j]*matrixB[2][1] + red[i+1][j+1]*matrixB[2][2]));
  	                neib[i-1][j-1]=(int) Math.abs((blue[i-1][j-1]*matrixB[0][0] + blue[i-1][j]*matrixB[0][1] + blue[i-1][j+1]*matrixB[0][2] + blue[i][j-1]*matrixB[1][0] + blue[i][j]*matrixB[1][1] + blue[i][j+1]*matrixB[1][2] + blue[i+1][j-1]*matrixB[2][0] + blue[i+1][j]*matrixB[2][1] + blue[i+1][j+1]*matrixB[2][2]));
  	                int p = (nei[i-1][j-1]<<24) | (neib[i-1][j-1]<<16) | (neig[i-1][j-1]<<8) | neir[i-1][j-1];
  	                dst.setRGB(i-1, j-1, p);
  	            }
  	        } 
  	    } else if (filter==8) {
  	   // MATRIX B IS THE 3X3 KERNEL THAT IS USED 
  	    	float [][] matrixB = {{-1,0,1},{-2,0,2},{-1,0,1}};
  	   // FOR EACH PIXEL WE GET THE RED GREEN BLUE AND ALPHA CHANNEL VALUE (0 TO 255)
  	    	for (int i = mStart; i < mStart + mLength; i++) {
  	            for (int j = 0; j < heigth; j++) {
  	            	int rgb = img.getRGB(i, j); 
  	                red[i][j] = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
  	                green[i][j] = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
  	                blue[i][j] = (rgb >> 16) & 0xFF;
  	                alph[i][j] = (rgb >> 24) & 0xFF;
  	            }
            }
  	   // ONCE WE HAVE THE CHANNELS WE APPLY THE CONVOLUTION TO EACH CHANNEL
  	    	for (int i = mStart+1; i < mStart+mLength-1; i++) {
  	            for (int j = 1; j < heigth-1; j++) {	                
  	                nei[i-1][j-1]=(int) Math.abs((alph[i-1][j-1]*matrixB[0][0] + alph[i-1][j]*matrixB[0][1] + alph[i-1][j+1]*matrixB[0][2] + alph[i][j-1]*matrixB[1][0] + alph[i][j]*matrixB[1][1] + alph[i][j+1]*matrixB[1][2] + alph[i+1][j-1]*matrixB[2][0] + alph[i+1][j]*matrixB[2][1] + alph[i+1][j+1]*matrixB[2][2])); 
  	                neig[i-1][j-1]=(int) Math.abs((green[i-1][j-1]*matrixB[0][0] + green[i-1][j]*matrixB[0][1] + green[i-1][j+1]*matrixB[0][2] + green[i][j-1]*matrixB[1][0] + green[i][j]*matrixB[1][1] + green[i][j+1]*matrixB[1][2] + green[i+1][j-1]*matrixB[2][0] + green[i+1][j]*matrixB[2][1] + green[i+1][j+1]*matrixB[2][2]));
  	                neir[i-1][j-1]=(int) Math.abs((red[i-1][j-1]*matrixB[0][0] + red[i-1][j]*matrixB[0][1] + red[i-1][j+1]*matrixB[0][2] + red[i][j-1]*matrixB[1][0] + red[i][j]*matrixB[1][1] + red[i][j+1]*matrixB[1][2] + red[i+1][j-1]*matrixB[2][0] + red[i+1][j]*matrixB[2][1] + red[i+1][j+1]*matrixB[2][2]));
  	                neib[i-1][j-1]=(int) Math.abs((blue[i-1][j-1]*matrixB[0][0] + blue[i-1][j]*matrixB[0][1] + blue[i-1][j+1]*matrixB[0][2] + blue[i][j-1]*matrixB[1][0] + blue[i][j]*matrixB[1][1] + blue[i][j+1]*matrixB[1][2] + blue[i+1][j-1]*matrixB[2][0] + blue[i+1][j]*matrixB[2][1] + blue[i+1][j+1]*matrixB[2][2]));
  	                int p = (nei[i-1][j-1]<<24) | (neib[i-1][j-1]<<16) | (neig[i-1][j-1]<<8) | neir[i-1][j-1];
  	                dst.setRGB(i-1, j-1, p);
  	            }
  	        } 
  	    }else if (filter==9) {
  	   // MATRIX B IS THE 3X3 KERNEL THAT IS USED 
  	    	float [][]matrixB= {{0.0f, 1.0f, 0.0f}, {-1.0f,e,1.0f},{0.0f,0.0f,0.0f}};
  	   // FOR EACH PIXEL WE GET THE RED GREEN BLUE AND ALPHA CHANNEL VALUE (0 TO 255)
  	    	for (int i = mStart; i < mStart + mLength; i++) {
  	            for (int j = 0; j < heigth; j++) {
  	            	int rgb = img.getRGB(i, j); 
  	                red[i][j] = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
  	                green[i][j] = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
  	                blue[i][j] = (rgb >> 16) & 0xFF;
  	                alph[i][j] = (rgb >> 24) & 0xFF;   		            	
  	            }
            }
  	   // ONCE WE HAVE THE CHANNELS WE APPLY THE CONVOLUTION TO EACH CHANNEL
  	    	for (int i = mStart+1; i < mStart+mLength-1; i++) {
  	            for (int j = 1; j < heigth-1; j++) {	                
  	                nei[i-1][j-1]=(int) Math.abs((alph[i-1][j-1]*matrixB[0][0] + alph[i-1][j]*matrixB[0][1] + alph[i-1][j+1]*matrixB[0][2] + alph[i][j-1]*matrixB[1][0] + alph[i][j]*matrixB[1][1] + alph[i][j+1]*matrixB[1][2] + alph[i+1][j-1]*matrixB[2][0] + alph[i+1][j]*matrixB[2][1] + alph[i+1][j+1]*matrixB[2][2])); 
  	                neig[i-1][j-1]=(int) Math.abs((green[i-1][j-1]*matrixB[0][0] + green[i-1][j]*matrixB[0][1] + green[i-1][j+1]*matrixB[0][2] + green[i][j-1]*matrixB[1][0] + green[i][j]*matrixB[1][1] + green[i][j+1]*matrixB[1][2] + green[i+1][j-1]*matrixB[2][0] + green[i+1][j]*matrixB[2][1] + green[i+1][j+1]*matrixB[2][2]));
  	                neir[i-1][j-1]=(int) Math.abs((red[i-1][j-1]*matrixB[0][0] + red[i-1][j]*matrixB[0][1] + red[i-1][j+1]*matrixB[0][2] + red[i][j-1]*matrixB[1][0] + red[i][j]*matrixB[1][1] + red[i][j+1]*matrixB[1][2] + red[i+1][j-1]*matrixB[2][0] + red[i+1][j]*matrixB[2][1] + red[i+1][j+1]*matrixB[2][2]));
  	                neib[i-1][j-1]=(int) Math.abs((blue[i-1][j-1]*matrixB[0][0] + blue[i-1][j]*matrixB[0][1] + blue[i-1][j+1]*matrixB[0][2] + blue[i][j-1]*matrixB[1][0] + blue[i][j]*matrixB[1][1] + blue[i][j+1]*matrixB[1][2] + blue[i+1][j-1]*matrixB[2][0] + blue[i+1][j]*matrixB[2][1] + blue[i+1][j+1]*matrixB[2][2]));
  	                int p = (nei[i-1][j-1]<<24) | (neib[i-1][j-1]<<16) | (neig[i-1][j-1]<<8) | neir[i-1][j-1];
  	                dst.setRGB(i-1, j-1, p);
  	            }
  	        } 
  	    }else if (filter==10) {
  	   // MATRIX B IS THE 3X3 KERNEL THAT IS USED 
  	    	float [][]matrixB= {{1f, 0f, 1f}, {0f,1, 0f},{1f,0f,1f}};
  	   // FOR EACH PIXEL WE GET THE RED GREEN BLUE AND ALPHA CHANNEL VALUE (0 TO 255)
  	    	for (int i = mStart; i < mStart + mLength; i++) {
  	            for (int j = 0; j < heigth; j++) {
  	            	int rgb = img.getRGB(i, j); 
  	                red[i][j] = rgb & 0xFF;  // & uses  0000000111 with the rgb number to eliminate all the bits but the las 3 (which represent 8 position which are used for 0 to 255 values) 
  	                green[i][j] = (rgb >> 8) & 0xFF;  // >> Bitwise shifts 8 positions  & makes  uses  0000000111 with the number and eliminates the rest
  	                blue[i][j] = (rgb >> 16) & 0xFF;
  	                alph[i][j] = (rgb >> 24) & 0xFF;   		            	
  	            }
            }
  	   // ONCE WE HAVE THE CHANNELS WE APPLY THE CONVOLUTION TO EACH CHANNEL
  	    	for (int i = mStart+1; i < mStart+mLength-1; i++) {
  	            for (int j = 1; j < heigth-1; j++) {	                
  	                nei[i-1][j-1]=(int) Math.abs((alph[i-1][j-1]*matrixB[0][0] + alph[i-1][j]*matrixB[0][1] + alph[i-1][j+1]*matrixB[0][2] + alph[i][j-1]*matrixB[1][0] + alph[i][j]*matrixB[1][1] + alph[i][j+1]*matrixB[1][2] + alph[i+1][j-1]*matrixB[2][0] + alph[i+1][j]*matrixB[2][1] + alph[i+1][j+1]*matrixB[2][2])); 
  	                neig[i-1][j-1]=(int) Math.abs((green[i-1][j-1]*matrixB[0][0] + green[i-1][j]*matrixB[0][1] + green[i-1][j+1]*matrixB[0][2] + green[i][j-1]*matrixB[1][0] + green[i][j]*matrixB[1][1] + green[i][j+1]*matrixB[1][2] + green[i+1][j-1]*matrixB[2][0] + green[i+1][j]*matrixB[2][1] + green[i+1][j+1]*matrixB[2][2]));
  	                neir[i-1][j-1]=(int) Math.abs((red[i-1][j-1]*matrixB[0][0] + red[i-1][j]*matrixB[0][1] + red[i-1][j+1]*matrixB[0][2] + red[i][j-1]*matrixB[1][0] + red[i][j]*matrixB[1][1] + red[i][j+1]*matrixB[1][2] + red[i+1][j-1]*matrixB[2][0] + red[i+1][j]*matrixB[2][1] + red[i+1][j+1]*matrixB[2][2]));
  	                neib[i-1][j-1]=(int) Math.abs((blue[i-1][j-1]*matrixB[0][0] + blue[i-1][j]*matrixB[0][1] + blue[i-1][j+1]*matrixB[0][2] + blue[i][j-1]*matrixB[1][0] + blue[i][j]*matrixB[1][1] + blue[i][j+1]*matrixB[1][2] + blue[i+1][j-1]*matrixB[2][0] + blue[i+1][j]*matrixB[2][1] + blue[i+1][j+1]*matrixB[2][2]));
  	                int p = (nei[i-1][j-1]<<24) | (neib[i-1][j-1]<<16) | (neig[i-1][j-1]<<8) | neir[i-1][j-1];
  	                dst.setRGB(i-1, j-1, p);
  	            }
  	        } 
  	    }
  	    
 }
  			    
int sThreshold = 1000;

// METHOD THAT DIVIDES THE PROBLEM AND ASSIGNS IT TO THREADS
protected void compute() {
	long start = System.currentTimeMillis();
	if (mLength < sThreshold) { // IF THE SIZE IS SMALL ENOUGH
	    computeDirectly(); // A THREAD WILL DO THE FILTERING
	    return;
	}  
    int split = mLength / 2;
    invokeAll(new FilterImage(img, mStart, split, dst), new FilterImage(img, mStart + split, mLength - split, dst)); // IF THE SIZE IS NOT SNALL ENOUGH, DIVIDE IT AGAIN
    long end = System.currentTimeMillis();
    long total=end-start;
    System.out.println("Single thread: " + total + " milliseconds.");
}

}

  		  
  		

	
}