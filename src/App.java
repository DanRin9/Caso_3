import java.util.ArrayList;

public class App {
    
    private int ni; // # Sensores
    private int nc; // # Clasificadores
    private int ns; // # Servidores
    private int numeroEventosBase;
    private int capacidadBuzonClasificacion;

    public App(int ni, int nc, int ns, int numeroEventos ){
        this.ni = ni;
        this.nc = nc;
        this.ns = ns;
        this.numeroEventosBase = numeroEventos;
        
    }

    public int generarCantidadEventos(int numeroBase, int idSensor){
        return numeroBase*idSensor;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println(  "║      SISTEMA IoT  -  INICIO      ║");
        System.out.println(  "╚══════════════════════════════════╝\n");

        //MAIN
        App mainApp = new App(2,3,3,5);
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║        CONFIGURACION DEL SISTEMA ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  Sensores (ni)       : " + mainApp.ni);
        System.out.println("║  Clasificadores (nc) : " + mainApp.nc);
        System.out.println("║  Servidores (ns)     : " + mainApp.ns);
        System.out.println("║  Eventos base        : " + mainApp.numeroEventosBase);
        System.out.println("╚══════════════════════════════════╝\n");

        //CREACION MONITORES
        System.out.println(">> Inicializando monitores...");
        MonitorEntradaEventos monitorEntrada = new MonitorEntradaEventos();
        System.out.println("   [OK] MonitorEntradaEventos creado.");
        MonitorBuzonAlertas monitorAlertas = new MonitorBuzonAlertas();
        System.out.println("   [OK] MonitorBuzonAlertas creado.");
        MonitorBuzonClasificacion monitorClasificacion = new MonitorBuzonClasificacion(10); // Tamaño limitado del buzon
        System.out.println("   [OK] MonitorBuzonClasificacion creado (cap: 10).\n");


        //----CREACION DE HILOS!!!-----

        //CREAR SENSORES
        System.out.println(">> Creando sensores...");
        SensorIoT[] sensores = new SensorIoT[mainApp.ni];
        int cantidadEventosTotales = 0;
        for (int i=0;i<mainApp.ni;i++){
            int cantidad = mainApp.generarCantidadEventos(mainApp.numeroEventosBase, i);
            cantidadEventosTotales += cantidad;
            sensores[i] = new SensorIoT(cantidad, i+1, 1, monitorEntrada);
            System.out.println("   [OK] Sensor-" + (i+1) + " creado  →  " + cantidad + " eventos a generar.");
        }
        System.out.println("   Total eventos esperados: " + cantidadEventosTotales + "\n");

        //CREAR Analizador (Broker)
        System.out.println(">> Creando BrokerAnalizador...");
        BrokerAnalizador analizador = new BrokerAnalizador(monitorEntrada, cantidadEventosTotales, monitorAlertas, monitorClasificacion);
        System.out.println("   [OK] Broker creado.\n");

        //CREAR Administrador
        System.out.println(">> Creando Administrador...");
        Administrador soyAdmin = new Administrador(mainApp.nc, monitorAlertas, monitorClasificacion);
        System.out.println("   [OK] Administrador creado.\n");


        //-----INICIAR THREADS------
        System.out.println("════════════════════════════════════");
        System.out.println("  LANZANDO THREADS");
        System.out.println("════════════════════════════════════\n");

        analizador.start();
        System.out.println("   [START] BrokerAnalizador");
        soyAdmin.start();
        System.out.println("   [START] Administrador");
        for (int i=0;i<mainApp.ni;i++){
            sensores[i].start();
            System.out.println("   [START] Sensor-" + (i+1));
        }
        System.out.println();


        //ESPERAR a TODOS los THREADS
        System.out.println("════════════════════════════════════");
        System.out.println("  ESPERANDO FINALIZACION DE THREADS");
        System.out.println("════════════════════════════════════\n");
        try{
            for (int i=0;i<mainApp.ni;i++){
            sensores[i].join();
            System.out.println("   [JOIN] Sensor-" + (i+1) + " finalizado.");
            }
            analizador.join();
            System.out.println("   [JOIN] BrokerAnalizador finalizado.");
            soyAdmin.join();
            System.out.println("   [JOIN] Administrador finalizado.\n");

        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        

        
        

        //FIN PAPÁ!
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println(  "║      FIN DE LA EJECUCION         ║");
        System.out.println(  "╚══════════════════════════════════╝\n");
    }
}

// Para los eventos finales id = fin, tipoEvento = 0 (lo crea el BrokerAnalizador)

