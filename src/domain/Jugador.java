package domain;

import java.util.ArrayList;

public class Jugador {
    private String color;
    private String name;
    private ArrayList<Comodin> inventario;

    public Jugador(String color, String name){
        this.color = color;
        this.name = name;
    }

    public void agregarComodin(Comodin comodin){
        inventario.add(comodin);
    }
    public void quitarComodin(Comodin comodin){
        inventario.remove(comodin);
    }
}
