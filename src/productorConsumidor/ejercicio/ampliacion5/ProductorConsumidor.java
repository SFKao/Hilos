package productorConsumidor.ejercicio.ampliacion5;

import java.util.Random;

public class ProductorConsumidor {
    public static void main(String[] args) {
        Almacen a = new Almacen();
        for(int i = 0; i < 10; i++)
            new Consumidor(String.valueOf(i+1),a).start();
        new Productor("Productor",a).start();
    }
}

class Consumidor extends Thread{
    String nombre;
    Almacen almacen;

    public Consumidor(String nombre, Almacen almacen) {
        this.nombre = nombre;
        this.almacen = almacen;
    }

    @Override
    public void run() {
        while(true){
            try {
                almacen.get(new Random().nextInt(10,25));
                Thread.sleep(new Random().nextInt(100,500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Productor extends Thread{
    String nombre;
    Almacen almacen;

    public Productor(String nombre, Almacen almacen) {
        this.nombre = nombre;
        this.almacen = almacen;
    }

    @Override
    public void run() {
        while(true){
            try {
                almacen.put(new Random().nextInt(50,120));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Almacen{

    int cantidadActual = 0;

    public void get(int cantidad) throws InterruptedException {
        synchronized (this){
            while (!canGet(cantidad)){
                System.out.println("No puede coger "+cantidad);
                wait();
            }
            cantidadActual-=cantidad;
            System.out.println("Se coge "+cantidad+", total "+cantidadActual);
            notifyAll();
        }
    }

    public void put(int cantidad) throws InterruptedException {
        synchronized (this){
            while(!canPut(cantidad)){
                System.out.println("No puede insertar "+cantidad);
                wait();
            }
            cantidadActual+=cantidad;
            System.out.println("Se coloca "+cantidad+", total "+cantidadActual);
            notifyAll();
        }
    }

    private boolean canGet(int cantidad){
        return cantidadActual>=cantidad;
    }

    private boolean canPut(int cantidad){
        return cantidad+cantidadActual<=600;
    }

}
