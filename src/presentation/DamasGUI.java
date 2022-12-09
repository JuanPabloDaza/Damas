package presentation;
import domain.Damas;
import domain.DamasException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class DamasGUI extends JFrame {

    private JMenuBar menubar;
    private JMenu menu;
    private JMenuItem nuevo,abrir;
    private JPanel tablero,juego,info,menuM;

    private JButton play;
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
    private Icon cambio;
    JLabel timeLabel;
    private int second,minute ;
    private Timer timer;
    private String ddSecond, ddMinute;
    private JLabel turno,jugador;

    DecimalFormat dFormat = new DecimalFormat("00");

    private int hora,minuto,segundo;

    private Damas logica;
    private DamasGUI(){
        super("Damas");
        logica = new Damas();
        turno = new JLabel("Turno: "+ Integer.toString(logica.getTurno()));
        prepareElements();
    }

    private void prepareJugadores(){
        for(int i = 1; i < 3; i++){
            String nombre = JOptionPane.showInputDialog(null, "Escriba el nombre del jugador " + Integer.toString(i) + ".");
            logica.agregarJugador(i, nombre);
        }
    }

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

    private void prepareElementsMainM() {

        menuM = new JPanel();
        menuM.add(new JLabel("Damas Poob"));
        play = new JButton();
        play.setText("PLAY");
        menuM.add(play);
        add(menuM);
        prepareActionsMenu();
    }

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

        timeLabel = new JLabel();
        timeLabel.setText("--:--");
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        info.add(timeLabel,BorderLayout.SOUTH);
        second = 60;
        minute = 1;
        timer();
        timer.start();
    }

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
                if (minute == 0 && second == 0){
                    timer.stop();
                }
            }
        });
    }
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
    }

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
    }

    private void accionNuevaCasilla(){
        int[] posicion = logica.getPosicionCasilla();
        if(logica.getTipoCasilla().equals("Mine")){
            malla[posicion[0]][posicion[1]].setIcon(casillaMinaIcono);
            malla[posicion[0]][posicion[1]].repaint();
        }
    }

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

    private void accionVictoria(){
        int output = JOptionPane.showConfirmDialog(null, "El jugador " + logica.getNombreJugador(logica.getJugador()) + " acaba de ganar.", "Victoria", JOptionPane.DEFAULT_OPTION);
    }

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
