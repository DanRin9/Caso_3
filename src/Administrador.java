import java.util.ArrayList;
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

    public ArrayList<Evento> generarEventosFinales(){
        ArrayList<Evento> ar = new ArrayList<Evento>();

        for(int i = 0; i < nc; i++){
            Evento e = new Evento("fin", 0);
            ar.add(e);
        }
        
        return ar;
    }

    @Override
    public void run(){

        System.out.println("\nAdmin activo!\n");

        while (!(monitorAlertas.getFirstBuzonEventos().getId().equals("fin"))){ //mientras que no haya un evento final:
            while (monitorAlertas.getSizeBuzon() == 0){ //mientras que no hayan eventos
                System.out.println("Admin - Ceder Procesador\n");
                Thread.yield();
            }
            System.out.println("Retirando evento: "+ monitorAlertas.getFirstBuzonEventos().getId());
            monitorAlertas.retirarEvento();

        }

        //Enviar finales a Buzon clasificacion
        System.out.println("Admin - Generando "+nc+ " eventos finales...\n");
        ArrayList<Evento> finales = generarEventosFinales();
        System.out.println("Enviando a Clasificacion -> Clasificadores...\n");
        
        
    }
    
}
