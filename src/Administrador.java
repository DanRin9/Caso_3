public class Administrador extends Thread {

    private int nc;
    private MonitorBuzonAlertas monitorAlertas;
    private MonitorBuzonClasificacion monitorClasificacion;

    private ContadorEventos cClasificados;
    private ContadorEventos cDescartados;

    public Administrador(int nc, MonitorBuzonAlertas ma, MonitorBuzonClasificacion mc, ContadorEventos cClasificados, ContadorEventos cDescartados){
        this.nc = nc;
        this.monitorAlertas = ma;
        this.monitorClasificacion = mc;
        this.cClasificados = cClasificados;
        this.cDescartados = cDescartados;
    }

    public void clasificarEvento(Evento e) throws InterruptedException {
        int n = (int)(Math.random()*20);
        System.out.println(n);
        if (n % 4 == 0){
            monitorClasificacion.depositarEnClasificacion(this, e);
            cClasificados.incrementar();
            System.out.println("[ADMIN   ]  ✓ Evento " + e.getId() + " CLASIFICADO  →  depositado en buzon clasificacion.\n");
        } else {
            cDescartados.incrementar();
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
            System.out.println("[ADMIN   ]  ~ Esperando primer evento en buzon de alertas...");
            while (monitorAlertas.estaVacio()){
                Thread.yield();
            }
            System.out.println("[ADMIN   ]  ✓ Primer evento detectado. Comenzando procesamiento.\n");

            while (true) {
                if (monitorAlertas.estaVacio()){
                    
                    Thread.yield();
                    continue;
                }
                Evento e = monitorAlertas.retirarEvento(this);
                if (e.getId().equals("fin")){
                    break;
                }
                System.out.println("[ADMIN   ]  ↓ Clasificando evento: " + e.getId() + "  del buzon de alertas.");
                clasificarEvento(e);
                System.out.println("[ADMIN   ]  ✓ Evento retirado y clasificado.\n");
            }

            System.out.println("[ADMIN   ]  ◆ Generando " + nc + " eventos finales para clasificadores...");
            generarEventosFinales();
            System.out.println("[ADMIN   ]  → Enviando eventos finales a Clasificacion...\n");

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
