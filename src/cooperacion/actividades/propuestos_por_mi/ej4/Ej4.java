package cooperacion.actividades.propuestos_por_mi.ej4;



public class Ej4 {
    public static final int CANTIDAD_HILOS = 10;
    public static final int CANTIDAD_A_CONTAR = 1000;

    public static void main(String[] args) {
        ContadorAtomico c = new ContadorAtomico();
        Thread h[] = new Thread[CANTIDAD_HILOS];
        for(int i = 0 ;i < h.length; i++)
            h[i] = new Thread(new Hilos(CANTIDAD_A_CONTAR/CANTIDAD_HILOS,c));
        for(Thread hs : h)
            hs.start();
        try{
            for(Thread hs : h)
                hs.join();
        }catch (Exception e){}
        System.out.printf("Cantidad contada = %d",c.getI());
    }
}

class Hilos implements Runnable{

    public Hilos(int cantidadAContar, ContadorAtomico c) {
        this.cantidadAContar = cantidadAContar;
        this.c = c;
    }

    private int cantidadAContar;
    private ContadorAtomico c;
    @Override
    public void run() {
        for(int i = 0; i < cantidadAContar; i++)
            c.aumentarI();
    }
}

class ContadorAtomico{
    private static int i = 0;

    public static synchronized int getI() {
        return i;
    }

    public static synchronized void aumentarI() {
        i++;
    }
}



