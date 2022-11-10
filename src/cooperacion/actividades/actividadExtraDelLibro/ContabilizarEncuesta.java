package cooperacion.actividades.actividadExtraDelLibro;

import java.util.HashMap;
import java.util.Random;

public class ContabilizarEncuesta {

    public static String[] zonas = {"Jaen","Cadiz","Sevilla","Cordoba","Malaga","Almeria","Granada","Huelva"};

    public static void main(String[] args) {
        Thread[] hilos = new Thread[zonas.length];
        Random r = new Random();
        for(int i  = 0; i < hilos.length; i++){
            hilos[i] = new Thread(new HiloCuentaRespuesta(zonas[i], r.nextInt(100)+100));
            hilos[i].start();
        }

        for(Thread t : hilos)
            try{
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Error al unir los hilos");
            }
        System.out.printf("Se han leido un total de %d Si, %d No y %d No se",Sincronizador.getRespuesta("Si"),Sincronizador.getRespuesta("No"),Sincronizador.getRespuesta(null));
    }

}

class HiloCuentaRespuesta implements Runnable{

    private final String idZona;
    private int contadorSi, contadorNo, contadorNoSe;
    private final int cantidadDeVotosAContar;

    public HiloCuentaRespuesta(String idZona, int cantidadDeVotosAContar) {
        this.idZona = idZona;
        this.cantidadDeVotosAContar = cantidadDeVotosAContar;
    }

    @Override
    public void run() {
        Random r = new Random();
        for(int i = 0; i < cantidadDeVotosAContar; i++){
            int respuesta = r.nextInt(3);
            switch (respuesta){
                case 0: Sincronizador.addRespuesta("Si");
                contadorSi++;
                case 1: Sincronizador.addRespuesta("No");
                contadorNo++;
                case 2: Sincronizador.addRespuesta(null);
                contadorNoSe++;
                default:
            }
        }
        System.out.printf("Soy el hilo de la zona de %s, mis votos son: %d SI, %d NO, %d No se%n",idZona,contadorSi,contadorNo,contadorNoSe);
    }
}

class Sincronizador {
    private static final HashMap<String, Integer> respuestas = new HashMap<>();

    synchronized static void addRespuesta(String respuesta){
        Integer i;
        if((i = respuestas.get(respuesta)) != null)
            respuestas.put(respuesta,i+1);
        else
            respuestas.put(respuesta, 1);
    }

    public static Integer getRespuesta(String respuesta){
        return respuestas.get(respuesta);
    }
}
