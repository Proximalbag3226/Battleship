package graficos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import mapa.Tablero;
import mapa.Casilla;
import mapa.EstadoCasilla;
import naves.*;

public class BattleshipGUI extends JFrame {
    private static final int TAMAÑO_CELDA = 40;
    private static final int TAMAÑO_TABLERO = 10;

    private static final Color COLOR_AGUA = new Color(135, 206, 235);
    private static final Color COLOR_BARCO = new Color(105, 105, 105);
    private static final Color COLOR_TOCADO = new Color(220, 20, 60);
    private static final Color COLOR_FALLO = new Color(25, 25, 112);

    private Tablero tableroJugador;
    private Tablero tableroEnemigo;
    private Barco[] miFlota;
    private Barco[] flotaCPU;

    private JButton[][] botonesJugador;
    private JButton[][] botonesEnemigo;
    private JTextArea areaLog;
    private JLabel lblEstado;
    private JPanel panelBotones;

    private boolean turnoJugador = true;
    private boolean juegoIniciado = false;
    private boolean modoColocacion = true;
    private int barcoActualIndex = 0;
    private boolean orientacionHorizontal = true;

    private JButton btnDisparo;
    private JButton btnPortaaviones;
    private JButton btnSubmarino;
    private JButton btnAcorazado;
    private JButton btnCambiarOrientacion;

    public BattleshipGUI() {
        setTitle("BATTLESHIP - Batalla Naval");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        inicializarJuego();
        crearInterfaz();

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarJuego() {
        tableroJugador = new Tablero();
        tableroEnemigo = new Tablero();

        miFlota = new Barco[]{
                new Portaaviones(),
                new Acorazado(),
                new Submarino(),
                new Destructor()
        };

        flotaCPU = new Barco[]{
                new Portaaviones(),
                new Acorazado(),
                new Submarino(),
                new Destructor()
        };

        colocarBarcosCPU();
    }

    private void crearInterfaz() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("⚓ BATTLESHIP ⚓", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(titulo, BorderLayout.NORTH);

        lblEstado = new JLabel("Coloca tu flota - " + miFlota[0].getNombre(), SwingConstants.CENTER);
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 16));
        panelSuperior.add(lblEstado, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelTableros = new JPanel(new GridLayout(1, 2, 20, 0));
        panelTableros.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelTableros.add(crearPanelTablero("TU FLOTA", true));
        panelTableros.add(crearPanelTablero("ENEMIGO", false));

        add(panelTableros, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        areaLog = new JTextArea(8, 50);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        panelInferior.add(scrollLog, BorderLayout.CENTER);

        panelBotones = crearPanelBotones();
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        log("=== BIENVENIDO A BATTLESHIP ===");
        log("Coloca tus barcos en el tablero izquierdo");
        log("Barco actual: " + miFlota[0].getNombre() + " (Tamaño: " + miFlota[0].getTamaño() + ")");
    }

    private JPanel crearPanelTablero(String titulo, boolean esJugador) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(TAMAÑO_TABLERO + 1, TAMAÑO_TABLERO + 1, 1, 1));
        gridPanel.setBackground(Color.BLACK);

        gridPanel.add(new JLabel(""));

        for (int i = 0; i < TAMAÑO_TABLERO; i++) {
            JLabel lbl = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            gridPanel.add(lbl);
        }

        JButton[][] botones = new JButton[TAMAÑO_TABLERO][TAMAÑO_TABLERO];

        for (int i = 0; i < TAMAÑO_TABLERO; i++) {
            JLabel lbl = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            gridPanel.add(lbl);

            for (int j = 0; j < TAMAÑO_TABLERO; j++) {
                JButton btn = new JButton("~");
                btn.setPreferredSize(new Dimension(TAMAÑO_CELDA, TAMAÑO_CELDA));
                btn.setFont(new Font("Monospaced", Font.BOLD, 16));
                btn.setBackground(COLOR_AGUA);
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

                final int fila = i;
                final int col = j;

                if (esJugador) {
                    btn.addActionListener(e -> clickTableroJugador(fila, col));
                } else {
                    btn.addActionListener(e -> clickTableroEnemigo(fila, col));
                }

                botones[i][j] = btn;
                gridPanel.add(btn);
            }
        }

        if (esJugador) {
            botonesJugador = botones;
        } else {
            botonesEnemigo = botones;
        }

