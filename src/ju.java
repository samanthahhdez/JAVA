import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ju {
	public static void main(String[] args) throws IOException {
		int rows=5, cols=5;
		int r=3, c=3;
		int rr=4, cc=4;
		int [][] matrixA = new int [rows][cols];
		int [][] matrixB = new int [r][c];
		int [][] matrixC = new int [rr][cc];
		int t=1;
		
		for(int i = 0; i < rows; i++)
	    {
	        for(int j = 0; j < cols; j++)
	        {
	            matrixA[i][j] = t;
	            t++;
	        }
	    }
		t=1;
		for(int i = 0; i < r; i++)
	    {
	        for(int j = 0; j < c; j++)
	        {
	            matrixB[i][j] = t;
	            t=-t;
	        }
	    }
		for(int i = 0; i < rows; i++)
	    {
	        for(int j = 0; j < cols; j++)
	        {
	            System.out.print(matrixA[i][j]+" ");
	        }
	        System.out.println();
	    }
		for(int i = 0; i < r; i++)
	    {
	        for(int j = 0; j < c; j++)
	        {
	        	System.out.print(matrixB[i][j]+" ");
	        }
	        System.out.println();
	    }
		for(int i = 1; i < rr; i++)
	    {
	        for(int j = 1; j < cc; j++)
	        {
	        	matrixC[i-1][j-1]=matrixA[i-1][j-1]*matrixB[0][0] + matrixA[i-1][j]*matrixB[0][1] + matrixA[i-1][j+1]*matrixB[0][2] + matrixA[i][j-1]*matrixB[1][0] + matrixA[i][j]*matrixB[1][1] + matrixA[i][j+1]*matrixB[1][2] + matrixA[i+1][j-1]*matrixB[2][0] + matrixA[i+1][j]*matrixB[2][1] + matrixA[i+1][j+1]*matrixB[2][2];   
	        }
	    }
		
		for(int i = 0; i < r; i++)
	    {
	        for(int j = 0; j < cc; j++)
	        {
	            System.out.print(matrixC[i][j]+" ");
	        }
	        System.out.println();
	    }
		
		 BufferedImage img = ImageIO.read(new File(System.getProperty("user.dir")+"\\mapache-2.jpg"));
	        int numRows = img.getHeight();
	        int numCols = img.getWidth();
		
	}
}
