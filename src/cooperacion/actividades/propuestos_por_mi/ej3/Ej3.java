package cooperacion.actividades.propuestos_por_mi.ej3;



import java.util.Random;
import java.util.Stack;

public class Ej3 {

    public static final int CANTIDAD_HILOS = 10;

    public static void main(String[] args) {
        Almacen a = new Almacen();
        Thread h[] = new Thread[CANTIDAD_HILOS];
        for(int i = 0 ;i < h.length; i++)
            h[i] = new HilosSacaNumeros(a,i);
        for(Thread hs : h)
            hs.start();
        try{
            for(Thread hs : h)
                hs.join();
        }catch (Exception e){}
    }

}



class HilosSacaNumeros extends Thread{

    private Almacen almacen;
    private int id;

    public HilosSacaNumeros(Almacen almacen, int id) {
        this.almacen = almacen;
        this.id = id;
    }

    @Override
    public void run() {
        Random r = new Random();
        while(almacen.size()>0){
            System.out.printf("Hilo %d saca %s%n",id,almacen.pop());
            try {
                Thread.sleep(r.nextInt(100,301));
            } catch (InterruptedException e) {}
        }
    }
}

class Almacen{
    private Stack<String> stack;

    public Almacen() {
        stack = new Stack<>();
        for(int i = 0; i < 100; i++)
            stack.push(String.valueOf(i));
    }

    public synchronized String pop(){
        return stack.pop();
    }

    public synchronized int size(){
        return stack.size();
    }
}
