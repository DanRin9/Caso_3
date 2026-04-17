public class Evento {
    
    private boolean esSospechoso; 
    private int id; //numero seudoaleatorio entre 1 y ns (numero de servidores)

    public Evento(int id){
        this.id = id;
    }


    public void setTipoEvento(boolean tipoEvento){
        this.esSospechoso = tipoEvento;
    }

    public int getId(){
        return this.id;
    }
    
    public boolean getEsSospechoso(){
        return this.esSospechoso;
    }

}
