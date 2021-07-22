package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class InventoryStorage {

    private static InventoryStorage mInstance;

    ObservableList<InventoryItem> items = FXCollections.observableArrayList();

    private InventoryStorage() {}

    public static InventoryStorage getInstance() {
        if (null == mInstance) {
            mInstance = new InventoryStorage();
        }
        return mInstance;
    }

    static InventoryStorage getTestInstance() {
        InventoryStorage storage = new InventoryStorage();
        storage.addSampleData();
        return storage;
    }

    void addSampleData() {
        InventoryItem xbox = new InventoryItem("AXB124AXY3",399, "Xbox One");
        InventoryItem tv = new InventoryItem("S40AZBDE47",599.99f,"Samsung TV");

        items.add(xbox);
        items.add(tv);
    }

    public boolean addNewInventoryItem(InventoryItem item) {
        if (!items.contains(item)) {
            items.add(item);
            return true;
        }
        return false;
    }

    public boolean updateInventoryItem(String serialNo, InventoryItem item) {
        if (!serialNo.equals(item.getSerialNo())
                && items.contains(item)) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            if (serialNo.equals(items.get(i).getSerialNo())) {
                items.set(i,item);
                return true;
            }
        }
        return false;
    }

    public InventoryItem findInventoryItemBySerialNo(String serialNo) {
        for (InventoryItem item : items) {
            if (item.getSerialNo().equals(serialNo)) {
                return item;
            }
        }
        return null;
    }

    public InventoryItem findInventoryItemByName(String name) {
        for (InventoryItem item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public void removeInventoryItem(InventoryItem item) {
        items.remove(item);
    }

    public ObservableList<InventoryItem> getAllItems() {
        return items;
    }

    public void addAllItems(List<InventoryItem> items) {
        for (InventoryItem item : items) {
            addNewInventoryItem(item);
        }
    }
}
