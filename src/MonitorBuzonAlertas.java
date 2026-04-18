import java.util.ArrayList;

public class MonitorBuzonAlertas {
    private ArrayList<Evento> buzonEventos;

    public MonitorBuzonAlertas(){
        this.buzonEventos = new ArrayList<Evento>();
    }

    public synchronized void depositarEnAlertas(Thread t, Evento e){
        
    }


}
