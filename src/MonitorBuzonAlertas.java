import java.util.LinkedList;

public class MonitorBuzonAlertas {
    private LinkedList<Evento> buzonEventos;
    private int totalAlertas;
    private int currentAlertas;

    public MonitorBuzonAlertas(){
        this.buzonEventos = new LinkedList<Evento>();
    }

    public synchronized Evento getFirstBuzonEventos(){
        return this.buzonEventos.getFirst();
    }

    public synchronized int getSizeBuzon(){
        return buzonEventos.size();
    }


    public synchronized void depositarEnAlertas(Thread t, Evento e){

        System.out.println("[ALERTAS ]  ↓ " + t.getName() + "  →  Depositando evento " + e.getId() + "  (buzon: " + buzonEventos.size() + " → " + (buzonEventos.size()+1) + ")");
        buzonEventos.add(e);
        System.out.println("[ALERTAS ]  ✓ Evento " + e.getId() + " en buzon. Notificando...\n");

        try{
            notify();
        }catch (Exception ex){
            ex.printStackTrace();;
        }


    }



    public synchronized Evento retirarEvento(){

        System.out.println("[ALERTAS ]  ↑ Retirando evento del buzon  (buzon: " + buzonEventos.size() + " → " + (buzonEventos.size()-1) + ")");
        Evento e = buzonEventos.removeFirst();
        System.out.println("[ALERTAS ]  ✓ Evento " + e.getId() + " retirado.\n");
        return e;

    }

    


}
