import java.util.LinkedList;

public class MonitorBuzonAlertas {
    private LinkedList<Evento> buzonEventos;

    public MonitorBuzonAlertas(){
        this.buzonEventos = new LinkedList<Evento>();
    }

    public synchronized void depositarEnAlertas(Thread t, Evento e){
        buzonEventos.add(e);
        System.out.println("[ALERTAS ]  ↓ " + t.getName() + "  →  Evento " + e.getId() + " depositado  (buzon: " + buzonEventos.size() + ")");
        notifyAll();
    }

    public synchronized boolean estaVacio(){
        return buzonEventos.isEmpty();
    }

    public synchronized Evento retirarEvento(Thread t){
        Evento e = buzonEventos.removeFirst();
        System.out.println("[ALERTAS ]  ↑ " + t.getName() + "  →  Evento " + e.getId() + " retirado  (buzon: " + buzonEventos.size() + ")");
        return e;
    }
}
