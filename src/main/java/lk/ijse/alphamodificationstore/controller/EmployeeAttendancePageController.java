package lk.ijse.alphamodificationstore.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.alphamodificationstore.dto.EmployeeAttendanceDto;
import lk.ijse.alphamodificationstore.dto.Tm.EmployeeAttendanceTm;
import lk.ijse.alphamodificationstore.model.EmployeeAttendanceModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeAttendancePageController implements Initializable {
    public Label lblAttendanceId;
    public TextField txtNic;
    public TextField txtDate;
    public TextField txtAttendTime;
    public TextField txtDuration;

    public TableView<EmployeeAttendanceTm> tblAttendance;
    public TableColumn<EmployeeAttendanceTm , String> colId;
    public TableColumn<EmployeeAttendanceTm , String> colNic;
    public TableColumn<EmployeeAttendanceTm , String> colDate;
    public TableColumn<EmployeeAttendanceTm , String> colAttendTime;
    public TableColumn<EmployeeAttendanceTm , String> colDuration;

    private final EmployeeAttendanceModel employeeAttendanceModel = new EmployeeAttendanceModel();

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TextField searchField;

    private final String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";
    private final String timePattern = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
    private final String durationPattern = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
    public AnchorPane ancAttendancePage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("attendanceId"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("employeeNic"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAttendTime.setCellValueFactory(new PropertyValueFactory<>("attendTime"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblAttendance.setItems(FXCollections.observableArrayList(
                employeeAttendanceModel.getAllAttendance().stream()
                        .map(employeeAttendanceDto -> new EmployeeAttendanceTm(
                                employeeAttendanceDto.getAttendanceId(),
                                employeeAttendanceDto.getEmployeeNic(),
                                employeeAttendanceDto.getDate(),
                                employeeAttendanceDto.getAttendTime(),
                                employeeAttendanceDto.getDuration()
                        )).toList()
        ));
    }
    public void resetPage(){
        try {
            loadTableData();
            loadNextId();

            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            txtNic.setText(null);
            txtDate.setText(java.time.LocalDate.now().toString());
            txtAttendTime.setText(null);
            txtDuration.setText(null);
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {

        String attendanceId = lblAttendanceId.getText();
        String employeeNic = txtNic.getText();
        String date = txtDate.getText();
        String attendTime = txtAttendTime.getText();
        String duration = txtDuration.getText();

        boolean isValidNic = employeeNic.length() == 12 && employeeNic.charAt(9) == '-';
        boolean isValidDate = date.matches(datePattern);
        boolean isValidAttTime = attendTime.matches(timePattern);
        boolean isValidDuration = duration.matches(durationPattern);

        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;-fx-border-radius: 20;");
        txtDate.setStyle(txtDate.getStyle() + ";-fx-border-color: #7367F0; -fx-border-radius: 20;");
        txtAttendTime.setStyle(txtAttendTime.getStyle() + ";-fx-border-color: #7367F0;-fx-border-radius: 20;");
        txtDuration.setStyle(txtDuration.getStyle() + ";-fx-border-color: #7367F0;-fx-border-radius: 20;");

        if (!isValidNic) txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
        if (!isValidDate) txtDate.setStyle(txtDate.getStyle() + ";-fx-border-color: red;");
        if (!isValidAttTime) txtAttendTime.setStyle(txtAttendTime.getStyle() + ";-fx-border-color: red;");
        if (!isValidDuration) txtDuration.setStyle(txtDuration.getStyle() + ";-fx-border-color: red;");

        EmployeeAttendanceDto employeeAttendanceDto = new EmployeeAttendanceDto(
                attendanceId,
                employeeNic,
                date,
                attendTime,
                duration
        );

        if(isValidNic && isValidDate && isValidAttTime && isValidDuration){
            try {
                boolean isSaved = employeeAttendanceModel.saveAttendance(employeeAttendanceDto);

                if (isSaved){
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION,"Attendance Saved Successfully").show();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Save Failed").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Something went Wrong").show();
            }
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String attendanceId = lblAttendanceId.getText();
        String employeeNic = txtNic.getText();
        String date = txtDate.getText();
        String attendTime = txtAttendTime.getText();
        String duration = txtDuration.getText();

        EmployeeAttendanceDto employeeAttendanceDto = new EmployeeAttendanceDto(
                attendanceId,
                employeeNic,
                date,
                attendTime,
                duration
        );
        try {
            boolean isUpdated = employeeAttendanceModel.updateAttendance(employeeAttendanceDto);

            if (isUpdated){
                resetPage();
                new Alert(Alert.AlertType.INFORMATION,"Attendance Updated Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Update Failed").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went Wrong").show();
        }

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are You Sure ? ",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES){
            String attId = lblAttendanceId.getText();

            try {
                boolean isDeleted = employeeAttendanceModel.deleteAttendance(attId);
                if (isDeleted){
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION,"Attendance Deleted Successfully").show();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Delete Failed").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Something went Wrong").show();
            }
        }
    }

    private void loadNextId() throws ClassNotFoundException , SQLException{
        String nextId = employeeAttendanceModel.getNextAttendanceId();
        lblAttendanceId.setText(nextId);

    }
    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        EmployeeAttendanceTm selectedAttendance = tblAttendance.getSelectionModel().getSelectedItem();

        if (selectedAttendance != null){
            lblAttendanceId.setText(selectedAttendance.getAttendanceId());
            txtNic.setText(selectedAttendance.getEmployeeNic());
            txtDate.setText(selectedAttendance.getDate());
            txtAttendTime.setText(selectedAttendance.getAttendTime());
            txtDuration.setText(selectedAttendance.getDuration());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }


    private void navigateTo(String path) {
        try {
            ancAttendancePage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancAttendancePage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancAttendancePage.heightProperty());

            ancAttendancePage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()){
            try {
                loadTableData();
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Faild to load Attendance").show();
            }
        }else {
            try {
                ArrayList<EmployeeAttendanceDto> employeeDtoArrayList = employeeAttendanceModel.searchAttendane(searchText);
                tblAttendance.setItems(FXCollections.observableArrayList(
                        employeeDtoArrayList.stream()
                                .map(employeeAttendanceDto -> new EmployeeAttendanceTm(
                                        employeeAttendanceDto.getAttendanceId(),
                                        employeeAttendanceDto.getEmployeeNic(),
                                        employeeAttendanceDto.getDate(),
                                        employeeAttendanceDto.getAttendTime(),
                                        employeeAttendanceDto.getDuration()
                                )).toList()
                ));
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search attendance").show();
            }
        }
    }
    /*private void loadSearchResults(String nic) {
        try {
            ArrayList<EmployeeAttendanceDto> contacts = employeeAttendanceModel.getAttendanceDetailsFromNic(nic);
            if (contacts == null) {
                tblAttendance.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            } else {
                tblAttendance.setItems(FXCollections.observableArrayList(
                        employeeAttendanceModel.getAttendanceDetailsFromNic(nic).stream()
                                .map(employeeAttendanceDto -> new EmployeeAttendanceTm(
                                        employeeAttendanceDto.getAttendanceId(),
                                        employeeAttendanceDto.getEmployeeNic(),
                                        employeeAttendanceDto.getDate(),
                                        employeeAttendanceDto.getAttendTime(),
                                        employeeAttendanceDto.getDuration()
                                )).toList()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error when display results").show();
        }
    }
*/
    public void goToDashboard(MouseEvent mouseEvent) {
        navigateTo("/view/DashboardPage.fxml");
    }
}
