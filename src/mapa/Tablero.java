package mapa;

import naves.Barco;

public class Tablero {
    private static final int TAMAÑO = 10;
    private Casilla[][] casillas;

    public Tablero() {
        casillas = new Casilla[TAMAÑO][TAMAÑO];
        for (int i = 0; i < TAMAÑO; i++) {
            for (int j = 0; j < TAMAÑO; j++) {
                casillas[i][j] = new Casilla();
            }
        }
    }

    public void mostrarTablero(boolean esEnemigo) {
        System.out.print("  ");
        for (int i = 0; i < TAMAÑO; i++) System.out.print(i + " ");
        System.out.println();

        for (int i = 0; i < TAMAÑO; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < TAMAÑO; j++) {
                System.out.print(casillas[i][j].obtenerSimbolo(esEnemigo) + " ");
            }
            System.out.println();
        }
    }

    public boolean colocarBarco(Barco barco, int fila, int col, boolean horizontal) {

        if (horizontal) {
            if (col + barco.getTamaño() > TAMAÑO) return false;
        } else {
            if (fila + barco.getTamaño() > TAMAÑO) return false;
        }

        for (int i = 0; i < barco.getTamaño(); i++) {
            int f = horizontal ? fila : fila + i;
            int c = horizontal ? col + i : col;

            if (casillas[f][c].estaOcupada()) return false;
        }

        for (int i = 0; i < barco.getTamaño(); i++) {
            int f = horizontal ? fila : fila + i;
            int c = horizontal ? col + i : col;
            casillas[f][c].colocarBarco(barco);
        }
        return true;
    }

    public String lanzarDisparo(int fila, int col) {
        if (fila < 0 || fila >= TAMAÑO || col < 0 || col >= TAMAÑO) {
            return "Coordenadas inválidas";
        }
        return casillas[fila][col].atacar();
    }

    public boolean esCoordenadaValida(int fila, int col) {
        return fila >= 0 && fila < 10 && col >= 0 && col < 10;
    }

    public boolean hayBarcoEn(int fila, int col) {
        if (esCoordenadaValida(fila, col)) {
            return casillas[fila][col].estaOcupada();
        }
        return false;
    }

    public boolean todosLosBarcosHundidos() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (casillas[i][j].getEstado() == EstadoCasilla.BARCO) {
                    return false;
                }
            }
        }
        return true;
    }

}