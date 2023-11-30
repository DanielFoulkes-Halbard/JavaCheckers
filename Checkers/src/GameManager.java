import javafx.scene.layout.GridPane;
import javafx.application.Application;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class GameManager {
    Checker[][] board = new Checker[8][8];
    Player white;
    Player red;
    boolean isPlayerTurn;
    Integer maxDepth;

    public GameManager(Player w, Player r, Integer d){
        white = w;
        red = r;
        maxDepth = d;
    }

    public int getGoalState1(Player w, Player r){
        return 0;
    }

    public void setPlayer(String choice){
        if (Objects.equals(choice, "White")){
            white.setAI(false);
            red.setAI(true);
            System.out.println("Player is White");
            setIsPlayersTurn(true);
        }
        else if(Objects.equals(choice, "Red")){
            white.setAI(true);
            red.setAI(false);
            System.out.println("Player is Red");
            setIsPlayersTurn(false);
        }
        else {System.out.println("None");}
    }

    public void switchPlayer(){
        isPlayerTurn = !isPlayerTurn;
        if (isPlayerTurn){
            System.out.println("Players Turn");
        }
        else {
            System.out.println("AI's Turn");
        }
    }

    public Checker[][] getBoard(){
        return board;
    }
    public void addCheckerAtPos(Checker c, String p){
        board[c.getPosX()][c.getPosY()] = c;
        if(p.equals("White")){white.addPlayerGamePiece(c);c.setOwner(white);}
        if(p.equals("Red")){red.addPlayerGamePiece(c);c.setOwner(red);}
    }

    public void removeCheckerAtPos(int row, int col){
        board[row][col] = null;
    }

    public void removeCheckerAtPos(int row, int col, Checker[][] gS){
        gS[row][col] = null;
    }

    public void takeAwayGamePieceFromPlayer(String player){
        if(player.equals("White")){white.minusCheckerCount();}
        else{red.minusCheckerCount();}
    }

    public void moveCheckerTo(Checker c, int x, int y){
        System.out.println(x + "" + y);
        int currPosX = c.getPosX();
        int currPosY = c.getPosY();
        //System.out.println("Current Position of Checker: " + currPosX + " " + currPosY);
        removeCheckerAtPos(currPosX, currPosY);
        c.setPosX(x);
        c.setPosY(y);
        board[x][y] = c;
        //System.out.println("Removed from: " + currPosX + " " + currPosY +  " To: " + x + " " + y);
    }

    public void moveCheckerTo(Checker c, int x, int y, Checker[][] gS){
        int currPosX = c.getPosX();
        int currPosY = c.getPosY();
        removeCheckerAtPos(currPosX, currPosY, gS);

        Checker newChecker = new Checker(c.getPlayer(), c.getPosX(),c.getPosY());
        newChecker.setPosX(x);
        newChecker.setPosY(y);
        gS[x][y] = c;
    }


    public void setIsPlayersTurn(boolean b){
        isPlayerTurn = b;
    }

    public ArrayList<GridTuple> checkAvailableMoves(String player, int r, int c){
        Checker clickedPiece = board[r][c];
        ArrayList<GridTuple> captureMoves = new ArrayList<>();
        ArrayList<GridTuple> moves = new ArrayList<>();
        // for White player
        if (player.equals("White")){
            if(clickedPiece.isKing()){
                // backwards movement
                // Backward movement to the left
                    if (r - 1 >= 0 && c - 1 >= 0 && board[r - 1][c - 1] == null) {
                        moves.add(new GridTuple(r - 1, c - 1, false, null, clickedPiece));
                    }
                    // Backward capture to the left
                    else if (r - 1 >= 0 && c - 1 >= 0 && isOpponentPiece(board[r - 1][c - 1], "Red") &&
                            r - 2 >= 0 && c - 2 >= 0 && board[r - 2][c - 2] == null) {
                        moves.add(new GridTuple(r - 2, c - 2, true, board[r - 1][c - 1], clickedPiece));
                    }

                    // Backward movement to the right
                    if (r - 1 >= 0 && c + 1 < board[r].length && board[r - 1][c + 1] == null) {
                        moves.add(new GridTuple(r - 1, c + 1, false, null, clickedPiece));
                    }
                    // Backward capture to the right
                    else if (r - 1 >= 0 && c + 1 < board[r].length && isOpponentPiece(board[r - 1][c + 1], "Red") &&
                            r - 2 >= 0 && c + 2 < board[r].length && board[r - 2][c + 2] == null) {
                        moves.add(new GridTuple(r - 2, c + 2, true, board[r - 1][c + 1], clickedPiece));
                    }
                    if(r + 1 < board.length && c - 1 >= 0 && board[r + 1][c - 1] == null){
                        moves.add(new GridTuple(r+1, c-1, false, null,clickedPiece));
                    }
                    else if(r + 1 < board.length && c - 1 >= 0 && isOpponentPiece(board[r+1][c-1], "Red")
                            && r+2 < board.length && c-2 >= 0 && board[r+2][c-2] ==null){//if there is a piece in the way
                        moves.add(new GridTuple(r+2, c-2, true, board[r+1][c-1], clickedPiece));
                    }

                    if(r + 1 < board.length && c + 1 < board[r].length && board[r + 1][c + 1] == null){
                        moves.add(new GridTuple(r+1, c+1, false, null, clickedPiece));
                    }
                    else if(r + 1 < board.length && c + 1 < board[r].length && isOpponentPiece(board[r+1][c+1], "Red") &&
                            r +2 < board.length && c+2 < board[r].length && board[r+2][c+2] == null){// if there is a piece in the way
                        moves.add(new GridTuple(r+2, c+2, true, board[r+1][c+1], clickedPiece));
                    }
                }
            else{
                if(r + 1 < board.length && c - 1 >= 0 && board[r + 1][c - 1] == null){
                    moves.add(new GridTuple(r+1, c-1, false, null,clickedPiece));
                }
                else if(r + 1 < board.length && c - 1 >= 0 && isOpponentPiece(board[r+1][c-1], "Red")
                        && r+2 < board.length && c-2 >= 0 && board[r+2][c-2] ==null){//if there is a piece in the way
                        moves.add(new GridTuple(r+2, c-2, true, board[r+1][c-1], clickedPiece));
                     }

                if(r + 1 < board.length && c + 1 < board[r].length && board[r + 1][c + 1] == null){
                    moves.add(new GridTuple(r+1, c+1, false, null, clickedPiece));
                }
                else if(r + 1 < board.length && c + 1 < board[r].length && isOpponentPiece(board[r+1][c+1], "Red") &&
                        r +2 < board.length && c+2 < board[r].length && board[r+2][c+2] == null){// if there is a piece in the way
                        moves.add(new GridTuple(r+2, c+2, true, board[r+1][c+1], clickedPiece));
                    }
            }
        }


        // for non king
        // for king
        if (player.equals("Red")){
            if(clickedPiece.isKing()){
                // backwards movement
                    // Backward movement to the left
                if (r + 1 < board.length && c - 1 >= 0 && board[r + 1][c - 1] == null) {
                    moves.add(new GridTuple(r + 1, c - 1, false, null, clickedPiece));
                }
                // Backward capture to the left
                else if (r + 1 < board.length && c - 1 >= 0 && isOpponentPiece(board[r + 1][c - 1], "White") &&
                        r + 2 < board.length && c - 2 >= 0 && board[r + 2][c - 2] == null) {
                    moves.add(new GridTuple(r + 2, c - 2, true, board[r + 1][c - 1], clickedPiece));
                }

                // Backward movement to the right
                if (r + 1 < board.length && c + 1 < board[r].length && board[r + 1][c + 1] == null) {
                    moves.add(new GridTuple(r + 1, c + 1, false, null, clickedPiece));
                }
                // Backward capture to the right
                else if (r + 1 < board.length && c + 1 < board[r].length && isOpponentPiece(board[r + 1][c + 1], "White") &&
                        r + 2 < board.length && c + 2 < board[r].length && board[r + 2][c + 2] == null) {
                    moves.add(new GridTuple(r + 2, c + 2, true, board[r + 1][c + 1], clickedPiece));
                }

                if(r - 1 >= 0 && c - 1 >= 0 && board[r - 1][c - 1] == null){
                    moves.add(new GridTuple(r-1, c-1, false, null, clickedPiece));
                }
                else if(r - 1 >= 0 && c - 1 >= 0 && isOpponentPiece(board[r - 1][c - 1], "White") &&
                        r - 2 >= 0 && c - 2 >= 0 && board[r - 2][c - 2] == null){//if there is a piece in the way
                    moves.add(new GridTuple(r-2, c-2, true, board[r-1][c-1], clickedPiece));
                }

                if(r-1 >= 0 && c + 1 < board[r].length && board[r - 1][c + 1] == null){
                    moves.add(new GridTuple(r-1, c+1, false, null, clickedPiece));
                }
                else if(r - 1 >= 0 && c + 1 < board[r].length && isOpponentPiece(board[r - 1][c + 1], "White") &&
                        r - 2 >= 0 && c + 2 < board[r].length && board[r - 2][c + 2] == null){// if there is a piece in the way
                    moves.add(new GridTuple(r-2, c+2, true, board[r-1][c+1], clickedPiece));
                }


            }
            else{
                if(r - 1 >= 0 && c - 1 >= 0 && board[r - 1][c - 1] == null){
                    moves.add(new GridTuple(r-1, c-1, false, null, clickedPiece));
                }
                else if(r - 1 >= 0 && c - 1 >= 0 && isOpponentPiece(board[r - 1][c - 1], "White") &&
                        r - 2 >= 0 && c - 2 >= 0 && board[r - 2][c - 2] == null){//if there is a piece in the way
                    moves.add(new GridTuple(r-2, c-2, true, board[r-1][c-1], clickedPiece));
                }

                if(r-1 >= 0 && c + 1 < board[r].length && board[r - 1][c + 1] == null){
                    moves.add(new GridTuple(r-1, c+1, false, null, clickedPiece));
                }
                else if(r - 1 >= 0 && c + 1 < board[r].length && isOpponentPiece(board[r - 1][c + 1], "White") &&
                        r - 2 >= 0 && c + 2 < board[r].length && board[r - 2][c + 2] == null){// if there is a piece in the way
                    moves.add(new GridTuple(r-2, c+2, true, board[r-1][c+1], clickedPiece));
                }
            }
        }

        for (GridTuple m : moves) {
            if (m.capture) {
                captureMoves.add(m);
            }
        }
        return captureMoves.isEmpty() ? moves : captureMoves;
    }

    public ArrayList<GridTuple> checkAvailableMoves(String player, int r, int c, Checker [][] gameState){
        Checker clickedPiece = gameState[r][c];
        ArrayList<GridTuple> moves = new ArrayList<>();
        ArrayList<GridTuple> captureMoves = new ArrayList<>();
        // for White player
        if (player.equals("White")){
            if(clickedPiece.isKing()){
                // backwards movement
                if (r - 1 >= 0 && c - 1 >= 0 && gameState[r - 1][c - 1] == null) {
                    moves.add(new GridTuple(r - 1, c - 1, false, null, clickedPiece));
                }
                // Backward capture to the left
                else if (r - 1 >= 0 && c - 1 >= 0 && isOpponentPiece(gameState[r - 1][c - 1], "Red") &&
                        r - 2 >= 0 && c - 2 >= 0 && gameState[r - 2][c - 2] == null) {
                    moves.add(new GridTuple(r - 2, c - 2, true, gameState[r - 1][c - 1], clickedPiece));
                }

                // Backward movement to the right
                if (r - 1 >= 0 && c + 1 < gameState[r].length && gameState[r - 1][c + 1] == null) {
                    moves.add(new GridTuple(r - 1, c + 1, false, null, clickedPiece));
                }
                // Backward capture to the right
                else if (r - 1 >= 0 && c + 1 < gameState[r].length && isOpponentPiece(gameState[r - 1][c + 1], "Red") &&
                        r - 2 >= 0 && c + 2 < gameState[r].length && gameState[r - 2][c + 2] == null) {
                    moves.add(new GridTuple(r - 2, c + 2, true, gameState[r - 1][c + 1], clickedPiece));
                }
                if(r + 1 < gameState.length && c - 1 >= 0 && gameState[r + 1][c - 1] == null){
                    moves.add(new GridTuple(r+1, c-1, false, null,clickedPiece));
                }
                else if(r + 1 < gameState.length && c - 1 >= 0 && isOpponentPiece(gameState[r+1][c-1], "Red")
                        && r+2 < gameState.length && c-2 >= 0 && gameState[r+2][c-2] ==null){//if there is a piece in the way
                    moves.add(new GridTuple(r+2, c-2, true, gameState[r+1][c-1], clickedPiece));
                }

                if(r + 1 < gameState.length && c + 1 < gameState[r].length && gameState[r + 1][c + 1] == null){
                    moves.add(new GridTuple(r+1, c+1, false, null, clickedPiece));
                }
                else if(r + 1 < gameState.length && c + 1 < gameState[r].length && isOpponentPiece(gameState[r+1][c+1], "Red") &&
                        r +2 < gameState.length && c+2 < gameState[r].length && gameState[r+2][c+2] == null){// if there is a piece in the way
                    moves.add(new GridTuple(r+2, c+2, true, gameState[r+1][c+1], clickedPiece));
                }
            }
            else{
                if(r + 1 < gameState.length && c - 1 >= 0 && gameState[r + 1][c - 1] == null){
                    moves.add(new GridTuple(r+1, c-1, false, null, clickedPiece));
                }
                else if(r + 1 < gameState.length && c - 1 >= 0 && isOpponentPiece(gameState[r+1][c-1], "Red")
                        && r+2 < gameState.length && c-2 >= 0 && gameState[r+2][c-2] == null){//if there is a piece in the way
                    moves.add(new GridTuple(r+2, c-2, true, gameState[r+1][c-1], clickedPiece));
                }

                if(r + 1 < gameState.length && c + 1 < gameState[r].length && gameState[r + 1][c + 1] == null){
                    moves.add(new GridTuple(r+1, c+1, false, null, clickedPiece));
                }
                else if(r + 1 < gameState.length && c + 1 < gameState[r].length && isOpponentPiece(gameState[r+1][c+1], "Red") &&
                        r +2 < gameState.length && c+2 < gameState[r].length && gameState[r+2][c+2] == null){// if there is a piece in the way
                    moves.add(new GridTuple(r+2, c+2, true, gameState[r+1][c+1], clickedPiece));
                }
            }
        }


        // for non king
        // for king
        if (player.equals("Red")){
            if(clickedPiece.isKing()){
                // backwards movement
                if (r + 1 < gameState.length && c - 1 >= 0 && gameState[r + 1][c - 1] == null) {
                    moves.add(new GridTuple(r + 1, c - 1, false, null, clickedPiece));
                }
                // Backward capture to the left
                else if (r + 1 < gameState.length && c - 1 >= 0 && isOpponentPiece(gameState[r + 1][c - 1], "White") &&
                        r + 2 < gameState.length && c - 2 >= 0 && gameState[r + 2][c - 2] == null) {
                    moves.add(new GridTuple(r + 2, c - 2, true, gameState[r + 1][c - 1], clickedPiece));
                }

                // Backward movement to the right
                if (r + 1 < gameState.length && c + 1 < gameState[r].length && gameState[r + 1][c + 1] == null) {
                    moves.add(new GridTuple(r + 1, c + 1, false, null, clickedPiece));
                }
                // Backward capture to the right
                else if (r + 1 < gameState.length && c + 1 < gameState[r].length && isOpponentPiece(gameState[r + 1][c + 1], "White") &&
                        r + 2 < gameState.length && c + 2 < gameState[r].length && gameState[r + 2][c + 2] == null) {
                    moves.add(new GridTuple(r + 2, c + 2, true, gameState[r + 1][c + 1], clickedPiece));
                }

                if(r - 1 >= 0 && c - 1 >= 0 && gameState[r - 1][c - 1] == null){
                    moves.add(new GridTuple(r-1, c-1, false, null, clickedPiece));
                }
                else if(r - 1 >= 0 && c - 1 >= 0 && isOpponentPiece(gameState[r - 1][c - 1], "White") &&
                        r - 2 >= 0 && c - 2 >= 0 && gameState[r - 2][c - 2] == null){//if there is a piece in the way
                    moves.add(new GridTuple(r-2, c-2, true, gameState[r-1][c-1], clickedPiece));
                }

                if(r-1 >= 0 && c + 1 < gameState[r].length && gameState[r - 1][c + 1] == null){
                    moves.add(new GridTuple(r-1, c+1, false, null, clickedPiece));
                }
                else if(r - 1 >= 0 && c + 1 < gameState[r].length && isOpponentPiece(gameState[r - 1][c + 1], "White") &&
                        r - 2 >= 0 && c + 2 < gameState[r].length && gameState[r - 2][c + 2] == null){// if there is a piece in the way
                    moves.add(new GridTuple(r-2, c+2, true, gameState[r-1][c+1], clickedPiece));
                }
            }
            else{
                if(r - 1 >= 0 && c - 1 >= 0 && gameState[r - 1][c - 1] == null){
                    moves.add(new GridTuple(r-1, c-1, false, null, clickedPiece));
                }
                else if(r - 1 >= 0 && c - 1 >= 0 && isOpponentPiece(gameState[r - 1][c - 1], "White") &&
                        r - 2 >= 0 && c - 2 >= 0 && gameState[r - 2][c - 2] == null){//if there is a piece in the way
                    moves.add(new GridTuple(r-2, c-2, true, gameState[r-1][c-1], clickedPiece));
                }

                if(r-1 >= 0 && c + 1 < gameState[r].length && gameState[r - 1][c + 1] == null){
                    moves.add(new GridTuple(r-1, c+1, false, null, clickedPiece));
                }
                else if(r - 1 >= 0 && c + 1 < gameState[r].length && isOpponentPiece(gameState[r - 1][c + 1], "White") &&
                        r - 2 >= 0 && c + 2 < gameState[r].length && gameState[r - 2][c + 2] == null){// if there is a piece in the way
                    moves.add(new GridTuple(r-2, c+2, true, gameState[r-1][c+1], clickedPiece));
                }
            }
        }
        for (GridTuple m : moves) {
            if (m.capture) {
                captureMoves.add(m);
            }
        }

        return captureMoves.isEmpty() ? moves : captureMoves;
    }

    private boolean isOpponentPiece(Checker c, String opponentColour){
     return c != null && c.getPlayer().equals(opponentColour);
    }

    public ArrayList<GridTuple> checkAllValidMoves(Checker[][] gameState, Player p){
        ArrayList<GridTuple> validMoves = new ArrayList<>();
        for (int row = 0; row < gameState.length; row++) {
            for (int col = 0; col < gameState[row].length; col++) {
                Checker checker = gameState[row][col];
                // Check if there's a checker and it belongs to the player
                if (checker != null && checker.getPlayer().equals(p.playerColour)) {
                    // Find and add all valid moves for this checker
                    validMoves.addAll(checkAvailableMoves(p.playerColour, row, col, gameState));
                }
            }
        }
        return validMoves;
    }

    public Checker[][] produceGameState(GridTuple gT, Checker[][] gS){
        Checker[][] genGameState = new Checker[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                genGameState[i][j] = gS[i][j];
            }
        }
        if(gT.capture){
            moveCheckerTo(gT.oriChecker, gT.X, gT.Y, genGameState);
            removeCheckerAtPos(gT.X, gT.Y, genGameState);
        }
        else{
            moveCheckerTo(gT.oriChecker, gT.X, gT.Y, genGameState);
        }
        return genGameState;
    }
    public int minimaxWithAlphaBeta(Checker[][] gameState, int depth, int alpha, int beta, Player p){
        // determine is game state is a terminal node if depth == 0 ||
        ArrayList<GridTuple> moves = checkAllValidMoves(gameState,p);

        if(depth == maxDepth || moves.isEmpty()){
            return h1(gameState);
        }

        if(p.playerColour.equals("White")){ // if maximising player
            int maxEval = Integer.MIN_VALUE;
            Integer eval;
            for (GridTuple gT: moves) {
                Checker[][] genGameState = produceGameState(gT, gameState);
                eval = minimaxWithAlphaBeta(genGameState, depth + 1, alpha, beta, red);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha){
                    break;          //prune
                }
            }
            return maxEval;
        }
        else{ // if minimising player
            int minEval = Integer.MAX_VALUE;
            Integer eval;
            for (GridTuple gT:moves) {
                Checker[][] genGameState = produceGameState(gT, gameState);
                eval = minimaxWithAlphaBeta(genGameState, depth + 1, alpha, beta, white);
                minEval = Math.min(minEval,eval);
                beta = min(beta, eval);
                if (beta <= alpha){
                    break;           //prune
                }
            }
            return minEval;
        }
    }

    public GridTuple findBestMove(Checker[][] gameState, Player p, Integer maxDepth){
        int bestEval = (p.playerColour.equals("White")) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        GridTuple bestMove = null;

        ArrayList<GridTuple> moves = checkAllValidMoves(gameState, p);
        ArrayList<GridTuple> captureMoves = new ArrayList<>();

        for (GridTuple move : moves) {
            if (move.capture) {
                captureMoves.add(move);
            }
        }

        if (captureMoves.isEmpty()) {
            for (GridTuple gT : moves) {
                Checker[][] genGameState = produceGameState(gT, gameState);
                int eval = minimaxWithAlphaBeta(genGameState, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, p);

                if ((p.playerColour.equals("White") && eval > bestEval) || (p.playerColour.equals("Red") && eval < bestEval)) {
                    bestEval = eval;
                    bestMove = gT;
                }
            }
        }
        else{
            for (GridTuple gT : captureMoves) {
                Checker[][] genGameState = produceGameState(gT, gameState);
                int eval = minimaxWithAlphaBeta(genGameState, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, p);

                if ((p.playerColour.equals("White") && eval > bestEval) || (p.playerColour.equals("Red") && eval < bestEval)) {
                    bestEval = eval;
                    bestMove = gT;
                }
            }
        }
        System.out.println("Evaluation at: " + bestEval);
        return bestMove;
    }
    public int h1(Checker[][] gameState) {
        int redScore = 0;
        int whiteScore = 0;

        // Define values
        int regularPieceValue = 1;
        int kingValue = 3;
        int vulnerabilityPenalty = -2; // Penalty for being in a position to be captured

        for (int row = 0; row < gameState.length; row++) {
            for (int col = 0; col < gameState[row].length; col++) {
                Checker checker = gameState[row][col];
                if (checker != null) {
                    int pieceValue = checker.isKing() ? kingValue : regularPieceValue;

                    // Check if the piece is vulnerable
                    if (isVulnerable(row, col, checker.getPlayer(), gameState)) {
                        pieceValue += vulnerabilityPenalty;
                    }

                    // Add to the respective player's score
                    if (checker.getPlayer().equals("White")) {
                        whiteScore += pieceValue;
                    } else {
                        redScore += pieceValue;
                    }
                }
            }
        }

        // Calculate piece difference based on which player is AI
        int pieceDifference = (white.isAI) ? whiteScore - redScore : redScore - whiteScore;

        // Return the heuristic value
        return pieceDifference;
    }

    private boolean isVulnerable(int row, int col, String player, Checker[][] gameState) {
        // Determine the opponent's color
        String opponentPlayer = player.equals("White") ? "Red" : "White";

        // Check for regular pieces and kings separately
        boolean isKing = gameState[row][col].isKing();

        // Check diagonally in all directions (for kings) or in forward directions (for regular pieces)
        int[] rowDeltas = isKing ? new int[]{-1, 1} : (player.equals("White") ? new int[]{1} : new int[]{-1});
        int[] colDeltas = new int[]{-1, 1};

        for (int rowDelta : rowDeltas) {
            for (int colDelta : colDeltas) {
                int opponentRow = row + rowDelta;
                int opponentCol = col + colDelta;
                int landingRow = row + 2 * rowDelta;
                int landingCol = col + 2 * colDelta;

                // Check if the opponent piece is in a position to capture
                if (isPositionValid(opponentRow, opponentCol, gameState.length, gameState[0].length) &&
                        gameState[opponentRow][opponentCol] != null &&
                        gameState[opponentRow][opponentCol].getPlayer().equals(opponentPlayer) &&
                        isPositionValid(landingRow, landingCol, gameState.length, gameState[0].length) &&
                        gameState[landingRow][landingCol] == null) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPositionValid(int row, int col, int maxRows, int maxCols) {
        return row >= 0 && row < maxRows && col >= 0 && col < maxCols;
    }



}

