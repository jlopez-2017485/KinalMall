/*
DOCUMENTACIÓN:
    PROGRAMADOR:
        Jose Daniel López Marroquín
    CREACIÓN:
        -05/05/2021
MODIFICACIONES:
    -13/05/2021
    -02/06/2021
    -03/06/2021
    -04/06/2021
 */
package org.joselopez.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.joselopez.controller.AdministracionController;
import org.joselopez.controller.ClienteController;
import org.joselopez.controller.CuentaPorPagarController;
import org.joselopez.controller.DepartamentoController;
import org.joselopez.controller.HorarioController;
import org.joselopez.controller.LocalController;
import org.joselopez.controller.MenuPrincipalController;
import org.joselopez.controller.ProgramadorController;
import org.joselopez.controller.ProveedorController;
import org.joselopez.controller.TipoClienteController;
import org.joselopez.controller.UsuarioController;


/**
 *
 * @author Jose López
 */
public class Principal extends Application {
        private final String PAQUETE_VISTA = "/org/joselopez/view/";
        private Stage escenarioPrincipal;
        private Scene escena;
        
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Kinal Mall ");
//        Parent root = FXMLLoader.load(getClass().getResource("/org/joselopez/view/MenuPrincipalView.fxml"));
//        Scene escena = new Scene (root);
//        escenarioPrincipal.setScene(escena);
          menuPrincipal();
          escenarioPrincipal.show();
        
    }

     public void menuPrincipal(){
         
         try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 580, 400) ;
        menu.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
         
     }   
     
     public void ventanaProgramador(){
         try{
             ProgramadorController progra = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 600, 400);
             progra.setEscenarioPrincipal(this);             
         }catch(Exception e){
             e.printStackTrace();
         }
     }       
     
     public void ventanaAdministracion(){
         try{
            AdministracionController admin = (AdministracionController)cambiarEscena("AdministracionView.fxml", 1065, 499);
            admin.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void ventanaTipoCliente(){
         try{
             TipoClienteController tipoC = (TipoClienteController) cambiarEscena("TipoClienteView.fxml", 800, 499);
             tipoC.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void ventanaLocal(){
         try{
             LocalController local = (LocalController) cambiarEscena("LocalesView.fxml", 1120, 533);
             local.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void ventanaDepartamento(){
         try{
             DepartamentoController departamento = (DepartamentoController) cambiarEscena("DepartamentosView.fxml", 800, 499);
             departamento.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void ventanaHorario(){
         try{
             HorarioController horario = (HorarioController) cambiarEscena("HorariosView.fxml", 905, 499);
             horario.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void ventanaCliente(){
         try{
             ClienteController cliente = (ClienteController) cambiarEscena("ClientesView.fxml", 1310, 499);
             cliente.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void ventanaProveedor(){
         try{
             ProveedorController proveedor = (ProveedorController) cambiarEscena("ProveedoresView.fxml", 1168, 499);
             proveedor.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void ventanaCuentaPorPagar(){
         try{
             CuentaPorPagarController cuentaPagar = (CuentaPorPagarController) cambiarEscena("CuentasPorPagarView.fxml", 1140, 499);
             cuentaPagar.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
   /*  public void ventanaUsuario(){
         try{
             UsuarioController usuario = (UsuarioController) cambiar Escena("UsuarioView.fxml", 896, 499);
             usuario.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }*/
         
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException{
        
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene ((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
        
    }
    
}
