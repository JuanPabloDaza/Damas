package domain;

import java.io.Serializable;

public abstract class Casillas implements Serializable {
    private int positionX;
    private int positionY;
    public Casillas(int positionX, int positionY){
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
