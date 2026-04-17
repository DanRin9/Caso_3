public class BrokerEventos extends Thread {

    private MonitorEntradaEventos monitor;

    public BrokerEventos(MonitorEntradaEventos monitor){
        this.monitor = monitor;
    }

    @Override
    public void run(){
        
        Evento ultimo = monitor.getUltimo();


    }
    
}
