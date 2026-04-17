public class SensorIoT extends Thread{

    private int cantidadEventos; //Valor base * id
    private int cantidadActualEventos = 0; //para saber cuando termina
    private int id; //secuencial
    private int ns;
    private MonitorEntradaEventos monitor;

    public SensorIoT(int cantidadEventos, int id, int ns, MonitorEntradaEventos monitor){
        this.cantidadEventos = cantidadEventos;
        this.id = id;
        this.monitor = monitor;
        this.ns = ns;
    }

    public Evento generarEvento(int id){

        Evento e = new Evento(id);
        return e;
    }

    public int generarId(){
        //entre 1 y ns, seudoaleatorio
        int limite = ns -1;
        return (int)(Math.random() * limite) + 1;
    }

    @Override
    public void run(){
        while(cantidadEventos >= cantidadActualEventos){
            int id = generarId();
            Evento eventoDepositar = generarEvento(id);
            monitor.depositarEvento(this, eventoDepositar);
        }
    }





    
}
