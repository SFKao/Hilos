package lanzahilos;

public class LanzaHilos {
    public static void main(String[] args) {
        Thread t1 = new HiloThread("1");
        Thread t2 = new HiloThread("2");
        t1.start();
        t2.start();
        System.out.println("Finalizado hilo principal");
    }

    public static void runnable(){

        Thread h1 = new Thread(new Hilo("1"));
        Thread h2 = new Thread(new Hilo("2"));
        h1.start();
        h2.start();
        System.out.println("Finalizando hilo principal");

    }


}