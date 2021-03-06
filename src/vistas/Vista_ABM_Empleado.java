package vistas;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import modelo.Cuadro;
import modelo.Empleado;
import modelo.Lote;
import modelo.Productor;
import servicios.Servicio_Cuadros;
import servicios.Servicio_Empleados;
import servicios.Servicio_Lotes;
import servicios.Servicio_Productores;
import servicios.Servicio_seguimientoEmpleado;


// Para utilizar esta vista, el constructor requiere los dos servicios utilizados.
// Para esta vista se utilizó como referencia la vista de empleado del ejemploIntegrador

public class Vista_ABM_Empleado implements Vista {

    // los servicios con el que se va a comunicar la vista
    private final Servicio_Empleados servicio;
    Servicio_seguimientoEmpleado servicio_seguimiento;
    Servicio_Productores servicio_Productores;
    Servicio_Lotes servicio_Lotes;
    Servicio_Cuadros servicio_Cuadros;
    private Empleado empleadoSeleccionado;


    // objetos de la pantalla
    
    Button botonAgregar, botonEliminar, botonLimpiar;
    Label etiquetaInteractiva, etiquetaInteractiva3;
    TableView<Empleado> tabla;
    TableColumn<Empleado, Integer> columnaId;
    TableColumn<Empleado, String> columnaNombres;
    TableColumn<Empleado, String> columnaApellidos;
    TableColumn<Empleado, String> columnaDni;
    TableColumn<Empleado, Boolean> columnaAlta;
    TextField entradaNombres, entradaApellidos, entradaDni;
  
    // objetos para seguimiento de empleados
    Button botonKgsPorProductor, botonKgsPorLote, botonKgsPorCuadro;
    ComboBox<Productor> productorBox;
    ComboBox<Lote> loteBox;
    ComboBox<Cuadro> cuadroBox;
    Font fuenteNegrita;
    Label etiquetaInteractiva2, etiquetaSalidaSeguimiento;
    Separator separador1, separador2, separador3, separador4, separador5, separador6, separador7;

    
    public Vista_ABM_Empleado(Servicio_Empleados servicio, Servicio_seguimientoEmpleado servicio_seguimiento,
     Servicio_Productores servicio_Productores, Servicio_Lotes servicio_Lotes, Servicio_Cuadros servicio_Cuadros) {
        this.servicio = servicio;
        this.servicio_seguimiento = servicio_seguimiento;
        this.servicio_Productores = servicio_Productores;
        this.servicio_Lotes = servicio_Lotes;
        this.servicio_Cuadros = servicio_Cuadros;
    }



