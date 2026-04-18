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
            System.out.println("[ADMIN   ]  ✓ Evento " + e.getId() + " CLASIFICADO  →  depositando en buzon clasificacion.\n");
        }else{
            System.out.println("[ADMIN   ]  ✗ Evento " + e.getId() + " DESCARTADO.\n");
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

        System.out.println("\n[ADMIN   ]  ◆ Administrador activo.\n");

        System.out.println("[ADMIN   ]  ~ Esperando primer evento en buzon de alertas...");
        while (monitorAlertas.getSizeBuzon() == 0){
            Thread.yield();
        }
        System.out.println("[ADMIN   ]  ✓ Primer evento detectado. Comenzando procesamiento.\n");

        while (!(monitorAlertas.getFirstBuzonEventos().getId().equals("fin"))){ //mientras que no haya un evento final:
            if (monitorAlertas.getSizeBuzon() == 0){ //si no hay eventos
                System.out.println("[ADMIN   ]  ~ Buzon vacio, cediendo procesador...\n");
                Thread.yield();
            }
            System.out.println("[ADMIN   ]  ↓ Retirando evento: " + monitorAlertas.getFirstBuzonEventos().getId() + "  del buzon de alertas.");
            monitorAlertas.retirarEvento();
            System.out.println("[ADMIN   ]  ✓ Evento retirado.\n");

        }

        //Enviar finales a Buzon clasificacion
        System.out.println("[ADMIN   ]  ◆ Generando " + nc + " eventos finales para clasificadores...");
        ArrayList<Evento> finales = generarEventosFinales();
        System.out.println("[ADMIN   ]  → Enviando eventos finales a Clasificacion...\n");

        
    }
    
}
