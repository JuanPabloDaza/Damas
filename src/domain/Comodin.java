package domain;

import java.io.Serializable;

public abstract class Comodin implements Serializable {
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