    @Override
    public Parent obtenerVista() {
        

    // definicion elementos de pantalla

        botonAgregar = new Button("Agregar");
        botonEliminar = new Button("Dar de baja");
        botonLimpiar = new Button("Limpiar");
        botonKgsPorProductor = new Button("obtener kgs cosechados por X productor");
        botonKgsPorLote = new Button("obtener kgs cosechados por X lote");
        botonKgsPorCuadro = new Button("obtener kgs cosechados por X cuadro");

        columnaId = new TableColumn<>("Id de Empleado");
        columnaDni = new TableColumn<>("DNI");
        columnaNombres = new TableColumn<>("Nombres");
        columnaApellidos = new TableColumn<>("Apellidos");
        columnaAlta = new TableColumn<>("Alta");

        VBox contenedor = new VBox();
        HBox contenedorBotones = new HBox();
        VBox contenedorCarga = new VBox();
        HBox contenedorSeguimientoEmpleados1 = new HBox();
        HBox contenedorSeguimientoEmpleados2 = new HBox();
        HBox contenedorSeguimientoEmpleados3 = new HBox();

        cuadroBox = new ComboBox<>();
        
        entradaNombres = new TextField();
        entradaApellidos = new TextField();
        entradaDni = new TextField();

        etiquetaInteractiva = new Label("Puede seleccionar filas de la tabla para modificarlas (si su estado de alta es Verdadero)");
        etiquetaInteractiva2 = new Label("Seguimiento de Producción de Kgs producidos por empleado por Productor, Lote o Cuadro. \nDebe seleccionar un empleado de la tabla");
        etiquetaInteractiva3 = new Label("");
        etiquetaSalidaSeguimiento= new Label("");
        
        fuenteNegrita = Font.font("", FontWeight.BOLD, FontPosture.REGULAR , 15);

        
        loteBox = new ComboBox<>();
        productorBox = new ComboBox<>();
    
        separador1 = new Separator(Orientation.HORIZONTAL); 
        separador2 = new Separator(Orientation.HORIZONTAL); 
        separador3 = new Separator(Orientation.VERTICAL); 
        separador4 = new Separator(Orientation.VERTICAL); 
        separador5 = new Separator(Orientation.VERTICAL); 
        separador6 = new Separator(Orientation.HORIZONTAL); 
        separador7 = new Separator(Orientation.HORIZONTAL); 


        tabla = new TableView<>();

       

    // propiedades de elementos

        //- COLUMNAS - propiedades
        columnaId.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        columnaDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columnaNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnaAlta.setCellValueFactory(new PropertyValueFactory<>("alta"));

        columnaId.setMinWidth(200);
        columnaDni.setMinWidth(200);
        columnaNombres.setMinWidth(300);
        columnaApellidos.setMinWidth(300);
        columnaAlta.setMinWidth(100);

        contenedor.setAlignment(Pos.CENTER);
        contenedorBotones.setAlignment(Pos.CENTER);
        contenedorCarga.setAlignment(Pos.CENTER);
        contenedorSeguimientoEmpleados1.setAlignment(Pos.CENTER);
        contenedorSeguimientoEmpleados2.setAlignment(Pos.CENTER);
        contenedorSeguimientoEmpleados3.setAlignment(Pos.CENTER);
        
        contenedorBotones.setPadding(new Insets(10, 10, 25, 10));
        contenedorCarga.setPadding(new Insets(10, 10, 10, 10));
        contenedorSeguimientoEmpleados1.setPadding(new Insets(30, 10, 20, 10));
        contenedorSeguimientoEmpleados2.setPadding(new Insets(20, 10, 20, 10));
        contenedorSeguimientoEmpleados3.setPadding(new Insets(20, 10, 30, 10));
        
        contenedorBotones.setSpacing(10);
        contenedorCarga.setSpacing(10);

        cuadroBox.setPromptText("Seleccione un cuadro");
        cuadroBox.setMinWidth(200);

        entradaNombres.setMaxWidth(400);
        entradaApellidos.setMaxWidth(400);
        entradaDni.setMaxWidth(300);

        entradaNombres.setPromptText("Nombres del empleado");       
        entradaApellidos.setPromptText("Apellidos del empleado");
        entradaDni.setPromptText("DNI del empleado");

        etiquetaInteractiva2.setPadding(new Insets(20, 0, 0, 0));
        etiquetaInteractiva3.setFont(fuenteNegrita);
        etiquetaSalidaSeguimiento.setFont(fuenteNegrita);

        loteBox.setPromptText("Seleccione un lote");
        loteBox.setMinWidth(200);

        productorBox.setPromptText("Seleccione un productor");
        productorBox.setMinWidth(200);

        separador1.setPadding(new Insets(0, 0, 30, 0));
        separador2.setPadding(new Insets(25, 0, 0, 0));
        separador3.setPadding(new Insets(0, 20, 0, 20));
        separador4.setPadding(new Insets(0, 20, 0, 20));
        separador5.setPadding(new Insets(0, 20, 0, 20));
        separador6.setPadding(new Insets(0, 0, 15, 0));

        tabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tabla.setPadding(new Insets(0, 0, 10, 0));
        tabla.setPrefHeight(350);



    // acciones sobre elementos

        botonAgregar.setOnAction(e -> clicAgregarEmpleado());
        botonEliminar.setOnAction(e -> clicEliminarEmpleado());
        botonLimpiar.setOnAction(e -> limpiar());
        botonKgsPorProductor.setOnAction(e -> clicBotonKgsPorProductor());
        botonKgsPorLote.setOnAction(e -> clicBotonKgsPorLote());
        botonKgsPorCuadro.setOnAction(e -> clicBotonKgsPorCuadro());
        tabla.getSelectionModel().selectedItemProperty().addListener(e -> cargarDatos());  

        //- cargamos datos a la tabla y los comboBoxs, a partir de consultas a la BD 
        tabla.getItems().addAll(this.servicio.listarEmpleados());

        productorBox.getItems().addAll(this.servicio_Productores.listarProductores());
        loteBox.getItems().addAll(this.servicio_Lotes.listarLotes());
        cuadroBox.getItems().addAll(this.servicio_Cuadros.listarCuadros());


        //- agregamos las columnas a la tabla
        tabla.getColumns().add(columnaId);
        tabla.getColumns().add(columnaDni);
        tabla.getColumns().add(columnaNombres);
        tabla.getColumns().add(columnaApellidos);
        tabla.getColumns().add(columnaAlta);
       
        //- agregamos contenido a los contenedores  
        contenedorBotones.getChildren().addAll(botonAgregar, botonEliminar, botonLimpiar);

        contenedorCarga.getChildren().addAll(etiquetaInteractiva, separador1, entradaNombres, entradaApellidos, entradaDni);

        contenedorSeguimientoEmpleados1.getChildren().addAll(productorBox, separador3, botonKgsPorProductor);

        contenedorSeguimientoEmpleados2.getChildren().addAll(loteBox, separador4, botonKgsPorLote);

        contenedorSeguimientoEmpleados3.getChildren().addAll(cuadroBox, separador5, botonKgsPorCuadro);

        contenedor.getChildren().addAll(tabla, contenedorCarga, contenedorBotones, etiquetaInteractiva3, separador2, etiquetaInteractiva2, contenedorSeguimientoEmpleados1, 
         contenedorSeguimientoEmpleados2,  contenedorSeguimientoEmpleados3, separador6, etiquetaSalidaSeguimiento);

        
        return contenedor;

    }



