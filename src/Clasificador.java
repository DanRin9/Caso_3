public class Clasificador extends Thread {

    private int id;
    private MonitorBuzonClasificacion monitorClasificacion;
    private MonitorBuzonConsolidacion[] buzonesConsolidacion;
    private static int clasificadoresActivos;
    private int ns;

    public Clasificador(int id, MonitorBuzonClasificacion mc, MonitorBuzonConsolidacion[] buzonesConsolidacion, int ns){
        this.id = id;
        this.monitorClasificacion = mc;
        this.buzonesConsolidacion = buzonesConsolidacion;
        this.ns = ns;
        clasificadoresActivos++;
    }

    private synchronized static void decrementarClasificadores(){
        clasificadoresActivos--;
    }

    private synchronized static int getClasificadoresActivos(){
        return clasificadoresActivos;
    }

    public void enviarEventoFin() throws InterruptedException {
        System.out.println("[CLASIF-" + id + "]  ◆ Soy el ultimo clasificador. Enviando fins a servidores...");
        for (int i = 0; i < ns; i++){
            Evento fin = new Evento("fin", 0);
            buzonesConsolidacion[i].depositarEnConsolidacion(this, fin);
            System.out.println("[CLASIF-" + id + "]  → Fin enviado a servidor " + (i+1));
        }
    }

    @Override
    public void run(){
        System.out.println("[CLASIF-" + id + "]  ◆ Clasificador activo.");
        try {
            boolean activo = true;
            while (activo){
                Evento e = monitorClasificacion.retirarDeClasificacion(this);
                if (e.getId().equals("fin")){
                    System.out.println("[CLASIF-" + id + "]  ◆ Evento fin recibido. Terminando...");
                    activo = false;
                } else {
                    int servidor = e.getTipoEvento() - 1;
                    System.out.println("[CLASIF-" + id + "]  → Evento " + e.getId() + " enviado a servidor " + (servidor+1));
                    buzonesConsolidacion[servidor].depositarEnConsolidacion(this, e);
                }
            }
            decrementarClasificadores();
            if (getClasificadoresActivos() == 0){
                enviarEventoFin();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[CLASIF-" + id + "]  ◆ Clasificador-" + id + " terminado.");
    }
}
