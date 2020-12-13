package MineSweeper;
import java.util.ArrayList;
import java.util.Random;




public class Board{
    ArrayList<ArrayList<Tile>> board;
    int bombs,rows,columns,totalToDisplay,totalDisplayed;

    
    public Board(int rows, int columns, int bombCount){
        this.board = new ArrayList<ArrayList<Tile>>(); 
        this.bombs = 0;
        this.rows = rows;
        this.columns = columns;
        this.totalToDisplay = (this.rows * this.columns) - bombCount;
        this.totalDisplayed = 0;
        for(int currentRow = 0; currentRow < rows; currentRow++){
            ArrayList<Tile> newRow = new ArrayList<Tile>();
            for(int currentColumn = 0; currentColumn < columns; currentColumn++){
                newRow.add(new Tile());
            }
            this.board.add(newRow);
        }
   

        // filling board with bombs
        Random ran = new Random();
        while(bombs < bombCount){
            int ranRow = ran.nextInt(rows);
            int ranCol = ran.nextInt(columns);

            if(!this.board.get(ranRow).get(ranCol).isBomb){
                this.board.get(ranRow).get(ranCol).isBomb = true;
                bombs++;
            }
        }
    }
 
    // returns false if out of bounds
    public Boolean inBounds(int testRow, int testCol){
        return testRow < this.rows & testCol < this.columns & testRow >= 0 & testCol >= 0 ;
    }
    
    public ArrayList<Tile> adjacentTiles(int row, int col){
        ArrayList<Tile> tiles = new ArrayList<Tile>(); 
        for(int currentRow = -1; currentRow < 2; currentRow++){
             for(int currentCol = -1; currentCol < 2; currentCol++){
                                
                if(inBounds(row+currentRow, col+currentCol) & !(currentCol == 0 & currentRow == 0)){
                    tiles.add(this.board.get(row+currentRow).get(col+currentCol));
                }
    
             }
         }
         return tiles;
    }

    public int adjacentBombs(int row, int col){
        ArrayList<Tile> tiles = adjacentTiles(row, col);
        int bombs = 0;
        for(Tile i : tiles){
            if (i.isBomb){
                bombs++;
            }
        }
        return bombs;
    }

    public String toString(){
        String  returnString = "";
        for(int currentColumn = 0; currentColumn < this.columns; currentColumn++){
            String toPrintString = "";
            for(int  currentRow = 0; currentRow  < this.rows; currentRow++){
                if(this.board.get(currentRow).get(currentColumn).isBomb){
                    toPrintString += "*";
                }else if(adjacentBombs(currentRow, currentColumn) > 0){
                    toPrintString += adjacentBombs(currentRow, currentColumn);
                }else{
                    toPrintString += "x";
                }
            }
            returnString += toPrintString+"\n";
        }
    
        return returnString;
    }
    
    public void reccurseDisplayTiles(int recurse_row,int recurse_column){
        for(int currentRow = -1; currentRow < 2; currentRow++){
            for(int currentCol = -1; currentCol < 2; currentCol++){
                               
               if(inBounds(recurse_row+currentRow, recurse_column+currentCol) & !(currentCol == 0 & currentRow == 0)){
                    Tile tile = board.get(recurse_row+currentRow).get(recurse_column+currentCol); 
                    
                    if(tile.isVisible | tile.isFlagged){
                        continue;
                    }
                    if( adjacentBombs(recurse_row+currentRow, recurse_column+currentCol) > 0){
                        this.totalDisplayed++;
                        tile.isVisible = true;
                        continue;
                    }         
                    

                    if(!(tile.isBomb)){
                        tile.isVisible = true;
                        reccurseDisplayTiles(recurse_row+currentRow, recurse_column+currentCol );
                        this.totalDisplayed++;

                    }
                }
            }
        }
    }

    public boolean isGameOver(){        
        return this.totalDisplayed == this.totalToDisplay;
    }
    public void bombSelected(){
        for(int currentRow = 0; currentRow < this.rows; currentRow++){
            for(int currentCol=0; currentCol < this.columns; currentCol++){
                this.board.get(currentRow).get(currentCol).isVisible = true;;
            }
        }
    }
}