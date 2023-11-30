public class GridTuple {
    int X;
    int Y;
    boolean capture;
    Checker capChecker;
    Checker oriChecker;
    public GridTuple(int x, int y, boolean cap, Checker cc, Checker oc){
        X = x;
        Y = y;
        capture = cap;
        capChecker = cc;
        oriChecker = oc;
    }

}
