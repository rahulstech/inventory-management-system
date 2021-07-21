package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryStorage {

    private static InventoryStorage mInstance;

    private ObservableList<InventoryItem> items = FXCollections.observableArrayList();

    public static InventoryStorage getInstance() {
        if (null == mInstance) {
            mInstance = new InventoryStorage();
        }
        return mInstance;
    }

    void addSampleData() {
        //$399.00 AXB124AXY3 Xbox One
        //$599.99 S40AZBDE47 Samsung TV

        InventoryItem xbox = new InventoryItem("AXB124AXY3",399, "Xbox One");
        InventoryItem tv = new InventoryItem("S40AZBDE47",599.99f,"Samsung TV");

        addNewInventoryItem(xbox);
        addNewInventoryItem(tv);

    }

    public boolean addNewInventoryItem(InventoryItem item) {
        if (null != findInventoryItemBySerialNo(item.getSerialNo()))
            return false;
        items.add(item);
        return true;
    }

    public InventoryItem findInventoryItemBySerialNo(String serialNo) {
        for (InventoryItem item : items) {
            if (item.getSerialNo().equals(serialNo)) {
                return item;
            }
        }
        return null;
    }

    public void removeInventoryItem(String serialNo) {
        InventoryItem item = findInventoryItemBySerialNo(serialNo);
        items.remove(item);
    }

    public ObservableList<InventoryItem> getAllItems() {
        return items;
    }
}
