package org.joselopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.joselopez.bean.Administracion;
import org.joselopez.bean.CuentaPorPagar;
import org.joselopez.bean.Proveedor;
import org.joselopez.db.Conexion;
import org.joselopez.system.Principal;


public class CuentaPorPagarController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    
    private ObservableList<CuentaPorPagar> listaCuentaPagar;
    private ObservableList<Administracion> listaAdmin;
    private ObservableList<Proveedor> listaProveedor;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private TextField txtCodigoCuentasPorPagar;
    @FXML private TextField txtNoFactura;
    @FXML private TextField txtFechaLimitePago;
    @FXML private TextField txtEstadoPago;
    @FXML private TextField txtValorNetoPago;

    @FXML private ComboBox cmbCodAdmin;
    @FXML private ComboBox cmbCodProveedor;
    
    @FXML private TableView tblCuentasPorPagar;
    
    @FXML private TableColumn colCodigoCuentasPorPagar;
    @FXML private TableColumn colNoFactura;
    @FXML private TableColumn colFechaLimitePago;
    @FXML private TableColumn colEstadoPago;
    @FXML private TableColumn colValorNetoPago;
    @FXML private TableColumn colCodAdmin;
    @FXML private TableColumn colCodProveedor;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
    }
    
    public void cargarDatos(){
        tblCuentasPorPagar.setItems(getCuentasPagar());
        colCodigoCuentasPorPagar.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Integer>("codigoCuentasPorPagar"));
        colNoFactura.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, String>("numeroFactura"));
        colFechaLimitePago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Date>("fechaLimitePago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, String>("estadoPago"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Double>("valorNetoPago"));
        colCodAdmin.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Integer>("codigoAdministracion"));
        colCodProveedor.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Integer>("codigoProveedor"));
        
        cmbCodAdmin.setItems(getAdministracion());
        cmbCodProveedor.setItems(getProveedor());
    }
    
    public ObservableList<CuentaPorPagar> getCuentasPagar(){
        ArrayList<CuentaPorPagar> lista = new ArrayList<CuentaPorPagar>();
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarCuentasPorPagar()}");
            ResultSet resultado = procedure.executeQuery();
            while(resultado.next()){
                lista.add(new CuentaPorPagar(resultado.getInt("codigoCuentasPorPagar"),
                                             resultado.getString("numeroFactura"),
                                             resultado.getDate("fechaLimitePago"),
                                             resultado.getString("estadoPago"),
                                             resultado.getDouble("valorNetoPago"),
                                             resultado.getInt("codigoAdministracion"),
                                             resultado.getInt("codigoProveedor")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCuentaPagar = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Proveedor> getProveedor(){
        ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarProveedores()}");
            ResultSet resultado = procedure.executeQuery();
            while(resultado.next()){
                lista.add(new Proveedor(resultado.getInt("codigoProveedor"),
                                        resultado.getString("NITProveedor"),
                                        resultado.getString("servicioPrestado"),
                                        resultado.getString("telefonoProveedor"),
                                        resultado.getString("direccionProveedor"),
                                        resultado.getDouble("saldoFavor"),
                                        resultado.getDouble("saldoContra"),
                                        resultado.getInt("codigoAdministracion")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaProveedor = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Administracion> getAdministracion(){       
        ArrayList<Administracion> lista = new ArrayList<Administracion>();
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");
            ResultSet resultado = procedure.executeQuery();
            while(resultado.next()){
            lista.add(new Administracion(resultado.getInt("codigoAdministracion"),
                                        resultado.getString("direccion"),
                                        resultado.getString("telefono")));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaAdmin = FXCollections.observableArrayList(lista); 
        
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/joselopez/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/joselopez/images/Cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/joselopez/images/Agregar.png"));
                imgEliminar.setImage(new Image("/org/joselopez/images/Eliminar.png"));
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void guardar(){
        
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/joselopez/images/Agregar.png"));
                imgEliminar.setImage(new Image("/org/joselopez/images/Eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de querer eliminar este registro?", "Eliminar Cuentas Por Pagar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarCuentasPorPagar(?)}");
                            procedure.setInt(1, ((Proveedor)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedure.execute();
                            listaProveedor.remove(tblCuentasPorPagar.getSelectionModel().getSelectedIndex());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debes de seleccionar un elemento");
                }
        }
    }
    
    public void editar(){
        activarControles();
        btnEditar.setText("Actualizar");
        switch(tipoOperacion){
            case NINGUNO:
                if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/joselopez/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/joselopez/images/Cancelar.png"));
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debes de seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/joselopez/images/editar.png"));
                imgReporte.setImage(new Image("/org/joselopez/images/reporte.png"));
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/joselopez/images/editar.png"));
                imgReporte.setImage(new Image("/or/joselopez/images/reporte.png"));
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void activarControles(){
        txtCodigoCuentasPorPagar.setEditable(false);
        txtNoFactura.setEditable(false);
//        txtFechaLimitePago.setEdtiable(false);
        txtEstadoPago.setEditable(false);
        txtValorNetoPago.setEditable(false);
        cmbCodAdmin.setDisable(true);
        cmbCodProveedor.setDisable(true);
    }
    
    public void desactivarControles(){
        txtCodigoCuentasPorPagar.setEditable(false);
        txtNoFactura.setEditable(true);
//        txtFechaLimitePago.setEdtiable(true);
        txtEstadoPago.setEditable(true);
        txtValorNetoPago.setEditable(true);
        cmbCodAdmin.setDisable(false);
        cmbCodProveedor.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCuentasPorPagar.clear();
        txtNoFactura.clear();
        txtFechaLimitePago.clear();
        txtEstadoPago.clear();
        txtValorNetoPago.clear();
        cmbCodAdmin.getSelectionModel().clearSelection();
        cmbCodProveedor.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
