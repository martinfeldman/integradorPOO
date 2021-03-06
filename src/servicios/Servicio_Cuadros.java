package servicios;
import repositorios.*;
import modelo.Cuadro;
import modelo.Lote;

import java.util.List;


public class Servicio_Cuadros{
  
    private Repositorio repositorio;

    public Servicio_Cuadros(Repositorio p) {
        this.repositorio = p;
    }


    
    // CUADRO 
    // Listar y Buscar

    public List<Cuadro> listarCuadros() {
        return this.repositorio.buscarTodos(Cuadro.class);
        // cambiar por buscar todos ordenados por
    }

    public Cuadro buscarCuadro(int idCuadro) {
        return this.repositorio.buscar(Cuadro.class, idCuadro);
    }



    // ABM CUADRO 

    // para agregar un cuadro hace falta lote y superficie
    public void agregarCuadro(Lote lote, Double superficie) {
        
        if (lote == null || superficie.toString() == null ) {
            throw new IllegalArgumentException("Faltan datos");
        }

        Cuadro cuadro = new Cuadro(lote, superficie);

        this.repositorio.iniciarTransaccion();
        this.repositorio.insertar(cuadro);
        this.repositorio.confirmarTransaccion();
    }

 

   
    public boolean modificarCuadro(int idCuadro, Lote loteNuevo , Double superficie ) {
        
        //- Exepcion si alguno de los datos(obligatrios) que toma de la Vista esta vacio o es NULL
        if (loteNuevo == null || superficie.toString() == null) {
            throw new IllegalArgumentException("Faltan datos");
        }

        //- buscar el cuadro en la base de datos a partir de su ID 
        Cuadro cuadro = this.repositorio.buscar(Cuadro.class, idCuadro);
        
        //- si regresa un objeto cuadro, se hacen TODAS las modificaciones debidas,
        //- incluyendo las dependencias en otras clases y se inicia una transaccion
        if (cuadro != null) {
            
        //- dependencias de la modificación    
            
            var loteParaQuitar = cuadro.getLote();
            this.repositorio.iniciarTransaccion();

        //- Solo se cambia el lote del Cuadro si es diferente del que ya posee    
            if (! loteParaQuitar.equals(loteNuevo)) {

                loteParaQuitar.quitarCuadro(cuadro); 
                loteNuevo.agregarCuadro(cuadro);

                this.repositorio.modificar(loteParaQuitar);
                this.repositorio.modificar(loteNuevo);
        
        //- modificaciones al objeto    

                cuadro.setLote(loteNuevo);
            
            } else {
                System.out.print("loteParaQuitar es igual a loteNuevo. Se omite esta modificación.\n");
            }

            cuadro.setSuperficie(superficie);

            this.repositorio.modificar(cuadro);
            this.repositorio.confirmarTransaccion();
            return true;

        //- sino se informa y se retorna modificarObjeto = falso     
        } else {
            System.out.print("repositorio.buscar(idCuadros) = NULL \n\n");
            return false;
        }
    }



    
    public boolean eliminarCuadro(int idCuadro) {
        // se implementa borrado logico

        // buscar el cuadro en la base de datos a partir de su ID 
        Cuadro cuadro = this.repositorio.buscar(Cuadro.class, (Object) idCuadro);

        // si bd no retorna objeto es porque no existe, eliminarProductor devuelve falso
        if (cuadro == null) {
            System.out.print("repositorio.buscar(idProductor) = NULL \n\n");
            return false;
 
        // sino comienza una transaccion con bd 
        // se quita el cuadro del lote al que pertenece, se da de baja el cuadro y se confirma transaccion
        } else {
            this.repositorio.iniciarTransaccion();

            // dar de baja en el lote al que pertenece 
            cuadro.getLote().quitarCuadro(cuadro);
            this.repositorio.modificar(cuadro.getLote()); 

            // dar de baja el cuadro
            cuadro.setAlta(false);
            this.repositorio.modificar(cuadro); 
 
            this.repositorio.confirmarTransaccion();
             
            return true; 
        }
    }

   
}
