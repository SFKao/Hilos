package notifywait.ejercicios;

import java.util.Random;

public class PingPong {

    public static void main(String[] args) {
        Thread t1,t2;
        Juego j = new Juego("Santi","Gabi");
        t1 = new Jugador("Santi",true,j);
        t2 = new Jugador("Gabi",false,j);

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Puntuacion = "+j.toString());


    }

}

class Jugador extends Thread{

    String nombre;
    boolean iniciadorDeTurno;
    final Juego juego;

    public Jugador(String nombre, boolean iniciadorDeTurno, Juego juego) {
        this.nombre = nombre;
        this.iniciadorDeTurno = iniciadorDeTurno;
        this.juego = juego;
    }

    @Override
    public void run() {

        while (!juego.finDePartida()){
            synchronized (juego){
                while (!juego.esMiTurno(iniciadorDeTurno)){
                    try {
                        juego.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if(juego.finDePartida())
                    return;
                System.out.println(nombre+" inicia el set");
                juego.simulaQuienGanaPunto();
                juego.notify();
            }
        }

    }
}


class Juego{

    boolean turnoActual;
    int puntuacion1 = 0, puntuacion2 = 0;
    String jugador1, jugador2;

    public Juego(String jugador1, String jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    public synchronized boolean finDePartida(){
        return puntuacion1 >= 15 || puntuacion2 >= 15;
    }

    public synchronized int aumentarPuntuacion1(){
        return ++puntuacion1;
    }

    public synchronized int aumentarPuntuacion2(){
        return ++puntuacion2;
    }

    public synchronized void simulaQuienGanaPunto(){
        turnoActual = !turnoActual;
        if(new Random().nextBoolean()) {
            aumentarPuntuacion1();
            System.out.println("Gana punto "+jugador1);
        }
        else {
            aumentarPuntuacion2();
            System.out.println("Gana punto "+jugador2);
        }
    }

    public synchronized boolean esMiTurno(boolean host){
        return host == turnoActual;
    }

    @Override
    public String toString() {
        return "Juego{" +
                "puntuacion "+jugador1+"= " + puntuacion1 +
                ",puntuacion "+jugador2+"= " + puntuacion2 +
                '}';
    }
}