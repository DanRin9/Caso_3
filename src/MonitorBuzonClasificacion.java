class MonitorBuzonClasificacion{
    private Evento[] buzonEventos;
    
    public MonitorBuzonClasificacion(int tamanio){
        this.buzonEventos = new Evento[tamanio];
    }

    public synchronized void depositarEnClasificacion(Thread t, Evento e){

    }
}