package domain;

import java.io.Serializable;

public abstract class Ficha implements Serializable {
    private String color;
    private int positionX, positionY;

    public void setColor(String color){
        this.color = color;
    }
    public void setPositionX(int x){
        positionX = x;
    }

    public void setPositionY(int y){
        positionY = y;
    }

    public int getPositionX(){
        return positionX;
    }
    public int getPositionY(){
        return positionY;
    }
    public String getColor(){
        return color;
    }
    @Override
    public boolean equals(Object ficha){
        if(ficha instanceof Ficha) {
            if (this.getPositionX() == ((Ficha) ficha).getPositionX() && this.getPositionY() == ((Ficha) ficha).getPositionY()) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }
}
