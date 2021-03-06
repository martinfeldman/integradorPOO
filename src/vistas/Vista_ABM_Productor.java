package vistas;

import modelo.Productor;
import servicios.Servicio_Productores;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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

// para esta vista se utilizó como referencia la vista de empleado del ejemploIntegrador

public class Vista_ABM_Productor implements Vista {

    // el servicio con el que se va a comunicar la vista
    private final Servicio_Productores servicio;
    private Productor productorSeleccionado;

    // objetos de la pantalla

    Button botonAgregar, botonEliminar, botonLimpiar;
    Font fuenteNegrita;
    Label etiquetaInteractiva, etiquetaInteractiva2;
    Separator separador;
    TableView<Productor> tabla;
    TableColumn<Productor, Integer> columnaId;
    TableColumn<Productor, String> columnaNombres;
    TableColumn<Productor, String> columnaApellidos;
    TableColumn<Productor, String> columnaDni;
    TableColumn<Productor, Boolean> columnaAlta;
    TextField entradaNombres, entradaApellidos, entradaDni;


    public Vista_ABM_Productor(Servicio_Productores servicio) {
        this.servicio = servicio;
    }


    
    @Override
    public Parent obtenerVista() {

        
    // definicion elementos de pantalla

        botonAgregar = new Button("Agregar/Modificar Selección");
        botonEliminar = new Button("Dar de baja");
        botonLimpiar = new Button("Limpiar");
    
        columnaId = new TableColumn<>("Id de Productor");
        columnaDni = new TableColumn<>("DNI");
        columnaNombres = new TableColumn<>("Nombres");
        columnaApellidos = new TableColumn<>("Apellidos");
        columnaAlta = new TableColumn<>("Alta");

        VBox contenedor = new VBox();
        HBox contenedorBotones = new HBox();
        VBox contenedorCarga = new VBox();

        entradaNombres = new TextField();
        entradaApellidos = new TextField();
        entradaDni = new TextField();
        
        etiquetaInteractiva = new Label("Puede seleccionar filas de la tabla para modificarlas (si su estado de alta es Verdadero)");
        etiquetaInteractiva2 = new Label("");
        
        fuenteNegrita = Font.font("", FontWeight.BOLD, FontPosture.REGULAR , 15);

        separador = new Separator(Orientation.HORIZONTAL); 

        tabla = new TableView<>();



    // propiedades de elementos

        contenedor.setAlignment(Pos.CENTER);
        contenedorBotones.setAlignment(Pos.CENTER);
        contenedorCarga.setAlignment(Pos.CENTER);

        contenedorBotones.setPadding(new Insets(10, 10, 30, 10));
        contenedorCarga.setPadding(new Insets(10, 10, 10, 10));
        contenedorBotones.setSpacing(10);
        contenedorCarga.setSpacing(10);

        //- COLUMNAS - propiedades

        columnaId.setCellValueFactory(new PropertyValueFactory<>("idProductor"));
        columnaDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columnaNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnaAlta.setCellValueFactory(new PropertyValueFactory<>("alta"));

        columnaId.setMinWidth(200);
        columnaDni.setMinWidth(200);
        columnaNombres.setMinWidth(300);
        columnaApellidos.setMinWidth(300);
        columnaAlta.setMinWidth(100);
        
        entradaNombres.setPromptText("Nombres del productor");
        entradaApellidos.setPromptText("Apellidos del productor");
        entradaDni.setPromptText("DNI del productor");

        entradaNombres.setMaxWidth(400);
        entradaApellidos.setMaxWidth(400);
        entradaDni.setMaxWidth(300);

        etiquetaInteractiva2.setFont(fuenteNegrita);

        separador.setPadding(new Insets(0, 0, 30, 0));

        tabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tabla.setPadding(new Insets(0, 0, 10, 0));
        tabla.setPrefHeight(350);



    // acciones sobre elementos 

        botonAgregar.setOnAction(e -> clicAgregarProductor());
        botonEliminar.setOnAction(e -> clicEliminarProductor());
        botonLimpiar.setOnAction(e -> limpiar());
        tabla.getSelectionModel().selectedItemProperty().addListener(e -> cargarDatos());

        //- cargamos datos a la tabla, a partir de consulta a la BD  
        tabla.getItems().addAll(this.servicio.listarProductores());


        //- agregamos las columnas a la tabla
        tabla.getColumns().add(columnaId);
        tabla.getColumns().add(columnaDni);
        tabla.getColumns().add(columnaNombres);
        tabla.getColumns().add(columnaApellidos);
        tabla.getColumns().add(columnaAlta);
        
       
        //- agregamos al contenedor la tabla
        contenedorBotones.getChildren().addAll(botonAgregar, botonEliminar, botonLimpiar);    
        contenedorCarga.getChildren().addAll(etiquetaInteractiva, separador, entradaNombres, entradaApellidos, entradaDni);
        contenedor.getChildren().addAll(tabla, contenedorCarga, contenedorBotones, etiquetaInteractiva2);
        
        return contenedor;

    }



    private boolean clicAgregarProductor(){

        //- A partir del objetoSeleccionado en la tabla
        productorSeleccionado = tabla.getSelectionModel().getSelectedItem();

        try {
        
        //- Si no hay elemento seleccionado en la tabla, se tiene un nuevo objeto por agregar
            if (productorSeleccionado == null) {
                servicio.agregarProductor(entradaNombres.getText(), entradaApellidos.getText(), entradaDni.getText());
            
               
        //- SINO, si el productorSeleccionado está de Alta, podremos modificarlo a partir de su id
            } else {
                
                if (productorSeleccionado.isAlta() == true) {
                    servicio.modificarProductor(productorSeleccionado.getIdProductor(), entradaNombres.getText(), 
                     entradaApellidos.getText(), entradaDni.getText());
        
        //- SINO, se informa y se retorna falso              
                } else {

                    limpiar();
                    etiquetaInteractiva2.setText("El productor seleccionado está dado de BAJA. No se puede modificar.\n"); 
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

        //- A partir de la seleccion de un objeto sobre la tabla, se cargan sus datos en los elementos de la pantalla
        productorSeleccionado = tabla.getSelectionModel().getSelectedItem();

        if (productorSeleccionado != null) {

            etiquetaInteractiva.setText("Está seleccionado el productor con id: " + productorSeleccionado.getIdProductor());
            
            entradaNombres.setText(productorSeleccionado.getNombres());
            entradaApellidos.setText(productorSeleccionado.getApellidos());
            entradaDni.setText(productorSeleccionado.getDni());

        }
    }



    private void clicEliminarProductor(){

        productorSeleccionado = tabla.getSelectionModel().getSelectedItem();
        
        if (productorSeleccionado != null) {
            
            servicio.eliminarProductor(productorSeleccionado.getIdProductor());
            limpiar();
        }
    }



     
    private void limpiar() {
    
        //- limpiar elementos de la vista    
        etiquetaInteractiva.setText("Puede seleccionar filas de la tabla para modificarlas (si su estado de alta es Verdadero)");
        etiquetaInteractiva2.setText("");

        entradaNombres.clear();
        entradaApellidos.clear();
        entradaDni.clear();

        tabla.getItems().clear();
        tabla.getItems().addAll(this.servicio.listarProductores());
    } 
}
