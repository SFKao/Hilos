package cooperacion.actividades.propuestos_por_mi.ej5;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Ej5 {

    public static final int CANTIDAD_HILOS = 10;

    public static void main(String[] args) {
        SacarNumeros s = new SacarNumeros();
        Thread h[] = new Thread[CANTIDAD_HILOS];
        for(int i = 0 ;i < h.length; i++)
            h[i] = new Thread(new HiloSacaNumeros(s,i));
        for(Thread hs : h)
            hs.start();
        try{
            for(Thread hs : h)
                hs.join();
        }catch (Exception e){}
        System.out.println("Fin del juego");
    }

}

class HiloSacaNumeros extends Thread{

    private SacarNumeros sacarNumeros;
    private int id;

    public HiloSacaNumeros(SacarNumeros sacarNumeros, int id) {
        this.sacarNumeros = sacarNumeros;
        this.id = id;
    }

    @Override
    public void run() {
        String sacado;
        Random r = new Random();
        while (true){
            sacado = sacarNumeros.obtenerNumero();
            if(sacado ==null)
                return;
            if(sacado.equals("1")){
                System.out.printf("El hilo %d ha encontrado el 1%n",id);
                return;
            }
            System.out.printf("El hilo %d ha sacado el %s%n",id,sacado);
            try{
                Thread.sleep(r.nextInt(100,300));
            }catch (Exception e){}
        }
    }
}


class SacarNumeros {
    private final ArrayList<String> arrayList;
    private boolean encontrado = false;
    private int pos = 0;

    public SacarNumeros() {
        arrayList = new ArrayList<>();
        for(int i = 0; i < 100; i++)
            arrayList.add(String.valueOf(i));
        Collections.shuffle(arrayList);
    }

    public synchronized String obtenerNumero(){
        if(encontrado)
            return null;
        String num = arrayList.get(pos++);
        if (num.equals("1"))
            encontrado = true;
        return num;
    }


}
