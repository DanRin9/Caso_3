public class SensorIoT extends Thread{

    private int cantidadEventos; //Valor base * id
    private int cantidadActualEventos = 0; //para saber cuando termina
    private int id; //secuencial
    private MonitorEntradaEventos monitor;

    public SensorIoT(int cantidadEventos, int id, MonitorEntradaEventos monitor){
        this.cantidadEventos = cantidadEventos;
        this.id = id;
        this.monitor = monitor;
    }

    public Evento generarEvento(int id){

        Evento e = new Evento(id);
        return e;
    }

    @Override
    public void run(){
        
    }





    
}
