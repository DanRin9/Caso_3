import java.util.LinkedList;

class MonitorBuzonClasificacion {
    private LinkedList<Evento> buzonEventos;
    private int tamanio; // tam1

    public MonitorBuzonClasificacion(int tamanio){
        this.buzonEventos = new LinkedList<Evento>();
        this.tamanio = tamanio;
    }

    public synchronized void depositarEnClasificacion(Thread t, Evento e) throws InterruptedException {
        while (buzonEventos.size() >= tamanio){ // si está lleno, espera
            System.out.println("[CLASIF  ]  ~ " + t.getName() + " esperando, buzon lleno...");
            wait();
        }
        buzonEventos.add(e);
        System.out.println("[CLASIF  ]  ↓ " + t.getName() + "  →  Evento " + e.getId() + " depositado  (buzon: " + buzonEventos.size() + ")");
        notifyAll(); // avisa a los clasificadores que hay algo nuevo
    }

    public synchronized Evento retirarDeClasificacion(Thread t) throws InterruptedException {
        while (buzonEventos.isEmpty()){ // si está vacío, espera
            System.out.println("[CLASIF  ]  ~ " + t.getName() + " esperando, buzon vacio...");
            wait();
        }
        Evento e = buzonEventos.removeFirst();
        System.out.println("[CLASIF  ]  ↑ " + t.getName() + "  →  Evento " + e.getId() + " retirado  (buzon: " + buzonEventos.size() + ")");
        notifyAll(); // avisa al Broker/Admin que hay espacio
        return e;
    }
}