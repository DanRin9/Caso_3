import java.util.ArrayList;

public class BrokerAnalizador extends Thread {

    private MonitorEntradaEventos monitorEntrada;
    private MonitorBuzonAlertas monitorAlertas;
    private MonitorBuzonClasificacion monitorClasificacion;

    private int numeroEventosEsperados;
    private int eventosAnalizados = 0;

    public BrokerAnalizador(MonitorEntradaEventos monitorEntrada, int numeroEventosEsperados){
        this.monitorEntrada = monitorEntrada;
        this.numeroEventosEsperados = numeroEventosEsperados;
    }

    public Evento generarEventoFinal(){
        Evento eFinal = new Evento("fin", 0);
        return eFinal;
        }
    

    public void clasificarEvento(Evento e){
        //generar un numero entre 0 y 200 y si es multiplo de 8 el evento es anomalo -> buzon alertas.
        int n = (int)(Math.random())*200;

        if (n % 8 == 0){
            //e.setEsSospechoso(true); 
            //implementacion de monitor buzon alertas
            System.out.println("[BROKER  ] !! SOSPECHOSO : Evento " + e.getId());
        }else{
            //e.setEsSospechoso(false);
            //implementacion de monitor buzon clasificacion
            System.out.println("[BROKER  ]  ✓ NORMAL     : Evento " + e.getId());
        }
        this.eventosAnalizados++;

    }

    @Override
    public void run(){
        System.out.println("[BROKER  ]  ◆ Analizador activado.");
        while (numeroEventosEsperados > eventosAnalizados){

            Evento e = monitorEntrada.esperarEvento(this); //Hay prints aca

            System.out.println("[BROKER  ]  → Clasificando evento " + e.getId() + "...");
            clasificarEvento(e);

            System.out.println("[BROKER  ]  ✓ Evento " + e.getId() + " procesado.\n");
        }

        Evento eventoFinal = generarEventoFinal();
        System.out.println("[BROKER  ]  ◆ Evento final generado.\n");

        

    }
    
}
