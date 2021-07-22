/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 first_name last_name
 */
package ucf.assignments;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryStorageTest {

    private InventoryStorage storage;

    @Before
    public void setUp() throws Exception {
        storage = InventoryStorage.getTestInstance();
    }

    @Test
    public void addNewInventoryItem() {
        InventoryItem item = new InventoryItem("ABC456GHP6",5043, "Washing Machine");
        assertEquals("addNewInventoryItem error",true,storage.addNewInventoryItem(item));
    }

    @Test
    public void addNewInventoryItem_Existing() {
        InventoryItem item = new InventoryItem("AXB124AXY3",5043, "Washing Machine");
        assertEquals("addNewInventoryItem for existing searial no error",false,storage.addNewInventoryItem(item));
    }

    @Test
    public void updateInventoryItem() {
        InventoryItem item = new InventoryItem("AXB656AXY4",500, "Xbox One");
        assertEquals("updateInventoryItem error", true,storage.updateInventoryItem("AXB124AXY3",item));
    }

    @Test
    public void updateInventoryItem_NonExisting() {
        InventoryItem item = new InventoryItem("AXB656AXY4",500, "Xbox One");
        assertEquals("updateInventoryItem error", false,storage.updateInventoryItem("AXB124AX53",item));
    }

    @Test
    public void updateInventoryItem_Duplicate() {
        InventoryItem item = new InventoryItem("S40AZBDE47",500, "Xbox One");
        assertEquals("updateInventoryItem error", false,storage.updateInventoryItem("AXB124AX53",item));
    }

    @Test
    public void findInventoryItemBySerialNo_existing() {
        InventoryItem expected = new InventoryItem("S40AZBDE47", 599.99f, "Samsung TV");
        InventoryItem actual = storage.findInventoryItemBySerialNo("S40AZBDE47");
        assertEquals("findInventoryItemBySerialNo error", expected, actual);
    }

    @Test
    public void findInventoryItemByName_existing() {
        InventoryItem expected = new InventoryItem("S40AZBDE47",599.99f,"Samsung TV");
        InventoryItem actual = storage.findInventoryItemByName("Samsung TV");
        assertEquals("findByInventoryItemName error", expected,actual);
    }

    @Test
    public void findInventoryItemBySerialNo_nonExisting() {
        InventoryItem actual = storage.findInventoryItemBySerialNo("S40AZBDE48");
        assertEquals("findInventoryItemBySerialNo for non existing serial no error", null, actual);
    }

    @Test
    public void findInventoryItemByName_nonExisting() {
        InventoryItem actual = storage.findInventoryItemByName("Washing Machine");
        assertEquals("findByInventoryItemName for non existing name error", null,actual);
    }

    @Test
    public void removeInventoryItem() {
        InventoryItem item = new InventoryItem("AXB124AXY3",399, "Xbox One");
        storage.removeInventoryItem(item);
        assertEquals("Item Remove Error", false,storage.items.contains(item));
    }
}