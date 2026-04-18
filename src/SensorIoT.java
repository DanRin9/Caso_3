
public class SensorIoT extends Thread{

    private int cantidadEventos; //Valor base * id
    private int cantidadActualEventos = 0; //para saber cuando termina
    private int id; //secuencial
    private int ns;
    private MonitorEntradaEventos monitor;
    private static ContadorEventos ids;

    public SensorIoT(int cantidadEventos, int id, int ns, MonitorEntradaEventos monitor){
        this.cantidadEventos = cantidadEventos;
        this.id = id;
        this.monitor = monitor;
        this.ns = ns;
        ids = new ContadorEventos(0);
    }

    public Evento generarEvento(){
        String id = generarIdEvento();
        int tipoEvento = generarSeudoelatorio();
        Evento e = new Evento(id, tipoEvento);
        return e;
    }

    private String generarIdEvento() {
        ids.incrementar();
        return this.id + "."+ids.getContador();
    }

    public int generarSeudoelatorio(){
        //entre 1 y ns, seudoaleatorio
        int limite = ns -1;
        return (int)(Math.random() * limite) + 1;
    }

    public int getid(){
        return this.id;
    }

    @Override
    public void run(){
        System.out.println("[SENSOR-" + id + "]  ◆ Sensor iniciado.");
        while(cantidadEventos >= cantidadActualEventos){
            System.out.println("[SENSOR-" + id + "]  • Generando evento...");
            Evento e = generarEvento();
            monitor.depositarEvento(this, e);
            System.out.println("[SENSOR-" + id + "]  ✓ Evento depositado y analizador notificado.");
            cantidadActualEventos++;
        }
    }





    
}
