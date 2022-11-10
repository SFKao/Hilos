package cooperacion;

public class LanzadorHilos {

    public static final int NUM_HILOS = 10;
    public static final int CANTIDAD_A_CONTAR = 100000;

    public static void main(String[] args) {
        Thread[] hilos = new Thread[NUM_HILOS];
        Contador c = new Contador();
        for (int i = 0; i < NUM_HILOS; i++) {
            hilos[i] = new Thread(new HiloContador(i,CANTIDAD_A_CONTAR/NUM_HILOS,c));
            hilos[i].start();
        }

        for (Thread t: hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Error al unir el hilo");
            }
        }

        System.out.printf("Se ha terminado de contar, el resultado es %d",c.getContador());
    }
}
