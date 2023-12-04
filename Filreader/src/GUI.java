import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUI extends JFrame {

    public void createAndShowGUI() {
        setTitle("Open File!"); // Open the file here
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // automatically finishes the program when it closes

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src")); // directing the directory to the file

        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON, CSV, or XML files", "json", "csv", "xml"));

        int choice = fileChooser.showOpenDialog(this); // initiating the int choice to be then implemented as a yes or no statement
                                                              // the APPROVE_OPTION is basically 0, and CANCEL becomes 1
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile(); // taking the directory
            String path = file.getPath(); // initiating which file will be read by the if statement later (csv, or Json, or xml)
            // checking if the file after the dot, is csv, Json, or xml
            String extension = path.substring(path.lastIndexOf('.') + 1);

            /*if (extension.equals("json")) {
                // Handle JSON file
                // Example: Read JSON file content
                /*try (FileReader reader = new FileReader(selectedFile)) {
                    // Your JSON handling logic here
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                /*System.out.println("JSON file selected");
            } */

            if (extension.equals("json")) {
                // initiating the table
                DefaultTableModel jsonTable = new DefaultTableModel();


                // creating the table model to be then used in the GUI, tablemodel is already in the intellij

                JTable tableForData = new JTable(jsonTable);
                tableForData.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // It will resize the columns so that the JTable is not limited to specific height or width

                // try (FileReader reader = new FileReader(file))
                // {
                    // Gson gson = new Gson();
                    // Type listType = new TypeToken<List<JsonConverter>>() {}.getType();
                    // List<JsonConverter> l = gson.fromJson(reader, listType);
                    // for (JsonConverter j : l) {
                        // System.out.println("Order Date : " + j.getOrderDate());
                        // System.out.println("Region : " + j.getRegion());
                        // Add all the other getters
                    // }
                    // System.out.println("this is json file");
                // }
                // catch (IOException e) {
                    // e.printStackTrace();
                    // Handle the exception as needed
                // }

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // BufferedReader is already in the intellij
                    Gson json = new Gson();
                    java.lang.reflect.Type typeOfTheToken = new TypeToken<List<Map<String, Object>>>() {}.getType(); // I cant initiate the library above, intellij opt to declare it here

                    // parsing the json file with the reader, it will put the data into Maps
                    // the values of the Maps are the string and the object, with string will be used as the header column and the objects are the rows
                    List<Map<String, Object>> dataFromJson = json.fromJson(reader, typeOfTheToken);

                    //putting the data into the table with if statements
                    if (! dataFromJson.isEmpty()) {

                        // trying to remove A, B, C, D but it removes the OrderDate, Name and etc instead(?)
                        Map<String,Object> upperRow = dataFromJson.get(0);
                        // The first row from Map dataFromJson is saved into the upperRow variable

                        for (String header: upperRow.keySet()) {
                            jsonTable.addColumn(header);
                            // System.out.println(header); // A, B, C, D osv, how do we delete the A B C D and change them into the OrderDate?
                            // I think the problem with the later method below, the one tableForData.setRowSorter(sorter);, is that it automatically makes the header as the head of the sorter,
                            // so that unless the json file is changed, it will automatically input the orderDate, Name, Rep1, Rep2 and etc as part of the rows.
                        }



                        for (Map<String, Object> data: dataFromJson) {// For every data outside of the first row from dataFromJson (because they are already declared in the upperRow variable)
                            Object[] row = data.values().toArray(); // they will be put horizontally in row, so every row is the profile from the OrderDate, Region, Name, and etc
                            jsonTable.addRow(row);
                            // System.out.println(row);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tableForData.setAutoCreateRowSorter(true);

                JScrollPane scrollingIt = new JScrollPane(tableForData);
                add(scrollingIt);

                System.out.println("JSON file selected");
            }
            else if (extension.equals("csv")) {
                // creating the initiation for the table
                DefaultTableModel scvTable = new DefaultTableModel();

                // creating the model for the table in JTable
                JTable tableForTheData = new JTable(scvTable);
                tableForTheData.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                JScrollPane scrollIt = new JScrollPane(tableForTheData);
                add(scrollIt);

                try{
                    BufferedReader reader = null; // creating controller variable
                    reader = new BufferedReader(new FileReader(file)); // we're taking the file to be read
                    String line;
                    boolean seeTheFirstLine = true;
                    while((line = reader.readLine()) != null){ // while the file is not empty,

                        String[] rowData = line.split(","); // we're creating variable rowdata by all the datas that are
                        // each separated by comas in the original csv file

                        if (seeTheFirstLine) {
                            for (String column : rowData) { // we are creating column into the table, using the first line inside the data
                                scvTable.addColumn(column);
                            }
                            seeTheFirstLine = false;
                        } else {
                            scvTable.addRow(rowData); // after the first line becomes the column, all of the rest will be added into the rowData
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }


                tableForTheData.setAutoCreateRowSorter(true);

                // System.out.println(path);
                // System.out.println(path.substring(path.length()-3));
                // System.out.println(file.getPath());
                // CSV
            /*if(extension.equals("csv"))
            {
                BufferedReader reader = null;
                String line = "";
                try{
                    for(String index : row){
                        System.out.printf("%12s", index);
                    }
                    System.out.println();
                    /*List<CSVConverter> l = new ArrayList<>();
                    reader = new BufferedReader(new FileReader((file)));
                    while((line = reader.readLine()) != null){ // we're going to continue reading hte next line, and if there is no next line then it finishes

                        String[] column = line.split(",");

                        CSVConverter c = new CSVConverter();
                        c.setOrderDate(column[0]);
                        c.setRegion(column[1]);
                        c.setRep1(column[2]);
                        c.setRep2(column[3]);
                        c.setItem(column[4]);
                        c.setUnits(column[5]);
                        c.setUnitCost(column[6]);
                        c.setTotal(column[7]);

                        l.add(c);
                        for(int i = 0; i<l.size(); i++)
                        {
                            System.out.println(l.get(i).toString());
                        }*/
                //}
                // supaya bisa ngurutkan secara alphabetical kamu masukkin ke class lalu pake method compare to untuk salah satu field atau attribute yang alphabetical
                //}
                //catch(Exception e){
                //e.printStackTrace();
                //}
                //finally {
                //reader.close();
                //}
                //CSVToJavaFX csvToJavaFX = new CSVToJavaFX();
                //csvToJavaFX.displayCSVData(file.getPath());
                // System.out.println("this is csv file");
                //}*/

                System.out.println("CSV File selected");
            } else if (extension.equals("xml")) {
                
                System.out.println("XML file selected");
            } else {
                System.out.println("Unsupported file format");
            }
        } else {
            System.out.println("No file selected");
        }
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
