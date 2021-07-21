package ucf.assignments;

import com.google.gson.Gson;
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
import java.util.ResourceBundle;

public class InventoryController implements Initializable {



    enum ExportType {
        TSV,
        HTML,
        JSON;
    }

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

    public InventoryController() {
        storage = InventoryStorage.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        storage.addSampleData();
        colValue.setCellValueFactory(param ->
                new SimpleStringProperty(String.format("$%.2f",param.getValue().getValue())));
        colSerialNo.setCellValueFactory(new PropertyValueFactory<>("serialNo"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryItemsTable.setItems(storage.getAllItems());
        inventoryItemsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void onClickExportMenu(ActionEvent e) {
        Object src = e.getSource();
        if (itemTSV == src) {
            export(ExportType.TSV);
        }
        else if (itemHTML == src) {
            export(ExportType.HTML);
        }
        else if (itemJSON == src) {
            export(ExportType.JSON);
        }
    }

    public void onClickClear(ActionEvent e) {
        inputSerialNo.setText(null);
        inputName.setText(null);
        inputValue.setText(null);
    }

    public void onClickSave() {
        if (validateOrShowErrorMessage()) {
            InventoryItem item = new InventoryItem();
            item.setSerialNo(inputSerialNo.getText());
            item.setName(inputName.getText());
            item.setValue(Float.valueOf(inputValue.getText()));
            if (!storage.addNewInventoryItem(item)) {
                showDialog("Save Error",
                        "Another item with same serial no already exists",
                        Alert.AlertType.ERROR);
            }
        }
    }

    public void onSearchInventoryItem(ActionEvent e) {
        Object src = e.getSource();
        if (btnSearchName == src) {

        }
        else if (btnSearchSerialNo == src) {

        }
    }

    public void onClickDeleteInventoryItem() {
        // TODO: implement delete item
    }

    public void onClickEditInventoryItem() {
        // TODO: implement edit item
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

    private void export(ExportType type) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Inventory Data");
        if (ExportType.TSV == type) {
            chooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("TSV","txt"));
        }
        else if (ExportType.HTML == type) {
            chooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("HTML","html"));
        }
        else {
            chooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("JSON","json"));
        }
        File selected = chooser.showSaveDialog(App.getMainWindow());
        if (null == selected) return;
        try (FileWriter writer = new FileWriter(selected)) {
            List<InventoryItem> items = storage.getAllItems();
            if (ExportType.TSV == type) {
                exportToTSV(writer,items);
            }
            else if (ExportType.HTML == type) {
                exportToHTML(writer,items);
            }
            else {
                exportToJSON(writer,items);
            }
        }
        catch (Exception e) {
            showDialog("Export Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void exportToTSV(Writer writer, List<InventoryItem> items) {
        PrintWriter pw = new PrintWriter(writer);
        for (InventoryItem item : items) {
            pw.println(item.getSerialNo()+"\t"+item.getName()+"\t"+String.format("$%.2f",item.getValue()));
        }
    }

    private void exportToHTML(Writer writer, List<InventoryItem> items) {
        PrintWriter pw = new PrintWriter(writer);
        pw.println("<html><body><table>");
        pw.println("<tr><td>Serial No.</td><td>Name</td><td>Value</td></tr>");
        for (InventoryItem item : items) {
            pw.println("<tr>");
            pw.println("<td>"+item.getSerialNo()+"</td>");
            pw.println("<td>"+item.getName()+"</td>");
            pw.println(String.format("<td>$%.2f</td>",item.getValue()));
            pw.println("</td>");
            pw.println("</tr>");
        }
        pw.println("</table></body></html>");
    }

    private void exportToJSON(Writer writer, List<InventoryItem> item) {
        Gson gson = new Gson();
        gson.toJson(item,writer);
    }

    private boolean isEmptyString(String s) {
        return null == s || s.isEmpty();
    }

    private void showDialog(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
