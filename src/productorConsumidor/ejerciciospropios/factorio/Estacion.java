package productorConsumidor.ejerciciospropios.factorio;

import javax.imageio.plugins.tiff.TIFFImageReadParam;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Estacion extends Thread{

    public static void main(String[] args) {
        Estacion estacion = new Estacion();
        Thread[] threads = new Thread[8];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Tren(estacion);
            threads[i].start();
        }
        estacion.start();
    }

    Semaphore semaforoMutex = new Semaphore(1);
    Semaphore semaforoLlegadaDeTrenes = new Semaphore(5);
    Semaphore semaforoLlamarATren = new Semaphore(0);

    @Override
    public void run() {

        try {
            while (true) {
                this.semaforoLlamarATren.acquire();
                System.out.println("Listo para cargar mas metal, trenes en espera: "+semaforoLlegadaDeTrenes.getQueueLength());
                this.semaforoMutex.release();
                Thread.sleep(10);
                this.semaforoLlegadaDeTrenes.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Tren extends Thread{
    Estacion estacion;
    int id = idStatic++;
    static int idStatic = 1;
    public Tren(Estacion estacion) {
        this.estacion = estacion;
    }

    @Override
    public void run() {
        try {
            while(true) {
                estacion.semaforoLlegadaDeTrenes.acquire();
                estacion.semaforoMutex.acquire();
                System.out.println(id + " recoge hierro");
                estacion.semaforoLlamarATren.release();
                Thread.sleep(new Random().nextInt(30,50));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
