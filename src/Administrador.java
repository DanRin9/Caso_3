import java.util.ArrayList;

public class Administrador extends Thread {

    private int nc;
    private MonitorBuzonAlertas monitorAlertas;
    private MonitorBuzonClasificacion monitorClasificacion;

    public Administrador(int nc, MonitorBuzonAlertas ma, MonitorBuzonClasificacion mc){
        this.nc = nc;
        this.monitorAlertas = ma;
        this.monitorClasificacion = mc;
    }

    public void clasificarEvento(Evento e) throws InterruptedException {
        int n = (int)(Math.random() * 20);
        if (n % 4 == 0){
            monitorClasificacion.depositarEnClasificacion(this, e);
            System.out.println("[ADMIN   ]  ✓ Evento " + e.getId() + " CLASIFICADO  →  depositado en buzon clasificacion.\n");
        } else {
            System.out.println("[ADMIN   ]  ✗ Evento " + e.getId() + " DESCARTADO.\n");
        }
    }

    public void generarEventosFinales() throws InterruptedException {
        System.out.println("[ADMIN   ]  ◆ Generando " + nc + " eventos finales para clasificadores...");
        for (int i = 0; i < nc; i++){
            Evento e = new Evento("fin", 0);
            monitorClasificacion.depositarEnClasificacion(this, e);
            System.out.println("[ADMIN   ]  → Evento fin " + (i+1) + "/" + nc + " enviado a clasificacion.");
        }
    }

    @Override
    public void run(){
        System.out.println("\n[ADMIN   ]  ◆ Administrador activo.\n");
        try {
            boolean activo = true;
            while (activo){
                while (monitorAlertas.estaVacio()){
                    System.out.println("[ADMIN   ]  ~ Buzon vacio, cediendo procesador...");
                    Thread.yield();
                }
                Evento e = monitorAlertas.retirarEvento(this);
                if (e.getId().equals("fin")){
                    System.out.println("[ADMIN   ]  ◆ Evento fin recibido. Terminando...\n");
                    activo = false;
                } else {
                    clasificarEvento(e);
                }
            }
            generarEventosFinales();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[ADMIN   ]  ◆ Administrador terminado.\n");
    }
}
