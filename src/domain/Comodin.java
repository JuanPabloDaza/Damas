package domain;

public abstract class Comodin {
    int posicionX;
    int posicionY;

    public Comodin(int positionX, int posicionY){
        this.posicionX = positionX;
        this.posicionY = posicionY;
    }

    public int getPosicionX(){
        return posicionX;
    }
    public int getPosicionY(){
        return posicionY;
    }

}
