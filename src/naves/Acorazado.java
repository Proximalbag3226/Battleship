package naves;
import mapa.Tablero;


public class Acorazado extends Barco{
    int contador_uso=0;
    public Acorazado() {
        super("Acorazado", 4);
    }

    public String disparar_area(Tablero tablero, int x, int y){
        contador_uso++;
        if (contador_uso>1){
            return "No puedes usar mas esta habilidad";
        }

        StringBuilder reporte = new StringBuilder("COMENZANDO ATAQUE DE RACIMO: \n");
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                String resultado = tablero.lanzarDisparo(x+i, y+j);
                reporte.append("   Misil #").append(i)
                        .append(" en (").append(x+i).append(",").append(y+j).append("): ")
                        .append(resultado).append("\n");
            }
        }
        return "Disparos en area lanzados";
    }
}
