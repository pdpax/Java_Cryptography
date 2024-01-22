import static java.lang.System.exit;

public class Main {
    public static final String TITLE = "\n\n==========================\n      CRYPTOGRAPHY      \n==========================\n";
    public static final String TASK_SELECT = "Task:\n1.Encryption\n2.Decryption\n3.Exit\nSelect Task: ";

    public static void main(String[] args) {
        while (true) {
            System.out.println(TITLE);
            System.out.print(TASK_SELECT);
            int task = UserInput.getInt(1, 3);
            if (task == 1) {
                Encryptor.encryption();
            } else if (task == 2) {
                Decryptor.decryption();
            } else {
                exit(0);
            }
        }
    }
}