public class BrokerAnalizador extends Thread {

    private MonitorEntradaEventos monitor;
    private int numeroEventosEsperados;

    public BrokerAnalizador(MonitorEntradaEventos monitor, int numeroEventosEsperados){
        this.monitor = monitor;
        this.numeroEventosEsperados = numeroEventosEsperados;
    }

    public Evento generarEventoFinal(){
        Evento eFinal = new Evento("fin", 0);
        return eFinal;
        }
    

    public boolean clasificarEvento(Evento e){
        //generar un numero entre 0 y 200 y si es multiplo de 8 el evento es anomalo -> buzon alertas.
        int n = (int)(Math.random())*200;
        
        if (n % 8 == 0){
            e.setEsSospechoso(true);
        }else{
            e.setEsSospechoso(false);
        }

        return false;
    }

    @Override
    public void run(){
        
        


    }
    
}
