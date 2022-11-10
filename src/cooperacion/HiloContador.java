package cooperacion;

public class HiloContador implements Runnable{

    private int idHilo;
    private int cantidadAContar;
    private int cantidadContada = 0;

    private Contador contador;

    public HiloContador(int idHilo, int cantidadAContar, Contador contador) {
        this.idHilo = idHilo;
        this.cantidadAContar = cantidadAContar;
        this.contador = contador;
    }

    @Override
    public void run() {
        for(int i = 0; i < cantidadAContar; i++){
            contador.aumentarContador();
            cantidadContada++;
        }
        System.out.printf("Soy el hilo %d, he contado %d veces%n",idHilo,cantidadContada);
    }
}
