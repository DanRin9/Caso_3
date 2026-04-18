import java.util.LinkedList;

public class Administrador extends Thread{

    private int nc;
    private MonitorBuzonAlertas monitorAlertas;
    private MonitorBuzonClasificacion monitorClasificacion;

    public Administrador(int nc, MonitorBuzonAlertas ma, MonitorBuzonClasificacion mc){
        this.nc = nc;
        this.monitorAlertas = ma;
        this.monitorClasificacion = mc;

    }

    public void clasificarEvento(Evento e){
        int n = (int)(Math.random())*20;
        if (n % 4 == 0){
            //monitorClasificacion.depositar
        }else{
            System.out.println("!Evento Decsartado.");
        }


    }

    public Evento generarEventoFinal(){
        Evento e = new Evento("fin", 0);
        return e;
    }

    @Override
    public void run(){
        
        System.out.println("Admin activo!");
        LinkedList<Evento> eventos = monitorAlertas.getBuzonEventos();
        int tamanioViejo = eventos.size();

        //Se puede asumir que eventos size no comienza siendo 0? No creo
        while (eventos.size() != 0){
            if (eventos.size() != tamanioViejo){
                for (int i = 0; i < eventos.size(); i++){
                    Evento e = eventos.removeFirst();
                    clasificarEvento(e);
                }
                
            }
            System.out.println("Admin esperando semiactivamente...");
            Thread.yield();
        }
        
    }
    
}
