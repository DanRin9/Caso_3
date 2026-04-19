public class Servidor extends Thread {

    private int id;
    private MonitorBuzonConsolidacion monitorConsolidacion;

    public Servidor(int id, MonitorBuzonConsolidacion mc){
        this.id = id;
        this.monitorConsolidacion = mc;
    }

    @Override
    public void run(){
        System.out.println("[SERVIDOR-" + id + "]  ◆ Servidor activo.");
        try {
            boolean activo = true;
            while (activo){
                Evento e = monitorConsolidacion.retirarDeConsolidacion(this);
                if (e.getId().equals("fin")){
                    System.out.println("[SERVIDOR-" + id + "]  ◆ Evento fin recibido. Terminando...");
                    activo = false;
                } else {
                    // simula procesamiento entre 100ms y 1000ms
                    int tiempo = (int)(Math.random() * 900) + 100;
                    System.out.println("[SERVIDOR-" + id + "]  → Procesando evento " + e.getId() + " (" + tiempo + "ms)...");
                    Thread.sleep(tiempo);
                    System.out.println("[SERVIDOR-" + id + "]  ✓ Evento " + e.getId() + " procesado.\n");
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[SERVIDOR-" + id + "]  ◆ Servidor-" + id + " terminado.");
    }
}