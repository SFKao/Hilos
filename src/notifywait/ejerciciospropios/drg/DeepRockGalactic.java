package notifywait.ejerciciospropios.drg;

import java.util.ArrayList;
import java.util.Random;

public class DeepRockGalactic {
    public static void main(String[] args) {
        ArrayList<Thread> hilos = new ArrayList<>();
        Molly molly = new Molly();
        hilos.add(new Enano(molly,"Artillero"));
        hilos.add(new Enano(molly,"Explorador"));
        hilos.add(new Enano(molly,"Perforador"));
        hilos.add(new Enano(molly,"Ingeniero"));
        hilos.add(new ControlDeMision(molly));

        hilos.forEach(Thread::start);

        hilos.forEach(hilo -> {
            try {
                hilo.join();
            }catch (Exception ignored){}
        });
        System.out.println("MISION FINALIZADA");
        System.out.println(molly);
    }
}


class Enano extends Thread{

    final Molly molly;
    String nombre;

    public Enano(Molly molly, String nombre) {
        this.molly = molly;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        for(int i = 0; i< 20; i++){
            boolean picadoOro = new Random().nextBoolean();
            int cantidad = new Random().nextInt(20,61);
            try{
                Thread.sleep(new Random().nextInt(50,150));
            }catch (Exception ignored){}
            synchronized (molly){
                if(picadoOro) {
                    molly.oro += cantidad;
                    System.out.println(nombre+" depositando "+cantidad+" de oro\n\t"+molly);
                }
                else {
                    molly.nitra += cantidad;
                    System.out.println(nombre + " depositando " + cantidad + " de nitra\n\t" + molly);
                }
                molly.notifyAll();
            }
        }
        molly.enanosFinalizados++;
    }
}

class ControlDeMision extends Thread{

    final Molly molly;

    public ControlDeMision(Molly molly) {
        this.molly = molly;
    }

    @Override
    public void run() {
        synchronized (molly){
            while (true) {
                while (!molly.tieneSuficienteNitra()) {
                    if(molly.enanosFinalizados==4)
                        return;
                    try {
                        molly.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
                molly.nitra -= 80;
                System.out.println("Control de mision: Enviando pod de municion");
            }
        }
    }
}

class Molly {
    int nitra = 0, oro = 0;
    int enanosFinalizados = 0;

    public Molly() {
    }

    public int getNitra() {
        return nitra;
    }

    public void setNitra(int nitra) {
        this.nitra = nitra;
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }

    public boolean tieneSuficienteNitra(){
        return nitra >= 80;
    }

    @Override
    public String toString() {
        return "Molly{" +
                "nitra=" + nitra +
                ", oro=" + oro +
                '}';
    }
}
