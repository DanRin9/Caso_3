import java.util.LinkedList;

public class MonitorBuzonAlertas {
    private LinkedList<Evento> buzonEventos;
    private int totalAlertas;
    private int currentAlertas;

    public MonitorBuzonAlertas(){
        this.buzonEventos = new LinkedList<Evento>();
    }

    public synchronized void depositarEnAlertas(Thread t, Evento e){

        buzonEventos.add(e);

        try{
            notify();
        }catch (Exception ex){
            ex.printStackTrace();;
        }

        
    }

    public synchronized LinkedList<Evento> getBuzonEventos(){
        return this.buzonEventos;
    }

    public synchronized Evento EsperarYRetirarEvento(Thread t){
        while (buzonEventos.size() == 0){
            try{
                wait();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }
        Evento e = buzonEventos.removeFirst();
        return e;

    }

    


}
