/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 first_name last_name
 */
package ucf.assignments;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static ucf.assignments.ImportExportHelper.Type.*;

public class InventoryController implements Initializable {

    public Label lblInventoryItemInput;
    public TextField inputSerialNo;
    public TextField inputName;
    public TextField inputValue;
    public TableView<InventoryItem> inventoryItemsTable;
    public TableColumn<InventoryItem,String> colValue;
    public TableColumn<InventoryItem,String> colSerialNo;
    public TableColumn<InventoryItem,String> colName;
    public TextArea inputSearch;
    public Button btnSearchName;
    public Button btnSearchSerialNo;
    public MenuItem itemTSV;
    public MenuItem itemHTML;
    public MenuItem itemJSON;

    private InventoryStorage storage;
    private Optional<InventoryItem> selection = Optional.empty();

    public InventoryController() {
        storage = InventoryStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colValue.setCellValueFactory(param ->
                new SimpleStringProperty(String.format("$%.2f",param.getValue().getValue())));
        colSerialNo.setCellValueFactory(new PropertyValueFactory<>("serialNo"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryItemsTable.setItems(storage.getAllItems());
        inventoryItemsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void onClickLoadItems() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Source File");
        chooser.getExtensionFilters().addAll(
                getExtensionFilter(TSV),
                getExtensionFilter(HTML),
                getExtensionFilter(JSON));
        File selected = chooser.showOpenDialog(App.getMainWindow());
        if (null == selected) return;
        try {
            List<InventoryItem> items = ImportExportHelper.importFrom(selected);
            storage.addAllItems(items);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog("Import Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onClickExportMenu(ActionEvent e) {
        Object src = e.getSource();
        if (itemTSV == src) {
            export(TSV);
        }
        else if (itemHTML == src) {
            export(HTML);
        }
        else if (itemJSON == src) {
            export(JSON);
        }
    }

    public void onClickCancel() {
        inputSerialNo.setText(null);
        inputName.setText(null);
        inputValue.setText(null);
        selection = Optional.empty();
    }

    public void onClickSave() {
        if (validateOrShowErrorMessage()) {
            InventoryItem item = new InventoryItem();
            item.setSerialNo(inputSerialNo.getText());
            item.setName(inputName.getText());
            item.setValue(Float.parseFloat(inputValue.getText()));

            boolean saved;
            if (selection.isEmpty()) {
                saved = storage.addNewInventoryItem(item);
            }
            else {
                InventoryItem selected = selection.get();
                saved = storage.updateInventoryItem(selected.getSerialNo(),item);
            }
            if (!saved) {
                showDialog("Save Error",
                        "Another item with same serial no already exists",
                        Alert.AlertType.ERROR);
            }
            else {
                selection = Optional.empty();
            }
        }
    }

    public void onSearchInventoryItem(ActionEvent e) {
        Object src = e.getSource();
        String searchPhrase = inputSearch.getText();
        InventoryItem item;
        if (btnSearchName == src) {
            item = storage.findInventoryItemByName(searchPhrase);
        }
        else {
            item = storage.findInventoryItemBySerialNo(searchPhrase);
        }
        if (null == item) {
            showDialog("Search Inventory Item", "No inventory item found for the given search", Alert.AlertType.ERROR);
        }
        else {
            String message = "SERIAL NO.: "+item.getSerialNo()+
                    "\nNAME: "+item.getName()+
                    "\nVALUE: "+ String.format("%.2f",item.getValue());
            showDialog("Search Inventory Item",message, Alert.AlertType.INFORMATION);
        }
    }

    public void onClickDeleteInventoryItem() {
        InventoryItem item = inventoryItemsTable.getSelectionModel()
                .getSelectedItem();
        storage.removeInventoryItem(item);
    }

    public void onClickEditInventoryItem() {
        InventoryItem item = inventoryItemsTable.getSelectionModel()
                .getSelectedItem();
        inputSerialNo.setText(item.getSerialNo());
        inputName.setText(item.getName());
        inputValue.setText(String.format("%.2f",item.getValue()));
        selection = Optional.of(item);
    }

    private boolean validateOrShowErrorMessage() {
        String serialNo = inputSerialNo.getText();
        String value = inputValue.getText();
        String name = inputName.getText();

        List<String> messages = new ArrayList<>();

        if (isEmptyString(serialNo)) {
            messages.add("no serial no provided");
        }
        else if (!serialNo.matches("[\\d\\S]{10}")){
            messages.add("Invalid serial no");
        }
        if (isEmptyString(name)) {
            messages.add("no name provided");
        }
        else if (name.length() < 2 || name.length() > 256) {
            messages.add("name must be between 2 and 256 (inclusive) characters long");
        }
        if (isEmptyString(value)) {
            messages.add("no value provided");
        }
        else if (!value.matches("\\d+(\\.\\d+)?")){
            messages.add("value is not a valid number");
        }

        if (!messages.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String m : messages) {
                builder.append(m).append("\n");
            }
            showDialog("Inventory Input Error",builder.toString(), Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void export(ImportExportHelper.Type type) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Inventory Data");
        FileChooser.ExtensionFilter filter = getExtensionFilter(type);
        chooser.getExtensionFilters().add(filter);
        File selected = chooser.showSaveDialog(App.getMainWindow());
        if (null == selected) return;
        try {
            ImportExportHelper.exportTo(selected,type,storage.getAllItems());
        }
        catch (Exception e) {
            showDialog("Export Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private FileChooser.ExtensionFilter getExtensionFilter(ImportExportHelper.Type type) {
        if (TSV == type) {
            return new FileChooser.ExtensionFilter(type.getDescription(),type.getExtension());
        }
        else if (HTML == type) {
            return new FileChooser.ExtensionFilter(type.getDescription(),type.getExtension());
        }
        else {
            return new FileChooser.ExtensionFilter(type.getDescription(),type.getExtension());
        }
    }

    private boolean isEmptyString(String s) {
        return null == s || s.isEmpty();
    }

    private void showDialog(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }
}
