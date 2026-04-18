import java.util.ArrayList;
import java.util.LinkedList;

class MonitorEntradaEventos{
    private LinkedList<Evento> eventos;


    public MonitorEntradaEventos(){
        this.eventos = new LinkedList<Evento>();
    }

    public synchronized LinkedList<Evento> getEventos(){
        return this.eventos;
    }


    public synchronized void depositarEvento(Thread t, Evento e){
        System.out.println(t.getName() + " Depositando Evento al Buzon. ID = "+e.getId());
        eventos.add(e);
        System.out.println("Notificando...\n");
        try{
            notify();
        }catch (Exception ex) {
            Thread.currentThread().interrupt();
        }
        
    }

    public synchronized Evento esperarEvento(Thread t){
        while(eventos.size() == 0){

            
            System.out.println("[Analizador] esperando para analizar eventos...");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Evento e = eventos.removeFirst();

        return e;


    }




}