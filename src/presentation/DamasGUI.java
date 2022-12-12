package presentation;
import domain.Comodin;
import domain.Damas;
import domain.DamasException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DamasGUI extends JFrame {

    private JMenuBar menubar;
    private JMenu menu;
    private JMenuItem nuevo,abrir;
    private JPanel tablero,juego,info,menuM;

    private JButton play,inventario,tiempo;
    private JButton[][] malla;
    private boolean alreadyOne = false;
    private int[][] movimiento = new int[2][2];
    private Icon fichaNormalNegra = new ImageIcon("Ficha_Normal_Negro.png");
    private Icon fichaNormalBlanca = new ImageIcon("Ficha_Normal_Blanca.png");
    private Icon casillaMinaIcono = new ImageIcon("Mine.png");
    private Icon comodinGunIcono = new ImageIcon("Gun.png");
    private Icon comodinStompIcono = new ImageIcon("Stomp.png");
    private Icon fichaReinaNegra = new ImageIcon("ficha_Reina_Negra.png");
    private Icon fichaReinaBlanca = new ImageIcon("ficha_Reina_Blanca.png");
    private Object[] opciones = {fichaReinaNegra};
    private Object[] inventarioNegro;
    private Object[] inventarioBlanco;
    private Object[] inventarioActual;
    private Icon cambio;
    private int second,minute,second2,minute2 ;
    private Timer timer,timer2;
    private String ddSecond, ddMinute,ddSecond2, ddMinute2;
    private JLabel turno,jugador, timeLabel,timeLabel2;

    DecimalFormat dFormat = new DecimalFormat("00");

    private int hora,minuto,segundo;

    private Damas logica;
    /*
    Constructor para los objetos de tipo DamasGUI
    * */
    private DamasGUI(){
        super("Damas");
        logica = new Damas();
        turno = new JLabel("Turno: "+ Integer.toString(logica.getTurno()));
        prepareElements();
    }
    /*
    funcion para preparar los jugadores, se solicita un nombre para asignarselo a cada jugador.
    * */
    private void prepareJugadores(){
        for(int i = 1; i < 3; i++){
            String nombre = JOptionPane.showInputDialog(null, "Escriba el nombre del jugador " + Integer.toString(i) + ".");
            logica.agregarJugador(i, nombre);
        }
    }

    /*
    Funcion para preparar los elementos de la ventana y el menu.
    * */
    private void prepareElements() {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) screenDimension.getWidth();
        int y = (int) screenDimension.getHeight();
        Dimension dimension = new Dimension(x/2, y/2);
        setSize(dimension);
        setLocation((x-this.getWidth())/2,(y-this.getHeight())/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        prepareElementsMainM();
    }
    /*
    Funcion para preparar los elementos del menu principal.
    * */
    private void prepareElementsMainM() {

        menuM = new JPanel();
        menuM.add(new JLabel("Damas Poob"));
        play = new JButton();
        play.setText("PLAY");
        tiempo = new JButton();
        tiempo.setText("TIME");
        menuM.add(play);
        menuM.add(tiempo);
        add(menuM);
        prepareActionsMenu();
    }
    /*
    Funcion para preparar los elementos del menu de opciones.
    * */
    private void prepareElementsMenu() {
        menubar = new JMenuBar();
        setJMenuBar(menubar);
        menu = new JMenu("Menu");
        menubar.add(menu);
        nuevo = new JMenuItem("Nuevo");
        menu.add(nuevo);
        abrir = new JMenuItem("Abrir");
        menu.add(abrir);
    }
    /*
    Funcion para preparar los elementos del tablero
    * */
    private void prepareElementsBoard() {
        tablero = new JPanel();
        tablero.setLayout(new BorderLayout());
        juego = new JPanel();
        juego.setLayout(new GridLayout(10,10,5,5));
        malla = new JButton[10][10];


        Icon fichaNormalNegra = new ImageIcon("Ficha_Normal_Negro.png");
        Icon fichaNormalBlanca = new ImageIcon("Ficha_Normal_Blanca.png");

        JButton btn = null;
        for(int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                if ((i + j) % 2 == 0) {
                    btn = new JButton();
                    btn.setBackground(Color.WHITE);
                    btn.setEnabled(false);
                    malla[i][j] = btn;
                } else {
                    if (i < 4) {
                        btn = new JButton(fichaNormalNegra);
                        btn.setBackground(Color.gray);
                        malla[i][j] = btn;
                    } else if (i>=4 && i<6) {
                        btn = new JButton();
                        btn.setBackground(Color.gray);
                        malla[i][j] = btn;
                    } else {
                        btn = new JButton(fichaNormalBlanca);
                        btn.setBackground(Color.gray);
                        malla[i][j] = btn;
                    }
                }
                juego.add(btn);
            }
        }

        tablero.add(juego,BorderLayout.CENTER);
        info = new JPanel();
        info.setLayout(new BorderLayout());
        info.add(turno, BorderLayout.NORTH);
        info.add(jugador, BorderLayout.CENTER);
        tablero.add(info,BorderLayout.EAST);
        add(tablero);

        inventario = new JButton();
        tablero.add(inventario, BorderLayout.SOUTH);
        inventario.setText("INVENTARIO");

        timeLabel = new JLabel();
        timeLabel.setText("--:--");
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel2 = new JLabel();
        timeLabel2.setText("--:--");
        timeLabel2.setHorizontalAlignment(JLabel.CENTER);
        info.add(timeLabel,BorderLayout.SOUTH);

        second = 5;
        minute = 0;
        second2 = 5;
        minute2 = 0;
        timer();
        timer.start();
        timer2();
    }
    /*
    Funcion para el cronometro del juego.
    * */
    private void timer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                second--;
                ddSecond = dFormat.format(second);
                ddMinute = dFormat.format(minute);
                timeLabel.setText(ddMinute+ ":" +ddSecond);

                if (second == -1) {
                    second = 60;
                    minute--;
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                    timeLabel.setText(ddMinute+ ":" +ddSecond);
                }
                if (minute == 0 && second == 0) {
                    timer.stop();
                    int outputTime = JOptionPane.showConfirmDialog(null, "El jugador " + logica.getNombreJugador(logica.getJugador()) + " acaba de perder.", "VictoriaTime1", JOptionPane.DEFAULT_OPTION);
                }
                if (logica.getJugador().equals("Blanco")) {
                    timer.stop();
                }
            }
        });
    }
    private void timer2() {
        timer2 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                second2--;
                ddSecond2 = dFormat.format(second2);
                ddMinute2 = dFormat.format(minute2);
                timeLabel2.setText(ddMinute2+ ":" +ddSecond2);

                if (second2 == -1) {
                    second2 = 60;
                    minute2--;
                    ddSecond2 = dFormat.format(second2);
                    ddMinute2 = dFormat.format(minute2);
                    timeLabel2.setText(ddMinute2+ ":" +ddSecond2);
                }
                if (minute2 == 0 && second2 == 0) {
                    timer2.stop();
                    int outputTime = JOptionPane.showConfirmDialog(null, "El jugador " + logica.getNombreJugador(logica.getJugador()) + " acaba de perder.", "VictoriaTime2", JOptionPane.DEFAULT_OPTION);
                }
                if (logica.getJugador().equals("Negro")) {
                    timer2.stop();
                }
            }
        });
    }

    /*
    Funcion para prepara los oyentes de las opciones del menu.
    * */
    private void prepareActionsMenu(){
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuM.setVisible(false);
                menuM.removeAll();
                prepareJugadores();
                jugador = new JLabel("Jugador: "+logica.getNombreJugador(logica.getJugador()));
                prepareElementsMenu();
                prepareElementsBoard();
                prepareActions();
            }
        });
    }
    /*
    Funcion para preparar los oyentes del tablero.
    * */
    private void prepareActions(){
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                malla[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        for (int row = 0; row < 10; row++) {
                            for (int col = 0; col < 10; col++) {
                                if (malla[row][col] == ev.getSource()) {
                                    try {
                                        logica.realizarMovimiento(row, col);
                                        if (alreadyOne) {
                                            movimiento[1][0] = row;
                                            movimiento[1][1] = col;
                                            accionMovimiento();
                                            alreadyOne = false;
                                        } else {
                                            movimiento[0][0] = row;
                                            movimiento[0][1] = col;
                                            alreadyOne = true;
                                        }
                                    } catch (DamasException e) {
                                        alreadyOne = false;
                                        JOptionPane.showMessageDialog(null, e.getMessage(), "Damas", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        inventario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                accionInventario();
            }
        });
    }

    /*
    Funcion para realizar un movimiento.
    * */
    private void accionMovimiento(){
        cambio = malla[movimiento[0][0]][movimiento[0][1]].getIcon();
        if(logica.getVictoria()){
            malla[movimiento[0][0]][movimiento[0][1]].setIcon(null);
            malla[movimiento[1][0]][movimiento[1][1]].setIcon(cambio);
            malla[movimiento[1][0]][movimiento[1][1]].repaint();
            accionVictoria();
        }else {
            malla[movimiento[0][0]][movimiento[0][1]].setIcon(null);
            if(logica.getCaptura()){
                accionCaptura();
            }else {
                malla[movimiento[1][0]][movimiento[1][1]].setIcon(cambio);
                malla[movimiento[1][0]][movimiento[1][1]].repaint();
            }
        }
        if(logica.getNuevaFicha()){
            accionNuevaFicha();
        }
        if(logica.getCasilla()){
            accionNuevaCasilla();
        } else if (logica.getComodin()) {
            accionNuevoComodin();
        }
        if(logica.getMine()){
            accionMina();
        }

        turno.setVisible(false);
        turno = new JLabel("Turno: "+ Integer.toString(logica.getTurno()));
        info.add(turno,BorderLayout.NORTH);
        turno.setVisible(true);

        jugador.setVisible(false);
        jugador = new JLabel("Jugardor: " +logica.getNombreJugador(logica.getJugador()));
        info.add(jugador,BorderLayout.CENTER);
        jugador.setVisible(true);

        if (logica.getJugador().equals("Negro")) {
            timer.start();
            timeLabel2.setVisible(false);
            timeLabel.setVisible(true);
            info.add(timeLabel,BorderLayout.SOUTH);
        } else {
            timer2.start();
            timeLabel.setVisible(false);
            timeLabel2.setVisible(true);
            info.add(timeLabel2,BorderLayout.SOUTH);
        }
    }
    /*
    Funcion para mostrar la nueva casilla creada.
    * */
    private void accionNuevaCasilla(){
        int[] posicion = logica.getPosicionCasilla();
        if(logica.getTipoCasilla().equals("Mine")){
            malla[posicion[0]][posicion[1]].setIcon(casillaMinaIcono);
            malla[posicion[0]][posicion[1]].repaint();
        }
    }
    /*
    Funcion para mostrar el nuevo comodin creado.
    * */
    private void accionNuevoComodin(){
        int[] posicion = logica.getPosicionComodin();
        if(logica.getTipoComodin().equals("Gun")){
            malla[posicion[0]][posicion[1]].setIcon(comodinGunIcono);
            malla[posicion[0]][posicion[1]].repaint();
        } else if (logica.getTipoComodin().equals("Stomp")) {
            malla[posicion[0]][posicion[1]].setIcon(comodinStompIcono);
            malla[posicion[0]][posicion[1]].repaint();
        }
    }
    /*
    Funcion para realizar la accion de la casilla Mina.
    * */
    private void accionMina(){
        for(int i = movimiento[1][0]-1; i < movimiento[1][0]+2; i++){
            for(int j = movimiento[1][1] - 1; j < movimiento[1][1] + 2; j++){
                try{
                    malla[i][j].setIcon(null);
                    malla[i][j].repaint();
                }catch (Exception e){

                }
            }
        }
    }

    /*
    Funcion mostrar la victoria del jugador.
    * */
    private void accionVictoria(){
        int output = JOptionPane.showConfirmDialog(null, "El jugador " + logica.getNombreJugador(logica.getJugador()) + " acaba de ganar.", "Victoria", JOptionPane.DEFAULT_OPTION);
    }
    /*
    Funcion para mostrar la accion de captura de una ficha y actualizar el tablero.
    * */
    private void accionCaptura(){
        if(movimiento[1][0] > movimiento[0][0]){
            if(movimiento[1][1] > movimiento[0][1]){
                malla[movimiento[1][0] - 1][movimiento[1][1] - 1].setIcon(null);
                malla[movimiento[1][0] - 1][movimiento[1][1] - 1].repaint();
            }else{
                malla[movimiento[1][0] - 1][movimiento[1][1] + 1].setIcon(null);
                malla[movimiento[1][0] - 1][movimiento[1][1] + 1].repaint();
            }
        }else{
            if(movimiento[1][1] > movimiento[0][1]){
                malla[movimiento[1][0] + 1][movimiento[1][1] - 1].setIcon(null);
                malla[movimiento[1][0] + 1][movimiento[1][1] - 1].repaint();
            }else{
                malla[movimiento[1][0] + 1][movimiento[1][1] + 1].setIcon(null);
                malla[movimiento[1][0] + 1][movimiento[1][1] + 1].repaint();
            }
        }
        malla[movimiento[1][0]][movimiento[1][1]].setIcon(cambio);
        malla[movimiento[1][0]][movimiento[1][1]].repaint();
    }
    /*
    Funcion para aignar el inventario del jugador.
    * */
    private void inventarioTurno() {
        if (logica.getJugador().equals("Blanco")) {
            inventarioBlanco = crearInventario("Blanco");
            inventarioActual = inventarioBlanco;
        }
        else {
            inventarioNegro = crearInventario("Negro");
            inventarioActual = inventarioNegro;
        }
    }
    /*
    Funcion para crear el inventario de Iconos del jugador.
    * */
    private Object[] crearInventario(String color){
        ArrayList<Comodin> inventarioN = logica.getInventarioJugador(color);
        Object[] iconos = new Object[inventarioN.size()];
        for(int i = 0;i < inventarioN.size(); i++){
            if(inventarioN.get(i) instanceof domain.Gun){
                iconos[i] = comodinGunIcono;
            } else if (inventarioN.get(i) instanceof domain.Stomp) {
                iconos[i] = comodinStompIcono;
            }
        }
        return iconos;
    }
    /*
    Funcion para mostrar el inventario de un jugador.
    * */
    private void accionInventario() {
        inventarioTurno();
        try {
            int n = JOptionPane.showOptionDialog(null,
                    "Inventario",
                    "Inventario ",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    inventarioActual,
                    inventarioActual[0]);
            logica.usarComodin(logica.getInventarioJugador(logica.getJugador()).get(n));
        }catch (Exception e ){
            JOptionPane.showMessageDialog(null,"Inventario Vacio","Damas",JOptionPane.ERROR_MESSAGE);
        }
    }
    /*
    Funcion para cambiar de ficha al llegar al otro lado del tablero.
    * */
    private void accionNuevaFicha(){
        int m = JOptionPane.showOptionDialog(null,
                "Seleccione un ficha para cambiar",
                "Damas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
        if(m == 0){
            if(movimiento[1][0] == 0){
                malla[movimiento[1][0]][movimiento[1][1]].setIcon(fichaReinaBlanca);
                malla[movimiento[1][0]][movimiento[1][1]].repaint();
            }else{
                malla[movimiento[1][0]][movimiento[1][1]].setIcon(fichaReinaNegra);
                malla[movimiento[1][0]][movimiento[1][1]].repaint();
            }
        }
        logica.cambiarFicha(m);
    }
    public static void main(String[] args) {
        DamasGUI gui = new DamasGUI();
        gui.setVisible(true);
    }
}
