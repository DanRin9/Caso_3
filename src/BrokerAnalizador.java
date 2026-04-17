public class BrokerAnalizador extends Thread {

    private MonitorEntradaEventos monitor;

    public BrokerAnalizador(MonitorEntradaEventos monitor){
        this.monitor = monitor;
    }

    public boolean clasificarEvento(Evento e){
        return false;
    }

    @Override
    public void run(){
        
        Evento ultimo = monitor.getUltimo();


    }
    
}
