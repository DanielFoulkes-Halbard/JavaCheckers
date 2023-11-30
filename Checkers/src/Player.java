import java.util.ArrayList;

public class Player {
    String playerColour;

    ArrayList<Checker> pieces = new ArrayList<>();
    int checkerCount = 12;

    boolean isAI;
    public Player(String color){
        this.playerColour = color;
    }

    public void addPlayerGamePiece(Checker c){
        pieces.add(c);
    }
    public void setAI(boolean b){
        isAI = b;
    }
    private String getColour(){
        return playerColour;
    }

    private int getCheckerCount(){
        return checkerCount;
    }
    public void minusCheckerCount(){checkerCount -= 1;}
}
