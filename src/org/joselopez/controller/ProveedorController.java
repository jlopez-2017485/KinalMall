package org.joselopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import org.joselopez.bean.Proveedor;
import org.joselopez.db.Conexion;
import org.joselopez.system.Principal;

public class ProveedorController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;    
    
    private ObservableList<Proveedor> listaProveedor;
    private ObservableList<Administracion> listaAdmin;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private TextField txtCodigoProveedor;
    @FXML private TextField txtNITProveedor;
    @FXML private TextField txtServicioPrestado;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtSaldoFavor;
    @FXML private TextField txtSaldoContra;
    
    @FXML private ComboBox cmbCodAdmin;
    
    @FXML private TableView tblProveedor;
    
    @FXML private TableColumn colCodigoProveedor;
    @FXML private TableColumn colNITProveedor;
    @FXML private TableColumn colServicioPrestado;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colSaldoFavor;
    @FXML private TableColumn colSaldoContra;
    @FXML private TableColumn colCodAdmin;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblProveedor.setItems(getProveedor());
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("codigoProveedor"));
        colNITProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("NITProveedor"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("servicioPrestado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("telefonoProveedor"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("direccionProveedor"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedor, Double>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedor, Double>("saldoContra"));
        colCodAdmin.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("codigoAdministracion"));
        cmbCodAdmin.setItems(getAdministracion());
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
    
    public void seleccionarElemento(){
        if(tblProveedor.getSelectionModel().getSelectedItem() != null){
            txtCodigoProveedor.setText(String.valueOf(((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            txtNITProveedor.setText(((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getNITProveedor());
            txtServicioPrestado.setText(((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getServicioPrestado());
            txtTelefono.setText(((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getTelefonoProveedor());
            txtDireccion.setText(((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getDireccionProveedor());
            txtSaldoFavor.setText(String.valueOf(((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getSaldoContra()));
            
            cmbCodAdmin.getSelectionModel().select(buscarAdmin(((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
        }
    }
    
    public Administracion buscarAdmin(int codigoAdministracion){
        Administracion resultado = null;
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarAdministracion(?)}");
            procedure.setInt(1, codigoAdministracion);
            ResultSet registro = procedure.executeQuery();
            while(registro.next()){
                resultado = new Administracion(registro.getInt("codigoAdministracion"),
                                               registro.getString("direccion"),
                                               registro.getString("telefono"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
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
        Proveedor registro = new Proveedor();
        registro.setNITProveedor(txtNITProveedor.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefonoProveedor(txtTelefono.getText());
        registro.setDireccionProveedor(txtDireccion.getText());
        registro.setSaldoFavor(Double.valueOf(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.valueOf(txtSaldoContra.getText()));
        registro.setCodigoAdministracion(((Administracion)cmbCodAdmin.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarProveedores(?,?,?,?,?,?,?)}");
            procedure.setString(1, registro.getNITProveedor());
            procedure.setString(2, registro.getServicioPrestado());
            procedure.setString(3, registro.getTelefonoProveedor());
            procedure.setString(4, registro.getDireccionProveedor());
            procedure.setDouble(5, registro.getSaldoFavor());
            procedure.setDouble(6, registro.getSaldoContra());
            procedure.setInt(7, registro.getCodigoAdministracion());
            procedure.execute();
            cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
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
                if(tblProveedor.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de querer eliminar este registro?", "Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarProveedores(?)}");
                            procedure.setInt(1, ((Proveedor)tblProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedure.execute();
                            listaProveedor.remove(tblProveedor.getSelectionModel().getSelectedIndex());
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
                if(tblProveedor.getSelectionModel().getSelectedItem() != null){
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
        Proveedor registro = (Proveedor)tblProveedor.getSelectionModel().getSelectedItem();
        registro.setNITProveedor(txtNITProveedor.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefonoProveedor(txtTelefono.getText());
        registro.setDireccionProveedor(txtDireccion.getText());
        registro.setSaldoFavor(Double.valueOf(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.valueOf(txtSaldoContra.getText()));
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarProveedores(?,?,?,?,?,?,?)}");
            procedure.setInt(1, registro.getCodigoProveedor());
            procedure.setString(2, registro.getNITProveedor());
            procedure.setString(3, registro.getServicioPrestado());
            procedure.setString(4, registro.getTelefonoProveedor());
            procedure.setString(5, registro.getDireccionProveedor());
            procedure.setDouble(6, registro.getSaldoFavor());
            procedure.setDouble(7, registro.getSaldoContra());
            procedure.execute();
            cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
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
    
    public void desactivarControles(){
        txtCodigoProveedor.setEditable(false);
        txtNITProveedor.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        cmbCodAdmin.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoProveedor.setEditable(false);
        txtNITProveedor.setEditable(true);
        txtServicioPrestado.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        cmbCodAdmin.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoProveedor.clear();
        txtNITProveedor.clear();
        txtServicioPrestado.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        cmbCodAdmin.getSelectionModel().clearSelection();
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
