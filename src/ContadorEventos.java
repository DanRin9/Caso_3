public class ContadorEventos {
    private int contador;

    public ContadorEventos(int contador){
        this.contador = contador;
    }

    public synchronized void incrementar(){
        this.contador++;
    }

    public synchronized int getContador(){
        return this.contador;
    }
}
