package domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable{
    private String color;
    private String name;
    private ArrayList<Comodin> inventario = new ArrayList<>();
    private ArrayList<Ficha> fichas = new ArrayList<>();

    public Jugador(String color){
        this.color = color;
    }
    public void agregarFicha(Ficha ficha){
        fichas.add(ficha);
    }

    public void quitarFicha(Ficha ficha){
        fichas.remove(ficha);
    }

    public void cambiarNombre(String nombre){
        name = nombre;
    }

    public String getName() {
        return name;
    }

    public void agregarComodin(Comodin comodin){
        inventario.add(comodin);
    }
    public void quitarComodin(Comodin comodin){
        inventario.remove(comodin);
    }
    public int getNumeroFichas(){
        return fichas.size();
    }
    public ArrayList<Comodin> getInventario(){
        return inventario;
    }
}
