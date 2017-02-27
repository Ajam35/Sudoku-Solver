//Sodoku puzzle lab
//Andrew LaRoche
//CS310 Data Structures


import java.applet.* ;
import java.awt.* ;
import java.util.Scanner;
import java.io.File;
import java.util.StringTokenizer;

/**
 * Solves a sudoku puzzle by recursion and backtracking
 */
public class SimplifiedSudoku extends Applet implements Runnable
{
  
  /** The model */
  protected int model[][] ;
  
  /** The view */
  protected Button view[][] ;
  
  //This method creates a model that has 16 boxes across and 16 boxes down 
  //and reads the input from "solution.txt" to fill in various boxes that help solve the puzzle.
  protected void createModel()
  {
    model = new int[16][16] ;
    try
    {
      //variables
      String input;
      String[] tokenz=new String[4];
      int row = 0;
      int col = 0;
      int value = 0;
      
      File file = new File("solution.txt");
      Scanner scan = new Scanner(file);
      int i =0;
      String temp="";
      while (scan.hasNext())
      {
        input = scan.nextLine(); 
        StringTokenizer st = new StringTokenizer(input);
        while (st.hasMoreTokens()) {
          
          temp= st.nextToken();
          if(i<2){
            temp = temp.substring(0, temp.length()-1);
          }
          tokenz[i++] = temp;
        }
        i=0;
        System.out.println(input);
        
        for(int r = 0; r < model.length; r++)
        {
          for(int c = 0; c < model[r].length; c++)
          {
            
            model[Integer.parseInt(tokenz[0])][Integer.parseInt(tokenz[1])] = Integer.parseInt(tokenz[2]);
          }
        }
                
      }
    }
    catch (Exception e)
    {
      System.out.println(e);
      e.printStackTrace();
    }
    
    
  }
  
  /** Creates an empty view */
  protected void createView()
  {
    setLayout( new GridLayout(16,16) ) ;
    
    view = new Button[16][16] ;
    
    // Create an empty view
    for( int row = 0; row < 16; row++ )
      for( int col = 0; col < 16; col++ )
    {
      view[row][col]  = new Button() ;
      add( view[row][col] ) ;
    }
  }
  //here is where I take view and set a background color for each numerical value being passed into the puzzle.
  
  protected void updateView() //colors here
  {
    
    for( int row = 0; row < 16; row++ )
    {
      for( int col = 0; col < 16; col++ )
      {
        if( model[row][col] != 0 )
        {
      
          if( model[row][col] == 1)
    {
      view[row][col].setBackground(Color.BLUE);
    }

    view[row][col].setLabel( String.valueOf(model[row][col]) ); 
        }
   else 
     view[row][col].setLabel( "" ) ;
}
    }  
    //view[row][col].setLabel( String.valueOf(model[row][col]) ) ;
    //if / else if statements view[row][col] (model[row][col]==1-16 set background(color.RGB); Color name = new color(RGB val);
    
  }
  
  /** This method is called by the browser when the applet is loaded */
  public void init()
  {
    createModel() ;
    createView() ;
    updateView() ;
  }
  
  /** Checks if num is an acceptable value for the given row */
  protected boolean checkRow( int row, int num )
  {
    for( int col = 0; col < 16; col++ )
      if( model[row][col] == num )
      return false ;
    
    return true ;
  }
  
  /** Checks if num is an acceptable value for the given column */
  protected boolean checkCol( int col, int num )
  {
    for( int row = 0; row < 16; row++ )
      if( model[row][col] == num )
      return false ;
    
    return true ;
  }
  
  /** Checks if num is an acceptable value for the box around row and col */
  protected boolean checkBox( int row, int col, int num )
  {
    row = (row / 4) * 4 ; //*** change from 3s to row/4 col/4 and all 3s->4s
    col = (col / 4) * 4 ;
    
    for( int r = 0; r < 4; r++ ) // *** here as well
      for( int c = 0; c < 4; c++ )
      if( model[row+r][col+c] == num )
      return false ;
    
    return true ;
  }
  
  /** This method is called by the browser to start the applet */
  public void start()
  {
    // This statement will start the method 'run' to in a new thread
    (new Thread(this)).start() ;
  }
  
  /** The active part begins here */
  public void run()
  {
    try
    {
      // Let the observers see the initial position
      Thread.sleep( 0 ) ;
      
      // Start to solve the puzzle in the left upper corner
      solve( 0, 0 ) ;
    }
    catch( Exception e )
    {
      System.out.println(e);
    }
  }
  
  /** Recursive function to find a valid number for one single cell */
  public void solve( int row, int col ) throws Exception
  {
    //System.out.println(row + " " + col);
    // Throw an exception to stop the process if the puzzle is solved
    if( col > 15 )
      throw new Exception( "Solution found" ) ;
    
    // If the cell is not empty, continue with the next cell
    if( model[row][col] != 0 )
      next( row, col ) ;
    else
    {
      // Find a valid number for the empty cell
      for( int num = 1; num < 17; num++ )
      {
        if( checkRow(row,num) && checkCol(col,num) && checkBox(row,col,num) )
        {
          model[row][col] = num ;
          updateView() ;
          
          // Let the observer see it
          Thread.sleep(0) ; //used to be 100***
          
          // Delegate work on the next cell to a recursive call
          next( row, col ) ;
        }
      }
      
      // No valid number was found, clean up and return to caller
      model[row][col] = 0 ;
      updateView() ;
    }
  }
  
  /** Calls solve for the next cell */
  public void next( int row, int col ) throws Exception
  {
    if( row < 15 )
      solve( row +1, col ) ;  //re-arrange the row and col variables to solve vertically (#4)
    else
      solve( 0, col + 1 ) ;
  }
}