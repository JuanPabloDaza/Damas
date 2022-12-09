package domain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Damas {
    private Timer timer;
    JLabel timeLabel;
    private Ficha[][] tablero = new Ficha[10][10];
    private ArrayList<Ficha> fichasB = new ArrayList<>();
    private ArrayList<Ficha> fichasN = new ArrayList<>();
    private boolean victoria = false;
    private int[][] movimiento = new int[2][2];
    private boolean nuevaFicha = false;

    private boolean captura = false;

    private String jugador = "Negro";
    private HashMap<String,Jugador> jugadores = new HashMap<>();
    private int turno = 1;
    DecimalFormat dFormat = new DecimalFormat("00");
    private String ddSecond, ddMinute;
    private int hora,minuto,segundo;

    public Damas(){
        crearTablero();
        reiniciarMovimiento();
    }

    private void crearTablero(){
        jugadores.put("Negro",new Jugador("Negro"));
        jugadores.put("Blanco", new Jugador("Blanco"));
        for(int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 4) {
                        FichaNormal ficha = new FichaNormal("Negro", i, j);
                        tablero[i][j] = ficha;
                        jugadores.get("Negro").agregarFicha(ficha);
                    } else if (i > 5) {
                        FichaNormal ficha = new FichaNormal("Blanco", i, j);
                        tablero[i][j] = ficha;
                        jugadores.get("Blanco").agregarFicha(ficha);
                    }
                }
            }
        }
    }

    public void agregarJugador(int i, String nombre){
        if(i == 1){
            jugadores.get("Negro").cambiarNombre(nombre);
        }else{
            jugadores.get("Blanco").cambiarNombre(nombre);
        }
    }

    public void realizarMovimiento(int fila, int columna) throws DamasException{
        if(movimiento[0][0] == -1){
            movimiento[0][0] = fila;
            movimiento[0][1] = columna;
        }else{
            movimiento[1][0] = fila;
            movimiento[1][1] = columna;
            nuevaFicha = false;
            reiniciarCaptura();
            validarMovimiento();
        }
    }

    private void validarMovimiento() throws DamasException{
        if(tablero[movimiento[0][0]][movimiento[0][1]] == null){
            reiniciarMovimiento();
            throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
        } else if (tablero[movimiento[1][0]][movimiento[1][1]] instanceof Ficha) {
            reiniciarMovimiento();
            throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
        }else {
            if (tablero[movimiento[0][0]][movimiento[0][1]].getColor().equals(jugador)) {
                if (tablero[movimiento[0][0]][movimiento[0][1]] instanceof FichaNormal) {
                    validarMovimientoFichaNormal();
                } else if (tablero[movimiento[0][0]][movimiento[0][1]] instanceof FichaReina) {
                    validarMovimientoFichaReina();
                }
            } else {
                reiniciarMovimiento();
                throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
            }
        }
    }

    private void validarMovimientoFichaReina() throws DamasException{
        boolean in = false;
        if(movimiento[1][0] > movimiento[0][0]){
            if(movimiento[1][1] > movimiento[0][1]){
                int col = movimiento[0][1] + 1;
                for(int i = movimiento[0][0] + 1; i < movimiento[1][0]; i++){
                    in = true;
                    if(i == movimiento[1][0]-1){
                        if(tablero[i][col] != null && !tablero[i][col].getColor().equals(jugador)){
                            capturarFicha();
                        }else if(tablero[i][col] == null) {
                            finalizarMovimiento();
                        }else{
                            reiniciarMovimiento();
                            throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                        }
                    }else if(tablero[i][col] != null){
                        reiniciarMovimiento();
                        throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                    }else{
                        col += 1;
                    }
                }
            }else{
                int col = movimiento[0][1] - 1;
                for(int i = movimiento[0][0] + 1; i < movimiento[1][0]; i++){
                    in = true;
                    if(i == movimiento[1][0]-1){
                        if(tablero[i][col] != null && !tablero[i][col].getColor().equals(jugador)){
                            capturarFicha();
                        }else if(tablero[i][col] == null) {
                            finalizarMovimiento();
                        }else{
                            reiniciarMovimiento();
                            throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                        }
                    }else if(tablero[i][col] != null){
                        reiniciarMovimiento();
                        throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                    }else{
                        col -= 1;
                    }
                }
            }
        }else{
            if(movimiento[1][1] > movimiento[0][1]){
                int col = movimiento[0][1] + 1;
                for(int i = movimiento[0][0] - 1; i > movimiento[1][0]; i--){
                    in = true;
                    if(i == movimiento[1][0]+1){
                        if(tablero[i][col] != null && !tablero[i][col].getColor().equals(jugador)){
                            capturarFicha();
                        }else if(tablero[i][col] == null) {
                            finalizarMovimiento();
                        }else{
                            reiniciarMovimiento();
                            throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                        }
                    }else if(tablero[i][col] != null){
                        reiniciarMovimiento();
                        throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                    }else{
                        col += 1;
                    }
                }
            }else{
                int col = movimiento[0][1] - 1;
                for(int i = movimiento[0][0] - 1; i > movimiento[1][0]; i--){
                    in = true;
                    if(i == movimiento[1][0]+1){
                        if(tablero[i][col] != null && !tablero[i][col].getColor().equals(jugador)){
                            capturarFicha();
                        }else if(tablero[i][col] == null) {
                            finalizarMovimiento();
                        }else{
                            reiniciarMovimiento();
                            throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                        }
                    }else if(tablero[i][col] != null){
                        reiniciarMovimiento();
                        throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                    }else{
                        col -= 1;
                    }
                }
            }
        }
        if(!in){
            finalizarMovimiento();
        }
    }

    private void validarMovimientoFichaNormal() throws DamasException{
        if((tablero[movimiento[0][0]][movimiento[0][1]].getColor().equals("Negro") && movimiento[1][0] > movimiento[0][0]) || (tablero[movimiento[0][0]][movimiento[0][1]].getColor().equals("Blanco") && movimiento[1][0] < movimiento[0][0])) {
            if (movimiento[1][0] > movimiento[0][0] + 1 || movimiento[1][0] < movimiento[0][0] - 1) {//Moviento es mas de una casilla, validar si come ficha o es invalido
                if (movimiento[1][0] > movimiento[0][0] + 2 || movimiento[1][0] < movimiento[0][0] - 2) {//No se come ficha, mas de una fila.
                    reiniciarMovimiento();
                    throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                } else if (movimiento[1][1] > movimiento[0][1] + 2 || movimiento[1][1] < movimiento[0][1] - 2) {//No se come ficha, mas de una columna
                    reiniciarMovimiento();
                    throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                } else {
                    capturarFicha();
                    reiniciarMovimiento();
                }
            } else if (movimiento[1][1] > movimiento[0][1] + 1 || movimiento[1][1] < movimiento[0][1] - 1) {
                throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
            } else {
                if (tablero[movimiento[0][0]][movimiento[0][1]] == null) {
                    reiniciarMovimiento();
                    throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                } else if (tablero[movimiento[1][0]][movimiento[1][1]] != null) {
                    reiniciarMovimiento();
                    throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                } else {
                    finalizarMovimiento();
                }
            }
        }else{
            reiniciarMovimiento();
            throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
        }
    }



    private void finalizarMovimiento(){
        tablero[movimiento[1][0]][movimiento[1][1]] = tablero[movimiento[0][0]][movimiento[0][1]];
        tablero[movimiento[0][0]][movimiento[0][1]] = null;
        reiniciarMovimiento();
        if(tablero[movimiento[1][0]][movimiento[1][1]] instanceof FichaNormal){
            validarCambioFicha();
        }
        if(victoria()){
            victoria = true;
        }else{
            siguienteTurno();
        }
    }

    private void capturarFicha() throws DamasException{
        if(movimiento[1][0] > movimiento[0][0]){
            if(movimiento[1][1] > movimiento[0][1]) {
                if(tablero[movimiento[1][0] - 1][movimiento[1][1] - 1] != null){
                    if(!tablero[movimiento[1][0] - 1][movimiento[1][1] - 1].getColor().equals(jugador)){
                        if(jugador.equals("Negro")){
                            jugadores.get("Blanco").quitarFicha(tablero[movimiento[1][0] - 1][movimiento[1][1] - 1]);
                        }else{
                            jugadores.get("Negro").quitarFicha(tablero[movimiento[1][0] - 1][movimiento[1][1] - 1]);
                        }
                        tablero[movimiento[1][0] - 1][movimiento[1][1] - 1] = null;
                        captura = true;
                        finalizarMovimiento();
                    }
                }else{
                    reiniciarMovimiento();
                    throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                }
            }else{
                if(tablero[movimiento[1][0] - 1][movimiento[1][1] + 1] != null){
                    if(!tablero[movimiento[1][0] - 1][movimiento[1][1] + 1].getColor().equals(jugador)){
                        if(jugador.equals("Negro")){
                            jugadores.get("Blanco").quitarFicha(tablero[movimiento[1][0] - 1][movimiento[1][1] + 1]);
                        }else{
                            jugadores.get("Negro").quitarFicha(tablero[movimiento[1][0] - 1][movimiento[1][1] + 1]);
                        }
                        tablero[movimiento[1][0] - 1][movimiento[1][1] + 1] = null;
                        captura = true;
                        finalizarMovimiento();
                    }
                }else{
                    reiniciarMovimiento();
                    throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                }
            }
        }else{
            if(movimiento[1][1] > movimiento[0][1]){
                if(tablero[movimiento[1][0] + 1][movimiento[1][1] - 1] != null){
                    if(!tablero[movimiento[1][0] + 1][movimiento[1][1] - 1].getColor().equals(jugador)){
                        if(jugador.equals("Negro")){
                            jugadores.get("Blanco").quitarFicha(tablero[movimiento[1][0] + 1][movimiento[1][1] - 1]);
                        }else{
                            jugadores.get("Negro").quitarFicha(tablero[movimiento[1][0] + 1][movimiento[1][1] - 1]);
                        }
                        tablero[movimiento[1][0] + 1][movimiento[1][1] - 1] = null;
                        captura = true;
                        finalizarMovimiento();
                    }
                }else{
                    reiniciarMovimiento();
                    throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                }

            }else{
                if(tablero[movimiento[1][0] + 1][movimiento[1][1] + 1] != null){
                    if(!tablero[movimiento[1][0] + 1][movimiento[1][1] + 1].getColor().equals(jugador)){
                        if(jugador.equals("Negro")){
                            jugadores.get("Blanco").quitarFicha(tablero[movimiento[1][0] + 1][movimiento[1][1] + 1]);
                        }else{
                            jugadores.get("Negro").quitarFicha(tablero[movimiento[1][0] + 1][movimiento[1][1] + 1]);
                        }
                        tablero[movimiento[1][0] + 1][movimiento[1][1] + 1] = null;
                        captura = true;
                        finalizarMovimiento();
                    }
                }else{
                    reiniciarMovimiento();
                    throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
                }
            }
        }
    }
    private void capturaPosible(){

    }

    public void cambiarFicha(int i){
        if(jugador.equals("Negro")){
            jugadores.get("Blanco").quitarFicha(tablero[movimiento[1][0]][movimiento[1][1]]);
            if(i == 0){
                Ficha ficha = new FichaReina("Blanco", movimiento[1][0], movimiento[1][1]);
                tablero[movimiento[1][0]][movimiento[1][1]] = ficha;
                jugadores.get("Blanco").agregarFicha(ficha);
            }
        }else{
            jugadores.get("Negro").quitarFicha(tablero[movimiento[1][0]][movimiento[1][1]]);
            if(i == 0){
                Ficha ficha = new FichaReina("Negro", movimiento[1][0], movimiento[1][1]);
                tablero[movimiento[1][0]][movimiento[1][1]] = ficha;
                jugadores.get("Negro").agregarFicha(ficha);
            }
        }
    }

    private void validarCambioFicha(){
        if(tablero[movimiento[1][0]][movimiento[1][1]].getColor().equals("Negro")){
            if(movimiento[1][0] == 9){
                nuevaFicha = true;
            }
        }else{
            if(movimiento[1][0] == 0){
                nuevaFicha = true;
            }
        }
    }
    private boolean victoria(){
        if(jugadores.get("Negro").getNumeroFichas() < 1 || jugadores.get("Blanco").getNumeroFichas() < 1){
            return true;
        }else {
            return false;
        }
    }
    private void reiniciarMovimiento(){
        movimiento[0][0] = -1;
    }
    public int getTurno(){
        return turno;
    }

    public String getJugador(){
        return jugador;
    }

    public String getNombreJugador(String color) {
        return jugadores.get(color).getName();
    }

    public boolean getVictoria(){
        return victoria;
    }

    private void reiniciarCaptura(){
        captura = false;
    }

    public boolean getCaptura(){
        return captura;
    }

    public boolean getNuevaFicha(){
        return nuevaFicha;
    }

    private void siguienteTurno(){
        if(jugador.equals("Negro")){
            jugador = "Blanco";
        }else{
            jugador = "Negro";
        }
        turno += 1;
    }
}
