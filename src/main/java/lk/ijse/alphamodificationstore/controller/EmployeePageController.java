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
import lk.ijse.alphamodificationstore.dto.EmployeeDto;
import lk.ijse.alphamodificationstore.dto.Tm.EmployeeTm;
import lk.ijse.alphamodificationstore.model.EmployeeModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeePageController implements Initializable {

    public AnchorPane ancEmployeePage;
    public TextField searchField;
    public Label lblEmpId;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtAddress;
    public TextField txtAge;
    public TextField txtSalary;
    public TextField txtNic;

    public TableView<EmployeeTm> tblEmployee;
    public TableColumn<EmployeeTm , String> colId;
    public TableColumn<EmployeeTm , String> colName;
    public TableColumn<EmployeeTm , String> colContact;
    public TableColumn<EmployeeTm , String> colAddress;
    public TableColumn<EmployeeTm , String> colNic;
    public TableColumn<EmployeeTm , Integer> colAge;
    public TableColumn<EmployeeTm , Double> colSalary;

    private final EmployeeModel employeeModel = new EmployeeModel();

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    private final String agePattern = "^(\\d+)$";
    private final String salaryPattern = "^(\\d+)$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("employeeContact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("employeeAddress"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("employeeNic"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("employeeAge"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong, please try again").show();
        }
    }
    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblEmployee.setItems(FXCollections.observableArrayList(
                employeeModel.getAllEmployee().stream()
                        .map(employeeDto -> new EmployeeTm(
                                employeeDto.getEmployeeId(),
                                employeeDto.getEmployeeName(),
                                employeeDto.getEmployeeContact(),
                                employeeDto.getEmployeeAddress(),
                                employeeDto.getEmployeeNic(),
                                employeeDto.getEmployeeAge(),
                                employeeDto.getSalary()
                        )).toList()
        ));
    }
    private void resetPage() {
        try {
            loadTableData();
            loadNextId();

            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            txtName.setText(null);
            txtContact.setText(null);
            txtAddress.setText(null);
            txtAge.setText(null);
            txtSalary.setText(null);
        }catch (Exception e){
            e.printStackTrace();
            new  Alert(Alert.AlertType.ERROR,"Something went wrong, please try again").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String employeeId = lblEmpId.getText();
        String employeeName = txtName.getText();
        String employeeContact = txtContact.getText();
        String employeeAddress = txtAddress.getText();
        String employeeNic = txtNic.getText();
        String employeeAge = txtAge.getText();
        String employeeSalary = txtSalary.getText();

        boolean isValidContact = employeeContact.length() == 10;
        boolean isValidNic = employeeNic.length() == 12 && employeeNic.charAt(9) == '-';
        boolean isValidAge = employeeAge.matches(agePattern);
        boolean isValidSalary = employeeSalary.matches(salaryPattern);

        txtName.setStyle(txtName.getStyle()+";-fx-border-color: #7367F0;");
        txtContact.setStyle(txtContact.getStyle()+";-fx-border-color: #7367F0");
        txtAddress.setStyle(txtAddress.getStyle()+";-fx-border-color: #7367F0");
        txtNic.setStyle(txtNic.getStyle()+";-fx-border-color: #7367F0");
        txtAge.setStyle(txtAge.getStyle()+";-fx-border-color: #7367F0");
        txtSalary.setStyle(txtSalary.getStyle()+";-fx-border-color: #7367F0");

        if (!isValidContact) txtContact.setStyle(txtContact.getStyle()+";-fx-border-color: red;");
        if (!isValidNic) txtNic.setStyle(txtNic.getStyle()+";-fx-border-color: red;");
        if (!isValidAge) txtAge.setStyle(txtAge.getStyle()+";-fx-border-color: red;");
        if (!isValidSalary) txtSalary.setStyle(txtSalary.getStyle()+";-fx-border-color: red;");

        int parsedAge = Integer.parseInt(employeeAge);
        double parsedSalary = Double.parseDouble(employeeSalary);

        EmployeeDto employeeDto = new EmployeeDto(
                employeeId,
                employeeName,
                employeeContact,
                employeeAddress,
                employeeNic,
                parsedAge,
                parsedSalary
        );
        if (isValidContact && isValidAge && isValidSalary) {
            try {
                boolean isSaved = employeeModel.saveEmployee(employeeDto);

                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION,"Employee Saved Successfully").show();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Employee Save failed").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Save failed").show();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String employeeId = lblEmpId.getText();
        String employeeName = txtName.getText();
        String employeeContact = txtContact.getText();
        String employeeAddress = txtAddress.getText();
        String employeeNic = txtNic.getText();
        String employeeAge = txtAge.getText();
        String employeeSalary = txtSalary.getText();

        int parsedAge = Integer.parseInt(employeeAge);
        double parsedSalary = Double.parseDouble(employeeSalary);

        EmployeeDto employeeDto = new EmployeeDto(
                employeeId,
                employeeName,
                employeeContact,
                employeeAddress,
                employeeNic,
                parsedAge,
                parsedSalary
        );

        try {
            boolean isUpdated = employeeModel.updateEmployee(employeeDto);

            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION,"Employee Updated Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Employee Update failed").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Update failed").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are You Sure ? ",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> response = alert.showAndWait();

        if(response.isPresent() && response.get() == ButtonType.YES){
            String empId = lblEmpId.getText();
            try {
                boolean isDeleted = employeeModel.deleteEmployee(empId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION,"Employee Deleted Successfully").show();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Employee Delete failed").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Delete failed").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = employeeModel.getNextEmployeeId();
        lblEmpId.setText(nextId);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        EmployeeTm selectedItem = tblEmployee.getSelectionModel().getSelectedItem();

        lblEmpId.setText(selectedItem.getEmployeeId());
        txtName.setText(selectedItem.getEmployeeName());
        txtContact.setText(selectedItem.getEmployeeContact());
        txtAddress.setText(selectedItem.getEmployeeAddress());
        txtNic.setText(selectedItem.getEmployeeNic());
        txtAge.setText(String.valueOf(selectedItem.getEmployeeAge()));
        txtSalary.setText(String.valueOf(selectedItem.getSalary()));

        btnSave.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()){
            try {
                loadTableData();
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Faild to load Customers").show();
            }
        }else {
            try {
                ArrayList<EmployeeDto> customerList = employeeModel.searchEmployee(searchText);
                tblEmployee.setItems(FXCollections.observableArrayList(
                        customerList.stream()
                                .map(employeeDto -> new EmployeeTm(
                                        employeeDto.getEmployeeId(),
                                        employeeDto.getEmployeeName(),
                                        employeeDto.getEmployeeContact(),
                                        employeeDto.getEmployeeAddress(),
                                        employeeDto.getEmployeeNic(),
                                        employeeDto.getEmployeeAge(),
                                        employeeDto.getSalary()
                                )).toList()
                ));
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search customers").show();
            }
        }
    }
    /*private void loadSearchResults(String phoneNum) {
        try {
            ArrayList<EmployeeDto> contacts = employeeModel.getEmployeeDetailsFromContact(phoneNum);
            if (contacts == null) {
                tblEmployee.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            } else {
                tblEmployee.setItems(FXCollections.observableArrayList(
                        employeeModel.getEmployeeDetailsFromContact(phoneNum).stream()
                                .map(employeeDto -> new EmployeeTm(
                                        employeeDto.getEmployeeId(),
                                        employeeDto.getEmployeeName(),
                                        employeeDto.getEmployeeContact(),
                                        employeeDto.getEmployeeAddress(),
                                        employeeDto.getEmployeeNic(),
                                        employeeDto.getEmployeeAge(),
                                        employeeDto.getSalary()
                                )).toList()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error when display results").show();
        }
    }*/

    public void goToDashboard(MouseEvent mouseEvent) {
        navigateTo("/view/DashboardPage.fxml");
    }

    private void navigateTo(String path) {
        try {
            ancEmployeePage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancEmployeePage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancEmployeePage.heightProperty());

            ancEmployeePage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void goToEmployeeAttendancePage(MouseEvent mouseEvent) {
        navigateTo("/view/EmployeeAttendancePage.fxml");
    }
}
