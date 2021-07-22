package ucf.assignments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImportExportHelperTest {

    private List<InventoryItem> items;

    @BeforeEach
    public void start() {
        items = new ArrayList<>();
        InventoryItem xbox = new InventoryItem("AXB124AXY3",399, "Xbox One");
        InventoryItem tv = new InventoryItem("S40AZBDE47",599.99f,"Samsung TV");

        items.add(xbox);
        items.add(tv);
    }


    @ParameterizedTest
    @MethodSource
    public void importFrom(String test, File file) throws Exception {
        List<InventoryItem> loaded = ImportExportHelper.importFrom(file);
        assertEquals(items,loaded,test);
    }

    public static Stream<Arguments> importFrom() {
        return Stream.of(
                Arguments.of("TSV Import", new File("sample/inventory.txt")),
                Arguments.of("HTML Import", new File("sample/inventory.html")),
                Arguments.of("HTML Import", new File("sample/inventory.json"))
        );
    }
}