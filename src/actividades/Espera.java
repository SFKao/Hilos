package actividades;

import lanzahilos.HiloThread;

public class Espera {
    public static void main(String[] args) {
        Thread t1 = new HiloThread("1");
        Thread t2 = new HiloThread("2");
        t1.start();
        t2.start();
        try{
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Soy el padre, terminando");
    }

}
