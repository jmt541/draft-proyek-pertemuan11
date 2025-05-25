package com.example.bdsqltester.scenes.user;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BuatPesananController {

    @FXML
    private TextField namaField;

    @FXML
    private ComboBox<String> menuComboBox;

    @FXML
    private TextField jumlahField;

    @FXML
    private void initialize() {
        // Dummy data menu
        menuComboBox.getItems().addAll("Nasi Goreng", "Mie Ayam", "Sate Ayam", "Bakso");
    }

    @FXML
    private void handleSubmit() {
        String nama = namaField.getText();
        String menu = menuComboBox.getValue();
        String jumlah = jumlahField.getText();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Pesanan Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("Pesanan atas nama " + nama + " berhasil dibuat:\nMenu: " + menu + "\nJumlah: " + jumlah);
        alert.showAndWait();
    }
}
