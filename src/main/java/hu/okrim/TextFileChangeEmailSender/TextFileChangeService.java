package hu.okrim.TextFileChangeEmailSender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class TextFileChangeService {
    @Value("${target.file.filename}")
    private String examinedFileName;
    private static final String FILE_NAME = "text_history";
    private static final String RESOURCE_DIRECTORY = "src/main/resources/";
    // A file to keep track of the number of rows in the examined file
    // Every time the history file is update a new row is added in the following format:
    //  <date>:<rowCount>
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

    // If changes occurred then the history file is also updated to reflect the changes
    boolean checkForChangesInExaminedFile(String examinedTextFilePath){
        boolean changesOccurred = false;
        int lastRowCount = getLastRowCountFromHistory(examinedTextFilePath);
        String lastLine = null;
        File examinedTextFile = new File(examinedTextFilePath);

        if (examinedTextFile.exists()) {
            try (Scanner scanner = new Scanner(new File(examinedTextFilePath))) {
                while (scanner.hasNextLine()) {
                    lastLine = scanner.nextLine();
                }
                // Current row count is set to 0 if the lastLine was null
                int currentRowCount = lastLine == null ? 0 : Integer.parseInt(lastLine.split(":")[1]);
                // Checking if the two rowCount values differ
                if(lastRowCount != currentRowCount){
                    changesOccurred = true;
                    // Writing the new rowNumber into the history file
                    File file = new File(RESOURCE_DIRECTORY + FILE_NAME);
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(LocalDateTime.now() + "=" + currentRowCount);
                        System.out.println("text_history file updated successfully.");
                    } catch (IOException e) {
                        System.err.println("Error updating the text_history file: " + e.getMessage());
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The examined text file does not exist. " +
                    "Make sure that the file path is correctly set in the " +
                    "application.properties file!");
        }
        return changesOccurred;
    }

    int getLastRowCountFromHistory(String examinedTextFilePath){
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
        int lastRowCount = getLastRowCountFromHistory(examinedTextFilePath);
        File examinedTextFile = new File(examinedTextFilePath);

        try (Scanner scanner = new Scanner(examinedTextFile)) {
            int counter = 0;
            while (scanner.hasNextLine()) {
                counter++;
                // If the row is new then adding it to the array
                if(counter > lastRowCount){
                    newLinesString.append(scanner.nextLine());
                    newLinesString.append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newLinesString.toString();
    }
}
