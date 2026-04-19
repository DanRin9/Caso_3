public class Evento {
    
    private boolean esSospechoso; 
    private int tipoEvento;//numero seudoaleatorio entre 1 y ns (numero de servidores)
    private String id; //identificador del sensor + secuencial

    public Evento(String id, int tipoEvento){
        this.id = id;
        this.tipoEvento = tipoEvento;
    }



    public String getId(){
        return this.id;
    }
    public int getTipoEvento(){
    return this.tipoEvento;
    }

    public void setEsSospechoso(boolean es){
        this.esSospechoso = es;
    }
    
    public boolean getEsSospechoso(){
        return this.esSospechoso;
    }

}