    private boolean clicAgregarEmpleado() {
        
        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();

        try {

            // Si no hay elemento seleccionado en la tabla, se tiene un nuevo objeto por agregar
            if (empleadoSeleccionado == null) {
                servicio.agregarEmpleado(entradaNombres.getText(), entradaApellidos.getText(), entradaDni.getText());
           

            //- SINO, si el cuadroSeleccionado está de Alta, podremos modificarlo a partir de su id
            } else {

                if (empleadoSeleccionado.isAlta() == true) {
                    servicio.modificarEmpleado(empleadoSeleccionado.getIdEmpleado(), entradaNombres.getText(), entradaApellidos.getText(), entradaDni.getText());

            //- SINO, se informa y se retorna falso 
                } else {

                    limpiar();
                    etiquetaInteractiva3.setText("El empleado seleccionado está dado de BAJA. No se puede modificar.\n"); 
                    return false;

                }

            }

            limpiar();

        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar", e.getMessage());
        }

        return true; 
    }



    private void cargarDatos() {

        //- A partir de la seleccion de un objeto sobre la tabla, Se cargan sus datos en los elementos de la pantalla
        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {

            etiquetaInteractiva.setText("Está seleccionado el Empleado con id: " + empleadoSeleccionado.getIdEmpleado());
            
            entradaNombres.setText(empleadoSeleccionado.getNombres());
            entradaApellidos.setText(empleadoSeleccionado.getApellidos());
            entradaDni.setText(empleadoSeleccionado.getDni());
        }
    }

    

    private void clicEliminarEmpleado() {

        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {

            servicio.eliminarEmpleado(empleadoSeleccionado.getIdEmpleado());
            limpiar();
        }

    }



    private void clicBotonKgsPorProductor (){

        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();

     
        //- Se utiliza una etiqueta para desplegar la salida del método 
        if (empleadoSeleccionado != null) {

            if (productorBox.getValue() == null){
                etiquetaSalidaSeguimiento.setText("Si bien tiene seleccionado un empleado, no ha especificado el productor");

            } else {
                etiquetaSalidaSeguimiento.setText(servicio_seguimiento.obtenerKgsEmpleado_productor(empleadoSeleccionado, productorBox.getValue()));
            }
    

        } else {
            etiquetaSalidaSeguimiento.setText("No ha seleccionado ningún empleado de la tabla");
        }
    }



    private void clicBotonKgsPorLote (){

        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();

        //- Se utiliza una etiqueta para desplegar la salida del método 
        if (empleadoSeleccionado != null) {

            if (loteBox.getValue() == null){
                etiquetaSalidaSeguimiento.setText("Si bien tiene seleccionado un empleado, no ha especificado el lote");

            } else {
                etiquetaSalidaSeguimiento.setText(servicio_seguimiento.obtenerKgsEmpleado_lote(empleadoSeleccionado, loteBox.getValue()));
            }
                

        } else {
            etiquetaSalidaSeguimiento.setText("No ha seleccionado ningún empleado de la tabla");
        }
    }



    private void clicBotonKgsPorCuadro (){

        empleadoSeleccionado = tabla.getSelectionModel().getSelectedItem();
        
        //- Se utiliza una etiqueta para desplegar la salida del método 
        if (empleadoSeleccionado != null) {
            
            if (cuadroBox.getValue() == null){
                etiquetaSalidaSeguimiento.setText("Si bien tiene seleccionado un empleado, no ha especificado el cuadro");

            } else {
                etiquetaSalidaSeguimiento.setText(servicio_seguimiento.obtenerKgsEmpleado_cuadro(empleadoSeleccionado, cuadroBox.getValue()));
            }

        } else {
            etiquetaSalidaSeguimiento.setText("No ha seleccionado ningún empleado de la tabla");
        }
    }



    private void limpiar() {
        
        //- limpiar elementos de la vista 
        etiquetaInteractiva.setText("Puede seleccionar filas de la tabla para modificarlas (si su estado de alta es Verdadero)");
        etiquetaInteractiva3.setText("");
        etiquetaSalidaSeguimiento.setText("");
        
        entradaNombres.clear();
        entradaApellidos.clear();
        entradaDni.clear();

        cuadroBox.setPromptText("Seleccione un cuadro");
        loteBox.setPromptText("Seleccione un lote");
        productorBox.setPromptText("Seleccione un productor");

        tabla.getItems().clear();
        tabla.getItems().addAll(this.servicio.listarEmpleados());
    } 


}
