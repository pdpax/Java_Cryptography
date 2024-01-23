import java.io.*;
import java.util.Scanner;

public class Encryptor {
    String text;
    private String encryptedText;
    private int key;
    private static final int ASCII_MAX = 126;
    private static final int ASCII_LENGTH = 95;
    private static final String SELECT_SOURCE = "\nSource:\n1.File\n2.User Input\nSelect Source: ";
    private static final String ENTER_PATH = "\nEnter File Path: ";
    private static final String ENTER_TEXT = "\nEnter Text: ";

    private static final String ENTER_KEY = "\nEnter Encryption Key: ";


    public static void encryption() {
        createFolder();
        Encryptor encryptor = new Encryptor();
        System.out.print(SELECT_SOURCE);
        int source = UserInput.getInt(1, 2);
        encryptor.getSource(source);
        System.out.print(ENTER_KEY);
        encryptor.key = UserInput.getInt(1, ASCII_LENGTH - 1);
        encryptor.encrypt();
        encryptor.printToFile();
    }

    private static void createFolder() {
        String folderPath = "src\\Encrypted";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    private void encrypt() {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.text.length(); i++) {
            if (this.text.charAt(i) == '\r') {

            } else if (this.text.charAt(i) == '\n') {
                stringBuilder.append(System.lineSeparator());
            } else {
                int character = this.text.charAt(i);
                //32-126 inclusive => length = 126-32 +1 = 95

                character += this.key;
                if (character > ASCII_MAX) {
                    character -= ASCII_LENGTH;
                }
                stringBuilder.append((char) character);
            }
        }
        this.encryptedText = stringBuilder.toString();
    }

    private void getSource(int source) {
        if (source == 1) {
            System.out.print(ENTER_PATH);
            try (FileReader fr = new FileReader(UserInput.getPath());
                 BufferedReader br = new BufferedReader(fr);) {
                StringBuilder sb = new StringBuilder();
                while (br.ready()) {
                    //sb.append(br.readLine());
                    char character = (char) br.read(); //this way we preserve the new line characters
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

    private void printToFile() {
        final String SAVE_DIRECTORY = "src\\Encrypted\\";
        int fileCount = 0;
        String fileName = "";
        while (true) {
            fileName = fileCount + " K" + this.key + ".txt";
            File file = new File(SAVE_DIRECTORY, fileName);
            if (file.exists()) {
                fileCount++;
            } else {
                break;
            }
        }

        try (FileWriter fw = new FileWriter(SAVE_DIRECTORY + fileName);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(this.encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("File Successfully Saved As " + fileName);
        System.out.print("Press Enter to Continue...");
        new Scanner(System.in).nextLine();
    }
}
