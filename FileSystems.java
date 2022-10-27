import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileSystems {
    public static void main(String[] args) {
        boolean hidden = false;
        System.out.println("Enter a directory path: ");
        Scanner fileLocation = new Scanner(System.in);
        String filePath = fileLocation.nextLine();
        String[] inputParts = filePath.split(" ");
        if (inputParts.length > 1 && inputParts[1].equals("-h")){
            hidden = true;
        }
        System.out.printf("%s = %s\n%s = %s\n", "folder", inputParts[0], "hidden", hidden);
        String fileSeparator = File.separator;
        if (fileSeparator.equals("/")){
            char separator = File.separatorChar;
            int count = 0;
            int[] array = new int[]{0,0,0,0,0,0,0,0,0,0,0};
            for (int a = 0; a < inputParts[0].length(); a++){
                if (inputParts[0].charAt(a) == separator){
                    array[count] = a;
                    count++;
                }
            }
            for (int b = 0; b < count; b++){
                String newInputParts = inputParts[0] + "/";
                int end = newInputParts.indexOf("/") + 1;
                String newInput = newInputParts.substring(0, array[b]+1);
                File currentFile = new File(newInput);
                System.out.printf("\n%s%s\n", "Reading folder ", newInput);
                File[] fileList = currentFile.listFiles();
                printFileContents(fileList, hidden);
            }
            System.out.printf("\n%s%s\n", "Reading folder ", inputParts[0]);
            File currentFile = new File(inputParts[0]);
            File[] fileList = currentFile.listFiles();
            printFileContents(fileList, hidden);
        }
        if (fileSeparator.equals("\\")){
            char separator = File.separatorChar;
            int count = 0;
            int[] array = new int[]{0,0,0,0,0,0,0,0,0,0,0};
            for (int a = 0; a < inputParts[0].length(); a++){
                if (inputParts[0].charAt(a) == separator){
                    array[count] = a;
                    count++;
                }
            }
            for (int b = 0; b < count; b++){
                String newInputParts = inputParts[0] + "\\";
                int end = newInputParts.indexOf("\\") + 1;
                String newInput = newInputParts.substring(0, array[b]+1);
                File currentFile = new File(newInput);
                System.out.printf("\n%s%s\n", "Reading folder ", newInput);
                File[] fileList = currentFile.listFiles();
                printFileContents(fileList, hidden);
            }
            System.out.printf("\n%s%s\n", "Reading folder ", inputParts[0]);
            File currentFile = new File(inputParts[0]);
            File[] fileList = currentFile.listFiles();
            printFileContents(fileList, hidden);
        }
    }
    public static void printFileContents(File[] fileList, boolean hidden){
        for (File current : fileList){
            String hiddenString = "      ";
            String readSymbol = "-";
            String writeSymbol = "-";
            String executeSymbol = "-  ";
            String fileType = "";
            boolean isDir = current.isDirectory();
            boolean isFile = current.isFile();
            if (isDir){
                fileType = "dir   ";
            }
            else if (isFile){
                fileType = "file  ";
            }
            else {
                fileType = "other ";
            }
            boolean canRead = current.canRead();
            if (canRead){
                readSymbol = "r";
            }
            boolean canWrite = current.canWrite();
            if (canWrite){
                writeSymbol = "w";
            }
            boolean canExecute = current.canExecute();
            if (canExecute){
                executeSymbol = "x  ";
            }
            long length = current.length();
            if (hidden && current.isHidden()){
                hiddenString = "hidden";
            }
            String fileName = current.getName();
            System.out.printf("%s%5s%5s%s%s%10d %s\n", fileType, hiddenString, readSymbol, writeSymbol, executeSymbol, length, fileName);
        }
    }
}
