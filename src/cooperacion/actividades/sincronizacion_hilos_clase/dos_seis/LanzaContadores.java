package cooperacion.actividades.sincronizacion_hilos_clase.dos_seis;


public class LanzaContadores {

    public static final int CANTIDAD_A_CONTAR = 1000000;
    public static final int NUMERO_HILOS = 10;

    public static void main(String[] args) {
        Contador c = new Contador();
        Thread hilos[] = new Thread[NUMERO_HILOS];
        for(int i = 0; i < NUMERO_HILOS; i++)
            hilos[i] = new HiloContador(c,String.valueOf(i+1),CANTIDAD_A_CONTAR/NUMERO_HILOS);
        for(Thread t : hilos)
            t.start();
        for(Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("Contador1 = %d, Contador2 = %d%n",c.getContador1(),c.getContador2());



    }

}

class HiloContador extends Thread {

    private final Contador contador;
    private final String nombre;
    private final int cantidadAContar;

    public HiloContador(Contador contador, String nombre, int cantidadAContar) {
        this.contador = contador;
        this.nombre = nombre;
        this.cantidadAContar = cantidadAContar;
    }

    @Override
    public void run() {
        for(int i = 0; i < cantidadAContar;i++)
            contador.incrementar1();
        for(int i = 0; i < cantidadAContar;i++)
            contador.incrementar2();
        System.out.printf("Hilo %s ha terminado de contar%n",nombre);
    }
}

class Contador {
    private int contador1 = 0;
    private int contador2 = 0;

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public Contador() {
    }

    public void incrementar1(){
        synchronized (lock1){
            contador1++;
        }
    }

    public int getContador1(){
        synchronized (lock1){
            return contador1;
        }
    }

    public void incrementar2(){
        synchronized (lock2){
            contador2++;
        }
    }

    public int getContador2(){
        synchronized (lock2){
            return contador2;
        }
    }
}
