/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 first_name last_name
 */
package ucf.assignments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportExportHelper {

    public enum Type {
        TSV("TSV (Tab Separated Vale)","*.txt"),
        HTML("HTML","*.html"),
        JSON("JSON","*.json");

        private String description;
        private String extension;

        Type(String description, String extension) {
            this.description = description;
            this.extension = extension;
        }

        public String getDescription() {
            return description;
        }

        public String getExtension() {
            return extension;
        }
    }

    public static List<InventoryItem> importFrom(File file) throws Exception {
        String name = file.getName();
        Type type;
        if (name.endsWith("txt")) {
            type = Type.TSV;
        }
        else if (name.endsWith("html")) {
            type = Type.HTML;
        }
        else if (name.endsWith("json")){
            type = Type.JSON;
        }
        else {
            type = null;
        }
        try (FileReader reader = new FileReader(file)){
            switch (type) {
                case TSV: return importFromTSV(reader);
                case HTML: return importFromHTML(reader);
                case JSON: return importFromJSON(reader);
            }
        }
        return Collections.emptyList();
    }

    public static void exportTo(File file, Type type, List<InventoryItem> items) throws Exception {
        try (FileWriter writer = new FileWriter(file)) {
            switch (type) {
                case TSV: exportToTSV(writer,items);
                break;
                case HTML: exportToHTML(writer,items);
                break;
                case JSON: exportToJSON(writer,items);
            }
        }
    }

    private static List<InventoryItem> importFromTSV(Reader reader) throws IOException {
        List<InventoryItem> items = new ArrayList<>();
        BufferedReader buff = new BufferedReader(reader);
        String line;
        buff.readLine(); // ignore header
        while ((line = buff.readLine()) != null) {
            String[] parts = line.split("\t");
            InventoryItem item = new InventoryItem(parts[0],Float.parseFloat(parts[2]),parts[1]);
            items.add(item);
        }
        return items;
    }

    private static List<InventoryItem> importFromHTML(Reader reader) throws Exception{
        List<InventoryItem> items = new ArrayList<>();
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(reader));
        Node table = document.getElementsByTagName("table").item(0);
        if (table.hasChildNodes()) {
            NodeList rows = table.getChildNodes();
            int rCount = 0;
            for (int r = 0; r < rows.getLength(); r++) {
                Node row = rows.item(r);
                if (row.getNodeName().equals("tr")) {
                    rCount++;
                    if (1 == rCount) continue;
                    NodeList cols = row.getChildNodes();
                    int cCount = 0;
                    InventoryItem item = new InventoryItem();
                    for (int c = 0; c < cols.getLength(); c++) {
                        Node col = cols.item(c);
                        if (col.getNodeName().equals("td")) {
                            switch (cCount) {
                                case 0: {
                                    item.setSerialNo(col.getTextContent());
                                }
                                break;
                                case 1: {
                                    item.setName(col.getTextContent());
                                }
                                break;
                                case 2: {
                                    item.setValue(Float.parseFloat(col.getTextContent()));
                                }
                            }
                            cCount++;
                        }
                    }
                    items.add(item);
                }
            }
        }
        return items;
    }

    private static List<InventoryItem> importFromJSON(Reader reader ) {
        Gson gson = new Gson();
        return gson.fromJson(reader,
                TypeToken.getParameterized(List.class,InventoryItem.class).getType());
    }

    private static void exportToTSV(Writer writer, List<InventoryItem> items) {
        PrintWriter pw = new PrintWriter(writer);
        pw.println("Serial Number\tName\tValue");
        for (InventoryItem item : items) {
            pw.println(item.getSerialNo()+"\t"+item.getName()+"\t"+String.format("%.2f",item.getValue()));
        }
    }

    private static void exportToHTML(Writer writer, List<InventoryItem> items) throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element html = document.createElement("html");
        Element body = document.createElement("body");
        Element table = document.createElement("table");

        Element tr;
        Element serialNo;
        Element name;
        Element value;

        tr = document.createElement("tr");
        serialNo = document.createElement("td");
        name = document.createElement("td");
        value = document.createElement("td");
        serialNo.setTextContent("Serial Number");
        name.setTextContent("Name");
        value.setTextContent("Value");
        tr.appendChild(serialNo);
        tr.appendChild(name);
        tr.appendChild(value);
        table.appendChild(tr);

        for (InventoryItem item : items) {
            tr = document.createElement("tr");
            serialNo = document.createElement("td");
            name = document.createElement("td");
            value = document.createElement("td");
            serialNo.setTextContent(item.getSerialNo());
            name.setTextContent(item.getName());
            value.setTextContent(String.format("%.2f",item.getValue()));
            tr.appendChild(serialNo);
            tr.appendChild(name);
            tr.appendChild(value);
            table.appendChild(tr);
        }
        body.appendChild(table);
        html.appendChild(body);
        document.appendChild(html);

        DOMImplementationLS domImpl = ((DOMImplementationLS) DOMImplementationRegistry.newInstance().getDOMImplementation("LS"));
        LSSerializer serializer = domImpl.createLSSerializer();
        DOMConfiguration conf = serializer.getDomConfig();
        conf.setParameter("format-pretty-print", true);
        LSOutput output = domImpl.createLSOutput();
        output.setCharacterStream(writer);
        serializer.write(document,output);
    }

    private static void exportToJSON(Writer writer, List<InventoryItem> item) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(item,writer);
    }
}
