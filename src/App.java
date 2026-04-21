import java.io.BufferedReader;
import java.io.FileReader;

public class App {
    
    private int ni; // # Sensores
    private int nc; // # Clasificadores
    private int ns; // # Servidores
    private int numeroEventosBase;
    private int tam1; // capacidad buzón clasificación
    private int tam2; // capacidad buzones consolidación

    public App(int ni, int nc, int ns, int numeroEventos, int tam1, int tam2){
        this.ni = ni;
        this.nc = nc;
        this.ns = ns;
        this.numeroEventosBase = numeroEventos;
        this.tam1 = tam1;
        this.tam2 = tam2;
    }

    public int generarCantidadEventos(int numeroBase, int idSensor){
        return numeroBase * idSensor;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println(  "║      SISTEMA IoT  -  INICIO      ║");
        System.out.println(  "╚══════════════════════════════════╝\n");

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        int ni   = Integer.parseInt(br.readLine().trim());
        int neb  = Integer.parseInt(br.readLine().trim());
        int nc   = Integer.parseInt(br.readLine().trim());
        int ns   = Integer.parseInt(br.readLine().trim());
        int tam1 = Integer.parseInt(br.readLine().trim());
        int tam2 = Integer.parseInt(br.readLine().trim());
        br.close();
        App mainApp = new App(ni, nc, ns, neb, tam1, tam2);
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║        CONFIGURACION DEL SISTEMA ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  Sensores (ni)       : " + mainApp.ni);
        System.out.println("║  Clasificadores (nc) : " + mainApp.nc);
        System.out.println("║  Servidores (ns)     : " + mainApp.ns);
        System.out.println("║  Eventos base        : " + mainApp.numeroEventosBase);
        System.out.println("║  Tam buzón clasif    : " + mainApp.tam1);
        System.out.println("║  Tam buzón consol    : " + mainApp.tam2);
        System.out.println("╚══════════════════════════════════╝\n");

        // CREACION MONITORES
        System.out.println(">> Inicializando monitores...");
        MonitorEntradaEventos monitorEntrada = new MonitorEntradaEventos();
        System.out.println("   [OK] MonitorEntradaEventos creado.");
        MonitorBuzonAlertas monitorAlertas = new MonitorBuzonAlertas();
        System.out.println("   [OK] MonitorBuzonAlertas creado.");
        MonitorBuzonClasificacion monitorClasificacion = new MonitorBuzonClasificacion(mainApp.tam1);
        System.out.println("   [OK] MonitorBuzonClasificacion creado (cap: " + mainApp.tam1 + ").");

        // CREAR BUZONES CONSOLIDACION (uno por servidor)
        MonitorBuzonConsolidacion[] buzonesConsolidacion = new MonitorBuzonConsolidacion[mainApp.ns];
        for (int i = 0; i < mainApp.ns; i++){
            buzonesConsolidacion[i] = new MonitorBuzonConsolidacion(mainApp.tam2);
            System.out.println("   [OK] MonitorBuzonConsolidacion-" + (i+1) + " creado (cap: " + mainApp.tam2 + ").");
        }
        System.out.println();

        // CREAR SENSORES
        System.out.println(">> Creando sensores...");
        SensorIoT[] sensores = new SensorIoT[mainApp.ni];
        int cantidadEventosTotales = 0;
        for (int i = 0; i < mainApp.ni; i++){
            int cantidad = mainApp.generarCantidadEventos(mainApp.numeroEventosBase, i+1); // ← i+1
            cantidadEventosTotales += cantidad;
            sensores[i] = new SensorIoT(cantidad, i+1, mainApp.ns, monitorEntrada); // ← ns correcto
            System.out.println("   [OK] Sensor-" + (i+1) + " creado  →  " + cantidad + " eventos a generar.");
        }
        System.out.println("   Total eventos esperados: " + cantidadEventosTotales + "\n");

        // CREAR BROKER
        System.out.println(">> Creando BrokerAnalizador...");
        BrokerAnalizador analizador = new BrokerAnalizador(monitorEntrada, cantidadEventosTotales, monitorAlertas, monitorClasificacion);
        System.out.println("   [OK] Broker creado.\n");

        // CREAR ADMINISTRADOR
        System.out.println(">> Creando Administrador...");
        Administrador soyAdmin = new Administrador(mainApp.nc, monitorAlertas, monitorClasificacion);
        System.out.println("   [OK] Administrador creado.\n");

        // CREAR CLASIFICADORES
        System.out.println(">> Creando clasificadores...");
        Clasificador[] clasificadores = new Clasificador[mainApp.nc];
        for (int i = 0; i < mainApp.nc; i++){
            clasificadores[i] = new Clasificador(i+1, monitorClasificacion, buzonesConsolidacion, mainApp.ns);
            System.out.println("   [OK] Clasificador-" + (i+1) + " creado.");
        }
        System.out.println();

        // CREAR SERVIDORES
        System.out.println(">> Creando servidores...");
        Servidor[] servidores = new Servidor[mainApp.ns];
        for (int i = 0; i < mainApp.ns; i++){
            servidores[i] = new Servidor(i+1, buzonesConsolidacion[i]);
            System.out.println("   [OK] Servidor-" + (i+1) + " creado.");
        }
        System.out.println();

        // INICIAR THREADS
        System.out.println("════════════════════════════════════");
        System.out.println("  LANZANDO THREADS");
        System.out.println("════════════════════════════════════\n");

        analizador.start();
        System.out.println("   [START] BrokerAnalizador");
        soyAdmin.start();
        System.out.println("   [START] Administrador");
        for (int i = 0; i < mainApp.nc; i++){
            clasificadores[i].start();
            System.out.println("   [START] Clasificador-" + (i+1));
        }
        for (int i = 0; i < mainApp.ns; i++){
            servidores[i].start();
            System.out.println("   [START] Servidor-" + (i+1));
        }
        for (int i = 0; i < mainApp.ni; i++){
            sensores[i].start();
            System.out.println("   [START] Sensor-" + (i+1));
        }
        System.out.println();

        // ESPERAR TODOS LOS THREADS
        System.out.println("════════════════════════════════════");
        System.out.println("  ESPERANDO FINALIZACION DE THREADS");
        System.out.println("════════════════════════════════════\n");
        try {
            for (int i = 0; i < mainApp.ni; i++){
                sensores[i].join();
                System.out.println("   [JOIN] Sensor-" + (i+1) + " finalizado.");
            }
            analizador.join();
            System.out.println("   [JOIN] BrokerAnalizador finalizado.");
            soyAdmin.join();
            System.out.println("   [JOIN] Administrador finalizado.");
            for (int i = 0; i < mainApp.nc; i++){
                clasificadores[i].join();
                System.out.println("   [JOIN] Clasificador-" + (i+1) + " finalizado.");
            }
            for (int i = 0; i < mainApp.ns; i++){
                servidores[i].join();
                System.out.println("   [JOIN] Servidor-" + (i+1) + " finalizado.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println(  "║      FIN DE LA EJECUCION         ║");
        System.out.println(  "╚══════════════════════════════════╝\n");
    }
}
