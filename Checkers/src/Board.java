import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board extends Application {
    private final int squareSize = 50;
    private GridPane grid;
    public GameManager gM;
    private Label playerColorLabel;
    String selectedColour;
    private Label turnLabel = new Label();

    private String wStr = "Turn: White";
    private String rStr = "Turn: Red";
    private PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
    VBox labelsContainer = new VBox(10);
    ArrayList<GridTuple> moves = new ArrayList<>();
    int playerClickedRow;
    Circle selectedCheckerGrid;
    Checker selectedCheckerState;
    private void playerTurn(int r, int c, Circle ci){
        String play;
        ArrayList<GridTuple> allPlayerMoves = new ArrayList<>();
        if (turnLabel.getText().equals(wStr)){
            play = "White";
        }
        else{
            play = "Red";
        }

        if(play.equals("White")){allPlayerMoves = gM.checkAllValidMoves(gM.getBoard(),gM.white);}
        else {allPlayerMoves = gM.checkAllValidMoves(gM.getBoard(),gM.red);}

        boolean hasCaptureMoves = false;
        for (GridTuple move : allPlayerMoves) {
            if (move.capture) {
                hasCaptureMoves = true;
                break;
            }
        }


        moves = gM.checkAvailableMoves(selectedColour,r,c);

        if (hasCaptureMoves) {
            moves.removeIf(move -> !move.capture);
        }

        selectedCheckerGrid = ci;
        selectedCheckerState = gM.board[r][c];
        if (!moves.isEmpty()){
            System.out.println("Make a move");
        } else if (hasCaptureMoves){
            System.out.println("You must capture!");
        }
    }
    private void AITurn(){
        System.out.println("AI turn started");
        if(turnLabel.getText().equals(rStr)){
            moves.add(gM.findBestMove(gM.getBoard(),gM.red, 0));
        }
        else{
            moves.add(gM.findBestMove(gM.getBoard(),gM.white, 0));
        }
        selectedCheckerState = moves.get(0).oriChecker;
        selectedCheckerGrid = findCircleAtPosition(moves.get(0).oriChecker.getPosX(), moves.get(0).oriChecker.getPosY());
        movePieceEvent(moves.get(0).X, moves.get(0).Y);//*/
        if(selectedCheckerState.isKing() == false){
           checkIfCanBeKing(moves.get(0).oriChecker, moves.get(0).X);
        }
        endOfTurnEvent();
    }

    public void checkIfCanBeKing(Checker c, int row){
        boolean becameKing = false;
        if(turnLabel.getText().equals(wStr)){
            if(row == 7){
                c.makeKing();
                becameKing = true;
            }
        }
        else{
            if(row == 0){
                c.makeKing();
                becameKing = true;
            }
        }

        if(becameKing){
            Circle checkerCircle = findCircleAtPosition(c.getPosX(), c.getPosY());
            changeToKingAppearance(checkerCircle, c);
        }
    }
    public Circle findCircleAtPosition(int row, int col) {
        ObservableList<Node> children = grid.getChildren();
        for (Node node : children) {
            //System.out.println("Node: " + node);
            //System.out.println("Row index: " + grid.getRowIndex(node) + " " + row);
            //System.out.println("Column index: " + grid.getColumnIndex(node) + " " + col);
            if (node instanceof Circle && grid.getRowIndex(node) == row && grid.getColumnIndex(node) == col) {
                return (Circle) node;
            }
        }
        return null;
    }

    public LinearGradient createGradientForWhiteKing() {
        return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.BLACK));
    }

    public LinearGradient createGradientForRedKing() {
        return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.RED),
                new Stop(1, Color.PURPLE));
    }

    public void changeToKingAppearance(Circle checkerCircle, Checker checker) {
        LinearGradient gradient;
        if (checker.getPlayer().equals("White")) {
            gradient = createGradientForWhiteKing();
        } else {
            gradient = createGradientForRedKing();
        }
        checkerCircle.setFill(gradient);
    }

    private void switchPlayerTurn(){

        if (turnLabel.getText().equals(wStr)){
            //System.out.println("This should fire white");
            turnLabel.setText(rStr);
            gM.switchPlayer();
        }
        else if(turnLabel.getText().equals(rStr)){
            //System.out.println("This should fire red");
            turnLabel.setText(wStr);
            gM.switchPlayer();
        }
    }
    private void addCheckerKing(int row, int col, LinearGradient color) {
        Circle checker = new Circle((double) squareSize / 2 - 5); // Slightly smaller than half the square
        checker.setFill(color);
        // Final variables for lambda capture

        checker.setOnMouseClicked(event -> {
            if(gM.isPlayerTurn) {
                if(turnLabel.getText().equals("Turn: White") && gM.board[row][col].getPlayer().equals("White")){
                    //System.out.println("Row: " + gM.board[row][col].getPosX() + " " + "Column: " + gM.board[row][col].getPosY());
                    playerTurn(row, col, checker);
                }
                if(turnLabel.getText().equals("Turn: Red") && gM.board[row][col].getPlayer().equals("Red")){
                    //System.out.println(gM.board[row][col].getPlayer());
                    playerTurn(row, col, checker);
                }
            }
        });

        grid.add(checker, col, row);
        //System.out.println("Added Circle at row " + row + ", column " + col);
        GridPane.setColumnIndex(checker, col);
        GridPane.setRowIndex(checker,row);
        GridPane.setHalignment(checker, HPos.CENTER);
        GridPane.setValignment(checker, VPos.CENTER);
        if (selectedCheckerState.getPlayer().equals("White")){
            gM.addCheckerAtPos(new Checker("White", row, col), "White");
        }
        else {
            gM.addCheckerAtPos(new Checker("Red", row, col), "Red");
        }
    }

    private void addCheckerNormal(int row, int col, Color color) {
        Circle checker = new Circle((double) squareSize / 2 - 5); // Slightly smaller than half the square
        checker.setFill(color);
        // Final variables for lambda capture

        checker.setOnMouseClicked(clickEvent -> {
            if(gM.isPlayerTurn) {
                if(turnLabel.getText().equals("Turn: White") && gM.board[row][col].getPlayer().equals("White")){
                    //System.out.println("Row: " + gM.board[row][col].getPosX() + " " + "Column: " + gM.board[row][col].getPosY());
                    playerTurn(row, col, checker);
                }
                if(turnLabel.getText().equals("Turn: Red") && gM.board[row][col].getPlayer().equals("Red")){
                    //System.out.println(gM.board[row][col].getPlayer());
                    playerTurn(row, col, checker);
                }
            }
        });

        grid.add(checker, col, row);
        //System.out.println("Added Circle at row " + row + ", column " + col);
        GridPane.setColumnIndex(checker, col);
        GridPane.setRowIndex(checker,row);
        GridPane.setHalignment(checker, HPos.CENTER);
        GridPane.setValignment(checker, VPos.CENTER);
        if (color ==  Color.WHITE){
            gM.addCheckerAtPos(new Checker("White", row, col), "White");
        }
        else {
            gM.addCheckerAtPos(new Checker("Red", row, col), "Red");
        }
    }


    private void removeCheckerAtPos(int row, int col) {
        Circle checkerToRemove = null;
        //System.out.println("Removing at Row: " + row + ", Column: " + col);
        for (Node node : grid.getChildren()) {
            if (node instanceof Circle) {
                int nodeRow = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
                int nodeCol = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);

                //System.out.println("Checker found at Row: " + nodeRow + ", Column: " + nodeCol);

                if (nodeRow == row && nodeCol == col) {
                    checkerToRemove = (Circle) node;
                    break;
                }
            }
        }
        if (checkerToRemove != null) {
            System.out.println("Removing checker at Row: " + row + ", Column: " + col);
            grid.getChildren().remove(checkerToRemove);
        } else {
            System.out.println("No checker found at Row: " + row + ", Column: " + col);
        }
    }


    private void addCheckersInit() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row < 3 && (row + col) % 2 != 0) {
                    addCheckerNormal(row, col, Color.WHITE);
                } else if (row > 4 && (row + col) % 2 != 0) {
                    addCheckerNormal(row, col, Color.RED);
                }
            }
        }
    }

    public void endOfTurnEvent(){
        int whiteCheckersCount = countCheckers("White", gM.getBoard());
        int redCheckersCount = countCheckers("Red", gM.getBoard());

        if (whiteCheckersCount == 0 || redCheckersCount == 0) {
            String winner = whiteCheckersCount == 0 ? "Red" : "White";
            turnLabel.setText(winner + " is the winner!");
        }
        else{
            if(moves != null){moves.clear();}
            switchPlayerTurn();
            if(gM.isPlayerTurn == false) {
                pause.play();
            }
            selectedCheckerGrid = null;
            selectedCheckerState = null;
        }
    }

    private int countCheckers(String playerColour, Checker[][] board) {
        int count = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Checker checker = board[row][col];
                if (checker != null && checker.getPlayer().equals(playerColour)) {
                    count++;
                }
            }
        }
        return count;
    }


    public void movePieceEvent(int x, int y){
        Integer row = GridPane.getRowIndex(selectedCheckerGrid);
        Integer col = GridPane.getColumnIndex(selectedCheckerGrid);
        boolean addiMoved = false;
        if(moves != null && !moves.isEmpty()){
            for (GridTuple gT:moves) {
                if (gT.X == x && gT.Y == y){
                    removeCheckerAtPos(row, col);
                    if(turnLabel.getText().equals(wStr)){
                        if(gT.capture){
                            //System.out.println("Check white1");
                            if(selectedCheckerState.isKing()){
                                LinearGradient lG = createGradientForWhiteKing();
                                addCheckerKing(gT.X, gT.Y, lG);
                                selectedCheckerState.makeKing();}
                            else{// check regicide
                                if(gT.capChecker.isKing()){
                                    LinearGradient lG = createGradientForWhiteKing();
                                    addCheckerKing(gT.X, gT.Y, lG);
                                    selectedCheckerState.makeKing();
                                }
                                else{addCheckerNormal(gT.X, gT.Y, Color.WHITE);}
                            }
                            gM.moveCheckerTo(selectedCheckerState, gT.X, gT.Y);
                            removeCheckerAtPos(gT.capChecker.getPosX(), gT.capChecker.getPosY());
                            gM.removeCheckerAtPos(gT.capChecker.getPosX(), gT.capChecker.getPosY());
                            gM.takeAwayGamePieceFromPlayer("White");

                            //Additional captures
                            ArrayList<GridTuple> AddiMoves = new ArrayList<>();
                            AddiMoves = gM.checkAvailableMoves("White", selectedCheckerState.getPosX(), selectedCheckerState.getPosY());
                            AddiMoves.removeIf(move -> !move.capture);
                            if(!AddiMoves.isEmpty()){
                                //there are additional moves
                                addiMoved = true;
                                if(gM.isPlayerTurn){
                                    playerTurn(selectedCheckerState.getPosX(), selectedCheckerState.getPosY(), findCircleAtPosition(selectedCheckerState.getPosX(), selectedCheckerState.getPosY()));
                                }
                                else {
                                    int bestEval = (turnLabel.getText().equals("White")) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                                    GridTuple bestMove = null;
                                    for (GridTuple gridTupe : AddiMoves) {
                                        Checker[][] genGameState = gM.produceGameState(gridTupe, gM.board);
                                        int eval = gM.minimaxWithAlphaBeta(genGameState, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, gM.red);

                                        if (eval > bestEval) {
                                            bestEval = eval;
                                            bestMove = gT;
                                        }
                                    }
                                    assert bestMove != null;
                                    movePieceEvent(bestMove.X,bestMove.Y);
                                }
                            }
                        }
                        else{
                            if(selectedCheckerState.isKing()){
                                LinearGradient lG = createGradientForWhiteKing();
                                addCheckerKing(gT.X, gT.Y, lG);}
                            else{addCheckerNormal(gT.X, gT.Y, Color.WHITE);}
                            //System.out.println("Check white2");
                            gM.moveCheckerTo(selectedCheckerState, gT.X, gT.Y);
                        }
                    }
                    else{ // red player
                        if(gT.capture){
                            //System.out.println("Check red1");
                            if(selectedCheckerState.isKing()){
                                LinearGradient lG = createGradientForRedKing();
                                addCheckerKing(gT.X, gT.Y, lG);
                                selectedCheckerState.makeKing();
                            }
                            else{
                                if(gT.capChecker.isKing()){
                                LinearGradient lG = createGradientForRedKing();
                                addCheckerKing(gT.X, gT.Y, lG);
                                selectedCheckerState.makeKing();
                            }
                            else{addCheckerNormal(gT.X, gT.Y, Color.RED);}
                            }
                            gM.moveCheckerTo(selectedCheckerState, gT.X, gT.Y);
                            removeCheckerAtPos(gT.capChecker.getPosX(), gT.capChecker.getPosY());
                            gM.removeCheckerAtPos(gT.capChecker.getPosX(), gT.capChecker.getPosY());
                            gM.takeAwayGamePieceFromPlayer("Red");

                            //Additional captures

                            ArrayList<GridTuple> AddiMoves = new ArrayList<>();
                            AddiMoves = gM.checkAvailableMoves("Red", selectedCheckerState.getPosX(), selectedCheckerState.getPosY());
                            AddiMoves.removeIf(move -> !move.capture);
                            if(!AddiMoves.isEmpty()){
                                //there are additional moves
                                addiMoved = true;
                                if(gM.isPlayerTurn){
                                    playerTurn(selectedCheckerState.getPosX(), selectedCheckerState.getPosY(), findCircleAtPosition(selectedCheckerState.getPosX(), selectedCheckerState.getPosY()));
                                }
                                else {
                                    int bestEval = 0;
                                    GridTuple bestMove = AddiMoves.get(0);
                                    for (GridTuple gridTupe : AddiMoves) {
                                        Checker[][] genGameState = gM.produceGameState(gridTupe, gM.board);
                                        int eval = gM.minimaxWithAlphaBeta(genGameState, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, gM.white);

                                        if (eval > bestEval) {
                                            bestEval = eval;
                                            bestMove = gT;
                                        }
                                    }
                                    assert bestMove != null;
                                    movePieceEvent(bestMove.X,bestMove.Y);
                                }
                            }

                        }
                        else{
                            if(selectedCheckerState.isKing()){
                                LinearGradient lG = createGradientForRedKing();
                                addCheckerKing(gT.X, gT.Y, lG);
                            }
                            else{addCheckerNormal(gT.X, gT.Y, Color.RED);}
                            //System.out.println("Check red2");
                            gM.moveCheckerTo(selectedCheckerState, gT.X, gT.Y);
                        }
                    }
                }
            }

            if(!addiMoved) {
                if (gM.isPlayerTurn) {
                    if (selectedCheckerState.isKing() == false) {
                        checkIfCanBeKing(selectedCheckerState, playerClickedRow);
                    }
                    endOfTurnEvent();
                }
            }
        }
    }
    @Override
    public void start(Stage stage) {
        ArrayList<String> choices = new ArrayList<>();
        choices.add("White");
        choices.add("Red");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("White", choices);
        dialog.setTitle("Choose Colour");
        dialog.setHeaderText("Select the colour you want to play as:");
        dialog.setContentText("Colour:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            selectedColour = result.get();

            // Proceed to ask for difficulty setting
            ArrayList<String> difficultyChoices = new ArrayList<>();
            difficultyChoices.add("Easy");
            difficultyChoices.add("Medium");
            difficultyChoices.add("Hard");
            ChoiceDialog<String> difficultyDialog = new ChoiceDialog<>("Medium", difficultyChoices);
            difficultyDialog.setTitle("Select Difficulty");
            difficultyDialog.setHeaderText("Choose your difficulty level:");
            difficultyDialog.setContentText("Difficulty:");

            Optional<String> difficultyResult = difficultyDialog.showAndWait();
            difficultyResult.ifPresent(selectedDifficulty -> {
                if (selectedDifficulty.equals("Easy")) {
                    gM = new GameManager(new Player("White"), new Player("Red"), 1);
                } else if (selectedDifficulty.equals("Medium")) {
                    gM = new GameManager(new Player("White"), new Player("Red"), 4);
                } else if (selectedDifficulty.equals("Hard")) {
                    gM = new GameManager(new Player("White"), new Player("Red"), 8);
                }
            });

            gM.setPlayer(selectedColour);
            // Grid for the checkers board
            grid = new GridPane();
            int squareSize = 50; // Size of each square
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Rectangle square = getRectangle(squareSize, row, col);
                    grid.add(square, col, row);
                }
            }

            addCheckersInit();

            // StackPane for centering and border
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(grid);
            stackPane.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: white;");
            stackPane.setMaxSize(GridPane.USE_PREF_SIZE, GridPane.USE_PREF_SIZE); // Set max size to the preferred size of the grid

            // Labels for player color and turn
            playerColorLabel = new Label("Player Color: " + selectedColour);
            if (selectedColour.equals("White")){
                turnLabel.setText(wStr);
            } else if (selectedColour.equals("Red")) {
                turnLabel.setText(wStr);
                AITurn();
            }


            // VBox for player color and turn labels
            labelsContainer.setAlignment(Pos.CENTER);
            labelsContainer.getChildren().add(playerColorLabel);
            labelsContainer.getChildren().add(turnLabel);

            pause.setOnFinished(event -> {
                // Code to execute after the delay
                AITurn();
            });

            // Root layout
            BorderPane root = new BorderPane();
            root.setTop(labelsContainer); // Adding labels container to the top
            root.setCenter(stackPane); // Adding StackPane to the center

            // Setting up the scene and stage
            Scene scene = new Scene(root, 800, 800); // Adjust size as needed
            stage.setScene(scene);
            stage.setTitle("Checkers Game");
            stage.show();
        }
    }

    private Rectangle getRectangle(int squareSize, int row, int col) {
        Rectangle square = new Rectangle(squareSize, squareSize);
        Color color = (row + col) % 2 == 0 ? Color.WHITE : Color.BLACK;
        square.setFill(color);
        square.setOnMouseClicked(event ->{
            boolean found = false;
            if(moves != null && !moves.isEmpty()) {
                for (GridTuple gt : moves) {
                    if (gt.X == row && gt.Y == col) {
                        found = true;
                        break;
                    }
                }
            }
            if(found){
                playerClickedRow = row;
                movePieceEvent(row, col);
            }
        });
        return square;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

