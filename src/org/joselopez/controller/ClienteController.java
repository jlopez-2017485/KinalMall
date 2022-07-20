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
import org.joselopez.bean.Cliente;
import org.joselopez.bean.Local;
import org.joselopez.bean.TipoCliente;
import org.joselopez.db.Conexion;
import org.joselopez.system.Principal;


public class ClienteController implements Initializable{
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    
    private ObservableList<Cliente> listaCliente;
    private ObservableList<Local> listaLocal;
    private ObservableList<Administracion> listaAdmin;
    private ObservableList<TipoCliente> listaTipoCliente;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private TextField txtCodigoCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtApellidoCliente;
    @FXML private TextField txtTelCliente;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEmail;
    
    @FXML private ComboBox cmbCodLocal;
    @FXML private ComboBox cmbCodAdmin;
    @FXML private ComboBox cmbCodTipoCliente;
    
    @FXML private TableView tblCliente;
    
    @FXML private TableColumn colCodigoCliente;
    @FXML private TableColumn colNombreCliente;
    @FXML private TableColumn colApellidoCliente;
    @FXML private TableColumn colTelCliente;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colEmail;
    @FXML private TableColumn colCodLocal;
    @FXML private TableColumn colCodAdmin;
    @FXML private TableColumn colCodTipoCliente;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCliente.setItems(getCliente());
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("codigoCliente"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombresCliente"));
        colApellidoCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellidosCliente"));
        colTelCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefonoCliente"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccionCliente"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Cliente, String>("email"));
        colCodLocal.setCellValueFactory(new PropertyValueFactory<Local, Integer>("codigoLocal"));
        colCodAdmin.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodTipoCliente.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("codigoTipoCliente"));
        cmbCodLocal.setItems(getLocal());
        cmbCodAdmin.setItems(getAdministracion());
        cmbCodTipoCliente.setItems(getTipoCliente());
    } 
    
    public ObservableList<Cliente> getCliente(){
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarClientes()}");
            ResultSet resultado = procedure.executeQuery();
            while(resultado.next()){
                lista.add(new Cliente(resultado.getInt("codigoCliente"),
                                      resultado.getString("nombresCliente"),
                                      resultado.getString("apellidosCliente"),
                                      resultado.getString("telefonoCliente"),
                                      resultado.getString("direccionCliente"),
                                      resultado.getString("email"),
                                      resultado.getInt("codigoLocal"),
                                      resultado.getInt("codigoAdministracion"),
                                      resultado.getInt("codigoTipoCliente")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return listaCliente = FXCollections.observableArrayList(lista);
        
    }
    
    public ObservableList<Local> getLocal(){
        ArrayList<Local> lista = new ArrayList<Local>();
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarLocales()}");
            ResultSet resultado = procedure.executeQuery();
            while(resultado.next()){
                lista.add(new Local(resultado.getInt("codigoLocal"),
                                    resultado.getDouble("saldoFavor"),
                                    resultado.getDouble("saldoContra"),
                                    resultado.getInt("mesesPendientes"),
                                    resultado.getBoolean("disponibilidad"),
                                    resultado.getDouble("valorLocal"),
                                    resultado.getDouble("valorAdministracion")));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaLocal = FXCollections.observableArrayList(lista);
        
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
    
    public ObservableList<TipoCliente> getTipoCliente(){
        ArrayList<TipoCliente> lista = new ArrayList<TipoCliente>();
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarTipoCliente()}");
            ResultSet resultado = procedure.executeQuery();
            while(resultado.next()){
                lista.add(new TipoCliente(resultado.getInt("codigoTipoCliente"),
                                          resultado.getString("descripcion")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return listaTipoCliente = FXCollections.observableArrayList(lista);
        
    }
    
    public void seleccionarElemento(){
        if(tblCliente.getSelectionModel().getSelectedItem()!= null){
           txtCodigoCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoCliente()));
           txtNombreCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getNombresCliente()));
           txtApellidoCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getApellidosCliente()));
           txtTelCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getTelefonoCliente()));
           txtDireccion.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getDireccionCliente()));
           txtEmail.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getEmail()));
           
           cmbCodLocal.getSelectionModel().select(buscarLocal(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoLocal()));
           cmbCodAdmin.getSelectionModel().select(buscarAdministracion(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
           cmbCodTipoCliente.getSelectionModel().select(buscarTipoCliente(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
        }else{
            JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento.");
        }
    }
    
    public Local buscarLocal(int codigoLocal){
        Local resultado = null;
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarLocales(?)}");
            procedure.setInt(1, codigoLocal);
            ResultSet registro = procedure.executeQuery();
            while(registro.next()){
                resultado = new Local(registro.getInt("codigoLocal"),
                                      registro.getDouble("saldoFavor"),
                                      registro.getDouble("saldoContra"),
                                      registro.getInt("mesesPendientes"),
                                      registro.getBoolean("disponibilidad"),
                                      registro.getDouble("valorLocal"),
                                      registro.getDouble("valorAdministracion"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
    }
    
    public Administracion buscarAdministracion(int codigoAdministracion){
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
    
    public TipoCliente buscarTipoCliente(int codigoTipoCliente){
        TipoCliente resultado = null;
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarTipoCliente(?)}");
            procedure.setInt(1, codigoTipoCliente);
            ResultSet registro = procedure.executeQuery();
            while(registro.next()){
                resultado = new TipoCliente(registro.getInt("codigoTipoCliente"),
                                            registro.getString("descripcion"));
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
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/joselopez/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/joselopez/images/Cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/joselopez/images/Agregar.png"));
                imgEliminar.setImage(new Image("/org/joselopez/images/Eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Cliente registro = new Cliente();
        registro.setNombresCliente(txtNombreCliente.getText());
        registro.setApellidosCliente(txtApellidoCliente.getText());
        registro.setTelefonoCliente(txtTelCliente.getText());
        registro.setDireccionCliente(txtDireccion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setCodigoLocal(((Local)cmbCodLocal.getSelectionModel().getSelectedItem()).getCodigoLocal());
        registro.setCodigoAdministracion(((Administracion)cmbCodAdmin.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        registro.setCodigoTipoCliente(((TipoCliente)cmbCodTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente());
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarTipoCliente(?,?,?,?,?,?,?,?)}");
            procedure.setString(1, registro.getNombresCliente());
            procedure.setString(2, registro.getApellidosCliente());
            procedure.setString(3, registro.getTelefonoCliente());
            procedure.setString(4, registro.getDireccionCliente());
            procedure.setString(5, registro.getEmail());
            procedure.setInt(6, registro.getCodigoLocal());
            procedure.setInt(7, registro.getCodigoAdministracion());
            procedure.setInt(8, registro.getCodigoTipoCliente());
            procedure.execute();
            cargarDatos();
        }catch (Exception e){
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
                if(tblCliente.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de querer eliminar este registro?", "Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarClientes(?)}");
                            procedure.setInt(1, ((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedure.execute();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un elemento");
                }
        }
    }
    
    public void editar(){
        activarControles();
        btnEditar.setText("Actualizar");
        switch(tipoOperacion){
            case NINGUNO:
                if(tblCliente.getSelectionModel().getSelectedItem() != null){
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
        Cliente registro = (Cliente)tblCliente.getSelectionModel().getSelectedItem();
        registro.setNombresCliente(txtNombreCliente.getText());
        registro.setApellidosCliente(txtApellidoCliente.getText());
        registro.setTelefonoCliente(txtTelCliente.getText());
        registro.setDireccionCliente(txtDireccion.getText());
        registro.setEmail(txtEmail.getText());
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarClientes(?,?,?,?,?,?)}");
            procedure.setInt(1, registro.getCodigoCliente());
            procedure.setString(2, registro.getNombresCliente());
            procedure.setString(3, registro.getApellidosCliente());
            procedure.setString(4,registro.getTelefonoCliente());
            procedure.setString(5, registro.getDireccionCliente());
            procedure.setString(6, registro.getEmail());
            procedure.execute();
            cargarDatos();
        }catch (Exception e){
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
                imgReporte.setImage(new Image("/org/joselopez/images/reporte.png"));
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigoCliente.setEditable(false);
        txtNombreCliente.setEditable(false);
        txtApellidoCliente.setEditable(false);
        txtTelCliente.setEditable(false);
        txtDireccion.setEditable(false);
        txtEmail.setEditable(false);
        cmbCodLocal.setDisable(true);
        cmbCodAdmin.setDisable(true);
        cmbCodTipoCliente.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoCliente.setEditable(false);
        txtNombreCliente.setEditable(true);
        txtApellidoCliente.setEditable(true);
        txtTelCliente.setEditable(true);
        txtDireccion.setEditable(true);
        txtEmail.setEditable(true);
        cmbCodLocal.setDisable(false);
        cmbCodAdmin.setDisable(false);
        cmbCodTipoCliente.setDisable(false);       
    }
    
    public void limpiarControles(){
        txtCodigoCliente.clear();
        txtNombreCliente.clear();
        txtApellidoCliente.clear();
        txtTelCliente.clear();
        txtDireccion.clear();
        txtEmail.clear();
        cmbCodLocal.getSelectionModel().clearSelection();
        cmbCodAdmin.getSelectionModel().clearSelection();
        cmbCodTipoCliente.getSelectionModel().clearSelection();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}
