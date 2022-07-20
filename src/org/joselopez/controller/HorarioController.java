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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.joselopez.bean.Horario;
import org.joselopez.db.Conexion;
import org.joselopez.system.Principal;


public class HorarioController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    
    private ObservableList<Horario> listaHorario;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private TextField txtCodigoHorario;
    @FXML private TextField txtEntrada;
    @FXML private TextField txtSalida;
    @FXML private TextField txtLunes;
    @FXML private TextField txtMartes;
    @FXML private TextField txtMiercoles;
    @FXML private TextField txtJueves;
    @FXML private TextField txtViernes;
    
    @FXML private TableView tblHorario;
    
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblHorario.setItems(getHorario());
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codigoHorario"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("viernes"));
    }
    
    public ObservableList<Horario> getHorario(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarHorarios()}");
            ResultSet resultado = procedure.executeQuery();
            while(resultado.next()){
                lista.add(new Horario(resultado.getInt("codigoHorario"),
                                      resultado.getString("horarioEntrada"),
                                      resultado.getString("horarioSalida"),
                                      resultado.getBoolean("lunes"),
                                      resultado.getBoolean("martes"),
                                      resultado.getBoolean("miercoles"),
                                      resultado.getBoolean("jueves"),
                                      resultado.getBoolean("viernes")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaHorario = FXCollections.observableArrayList(lista);
        
    }
    
    public void seleccionarElemento(){
        if(tblHorario.getSelectionModel().getSelectedItem() != null){
            txtCodigoHorario.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).getCodigoHorario()));
            txtEntrada.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).getHorarioEntrada()));
            txtSalida.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).getHorarioSalida()));
            txtLunes.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isLunes()));
            txtMartes.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isMartes()));
            txtMiercoles.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isMiercoles()));
            txtJueves.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isJueves()));
            txtViernes.setText(String.valueOf(((Horario)tblHorario.getSelectionModel().getSelectedItem()).isViernes()));
        }else{
            JOptionPane.showMessageDialog(null, "Por favor, selecciona un elemento");
        }
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image ("/org/joselopez/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/joselopez/images/Cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
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
        Horario registro = new Horario();
        registro.setHorarioEntrada(txtEntrada.getText());
        registro.setHorarioSalida(txtSalida.getText());
        registro.setLunes(Boolean.valueOf(txtLunes.getText()));
        registro.setMartes(Boolean.valueOf(txtMartes.getText()));
        registro.setMiercoles(Boolean.valueOf(txtMiercoles.getText()));
        registro.setJueves(Boolean.valueOf(txtJueves.getText()));
        registro.setViernes(Boolean.valueOf(txtViernes.getText()));
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarHorarios(?,?,?,?,?,?,?)}");
            procedure.setString(1, registro.getHorarioEntrada());
            procedure.setString(2, registro.getHorarioSalida());
            procedure.setBoolean(3, registro.isLunes());
            procedure.setBoolean(4, registro.isMartes());
            procedure.setBoolean(5, registro.isMiercoles());
            procedure.setBoolean(6, registro.isJueves());
            procedure.setBoolean(7, registro.isViernes());
            procedure.execute();
            listaHorario.add(registro);
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
                imgNuevo.setImage(new Image("/org/joselopez/images/Agregar.png"));
                imgEliminar.setImage(new Image("/org/joselopez/images/Eliminar.png"));
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblHorario.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de querer eliminar el registro?", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarHorarios(?)}");
                            procedure.setInt(1, ((Horario)tblHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            procedure.execute();
                            listaHorario.remove(tblHorario.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showConfirmDialog(null, "Debes seleccionar un elemento");
                }
        }
    }
    
    public void editar(){
        activarControles();
        btnEditar.setText("Actualizar");
        switch(tipoOperacion){
            case NINGUNO:
                if(tblHorario.getSelectionModel().getSelectedItem()  != null){
                    btnEditar.setText("Actulizar");
                    btnReporte.setText("Reporte");
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
        Horario registro = (Horario)tblHorario.getSelectionModel().getSelectedItem();
        registro.setHorarioEntrada(txtEntrada.getText());
        registro.setHorarioSalida(txtSalida.getText());
        registro.setLunes(Boolean.valueOf(txtLunes.getText()));
        registro.setMartes(Boolean.valueOf(txtMartes.getText()));
        registro.setMiercoles(Boolean.valueOf(txtMiercoles.getText()));
        registro.setJueves(Boolean.valueOf(txtJueves.getText()));
        registro.setViernes(Boolean.valueOf(txtViernes.getText()));
        try{
            PreparedStatement procedure = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarHorarios(?,?,?,?,?,?,?,?)}");
            procedure.setInt(1, registro.getCodigoHorario());
            procedure.setString(2, registro.getHorarioEntrada());
            procedure.setString(3, registro.getHorarioSalida());
            procedure.setBoolean(4, registro.isLunes());
            procedure.setBoolean(5, registro.isMartes());
            procedure.setBoolean(6, registro.isMiercoles());
            procedure.setBoolean(7, registro.isJueves());
            procedure.setBoolean(8, registro.isViernes());
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
                imgReporte.setImage(new Image("/org/joselopez/images/reporte.png"));
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigoHorario.setEditable(false);
        txtEntrada.setEditable(false);
        txtSalida.setEditable(false);
        txtLunes.setEditable(false);
        txtMartes.setEditable(false);
        txtMiercoles.setEditable(false);
        txtJueves.setEditable(false);
        txtViernes.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoHorario.setEditable(false);
        txtEntrada.setEditable(true);
        txtSalida.setEditable(true);
        txtLunes.setEditable(true);
        txtMartes.setEditable(true);
        txtMiercoles.setEditable(true);
        txtJueves.setEditable(true);
        txtViernes.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoHorario.clear();
        txtEntrada.clear();
        txtSalida.clear();
        txtLunes.clear();
        txtMartes.clear();
        txtMiercoles.clear();
        txtJueves.clear();
        txtViernes.clear();
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
