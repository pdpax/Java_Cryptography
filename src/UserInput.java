import java.io.File;
import java.util.Scanner;

public class UserInput {
    public static Scanner scanner = new Scanner(System.in);

    public static int getInt(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.printf("Input must be between %d and %d. Try again: ", min, max);
                }
            } catch (Exception e) {
                System.out.printf("Input must be an integer between %d and %d. Try again: ", min, max);
            }
        }
    }

    public static String getPath() {
        while (true) {
            String path = scanner.nextLine();
            File file = new File(path);
            if (file.exists()) {
                return path;
            } else {
                System.out.print("File doesn't exist. Enter a valid path: ");
            }
        }
    }

    public static String getString() {
        return scanner.nextLine();
    }
}
