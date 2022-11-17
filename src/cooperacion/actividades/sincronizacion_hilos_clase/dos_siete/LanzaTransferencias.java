package cooperacion.actividades.sincronizacion_hilos_clase.dos_siete;

import java.util.Stack;

public class LanzaTransferencias {


    public static final int NUMERO_DE_CUENTAS = 3;

    public static void main(String[] args) {

        Cuenta cuentas[] = new Cuenta[NUMERO_DE_CUENTAS];
        HilosTransferencias hilos[] = new HilosTransferencias[NUMERO_DE_CUENTAS-1];

        for(int i = 0; i < cuentas.length;i++)
            cuentas[i] = new Cuenta("ES000000011%d".formatted(i),1000);

        for(int i = 0; i < hilos.length;i++)
            hilos[i] = new HilosTransferencias(cuentas[i], cuentas[i+1],i);

        for(HilosTransferencias h : hilos)
            h.start();

        try{
            for (HilosTransferencias h : hilos)
                h.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(Cuenta c : cuentas)
            System.out.printf("Cuenta %s, dinero = %d%n",c.getNumeroDeCuenta(),c.getDinero());
    }

}

class HilosTransferencias extends Thread{
    private Cuenta c1;
    private Cuenta c2;
    private int id;

    public HilosTransferencias(Cuenta c1, Cuenta c2, int id) {
        this.c1 = c1;
        this.c2 = c2;
        this.id = id;
    }

    @Override
    public void run() {
        for(int i = 0; i < 3; i++){
            System.out.printf("Cuenta %s le envia %d a %s%n",c1.getNumeroDeCuenta(),10,c2.getNumeroDeCuenta());
            GestorTransferencias.transferencia(c1,c2,10);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class GestorTransferencias{
    public static void transferencia(Cuenta c1, Cuenta c2, int cantidad){
        Cuenta menor, mayor;
        if(c1.getNumeroDeCuenta().compareTo(c2.getNumeroDeCuenta()) < 0){
            menor = c1;
            mayor = c2;
        }else{
            menor = c2;
            mayor = c1;
        }

        synchronized (menor){
            synchronized (mayor){
                c1.addDinero(cantidad);
                c2.quitarDinero(cantidad);
            }
        }

    }
}

class Cuenta {
    private final String numeroDeCuenta;
    private int dinero;

    public Cuenta(String numeroDeCuenta, int dinero) {
        this.numeroDeCuenta = numeroDeCuenta;
        this.dinero = dinero;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public void addDinero(int dinero){
        this.dinero += dinero;
    }

    public void quitarDinero(int dinero){
        this.dinero -= dinero;
    }

    public String getNumeroDeCuenta() {
        return numeroDeCuenta;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroDeCuenta='" + numeroDeCuenta + '\'' +
                ", dinero=" + dinero +
                '}';
    }
}
