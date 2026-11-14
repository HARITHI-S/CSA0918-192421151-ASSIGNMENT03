import java.util.Scanner;
public class ATM {
    static final int BALANCE = 5000;
    static void withdraw(String account, int amount) {
        try {
            if (account == null || !account.matches("\\d{6}"))
                throw new IllegalArgumentException("Account number must contain 6 digits");
            if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive");
            if (amount > BALANCE) throw new IllegalStateException("Insufficient balance");
            System.out.println("Withdrawal successful: Rs." + amount);
        } catch (IllegalArgumentException e) {
            System.out.println("Input error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Transaction error: " + e.getMessage());
        } finally { System.out.println("ATM session closed."); }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("ATM Withdrawal Simulation");
        System.out.print("Enter 6-digit account number: ");
        String account = sc.nextLine();
        System.out.print("Enter withdrawal amount: ");
        String raw = sc.nextLine();
        try { withdraw(account, Integer.parseInt(raw)); }
        catch (NumberFormatException e) {
            System.out.println("Input error: Please enter a whole number.");
            System.out.println("ATM session closed.");
        }
        sc.close();
    }
}