        panel.add(gridPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));

        btnCambiarOrientacion = new JButton("🔄 Cambiar Orientación");
        btnCambiarOrientacion.addActionListener(e -> cambiarOrientacion());

        btnDisparo = new JButton("💣 Disparo Normal");
        btnDisparo.setEnabled(false);
        btnDisparo.addActionListener(e -> log("Modo: Disparo Normal - Haz clic en el tablero enemigo"));

        btnPortaaviones = new JButton("✈️ Portaaviones");
        btnPortaaviones.setEnabled(false);
        btnPortaaviones.addActionListener(e -> usarHabilidadPortaaviones());

        btnSubmarino = new JButton("📡 Submarino (Sonar)");
        btnSubmarino.setEnabled(false);
        btnSubmarino.addActionListener(e -> modoSonar());

        btnAcorazado = new JButton("💥 Acorazado (Racimo)");
        btnAcorazado.setEnabled(false);
        btnAcorazado.addActionListener(e -> modoRacimo());

        JButton btnReiniciar = new JButton("🔄 Nuevo Juego");
        btnReiniciar.addActionListener(e -> reiniciarJuego());

        panel.add(btnCambiarOrientacion);
        panel.add(btnDisparo);
        panel.add(btnPortaaviones);
        panel.add(btnSubmarino);
        panel.add(btnAcorazado);
        panel.add(btnReiniciar);

        return panel;
    }

    private void clickTableroJugador(int fila, int col) {
        if (modoColocacion && barcoActualIndex < miFlota.length) {
            Barco barco = miFlota[barcoActualIndex];

            if (tableroJugador.colocarBarco(barco, fila, col, orientacionHorizontal)) {
                log("✓ " + barco.getNombre() + " colocado en (" + fila + "," + col + ")");
                actualizarTableroJugador();

                barcoActualIndex++;

                if (barcoActualIndex < miFlota.length) {
                    Barco siguiente = miFlota[barcoActualIndex];
                    lblEstado.setText("Coloca tu flota - " + siguiente.getNombre());
                    log("Siguiente: " + siguiente.getNombre() + " (Tamaño: " + siguiente.getTamaño() + ")");
                } else {
                    modoColocacion = false;
                    juegoIniciado = true;
                    lblEstado.setText("⚡ TU TURNO - Ataca al enemigo ⚡");
                    log("=== ¡TODOS LOS BARCOS COLOCADOS! ===");
                    log("¡Comienza la batalla!");

                    btnCambiarOrientacion.setEnabled(false);
                    btnDisparo.setEnabled(true);
                    btnPortaaviones.setEnabled(true);
                    btnSubmarino.setEnabled(true);
                    btnAcorazado.setEnabled(true);
                }
            } else {
                log("✗ Posición inválida para " + barco.getNombre());
            }
        }
    }

    private void clickTableroEnemigo(int fila, int col) {
        if (!juegoIniciado || !turnoJugador) return;

        String resultado = tableroEnemigo.lanzarDisparo(fila, col);
        log("Tu disparo en (" + fila + "," + col + "): " + resultado);

        actualizarTableroEnemigo();
        verificarFinJuego();

        if (juegoIniciado) {
            turnoJugador = false;
            lblEstado.setText("⏳ Turno de la CPU...");
            deshabilitarBotones();
            Timer timer = new Timer(1500, e -> turnoCPU());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void modoSonar() {
        if (!juegoIniciado || !turnoJugador) return;

        if (miFlota[2].estaHundido()) {
            log("✗ El Submarino ha sido hundido. No puedes usar esta habilidad.");
            return;
        }

        try {
            String filaStr = JOptionPane.showInputDialog(this, "Fila central del sonar (0-9):");
            if (filaStr == null) return;
            String colStr = JOptionPane.showInputDialog(this, "Columna central del sonar (0-9):");
            if (colStr == null) return;

            int fila = Integer.parseInt(filaStr);
            int col = Integer.parseInt(colStr);

            Submarino s = (Submarino) miFlota[2];
            String resultado = s.activarSonar(tableroEnemigo, fila, col);
            log("📡 SONAR: " + resultado);

            verificarFinJuego();

            if (juegoIniciado) {
                turnoJugador = false;
                lblEstado.setText("⏳ Turno de la CPU...");
                deshabilitarBotones();
                Timer timer = new Timer(1500, e -> turnoCPU());
                timer.setRepeats(false);
                timer.start();
            }
        } catch (NumberFormatException ex) {
            log("✗ Coordenadas inválidas");
        }
    }

    private void modoRacimo() {
        if (!juegoIniciado || !turnoJugador) return;

        if (miFlota[1].estaHundido()) {
            log("✗ El Acorazado ha sido hundido. No puedes usar esta habilidad.");
            return;
        }

        try {
            String filaStr = JOptionPane.showInputDialog(this, "Fila del ataque de racimo (0-9):");
            if (filaStr == null) return;
            String colStr = JOptionPane.showInputDialog(this, "Columna del ataque de racimo (0-9):");
            if (colStr == null) return;

            int fila = Integer.parseInt(filaStr);
            int col = Integer.parseInt(colStr);

            Acorazado a = (Acorazado) miFlota[1];
            String resultado = a.disparar_area(tableroEnemigo, fila, col);
            log("💥 " + resultado);

            actualizarTableroEnemigo();
            verificarFinJuego();

            if (juegoIniciado) {
                turnoJugador = false;
                lblEstado.setText("⏳ Turno de la CPU...");
                deshabilitarBotones();
                Timer timer = new Timer(1500, e -> turnoCPU());
                timer.setRepeats(false);
                timer.start();
            }
        } catch (NumberFormatException ex) {
            log("✗ Coordenadas inválidas");
        }
    }

    private void usarHabilidadPortaaviones() {
        if (!juegoIniciado || !turnoJugador) return;

        if (miFlota[0].estaHundido()) {
            log("✗ El Portaaviones ha sido hundido. No puedes usar esta habilidad.");
            return;
        }

        Portaaviones p = (Portaaviones) miFlota[0];
        String resultado = p.Misiles(tableroEnemigo);
        log(resultado);

        actualizarTableroEnemigo();
        verificarFinJuego();

        if (juegoIniciado) {
            turnoJugador = false;
            lblEstado.setText("⏳ Turno de la CPU...");
            deshabilitarBotones();
            Timer timer = new Timer(1500, e -> turnoCPU());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void turnoCPU() {
        Random rand = new Random();
        int fila = rand.nextInt(10);
        int col = rand.nextInt(10);

        String resultado = tableroJugador.lanzarDisparo(fila, col);
        log("CPU dispara a (" + fila + "," + col + "): " + resultado);

        actualizarTableroJugador();
        verificarFinJuego();

        if (juegoIniciado) {
            turnoJugador = true;
            lblEstado.setText("⚡ TU TURNO - Ataca al enemigo ⚡");
            habilitarBotones();
        }
    }

    private void cambiarOrientacion() {
        orientacionHorizontal = !orientacionHorizontal;
        log("Orientación: " + (orientacionHorizontal ? "Horizontal ↔" : "Vertical ↕"));
    }

    private void actualizarTableroJugador() {
        for (int i = 0; i < TAMAÑO_TABLERO; i++) {
            for (int j = 0; j < TAMAÑO_TABLERO; j++) {
                actualizarBoton(botonesJugador[i][j], tableroJugador, i, j, false);
            }
        }
    }

    private void actualizarTableroEnemigo() {
        for (int i = 0; i < TAMAÑO_TABLERO; i++) {
            for (int j = 0; j < TAMAÑO_TABLERO; j++) {
                actualizarBoton(botonesEnemigo[i][j], tableroEnemigo, i, j, true);
            }
        }
    }

    private void actualizarBoton(JButton btn, Tablero tablero, int fila, int col, boolean oculto) {
        Casilla casilla = tablero.getCasilla(fila, col);
        if (casilla == null) return;

        EstadoCasilla estado = casilla.getEstado();

        switch (estado) {
            case AGUA:
                btn.setText("~");
                btn.setBackground(COLOR_AGUA);
                btn.setForeground(Color.WHITE);
                break;

            case BARCO:
                if (oculto) {
                    btn.setText("~");
                    btn.setBackground(COLOR_AGUA);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setText("■");
                    btn.setBackground(COLOR_BARCO);
                    btn.setForeground(Color.WHITE);
                }
                break;

            case TOCADO:
                btn.setText("X");
                btn.setBackground(COLOR_TOCADO);
                btn.setForeground(Color.WHITE);
                break;

            case FALLO:
                btn.setText("○");
                btn.setBackground(COLOR_FALLO);
                btn.setForeground(Color.WHITE);
                break;
        }
    }

    private void verificarFinJuego() {
        if (tableroEnemigo.todosLosBarcosHundidos()) {
            juegoIniciado = false;
            lblEstado.setText("🎉 ¡VICTORIA! 🎉");
            log("=== ¡HAS GANADO! ===");
            deshabilitarBotones();
            JOptionPane.showMessageDialog(this, "¡Felicidades! Has hundido toda la flota enemiga.", "VICTORIA", JOptionPane.INFORMATION_MESSAGE);
        } else if (tableroJugador.todosLosBarcosHundidos()) {
            juegoIniciado = false;
            lblEstado.setText("💀 DERROTA 💀");
            log("=== HAS SIDO DERROTADO ===");
            deshabilitarBotones();
            JOptionPane.showMessageDialog(this, "Tu flota ha sido destruida.", "DERROTA", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deshabilitarBotones() {
        btnDisparo.setEnabled(false);
        btnPortaaviones.setEnabled(false);
        btnSubmarino.setEnabled(false);
        btnAcorazado.setEnabled(false);
    }

    private void habilitarBotones() {
        btnDisparo.setEnabled(true);
        btnPortaaviones.setEnabled(true);
        btnSubmarino.setEnabled(true);
        btnAcorazado.setEnabled(true);
    }

    private void colocarBarcosCPU() {
        Random rand = new Random();
        for (Barco barco : flotaCPU) {
            boolean colocado = false;
            while (!colocado) {
                int f = rand.nextInt(10);
                int c = rand.nextInt(10);
                boolean h = rand.nextBoolean();
                colocado = tableroEnemigo.colocarBarco(barco, f, c, h);
            }
        }
    }

    private void reiniciarJuego() {
        dispose();
        new BattleshipGUI().setVisible(true);
    }

    private void log(String mensaje) {
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BattleshipGUI().setVisible(true);
        });
    }
}