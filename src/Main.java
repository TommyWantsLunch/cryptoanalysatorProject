import java.io.IOException;
import java.util.Scanner;
public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        System.out.println("\nЕсли хотите использовать программу в режиме \"шифрование\", введите 1." +
                "\nЕсли хотите использовать программу в режиме \"дешифрование\", введите 2." +
                "\nЕсли хотите использовать программу в режиме \"bruteforce\", введите 3." +
                "\nЕсли хотите использовать программу в режиме \"статистический анализ\", введите 4.");


        while(true) {
            String tmp = scanner.nextLine();
            if (tmp.equals("1")) {
                Encryption.encryption();
                break;
            }
            if (tmp.equals("2")) {
                Decryption.decryption();
                break;
            }
            if (tmp.equals("3")) {
                BruteForce.bruteforce();
                break;
            }
            if (tmp.equals("4")) {
                StatisticalAnalysis.statisticalAnalysis();
                break;
            }
            if(!tmp.equals("1") || !tmp.equals("2") || !tmp.equals("3") || !tmp.equals("4")) {
                System.out.println("Введите 1, 2, 3 или 4 для выбора режима программы, где 1 - режим \"шифрование\", где 2 - режим \"дешифрование\", где 3 - режим \"brutforce\" и где 4 - режим \"статистический анализ\"");
            }
        }
    }
}
