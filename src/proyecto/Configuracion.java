/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

/**
 *
 * @author César
 */
public class Configuracion implements Serializable {
     BorderPane root;
    ListView<String> listView;
    TreeMap<String, Pintura> colorMap;
    ColorPicker colorPicker;
    
    public Configuracion(TreeMap<String, Pintura> colorMap ){
        root=new BorderPane();
        this.colorMap=colorMap;  
        createContent();
    }
    
    public void createContent(){
        createContentLeft();
        createContentRight();
    }
    
    private void createContentLeft(){
        VBox vBox = new VBox(5);
        Label lblExtensiones = new Label("Extensiones:");
        listView = new ListView<>();
        
        actualizarListView();
        vBox.getChildren().addAll(lblExtensiones,listView);
        root.setLeft(vBox);
        
        listView.setOnMouseClicked(e->{
            double red=colorMap.get(listView.getSelectionModel().getSelectedItem()).getRed();
            double green=colorMap.get(listView.getSelectionModel().getSelectedItem()).getGreen();
            double blue=colorMap.get(listView.getSelectionModel().getSelectedItem()).getBlue();
        colorPicker.setValue(new Color(red,green,blue,1));
        // colorPicker.setValue(colorMap.get(listView.getSelectionModel().getSelectedItem()).getColor());
        
        
        
        });
        
    }
    
    private void createContentRight(){
        VBox vBox = new VBox(5);
        Label lblColor = new Label("Color:");
        colorPicker = new ColorPicker();
        
        HBox hBox = new HBox();
        Button btnEliminar = new Button("Eliminar");
        Button btnAgregar = new Button("Agregar");
        Button btnGuardar = new Button("Guardar cambios");
        hBox.getChildren().addAll(btnEliminar,btnAgregar,btnGuardar);
        
        colorPicker.setOnAction(e->{
            String extension = listView.getSelectionModel().getSelectedItem();
            if(extension!=null){
                colorMap.remove(extension);
                colorMap.put(extension,new Pintura(colorPicker.getValue()));
               
                actualizarListView();
            }
           
        
        });
        
        btnEliminar.setOnAction(e->{
          String extension = listView.getSelectionModel().getSelectedItem();
            if(extension!=null){
                colorMap.remove(extension);
            }
            actualizarListView();
        
        });
        
        btnAgregar.setOnAction(e->{
            TextInputDialog inputWindow = new TextInputDialog();
            inputWindow.setTitle("Ventana de entrada de texto");
            inputWindow.setHeaderText("Agregar extensión");
            inputWindow.setContentText("Ingrese la extensión:");
            inputWindow.initStyle(StageStyle.UTILITY);
            Optional<String> respuesta = inputWindow.showAndWait();
            if(respuesta.isPresent()){
                if(!respuesta.get().equals("")&&!colorMap.containsKey(respuesta.get())){
                    colorMap.put(respuesta.get(),new Pintura(colorPicker.getValue()));
                    actualizarListView();

                }else{
                    Alert alertDialog = new Alert(Alert.AlertType.ERROR);
                    alertDialog.setTitle("¡Error!");
                    alertDialog.setHeaderText("Entrada de texto no válida");
                    alertDialog.setContentText("La extensión ingresada ya existe o no es válida");
                    alertDialog.showAndWait();
                }
            }
        
        });
        
        
        btnGuardar.setOnAction(e->{
           serializar();
           actualizarListView();
        
        });
        
        
        vBox.getChildren().addAll(lblColor,colorPicker,hBox);
        root.setRight(vBox);
        
        
    }
    
    private void serializar(){
        FileOutputStream fos = null;
        ObjectOutputStream salida = null;
        try {
            fos = new FileOutputStream("colorMap.dat");
            salida = new ObjectOutputStream(fos);
            salida.writeObject(colorMap);
           
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(fos!=null) fos.close();
                if(salida!=null) salida.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    
        
    }
    
    private void actualizarListView(){
        ObservableList<String> elements = FXCollections.observableArrayList();
        Set<String> keys = colorMap.keySet();
        Iterator<String> it = keys.iterator();
        while(it.hasNext()){
            elements.add(it.next());
        }
        listView.setItems(elements);
        
        
    }

    public BorderPane getRoot() {
        return root;
    }
    
    
}
