package naves;

import mapa.Tablero;

public class Submarino extends Barco {
    int contador_uso = 0;
    public Submarino() {
        super("Submarino", 3);
    }

    public String activarSonar(Tablero tableroEnemigo, int fila, int col) {
        contador_uso++;
        if (contador_uso>2){
            return "No puedes usar mas esta habilidad";
        }
        System.out.println("ACTIVANDO SONAR EN EL SECTOR: " + fila + "," + col + "...");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {

                int filaAVerificar = fila + i;
                int colAVerificar = col + j;

                if (tableroEnemigo.hayBarcoEn(filaAVerificar, colAVerificar)) {
                    return "!Se ha encontrado un enemigo en la zona!";
                }
            }
        }
        return "No hay nada en esa zona :(";
    }
}