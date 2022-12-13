package domain;

public class DamasException extends Exception{
    public DamasException(String message){
        super(message);
    }

    public static final String MOVIMIENTO_INVALIDO = "Movimiento invalido.";
    public static final String INVENTARIO_VACIO = "El inventario esta vacio.";
    public static final String TYPE_DAT_ERROR = "Error en el tipo de dato";
    public static final String FILE_NOT_FOUND_ERROR = "No se encuentra el archivo";
}
