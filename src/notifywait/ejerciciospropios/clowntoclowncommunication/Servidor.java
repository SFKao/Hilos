package notifywait.ejerciciospropios.clowntoclowncommunication;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Servidor {

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        Thread mongoloEscritor1 = new MongoloEscritor("Oscar",servidor);
        Thread mongoloEscritor2 = new MongoloEscritor("Mesa",servidor);
        MongoloLector mongoloLector1 = new MongoloLector("Manu",servidor);
        MongoloLector mongoloLector2 = new MongoloLector("Alejandro",servidor);

        mongoloEscritor1.start();
        mongoloEscritor2.start();
        mongoloLector1.start();
        mongoloLector2.start();

        try{
            mongoloEscritor1.join();
            mongoloEscritor2.join();
            mongoloLector1.join(500);
            mongoloLector2.join(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(mongoloLector1.escrito);
        System.out.println(mongoloLector2.escrito);
    }

    Stack<String> mensajes = new Stack<>();
    int mongolosQueEstanEscribiendo = 2;
    final Object finalizadoDeEscribirLock = new Object();

    public String getMensaje() throws InterruptedException {
        synchronized (this){
            while(mensajes.size()==0) {
                System.out.println(", nada que leer");
                this.wait();
            }
            notifyAll();
            return mensajes.pop();
        }
    }

    public void addMensaje(String s) throws InterruptedException {
        synchronized (this){
            while (mensajes.size()>3) {
                System.out.println(", muchos mensajes");
                this.wait();
            }
            mensajes.add(s);
            notifyAll();
        }
    }

    public void dejarDeEscribir(){
        synchronized (finalizadoDeEscribirLock){
            mongolosQueEstanEscribiendo--;
        }
    }

    public boolean finalizadaEscritura(){
        synchronized (finalizadoDeEscribirLock){
            return mongolosQueEstanEscribiendo==0;
        }
    }

}

class MongoloLector extends Thread{
    String nombre;
    ArrayList<String> escrito = new ArrayList<>();
    Servidor servidor;

    public MongoloLector(String nombre, Servidor servidor) {
        this.nombre = nombre;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        while (!servidor.finalizadaEscritura()){
            try {
                Thread.sleep(new Random().nextInt(40,101));
                System.out.println(nombre+" va a leer");
                escrito.add(servidor.getMensaje());
                System.out.println(" ,lee mensaje");
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

class MongoloEscritor extends Thread{
    String nombre;
    Servidor servidor;

    public MongoloEscritor(String nombre, Servidor servidor) {
        this.nombre = nombre;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        for(int i = 0; i < 50; i++){
            try{
                Thread.sleep(new Random().nextInt(60,81));
                System.out.println(nombre+" va a escribir");
                servidor.addMensaje(nombre+"-"+i);
                System.out.println(nombre+" escribe mensaje");
            }catch (InterruptedException e){
                return;
            }
        }
    }
}