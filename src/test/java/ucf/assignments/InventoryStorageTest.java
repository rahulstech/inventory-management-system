/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 first_name last_name
 */
package ucf.assignments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryStorageTest {

    private InventoryStorage storage;

    @BeforeEach
    public void setUp() throws Exception {
        storage = InventoryStorage.getTestInstance();
    }

    @ParameterizedTest
    @MethodSource
    public void addNewInventoryItem(String name, InventoryItem item, boolean expected) {
        assertEquals(expected, storage.addNewInventoryItem(item), name);
    }

    public static Stream<Arguments> addNewInventoryItem() {
        return Stream.of(
                Arguments.of(
                        "Non Existing Item",
                        new InventoryItem("ABC456GHP6",5043, "Washing Machine"),
                        true
                ),
                Arguments.of(
                        "Existing Item",
                        new InventoryItem("AXB124AXY3",5043, "Washing Machine"),
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    public void updateInventoryItem(String name, String serialNo, InventoryItem item, boolean expected) {
        assertEquals(expected,storage.updateInventoryItem(serialNo,item),name);
    }

    public static Stream<Arguments> updateInventoryItem() {
        return Stream.of(
                Arguments.of("Existing", "AXB124AXY3", new InventoryItem("AXB656AXY4",500, "Xbox One"), true),
                Arguments.of("Non Existing", "AXB656AXY4",  new InventoryItem("AXB656AXY4",500, "Xbox One"), false),
                Arguments.of("Duplicate", "AXB124AXY3", new InventoryItem("S40AZBDE47",500, "Xbox One"),false)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void findInventoryItemBySerialNo(String name, String serialNo, InventoryItem expected) {
        assertEquals(expected,storage.findInventoryItemBySerialNo(serialNo),name);
    }

    public static Stream<Arguments> findInventoryItemBySerialNo() {
        return Stream.of(
                Arguments.of("Existing Serial No","S40AZBDE47",new InventoryItem("S40AZBDE47", 599.99f, "Samsung TV")),
                Arguments.of("Non Existing Serial No", "S40AZBDE48", null)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void findInventoryItemByName(String name, String itemName, InventoryItem expected) {
        assertEquals(expected,storage.findInventoryItemByName(itemName),name);
    }

    public static Stream<Arguments> findInventoryItemByName() {
        return Stream.of(
                Arguments.of("Existing Name","Samsung TV",new InventoryItem("S40AZBDE47",599.99f,"Samsung TV")),
                Arguments.of("Non Existing Name", "Washing Machine", null)
        );
    }

    @Test
    public void removeInventoryItem() {
        InventoryItem item = new InventoryItem("AXB124AXY3",399, "Xbox One");
        storage.removeInventoryItem(item);
        assertEquals(false,storage.items.contains(item),"Item Remove Error");
    }
}