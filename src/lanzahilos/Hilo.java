package lanzahilos;

public class Hilo implements Runnable {

    private final String nombre;

    public Hilo(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        System.out.printf("Soy el hilo: %s%n",nombre);
        System.out.printf("Finalizando hilo: %s%n",nombre);
    }

}
