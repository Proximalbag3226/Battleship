package naves;
import java.util.Random;
import mapa.Tablero;

public class Portaaviones extends Barco{
    int contador_uso = 0;

    public Portaaviones() {
        super("Portaaviones", 5);
    }

    public String Misiles(Tablero tablero) {
        contador_uso++;
        if (contador_uso>1) {
            return "No puedes utilizar mas esta habiliudad";
        }

        StringBuilder reporte = new StringBuilder("ATAQUE AEREO COMENZADO POR PORTAAVIONES: \n");
        Random rand = new Random();

        for (int i = 1; i <= 3; i++) {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);

            String resultado = tablero.lanzarDisparo(x, y);

            reporte.append("   Misil #").append(i)
                    .append(" en (").append(x).append(",").append(y).append("): ")
                    .append(resultado).append("\n");
        }
        return reporte.toString();
    }

    @Override
    public void recibirDisparo1() {
        this.vida--;

        if (estaHundido()) {
            System.out.println("!!Portaaviones enemigo destruido su flota aerea esta cayendo!!");
        } else {
            System.out.println("Portaaviones enemigo dañado");
        }
    }
}
