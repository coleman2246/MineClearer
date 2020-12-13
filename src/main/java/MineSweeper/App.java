package MineSweeper;
import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
 

public class App extends Application{
    public GridPane grid;
    public Scene scene;
    public Board board;
    public Stage global;
    int rows;
    int cols;
    int bomb;


    @Override
    public void start(Stage stage) {
        
        global = stage;
        startScreen();
        


        //stage.setScene(scene);
        //stage.show();
    }
    
    public void startScreen(){
        Button beginner = new Button("Easy");
        Button intermediate = new Button("Intermediate");
        Button expert = new Button("Expert");
        
        beginner.setOnMouseClicked((event) -> {
            initGame(9,9,10);
        });


        intermediate.setOnMouseClicked((event) -> {
            initGame(16,16,40);
        });


        expert.setOnMouseClicked((event) -> {
            initGame(30,16,99);
        });

        HBox container = new HBox();


        container.getChildren().addAll(beginner,intermediate,expert);

        scene = new Scene(container);
        global.setScene(scene);
        global.show();
        
    }

    public void initGame(int row, int column, int bombs){
        this.rows = row;
        this.cols = column;
        this.bomb = bombs;

        this.board = new Board(rows,cols,this.bomb);

        System.out.println(board.toString());

        updateBoard();
    }
    

    public void updateBoard(){
        grid = new GridPane();


        for(int i = 0; i < rows; i++){
            for(int x = 0; x< cols; x++){
                String title = "  ";
                Button button = new Button();

                Tile currentTile = board.board.get(i).get(x); 

                if (currentTile.isFlagged){
                    title = "f ";
                }else if(currentTile.isBomb & currentTile.isVisible){
                    title = "*";
                }else if(board.adjacentBombs(i, x) > 0 & currentTile.isVisible){
                    title = Integer.toString(board.adjacentBombs(i, x));

                    Color color = Color.web("#0000fd");
                    
                    switch ( board.adjacentBombs(i, x) ){
                        case 2:
                            color = Color.web("#047e03");
                            break;
                        case 3:
                            color = Color.web("#fb0101");
                            break;
                        case 4:
                            color = Color.web("#010082");
                            break;
                        case 5:
                            color = Color.web("#810103");
                            break;
                        case 6:
                            color = Color.web("#038081");
                            break;
                        case 7:
                            color = Color.web("#000000");
                            break;
                        case 8:
                            color = Color.web("#808080");
                            break;
                    }   
                    button.setTextFill(color);
                } 

                button.setText(title);
                

                if(!currentTile.isVisible){
                    button.setStyle("-fx-background-color: #afaeae; -fx-border-color: #c4c4c4; -fx-border-width: 2px;");

                }else{
                    button.setStyle(" -fx-border-color: #c4c4c4; -fx-border-width: 2px;");
                }
                button.setOnMouseClicked((event) -> {
                    // Button was clicked, do something...
                    int columnIndex = GridPane.getColumnIndex(button);
                    int rowIndex = GridPane.getRowIndex(button);


                    if (event.getButton() == MouseButton.SECONDARY){
                        if(board.board.get(columnIndex).get(rowIndex).isFlagged ){
                            board.board.get(columnIndex).get(rowIndex).isFlagged = false;
                        // if its already visible you cannot flag it 
                        }else if(!board.board.get(columnIndex).get(rowIndex).isVisible){
                            board.board.get(columnIndex).get(rowIndex).isFlagged = true;
                            
                        }
                        
                    }else if(event.getButton() == MouseButton.PRIMARY){
                        Tile selectedTile = board.board.get(columnIndex).get(rowIndex); 
                        if(!selectedTile.isBomb){
                            selectedTile.isVisible = true;
                            board.totalDisplayed++;
                            if(board.adjacentBombs(columnIndex,rowIndex ) == 0){
                                board.reccurseDisplayTiles(columnIndex,rowIndex);
                                //  recursively display the adjacent tiles 
                            }
                        }else if (selectedTile.isBomb){
                            this.board.bombSelected();
                            Label secondLabel = new Label("You Have Lost. Press Restart");
 
                            StackPane secondaryLayout = new StackPane();
                            secondaryLayout.getChildren().add(secondLabel);
                 
                            Scene secondScene = new Scene(secondaryLayout, 230, 100);
                 
                            // New window (Stage)
                            Stage newWindow = new Stage();
                            newWindow.setTitle("Game Over");
                            newWindow.setScene(secondScene);
                 
                            // Specifies the modality for new window.
                            newWindow.initModality(Modality.WINDOW_MODAL);
                 
                            // Specifies the owner Window (parent) for new window
                            newWindow.initOwner(global);
                 
                            // Set position of second window, related to primary window.
                            newWindow.setX(global.getX() + 200);
                            newWindow.setY(global.getY() + 100);
                 
                            newWindow.show();
                        }
                    }

                    if(this.board.isGameOver()){
                        // will show entire board
                        this.board.bombSelected();
                        Label secondLabel = new Label("You Have Won. Press Restart");
 
                        StackPane secondaryLayout = new StackPane();
                        secondaryLayout.getChildren().add(secondLabel);
                
                        Scene secondScene = new Scene(secondaryLayout, 230, 100);
                
                        // New window (Stage)
                        Stage newWindow = new Stage();
                        newWindow.setTitle("Game Over");
                        newWindow.setScene(secondScene);
                
                        // Specifies the modality for new window.
                        newWindow.initModality(Modality.WINDOW_MODAL);
                
                        // Specifies the owner Window (parent) for new window
                        newWindow.initOwner(global);
                
                        // Set position of second window, related to primary window.
                        newWindow.setX(global.getX() + 200);
                        newWindow.setY(global.getY() + 100);
                
                        newWindow.show();
                    }

                    updateBoard();
                });

                grid.add(button,i,x);
            }
        }

        
        Button newGame = new Button("New Game");

        newGame.setOnMouseClicked((event) -> {
            startScreen();
        });

        Button restart = new Button("Restart");


        restart.setOnMouseClicked((event) -> {
            initGame(this.rows, this.cols, this.bomb);
        });



        HBox menu = new HBox();

        menu.getChildren().addAll(newGame,restart);


        VBox gameContainer = new VBox();

        gameContainer.getChildren().addAll(menu,grid);
        
        scene = new Scene(gameContainer);
        global.setScene(scene);
        global.show();
    }
}
