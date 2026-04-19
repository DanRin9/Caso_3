
public class SensorIoT extends Thread{

    private int cantidadEventos; //Valor base * id
    private int cantidadActualEventos = 0; //para saber cuando termina
    private int id; //secuencial
    private int ns;
    private MonitorEntradaEventos monitor;
    private static ContadorEventos ids = new ContadorEventos(0); //iniciarlo afuera para que el primero sea 1

    public SensorIoT(int cantidadEventos, int id, int ns, MonitorEntradaEventos monitor){
        this.cantidadEventos = cantidadEventos;
        this.id = id;
        this.monitor = monitor;
        this.ns = ns;
    }

    public Evento generarEvento(){
        String id = generarIdEvento();
        int tipoEvento = generarSeudoaleatorio();
        Evento e = new Evento(id, tipoEvento);
        return e;
    }

    private String generarIdEvento() {
        ids.incrementar();
        return this.id + "."+ids.getContador();
    }

    public int generarSeudoaleatorio(){
        //entre 1 y ns, seudoaleatorio
        return (int)(Math.random() * ns) + 1; //asegura numero entre 1 y ns
    }

    public int getid(){
        return this.id;
    }

    @Override
    public void run(){
        System.out.println("[SENSOR-" + id + "]  ◆ Sensor iniciado.");
        while(cantidadActualEventos < cantidadEventos){
            System.out.println("[SENSOR-" + id + "]  • Generando evento...");
            Evento e = generarEvento();
            monitor.depositarEvento(this, e);
            System.out.println("[SENSOR-" + id + "]  ✓ Evento depositado y analizador notificado.");
            cantidadActualEventos++;
        }
    }





    
}
