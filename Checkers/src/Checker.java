
public class Checker {
    private String player;

    private Player owner;
    private boolean isKing;
    private int posX;
    private int posY;

    public Checker(String player, int X, int Y){
        this.player=player;
        this.isKing=false;
        this.posX = X;
        this.posY = Y;
    }

    public void setOwner(Player p){
        owner = p;
    }
    public int getPosX(){
        return posX;
    }

    public void setPosX(int x){
        posX = x;
    }

    public int getPosY(){
        return posY;
    }

    public void setPosY(int y){
        posY = y;
    }


    public void changePos(int x, int y){
        posX = x;
        posY = y;
    }
    public String getPlayer(){
        return player;
    }

    public boolean isKing(){
        return isKing;
    }

    public void makeKing(){
        this.isKing = true;
    }
}
