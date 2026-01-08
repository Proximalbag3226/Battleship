import java.util.Random;
import java.util.Scanner;
import mapa.Tablero;
import naves.*;

public class Main {



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("---BIENVENIDO A BATTLESHIP---");

        Tablero tableroJugador = new Tablero();
        Tablero tableroEnemigo = new Tablero();

        Barco[] miFlota = {
                new Portaaviones(),
                new Acorazado(),
                new Submarino(),
                new Destructor()
        };

        colocarBarcosJugador(tableroJugador, scanner, miFlota);

        System.out.println("\nLa CPU está desplegando sus naves...");
        colocarBarcosCPU(tableroEnemigo);

        boolean juegoTerminado = false;
        boolean turnoJugador = true;

        while (!juegoTerminado) {

            if (turnoJugador) {
                System.out.println("\n---------------------------------");
                System.out.println("⚡ TU TURNO ⚡");
                System.out.println("Mapa Enemigo (Vista Satélite):");
                tableroEnemigo.mostrarTablero(true);

                System.out.println("\n¿Qué quieres hacer?");
                System.out.println("1. Disparo de Cañón (Normal)");
                System.out.println("2. Habilidad Especial");
                System.out.print("Elige una opción: ");
                int opcion = scanner.nextInt();

                if (opcion == 1) {
                    System.out.print("Fila (0-9): ");
                    int x = scanner.nextInt();
                    System.out.print("Columna (0-9): ");
                    int y = scanner.nextInt();

                    String resultado = tableroEnemigo.lanzarDisparo(x, y);
                    System.out.println("\nResultados: " + resultado);

                } else if (opcion == 2) {
                    System.out.println("\n--- SELECCIÓN DE HABILIDAD ---");
                    System.out.println("1. Portaaviones (Ataque Aéreo - 3 misiles al azar)");
                    System.out.println("2. Submarino (Sonar - Escanear área 2x2)");
                    System.out.println("3. Acorzado (Ataque de racimo - Ataque en area de 2x2)");
                    System.out.print("-> Selecciona nave: ");
                    int seleccion = scanner.nextInt();

                    if (seleccion == 1) {
                        Barco nave = miFlota[0];
                        if (nave.estaHundido()) {
                            System.out.println("COMANDANTE: El Portaaviones ha sido destruido. No podemos despegar.");
                        } else {
                            Portaaviones p = (Portaaviones) nave;
                            System.out.println(p.Misiles(tableroEnemigo));
                        }

                    } else if (seleccion == 2) {
                        Barco nave = miFlota[2];
                        if (nave.estaHundido()) {
                            System.out.println("COMANDANTE: El Submarino no responde. Ha sido hundido.");
                        } else {
                            System.out.print("Fila central sonar: ");
                            int f = scanner.nextInt();
                            System.out.print("Columna central sonar: ");
                            int c = scanner.nextInt();

                            Submarino s = (Submarino) nave;
                            String hayEnemigo = s.activarSonar(tableroEnemigo, f, c);
                            System.out.println(hayEnemigo);

                        }
                    } else if (seleccion == 3) {
                        Barco nave = miFlota[1];
                        if (nave.estaHundido()) {
                            System.out.println("COMANDANTE: El acorazado no responde. Ha sido hundido :(.");
                        }
                        else {
                            System.out.println("FIla del ataque de racimo: ");
                            int f = scanner.nextInt();
                            System.out.println("Columna del ataque de racimo: ");
                            int c = scanner.nextInt();
                            Acorazado a = (Acorazado) nave;
                            String racimo = a.disparar_area(tableroEnemigo, f, c);
                            System.out.println(racimo);
                        }
                    } else {
                        System.out.println("Opción inválida. Pierdes el turno por dudar.");
                    }
                }

            } else {
                System.out.println("\nTURNO DE LA CPU");
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                System.out.println("La CPU dispara a (" + x + "," + y + ")");

                String resultado = tableroJugador.lanzarDisparo(x, y);
                System.out.println("Resultado: " + resultado);

                System.out.println("Tu Flota:");
                tableroJugador.mostrarTablero(false);
            }

            if (tableroEnemigo.todosLosBarcosHundidos()) {
                System.out.println("\n¡VICTORIA! Has hundido toda la flota enemiga.");
                juegoTerminado = true;
            } else if (tableroJugador.todosLosBarcosHundidos()) {
                System.out.println("\nDERROTA. Tu flota yace en el fondo del mar.");
                juegoTerminado = true;
            }

            turnoJugador = !turnoJugador;
        }

        scanner.close();
    }

    public static void colocarBarcosJugador(Tablero tablero, Scanner scanner, Barco[] flota) {
        System.out.println("\n---FASE DE DESPLIEGUE---");

        for (Barco barco : flota) {
            boolean colocado = false;
            while (!colocado) {
                System.out.println("\nTu tablero actual:");
                tablero.mostrarTablero(false);

                System.out.println("Colocando: " + barco.getNombre() + " (Tamaño: " + barco.getTamaño() + ")");
                System.out.print("Fila: ");
                int f = scanner.nextInt();
                System.out.print("Columna: ");
                int c = scanner.nextInt();
                System.out.print("¿Horizontal? (true/false): ");
                boolean h = scanner.nextBoolean();

                if (tablero.colocarBarco(barco, f, c, h)) {
                    System.out.println("Barco colocado.");
                    colocado = true;
                } else {
                    System.out.println("Error: Posición inválida o ocupada.");
                }
            }
        }
    }

    public static void colocarBarcosCPU(Tablero tablero) {
        Barco[] flotaCPU = { new Portaaviones(), new Acorazado(), new Submarino(), new Destructor() };
        Random rand = new Random();

        for (Barco barco : flotaCPU) {
            boolean colocado = false;
            while (!colocado) {
                int f = rand.nextInt(10);
                int c = rand.nextInt(10);
                boolean h = rand.nextBoolean();
                if (tablero.colocarBarco(barco, f, c, h)) {
                    colocado = true;
                }
            }
        }
    }
}