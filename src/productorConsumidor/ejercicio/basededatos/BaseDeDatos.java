package productorConsumidor.ejercicio.basededatos;

import java.util.Random;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class BaseDeDatos {

    public static void main(String[] args) {
        BaseDeDatos baseDeDatos = new BaseDeDatos();
        new ProcesoEscritor(baseDeDatos,"ESCRIBEMAN").start();;

        new ProcesoLector(baseDeDatos,"LECTORMAN").start();
    }

    Stack<String> datos = new Stack<>();
    final Semaphore semaforoEscritor = new Semaphore(50);
    final Semaphore semaforoLectura = new Semaphore(0);
    final Semaphore mutex = new Semaphore(1);

    public Semaphore getSemaforoEscritor() {
        return semaforoEscritor;
    }

    public Semaphore getSemaforoLectura() {
        return semaforoLectura;
    }

    public Semaphore getMutex() {
        return mutex;
    }
}

class ProcesoLector extends Thread{

    BaseDeDatos baseDeDatos;
    String nombre;

    public ProcesoLector(BaseDeDatos baseDeDatos, String nombre) {
        this.baseDeDatos = baseDeDatos;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            while (true) {
                baseDeDatos.semaforoLectura.acquire();
                baseDeDatos.mutex.acquire();

                System.out.println("TAM: "+baseDeDatos.datos.size()+", dato: "+baseDeDatos.datos.pop());

                baseDeDatos.mutex.release();
                baseDeDatos.semaforoEscritor.release();
            }
        } catch (InterruptedException ignored) {
        }
    }
}

class ProcesoEscritor extends Thread{
    BaseDeDatos baseDeDatos;
    String nombre;

    public ProcesoEscritor(BaseDeDatos baseDeDatos, String nombre) {
        this.baseDeDatos = baseDeDatos;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            while (true) {
                baseDeDatos.semaforoEscritor.acquire();
                baseDeDatos.mutex.acquire();

                baseDeDatos.datos.push(String.valueOf(new Random().nextInt(500)));
                System.out.println("TAM: "+baseDeDatos.datos.size());

                baseDeDatos.mutex.release();
                baseDeDatos.semaforoLectura.release();
            }
        } catch (InterruptedException ignored) {
        }
    }
}

