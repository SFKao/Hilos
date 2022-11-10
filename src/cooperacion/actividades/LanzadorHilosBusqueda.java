package cooperacion.actividades;

import java.util.Random;

public class LanzadorHilosBusqueda {

    public static final int NUMERO_MAXIMO = 10000;
    public static final int HILOS_MAX = 10;

    private static int numeroAzar;
    private static boolean acertado;

    synchronized public static int resolverNumero(int propuesta){
        if(acertado)
            return -1;
        if(propuesta == numeroAzar){
            acertado = true;
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        numeroAzar = new Random().nextInt(NUMERO_MAXIMO);
        Thread[] hilos = new Thread[HILOS_MAX];
        for(int i = 0; i < HILOS_MAX; i++){
            hilos[i] = new Thread(new HiloBusqueda(i));
            hilos[i].start();
        }
        System.out.printf("Numero azar = %d%n",numeroAzar);

    }

}

class HiloBusqueda implements Runnable{

    private int id;
    public HiloBusqueda(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        Random r = new Random();
        int salida = 0;
        while (salida == 0){
            int numeroAzar = (r.nextInt(LanzadorHilosBusqueda.NUMERO_MAXIMO));
            salida = LanzadorHilosBusqueda.resolverNumero(numeroAzar);
            if(salida == 1)
                System.out.printf("Hilo %d gana! Numero = %d%n",id,numeroAzar);
        }
    }
}
