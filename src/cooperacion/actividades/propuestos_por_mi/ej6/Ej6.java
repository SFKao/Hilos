package cooperacion.actividades.propuestos_por_mi.ej6;



import javax.imageio.plugins.tiff.TIFFImageReadParam;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Ej6 {

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
        for (int i = 0; i < 10; i++){
            if(r.nextBoolean()){
                almacen.add(String.valueOf(r.nextInt(1,100000)));
            }else{
                if (almacen.size()!=0)
                    System.out.printf("Hilo %d leo %s%n",id,almacen.get(r.nextInt(0,almacen.size())));
            }
            try{
                Thread.sleep(r.nextInt(100,300));
            }catch (Exception e){}
        }
    }
}

class Almacen{
    private ArrayList<String> stack;

    public Almacen() {
        stack = new ArrayList<>();
    }

    public synchronized void add(String e){
        stack.add(e);
    }

    public synchronized String get(int i){
        return stack.get(i);
    }

    public synchronized int size(){
        return stack.size();
    }
}
