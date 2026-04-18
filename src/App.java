import java.util.ArrayList;

public class App {
    
    private int cantidadSensores;
    private int numeroEventosBase;
    private int capacidadBuzonClasificacion;

    public App(int cantidadSensores, int numeroEventos ){
        this.cantidadSensores = cantidadSensores;
        this.numeroEventosBase = numeroEventos;
        
    }

    public int generarCantidadEventos(int numeroBase, int idSensor){
        return numeroBase*idSensor;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("\n\n=====INICIANDO EJECUCION===== \n\n");
        App mainApp = new App(3,5);

        //Monitor de Entrada de Eventos
        MonitorEntradaEventos monitorEntrada = new MonitorEntradaEventos();


        //CREAR SENSORES
        SensorIoT[] sensores = new SensorIoT[mainApp.cantidadSensores];
        int cantidadEventosTotales = 0;
        for (int i=0;i<mainApp.cantidadSensores;i++){
            int cantidad = mainApp.generarCantidadEventos(mainApp.numeroEventosBase, i);
            cantidadEventosTotales += cantidad;
            sensores[i] = new SensorIoT(cantidad, i+1, 1, monitorEntrada);
        }

        //Analizador (Broker)
        BrokerAnalizador analizador = new BrokerAnalizador(monitorEntrada, cantidadEventosTotales);

        //INICIAR THREADS
        analizador.start();
        for (int i=0;i<mainApp.cantidadSensores;i++){
            sensores[i].start();
        }
        try{

            for (int i=0;i<mainApp.cantidadSensores;i++){
            sensores[i].join();
            }
            analizador.join();

        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        

        
        


        System.out.println("FIN DE LA EJECUCION!");
    }
}

// Para los eventos finales id = fin, tipoEvento = 0 (lo crea el BrokerAnalizador)

