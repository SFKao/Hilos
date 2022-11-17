package cooperacion.actividades.propuestos_por_mi.ej7;

import java.util.ArrayList;
import java.util.Random;

public class Ej7 {

    public static void main(String[] args) {
        Persona p1 = new Persona("Oscar");
        Persona p2 = new Persona("Manulillo");
        Persona p3 = new Persona("Delegao");

        ArrayList<Thread> hilos = new ArrayList<>();
        hilos.add(new HilosHalloween(p1,p2));
        hilos.add(new HilosHalloween(p1,p3));
        hilos.add(new HilosHalloween(p2,p1));
        hilos.add(new HilosHalloween(p2,p3));
        hilos.add(new HilosHalloween(p3,p1));
        hilos.add(new HilosHalloween(p3,p1));

        for (Thread t: hilos) {
            t.start();
        }

        try{
            for(Thread t: hilos)
                t.join();
        }catch (Exception e){}
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
    }


}


class HilosHalloween extends Thread{

    private Persona p1, p2;

    public HilosHalloween(Persona p1, Persona p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void run() {
        for(int i = 0; i < 3; i++)
            GestorHalloween.transfiereCaramelos(p1,p2,new Random().nextInt(1,4));
    }
}

class GestorHalloween{

    public static void transfiereCaramelos(Persona p1, Persona p2, int cantidad){
        Persona menor, mayor;
        if(p1.getNombre().compareTo(p2.getNombre()) > 0){
            menor = p1;
            mayor = p2;
        }else{
            menor = p2;
            mayor = p1;
        }

        synchronized (menor){
            synchronized (mayor){
                p1.setCaramelos(p1.getCaramelos()-cantidad);
                p2.setCaramelos(p2.getCaramelos()+cantidad);
            }
        }

    }

}

class Persona{
    private String nombre;
    private int caramelos = 10;

    public Persona(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCaramelos() {
        return caramelos;
    }

    public void setCaramelos(int caramelos) {
        this.caramelos = caramelos;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", caramelos=" + caramelos +
                '}';
    }
}
