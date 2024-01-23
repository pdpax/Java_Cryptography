import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Decryptor {

    String text;
    private String decryptedText;
    private int key;
    private static final int ASCII_MIN = 32;
    private static final int ASCII_LENGTH = 95;
    private static final String SELECT_MODE = "\nMode:\n1.Use a Key\n2.Brute Force\n3.Statistical Analysis\nSelect Mode: ";
    private static final String ENTER_KEY = "\nEnter Decryption Key: ";
    private static final String SELECT_SOURCE = "\nSource:\n1.File\n2.User Input\nSelect Source: ";
    private static final String ENTER_PATH = "\nEnter File Path: ";
    private static final String ENTER_TEXT = "\nEnter Text: ";
    private static final String ALERT = "Note that using brute force will delete all existing files in src\\Decrypted\\Brute Candidates.\nPress Enter to Continue...";
    private static final String NOT_ELIGIBLE = "The text is not eligible for brute force as it does not contain long enough words.\nPress Enter to Continue...";
    private static final String BRUTE_SUCCESS = "Brute Force Succeeded!";
    private static final String BRUTE_FAIL = "Brute Force Failed...";


    public static void decryption() {
        Decryptor decryptor = new Decryptor();
        System.out.print(SELECT_SOURCE);
        int source = UserInput.getInt(1, 2);
        decryptor.getSource(source);
        System.out.print(SELECT_MODE);
        int mode = UserInput.getInt(1, 3);
        if (mode == 1) {
            System.out.print(ENTER_KEY);
            decryptor.key = UserInput.getInt(1, ASCII_LENGTH - 1);
            decryptor.useKey("Decryption");
        } else if (mode == 2) {
            decryptor.bruteForce();
        } else {
            System.out.println("UNDER DEVELOPMENT!");
            decryptor.statisticalAnalysis();
        }
    }

    private void useKey(String caller) {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.text.length(); i++) {
            if (this.text.charAt(i) == '\r') {

            } else if (this.text.charAt(i) == '\n') {
                stringBuilder.append(System.lineSeparator());
            } else {
                int character = this.text.charAt(i);
                //32-126 inclusive => length = 126-32 +1 = 95
                character -= this.key;
                if (character < ASCII_MIN) {
                    character += ASCII_LENGTH;
                }
                stringBuilder.append((char) character);
            }
        }
        this.decryptedText = stringBuilder.toString();
        if (caller.equals("Decryption")) {
            this.printToFile("Key");
        }
    }

    private void bruteForce() {

        boolean isEligible = this.checkBruteForceEligibility();
        if (!isEligible) {
            System.out.println(NOT_ELIGIBLE);
            new Scanner(System.in).nextLine();
            return;
        }

        System.out.print(ALERT);
        new Scanner(System.in).nextLine();
        clearBruteCandidates();
        final int DICTIONARY_MIN = 3;
        final int DICTIONARY_MAX = 9;
        try {
            List<FileReader> fr = new ArrayList<>();
            List<String> dictionary = new ArrayList<>();
            for (int i = DICTIONARY_MIN; i <= DICTIONARY_MAX; i++) {
                fr.add(new FileReader("src\\Dictionary\\" + i + " Letter Words.txt"));
                {
                    BufferedReader bf = new BufferedReader(fr.get(i - DICTIONARY_MIN));
                    dictionary.add(bf.readLine());
                    bf.close();
                }

            }   //0: 3 Letter Words ...... 6: 9 Letter Words

            //from 1 to length -1 inclusive
            boolean matchFound = false;
            boolean isDecrypted = false;
            for (int i = 0; i < ASCII_LENGTH; i++) {    //all possible keys
                matchFound = false;
                this.key = i;
                this.useKey("BruteForce");

                for (int j = 0; j < dictionary.size(); j++) {   //all dictionaries

                    Scanner scanner = new Scanner(this.decryptedText);
                    while (scanner.hasNext()) {
                        String word = scanner.next();
                        if (dictionary.get(j).contains(" " + word.toUpperCase() + " ")) {
                            matchFound = true;
                            isDecrypted = true;
                            this.printToFile("BruteForce");
                            break;
                        }
                    }
                    if (matchFound) {
                        break;
                    }
                }
            }
            if (isDecrypted) {
                System.out.println(BRUTE_SUCCESS);
            } else {
                this.decryptedText = null;
                System.out.println(BRUTE_FAIL);
            }
            System.out.println("Press Enter to Continue...");
            new Scanner(System.in).nextLine();

            for (int i = 0; i < fr.size(); i++) {
                fr.get(i).close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkBruteForceEligibility() {
        StringTokenizer tokenizer = new StringTokenizer(this.text);
        boolean isEligible = false;
        while (tokenizer.hasMoreTokens()) {
            if (tokenizer.nextToken().length() > 2) {
                isEligible = true;
                break;
            }
        }
        return isEligible;
    }

    private void clearBruteCandidates() {

        String directoryPath = "src\\Decrypted\\Brute Candidates\\";
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    private void statisticalAnalysis() {
    }

    private void getSource(int source) {
        if (source == 1) {
            System.out.print(ENTER_PATH);
            try (FileReader fr = new FileReader(UserInput.getPath());
                 BufferedReader br = new BufferedReader(fr);) {
                StringBuilder sb = new StringBuilder();
                while (br.ready()) {
                    char character = (char) br.read();
                    sb.append(character);
                }
                this.text = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.print(ENTER_TEXT);
            this.text = UserInput.getString();
        }

    }

    private void printToFile(String caller) {
        String saveDirectory;

        if (caller.equals("BruteForce")) {
            saveDirectory = "src\\Decrypted\\Brute Candidates\\";
        } else {
            saveDirectory = "src\\Decrypted\\";
        }
        int fileCount = 0;
        String fileName = "";
        while (true) {
            fileName = fileCount + ".txt";
            File file = new File(saveDirectory, fileName);
            if (file.exists()) {
                fileCount++;
            } else {
                break;
            }
        }

        try (FileWriter fw = new FileWriter(saveDirectory + fileName);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(this.decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("File Successfully Saved As " + fileName);
        if (!caller.equals("BruteForce")) {
            System.out.print("Press Enter to Continue...");
            new Scanner(System.in).nextLine();
        }
    }

}
