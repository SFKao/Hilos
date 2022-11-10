package lanzahilos;

public class HiloThread extends Thread{

    private final String nombre;

    public HiloThread(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        System.out.printf("Soy el hilo: %s%n",nombre);
        System.out.printf("Finalizando hilo: %s%n",nombre);
    }

}
