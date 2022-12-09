package domain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Damas {
    private Timer timer;
    JLabel timeLabel;
    private Ficha[][] tablero = new Ficha[10][10];
    private Casillas[][] tableroCasillas = new Casillas[10][10];
    private Comodin[][] tableroComodin = new Comodin[10][10];
    private ArrayList<Ficha> fichasB = new ArrayList<>();
    private ArrayList<Ficha> fichasN = new ArrayList<>();
    private boolean victoria = false;
    private int[][] movimiento = new int[2][2];
    private boolean nuevaFicha = false;

    private boolean captura = false;
    private boolean comodin = false;
    private int[] posicionComodin = new int[2];
    private String tipoComodin;
    private boolean casilla = false;
    private int[] posicionCasilla = new int[2];
    private boolean mine = false;

    private String tipoCasilla;
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
            reiniciarComodines();
            reiniciarCaptura();
            validarMovimiento();
        }
    }

    private void validarMovimiento() throws DamasException{
        if(tablero[movimiento[0][0]][movimiento[0][1]] == null){
            reiniciarMovimiento();
            throw new DamasException(DamasException.MOVIMIENTO_INVALIDO);
        } else if (tablero[movimiento[1][0]][movimiento[1][1]] != null) {
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
        if(tableroComodin[movimiento[1][0]][movimiento[1][1]] != null){
            accionComodin();
        } else if (tableroCasillas[movimiento[1][0]][movimiento[1][1]] != null) {
            accionCasilla();
        }
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

    private void accionComodin(){
        jugadores.get(jugador).agregarComodin(tableroComodin[movimiento[1][0]][movimiento[1][1]]);
        tableroComodin[movimiento[1][0]][movimiento[1][1]] = null;
    }

    private void accionCasilla(){
        if(tableroCasillas[movimiento[1][0]][movimiento[1][1]] instanceof Mine){
            accionMina();
        }else if(tableroCasillas[movimiento[1][0]][movimiento[1][1]] instanceof Teleport){
            accionTeleport();
        } else if (tableroCasillas[movimiento[1][0]][movimiento[1][1]] instanceof Jail) {
            accionJail();
        }
    }

    private void accionMina(){
        tableroCasillas[movimiento[1][0]][movimiento[1][1]] = null;
        tablero[movimiento[0][0]][movimiento[0][1]] = null;
        for(int i = movimiento[1][0]-1; i < movimiento[1][0]+2; i++){
            for(int j = movimiento[1][1] - 1; j < movimiento[1][1]+2; j++){
                try{
                    if(tablero[i][j] != null){
                        if(tablero[i][j].getColor().equals("Negro")){
                            jugadores.get("Negro").quitarFicha(tablero[i][j]);
                            tablero[i][j] = null;
                        }else{
                            jugadores.get("Blanco").quitarFicha(tablero[i][j]);
                            tablero[i][j] = null;
                        }
                        mine = true;
                    }
                }catch (Exception e){

                }
            }
        }
    }

    private void accionTeleport(){

    }
    private void accionJail(){

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
        if(turno % 5 == 0){
            int numero = (int)(Math.random()*(2));
            if(numero == 0){
                agregarCasilla();
            }else{
                agregarComodin();
            }
        }
        turno += 1;
    }

    private void agregarCasilla(){
        boolean put = false;
        int tipo = (int)(Math.random()*(1));
        int i = (int)(Math.random()*(10));
        int j = (int)(Math.random()*(10));
        while (!put){
            if ((i + j) % 2 != 0 && !(i == movimiento[1][0] && j == movimiento[1][1])) {
                if(tablero[i][j] == null){
                    if(tipo == 0){
                        tableroCasillas[i][j] = new Mine(i,j);
                        put = true;
                        tipoCasilla = "Mine";
                    }

                    casilla = true;
                    posicionCasilla[0] = i;
                    posicionCasilla[1] = j;
                }
            }
            i = (int)(Math.random()*(10));
            j = (int)(Math.random()*(10));
        }
    }
    private void agregarComodin(){
        boolean put = false;
        int tipo = (int)(Math.random()*(2));
        int i = (int)(Math.random()*(10));
        int j = (int)(Math.random()*(10));
        while (!put){
            if ((i + j) % 2 != 0 && !(i == movimiento[1][0] && j == movimiento[1][1]) ) {
                if(tablero[i][j] == null){
                    if(tipo == 0){
                        tableroComodin[i][j] = new Gun(i,j);
                        put = true;
                        tipoComodin = "Gun";
                    }else if(tipo == 1){
                        tableroComodin[i][j] = new Stomp(i,j);
                        put = true;
                        tipoComodin = "Stomp";
                    }
                    comodin = true;
                    posicionComodin[0] = i;
                    posicionComodin[1] = j;
                }
            }
            i = (int)(Math.random()*(10));
            j = (int)(Math.random()*(10));
        }
    }
    public void usarComodin(Comodin comodinUsado){
        if(comodinUsado instanceof Gun){
            accionGun();
        }
    }
    public void accionGun(){

    }

    private void reiniciarComodines(){
        casilla = false;
        comodin = false;
        mine = false;
    }
    public boolean getCasilla() {
        return casilla;
    }
    public boolean getComodin(){
        return comodin;
    }
    public int[] getPosicionCasilla(){
        return posicionCasilla;
    }
    public int[] getPosicionComodin(){
        return posicionComodin;
    }

    public String getTipoCasilla() {
        return tipoCasilla;
    }
    public String getTipoComodin(){
        return tipoComodin;
    }
    public boolean getMine(){
        return mine;
    }
    public ArrayList<Comodin> getInventarioJugador(String color){
        return jugadores.get(color).getInventario();
    }
}
