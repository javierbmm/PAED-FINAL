import java.util.Scanner;


public class ConsoleFormView  {
    private final Scanner scanner = new Scanner(System.in);

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public void displayError(String message) {
        System.out.println(ANSI_RED + "\nERROR: " + message + ANSI_RESET);
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public String getQueryResponse(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    public int getIntResponse(String message) {
        System.out.println(message);
        return scanner.nextInt();
    }
}
