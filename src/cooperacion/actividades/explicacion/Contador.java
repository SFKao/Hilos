package cooperacion.actividades.explicacion;

public class Contador {
    private int contador = 0;

    synchronized public void aumentarContador(){
        contador++;
    }

    public int getContador() {
        return contador;
    }
}
