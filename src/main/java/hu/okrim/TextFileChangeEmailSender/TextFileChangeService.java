package hu.okrim.TextFileChangeEmailSender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

@Service
public class TextFileChangeService {
    @Value("${target.file.filename}")
    private String examinedFileName;
    private static final String FILE_NAME = "text_history";
    private static final String RESOURCE_DIRECTORY = "resources/";

    // A file to keep track of the number of rows in the examined file
    // Every time the history file is update a new row is added in the following format:
    //  <date>=<rowCount>
    public void createHistoryFileIfNotExists() {
        File file = new File(RESOURCE_DIRECTORY + FILE_NAME);

        if (!file.exists()) {
            try {
                // Create an empty file
                boolean created = file.createNewFile();
                if (created) {
                    System.out.println("text_history file created successfully.");
                }
            } catch (IOException e) {
                System.err.println("Error creating text_history file: " + e.getMessage());
            }
        } else {
            System.out.println("text_history file was found.");
        }
    }

    // If changes occurred then the examined file is also updated to reflect the changes
    boolean changesOccurredInExaminedFile(String examinedTextFilePath){
        int lastRowCount = getLastRowCountFromHistory();
        int currentRowCount = countRowsInExaminedFile(examinedTextFilePath);
        return lastRowCount != currentRowCount;
    }

    int getLastRowCountFromHistory(){
        String lastLine = null;
        int lastLineRowCount = 0;

        try (Scanner scanner = new Scanner(new File(RESOURCE_DIRECTORY + FILE_NAME))) {
            while (scanner.hasNextLine()) {
                lastLine = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (lastLine != null) {
            // Splitting the last row of the history file at the ':' because they are stored
            // in the following format --> currentDate:rowCount
            // so the second part of the split string is what is needed
            lastLineRowCount = Integer.parseInt(lastLine.split("=")[1]);
        }

        return lastLineRowCount;
    }

    String getNewLinesFromExaminedFile(String examinedTextFilePath){
        StringBuilder newLinesString = new StringBuilder();
        newLinesString.append(String.format("These new lines have been added to the %s file:\n\n", examinedFileName));
        int lastRowCount = getLastRowCountFromHistory();
        File examinedTextFile = new File(examinedTextFilePath);

        try (Scanner scanner = new Scanner(examinedTextFile)) {
            int counter = 0;
            while (scanner.hasNextLine()) {
                counter++;
                String currentLine = scanner.nextLine();
                if (counter > lastRowCount) {
                    newLinesString.append(currentLine);
                    newLinesString.append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newLinesString.toString();
    }

    int countRowsInExaminedFile(String examinedTextFilePath){
        File examinedTextFile = new File(examinedTextFilePath);
        int rowCount = 0;
        if (examinedTextFile.exists()) {
            try (Scanner scanner = new Scanner(examinedTextFile)) {
                while (scanner.hasNextLine()) {
                    scanner.nextLine();
                    rowCount++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return rowCount;
    }

    void updateTextHistory(String examinedTextFilePath){
        File examinedTextFile = new File(examinedTextFilePath);
        // Writing the new rowNumber into the history file
        if(examinedTextFile.exists()){
            File file = new File(RESOURCE_DIRECTORY + FILE_NAME);
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write("\n" + LocalDateTime.now() + "=" + countRowsInExaminedFile(examinedTextFilePath));
                System.out.println("text_history file updated successfully.");
            } catch (IOException e) {
                System.err.println("Error updating the text_history file: " + e.getMessage());
            }
        } else {
            System.out.println("The examined text file does not exist. " +
                    "Make sure that the file path is correctly set in the " +
                    "application.properties file!");
        }
    }
}
