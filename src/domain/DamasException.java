package domain;

public class DamasException extends Exception{
    public DamasException(String message){
        super(message);
    }

    public static final String MOVIMIENTO_INVALIDO = "Movimiento invalido.";
}
