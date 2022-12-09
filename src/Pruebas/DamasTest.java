package Pruebas;
import static org.junit.Assert.*;
import com.sun.tools.javac.Main;
import domain.Damas;
import domain.DamasException;
import domain.Ficha;
import domain.FichaNormal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DamasTest {
    @Test
    public void deberiaCrearFicha() throws DamasException {
        Ficha nueva = new FichaNormal("Negro",3,0);
        Damas prueba = new Damas();
        Ficha[][] matriz = prueba.getTablero();
        assertEquals(matriz[3][0],nueva);
    }
    @Test
    public void deberiaRealizarElMovimiento() throws DamasException {
        Ficha nueva = new FichaNormal("Negro",3,0);
        Damas prueba = new Damas();
        prueba.realizarMovimiento(3,0);
        prueba.realizarMovimiento(4,1);
        Ficha[][] matriz = prueba.getTablero();
        assertEquals(matriz[4][1],nueva);
    }
    @Test
    public void deberiaCapturarFicha() throws DamasException{
        Damas prueba = new Damas();
        prueba.realizarMovimiento(3,0);
        prueba.realizarMovimiento(4,1);
        prueba.realizarMovimiento(6,3);
        prueba.realizarMovimiento(5,2);
        prueba.realizarMovimiento(4,1);
        prueba.realizarMovimiento(6,3);
        Ficha[][] matriz = prueba.getTablero();
        assertEquals(matriz[5][2],null);
    }
}
