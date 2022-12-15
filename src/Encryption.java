import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Encryption {
    private static String path = "";
    private static int key;

    public static void encryption() throws IOException {
        System.out.println("\nВведите полный путь к файлу с текстом, который нужно зашифровать." +
                "\nПример ввода: C:\\Users\\projects\\project.txt");

        boolean isRightPath = false;
        while (!isRightPath) {
            String tmp = Main.scanner.nextLine();

            if (Files.isRegularFile(Path.of(tmp)) && Files.exists(Path.of(tmp))) {
                path = tmp;
                break;
            } else {
                System.out.println("Введите полный путь к существующему файлу с текстом, который нужно зашифровать.");
            }
        }

        boolean isRightKeyFormat = false;
        while (!isRightKeyFormat) {
            System.out.println("\nВведите ключ в виде целого числа.");
            String tmp = Main.scanner.nextLine();

            try {
                key = Integer.parseInt(tmp);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Неправильный формат ввода ключа.");
            }
        }

        String outputPath = getNewFileName(path);
        if (Files.notExists(Path.of(outputPath))) {
            Files.createFile(Path.of(outputPath));
        }

        try (FileReader reader = new FileReader(path);
             FileWriter writer = new FileWriter(outputPath)) {
            char[] buffer = new char[128000];

            String letters = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя.,\":;-!? ";
            char[] chars = letters.toCharArray();
            List<Character> charsList = new ArrayList<>();
            for (char ch : chars) {
                charsList.add(ch);
            }

            key = checkKey(key, letters);

            while (reader.ready()) {
                int size = reader.read(buffer);
                for (int i = 0; i < size; i++) {
                    char letter = buffer[i];
                    int lettersInt = (int) letter;

                    if (charsList.contains(letter) && lettersInt != 10) {
                        int indexLetter = letters.indexOf(buffer[i]);
                        int newIndex = newIndex(key, indexLetter, letters);
                        buffer[i] = letters.charAt(newIndex);
                    }
                }
                writer.write(buffer, 0, size);
            }

        }

    }

    public static String getNewFileName(String oldFileName) {
        int dotIndex = oldFileName.lastIndexOf(".");
        String newFileName = oldFileName.substring(0, dotIndex) + "Encrypted" + oldFileName.substring(dotIndex);
        return newFileName;
    }

    public static int checkKey(int key, String letters) {
        int kkey = key;
        if (key < letters.length()) {
            return key;
        } else {
            while (kkey > letters.length()) {
                kkey = kkey - letters.length();
            }
            return kkey;
        }
    }

    public static int newIndex(int key, int currentIndex, String letters) {
        int newIndex = key + currentIndex;
        if (newIndex == letters.length()) {
            return 0;
        }
        if (newIndex > letters.length()) {
            return currentIndex + key - letters.length();
        }
        if (newIndex < letters.length()) {
            return newIndex;
        }
        return 0;
    }

}


