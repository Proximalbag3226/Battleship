package naves;

public abstract class Barco {
    protected String nombre;
    protected int tamano;
    protected int vida;

    public Barco(String nombre, int tamano) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.vida = tamano;
    }

    public void recibirDisparo1() {
        this.vida--;

        if (estaHundido()) {
            System.out.println("Has hundido el " + this.nombre + " enemigo");
        } else {
            System.out.println("Impacto en el " + this.nombre + "enemigo");
        }
    }

    public boolean estaHundido() {
        return this.vida <= 0;
    }


    public int getTamaño() {
        return tamano;
    }

    public String getNombre() {
        return nombre;
    }
}