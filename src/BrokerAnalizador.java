public class BrokerAnalizador extends Thread {

    private MonitorEntradaEventos monitorEntrada;
    private MonitorBuzonAlertas monitorAlertas;
    private MonitorBuzonClasificacion monitorClasificacion;

    private int numeroEventosEsperados;
    private int eventosAnalizados = 0;

    private ContadorEventos cAlertas;
    private ContadorEventos cNormales;

    public BrokerAnalizador(MonitorEntradaEventos monitorEntrada, int numeroEventosEsperados, MonitorBuzonAlertas ma, MonitorBuzonClasificacion mc, ContadorEventos cAlertas, ContadorEventos cNormales){
        this.monitorEntrada = monitorEntrada;
        this.numeroEventosEsperados = numeroEventosEsperados;
        this.monitorAlertas = ma;
        this.monitorClasificacion = mc;
        this.cAlertas = cAlertas;
        this.cNormales = cNormales;
    }

    public Evento generarEventoFinal(){
        Evento eFinal = new Evento("fin", 0);
        return eFinal;
    }

    public void clasificarEvento(Evento e) throws InterruptedException {
        int n = (int)(Math.random() * 200);
        if (n % 8 == 0){
            System.out.println("[BROKER  ]   ✓ ALERTA     : Evento " + e.getId() + "  →  ANOMALO.");
            monitorAlertas.depositarEnAlertas(this, e);
            cAlertas.incrementar();
            System.out.println("[BROKER  ]  !! SOSPECHOSO : Evento " + e.getId() + "  →  enviado a alertas.");
        } else {
            monitorClasificacion.depositarEnClasificacion(this, e);
            cNormales.incrementar();
            System.out.println("[BROKER  ]   ✓ NORMAL     : Evento " + e.getId() + "  →  enviado a clasificacion.");
        }
        this.eventosAnalizados++;
        System.out.println("[BROKER  ]   Progreso: " + this.eventosAnalizados + "/" + this.numeroEventosEsperados + " eventos analizados.\n");
    }

    @Override
    public void run(){
        System.out.println("[BROKER  ]  ◆ Analizador activado.");
        while (numeroEventosEsperados > eventosAnalizados){
            try {
                Evento e = monitorEntrada.esperarEvento(this);
                System.out.println("[BROKER  ]  → Clasificando evento " + e.getId() + "...");
                clasificarEvento(e);
                System.out.println("[BROKER  ]  ✓ Evento " + e.getId() + " procesado.\n");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        Evento eventoFinal = generarEventoFinal();
        System.out.println("[BROKER  ]  ◆ Evento final generado.\n");
        monitorAlertas.depositarEnAlertas(this, eventoFinal);
        System.out.println("[BROKER  ]    Evento final enviado!\n\n");
    }
}
