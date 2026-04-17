import java.util.ArrayList;

class MonitorEntradaEventos{
    private ArrayList<Evento> eventos;

    public MonitorEntradaEventos(){
        this.eventos = new ArrayList<Evento>();
    }

    public synchronized void depositarEvento(Thread t, Evento e){
        System.out.println(t.getName() + "Añadiendo Evento co ID = "+e.getId());
        eventos.add(e);
        System.out.println("Notificando a Todos... \n");
        notifyAll();
    }

    public synchronized void esperarEvento(Thread t){
        System.out.println(t.getName() + " esperando para analizar eventos...");
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }




}