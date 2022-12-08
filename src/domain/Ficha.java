package domain;

public abstract class Ficha {
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

    public boolean equals(Ficha ficha){
        if(this.getPositionX() == ficha.getPositionX() && this.getPositionY() == ficha.getPositionY()){
            return true;
        }else{
            return false;
        }
    }
}
