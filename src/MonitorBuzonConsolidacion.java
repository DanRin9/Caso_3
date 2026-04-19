import java.util.LinkedList;

public class MonitorBuzonConsolidacion {
    private LinkedList<Evento> buzonEventos;
    private int tamanio; // tam2

    public MonitorBuzonConsolidacion(int tamanio){
        this.buzonEventos = new LinkedList<Evento>();
        this.tamanio = tamanio;
    }

    public synchronized void depositarEnConsolidacion(Thread t, Evento e) throws InterruptedException {
        while (buzonEventos.size() >= tamanio){
            System.out.println("[CONSOL  ]  ~ " + t.getName() + " esperando, buzon lleno...");
            wait();
        }
        buzonEventos.add(e);
        System.out.println("[CONSOL  ]  ↓ " + t.getName() + "  →  Evento " + e.getId() + " depositado  (buzon: " + buzonEventos.size() + ")");
        notifyAll();
    }

    public synchronized Evento retirarDeConsolidacion(Thread t) throws InterruptedException {
        while (buzonEventos.isEmpty()){
            System.out.println("[CONSOL  ]  ~ " + t.getName() + " esperando, buzon vacio...");
            wait();
        }
        Evento e = buzonEventos.removeFirst();
        System.out.println("[CONSOL  ]  ↑ " + t.getName() + "  →  Evento " + e.getId() + " retirado  (buzon: " + buzonEventos.size() + ")");
        notifyAll();
        return e;
    }
}