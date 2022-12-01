package notifywait.explicaciones;

class Contador2{
    private int cuenta = 0;

    public Contador2(int valorInicial) {
        this.cuenta = valorInicial;
    }


    synchronized public int getCuenta(){
        return cuenta;
    }

    synchronized  public int incrementa(){
        this.cuenta++;
        return cuenta;
    }

    synchronized public int decrementa(){
        this.cuenta--;
        return  cuenta;
    }
}

class hiloIncr2 implements Runnable{
    private final String id;
    private final Contador2 cont;

    hiloIncr2 (String id, Contador2 c){
        this.id = id;
        this.cont = c;
    }

    @Override
    public void run() {
        while (true){
            synchronized (this.cont){
                while (this.cont.getCuenta() > 9){
                    System.out.printf("!!!Hilo %s no se puede incrementar, valor Contador2: %d\n", this.id, this.cont.getCuenta());
                    try{
                        this.cont.wait();
                    }catch (InterruptedException ex){}
                }

                this.cont.incrementa();
                this.cont.notifyAll();
                System.out.printf("Hilo %s incrementa, valor Contador2: %d\n", this.id, this.cont.getCuenta());
            }
        }
    }
}

class hiloDecr2 implements Runnable{
    private final String id;
    private final Contador2 cont;

    hiloDecr2(String id, Contador2 cont) {
        this.id = id;
        this.cont = cont;
    }

    @Override
    public void run() {
        while (true){
            synchronized (this.cont){
                while (this.cont.getCuenta()<1){
                    System.out.printf("!!!Hilo %s no se puede decrementar, valor Contador2: %d\n", this.id, this.cont.getCuenta());
                    try {
                        this.cont.wait();
                    }catch (InterruptedException ex){}
                }
                this.cont.decrementa();
                this.cont.notifyAll();
                System.out.printf("Hilo %s decrementa, valor Contador2: %d\n", this.id, this.cont.getCuenta());
            }
        }
    }
}

public class bloqueoConEstadosPrueba {
    public static final int NUM_HILOS_INC = 10;
    public static final int NUM_HILOS_DEC = 10;

    public static void main(String[] args) {
        Contador2 c = new Contador2(0);
        Thread[] hilosInc = new Thread[NUM_HILOS_INC];
        for (int i = 0; i < NUM_HILOS_INC; i++){
            Thread th = new Thread(new hiloIncr2("INC"+i,c));
            hilosInc[i] = th;
        }
        Thread[] hilosDec = new Thread[NUM_HILOS_DEC];
        for (int i = 0; i < NUM_HILOS_DEC; i++){
            Thread th = new Thread(new hiloDecr2("DEC"+i,c));
            hilosDec[i] = th;
        }
        for (int i = 0; i < NUM_HILOS_INC; i++){
            hilosInc[i].start();
        }
        for (int i = 0; i < NUM_HILOS_DEC; i++){
            hilosDec[i].start();
        }
    }
}
