import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        while (true) {//text lower better be placed in staatic final vars or even in class with all the text labels
            System.out.println("\nЕсли хотите использовать программу в режиме \"шифрование\", введите 1." +
                    "\nЕсли хотите использовать программу в режиме \"дешифрование\", введите 2." +
                    "\nЕсли хотите использовать программу в режиме \"bruteforce\", введите 3." +
                    "\nЕсли хотите использовать программу в режиме \"статистический анализ\", введите 4.");

            while (true) {
                String tmp = scanner.nextLine();
                //switch case would be better
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
                if (!tmp.equals("1") || !tmp.equals("2") || !tmp.equals("3") || !tmp.equals("4")) {
                    System.out.println("Введите 1, 2, 3 или 4 для выбора режима программы, где 1 - режим \"шифрование\", где 2 - режим \"дешифрование\", где 3 - режим \"brutforce\" и где 4 - режим \"статистический анализ\"");
                }
            }
            //text is better to store in var
            System.out.println("\nДля продолжения работы в программе напишите \"yes\", для выхода из программы напишите \"close\".");
            String continueExit = scanner.nextLine();
            //switch case
            if(continueExit.equals("yes")) {
                continue;
            }
            if(continueExit.equals("close")) {
                break;
            }
            if(!continueExit.equals("yes") || !continueExit.equals("close")) {
                System.out.println("\nДля продолжения работы в программы напишите \"yes\", для выхода из программы напишите \"close\".");
            }
        }
    }
}
