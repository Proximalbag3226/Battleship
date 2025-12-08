package mapa;
import naves.Barco;

public class Casilla {
    private EstadoCasilla estado;
    private Barco barco;

    public Casilla() {
        this.estado = EstadoCasilla.AGUA;
        this.barco = null;
    }

    public void colocarBarco(Barco barco) {
        this.barco = barco;
        this.estado = EstadoCasilla.BARCO;
    }

    public boolean estaOcupada() {
        return this.barco != null;
    }

    public String atacar() {
        if (estado == EstadoCasilla.AGUA) {
            estado = EstadoCasilla.FALLO;
            return "¡Agua!";
        } else if (estado == EstadoCasilla.BARCO) {
            estado = EstadoCasilla.TOCADO;
            barco.recibirDisparo1();
            return "¡Tocado!";
        } else {
            return "Ya habías disparado aquí.";
        }
    }

    public String obtenerSimbolo(boolean modoOculto) {
        switch (estado) {
            case AGUA: return "~";
            case BARCO: return modoOculto ? "~" : "B";
            case TOCADO: return "X";
            case FALLO: return "O";
            default: return "?";
        }
    }

    public EstadoCasilla getEstado() {
        return estado;
    }
}
