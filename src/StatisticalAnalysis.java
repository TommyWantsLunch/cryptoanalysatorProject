import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

public class StatisticalAnalysis {

    private static String mainTextPath = "";
    private static String textWithStatisticPath = "";

    public static void statisticalAnalysis() throws IOException {

        mainTextPath = pathForMainText();
        textWithStatisticPath = pathForTextWithStatistic();

        String outputPath = getNewFileName(mainTextPath);
        if (Files.notExists(Path.of(outputPath))) {
            Files.createFile(Path.of(outputPath));
        }

        //testReader считывает с файла, на основе которого будет происходить дешифровка
        //reader считывает с файла, который нужно дешифровать, но этот reader себя исчерпает пока будет делаться подсчет букв
        //readerForWriter считывает с файла, и по ходу считывания идет дешифровка
        try(FileReader testReader = new FileReader(textWithStatisticPath);
            FileReader reader = new FileReader(mainTextPath);
            FileReader readerForWriter = new FileReader(mainTextPath);
            FileWriter writer = new FileWriter(outputPath)) {

            //буфферы для ридеров, массив чаров для подсчета букв
            char[] bufferForExampleText = new char[128000];
            char[] buffer = new char[128000];
            String letters = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя.,\":;-!? ";
            char[] chars = letters.toCharArray();
            List<Character> charsList = new ArrayList<>();
            for (char ch : chars) {
                charsList.add(ch);
            }

            //массивы для сохранения результатов. двумерный массив делать не вижу смысла, а в мапе очень сложно доставать нужные значения в порядке убывания, скорее всего в силу неопытности
            String[] arrayForMainLetters = new String[75];
            String[] arrayForExampleLetters = new String[75];
            int[] arrayForMainCounting = new int[75];
            int[] arrayForExampleCounting = new int[75];

            //подсчет букв в основном файле и в том, на основе которого делается дешифровка
            while (testReader.ready()) {
                int size = testReader.read(bufferForExampleText);
                for (int i = 0; i < chars.length; i++) {
                    char mainLetter = chars[i];
                    int counter = 0;
                    for (int j = 0; j < size; j++) {
                        char letterFromText = bufferForExampleText[j];
                        if(mainLetter == letterFromText) {
                            counter++;
                        }
                    }
                    arrayForExampleLetters[i] = String.valueOf(mainLetter);
                    arrayForExampleCounting[i] = counter;

                }
            }
            while (reader.ready()) {
                int size = reader.read(buffer);
                for (int i = 0; i < chars.length; i++) {
                    char mainLetter = chars[i];
                    int counter = 0;
                    for (int j = 0; j < size; j++) {
                        char letterFromText = buffer[j];
                        if(mainLetter == letterFromText) {
                            counter++;
                        }
                    }
                    arrayForMainLetters[i] = String.valueOf(mainLetter);
                    arrayForMainCounting[i] = counter;
                }
            }

            //сортировка от большего к меньшему, показалось, что так удобнее
            bubbleSort(arrayForMainCounting, arrayForMainLetters);
            bubbleSort(arrayForExampleCounting, arrayForExampleLetters);

            //дешифровка и запись готового текста в новый файл
            while (readerForWriter.ready()) {
                int size = readerForWriter.read(buffer);
                for (int i = 0; i < buffer.length; i++) {
                    String letter = String.valueOf(buffer[i]);
                    for (int j = 0; j < arrayForMainLetters.length; j++) {
                        String tmp = arrayForMainLetters[j];
                        if(letter.equals(tmp)) {
                            buffer[i] = arrayForExampleLetters[j].charAt(0);
                        }
                    }
                }
                writer.write(buffer, 0, size);
            }

        }



    }

    private static String pathForTextWithStatistic() {
        System.out.println("\nВведите полный путь к файлу с текстом, на основе которого будет проведен статистический анализ." +
                "\nПример ввода: C:\\Users\\projects\\project.txt");

        boolean isRightExtraPath = false;
        String methodPath = "";
        while (!isRightExtraPath) {
            try {
                String tmp = Main.scanner.nextLine();
                if (Files.isRegularFile(Path.of(tmp)) && Files.exists(Path.of(tmp))) {
                    methodPath = tmp;
                    break;
                } else {
                    System.out.println("Введите полный путь к существующему файлу с текстом, на основе которого будет проведен статистический анализ.");
                }
            }catch (InvalidPathException e) {
                System.out.println("\nВведите полный путь к файлу с текстом, на основе которого будет проведен статистический анализ." +
                        "\nПример ввода: C:\\Users\\projects\\project.txt");
            }
        }
        return methodPath;
    }
    private static String pathForMainText() {
        System.out.println("\nВведите полный путь к файлу с текстом, который нужно дешифровать через статистический анализ." +
                "\nПример ввода: C:\\Users\\projects\\project.txt");

        boolean isRightPath = false;
        String methodPath = "";
        while (!isRightPath) {
            try {
                String tmp = Main.scanner.nextLine();
                if (Files.isRegularFile(Path.of(tmp)) && Files.exists(Path.of(tmp))) {
                    methodPath = tmp;
                    break;
                } else {
                    System.out.println("Введите полный путь к существующему файлу с текстом, который нужно дешифровать через статистический анализ.");
                }
            }catch (InvalidPathException e) {
                System.out.println("\nВведите полный путь к файлу с текстом, который нужно дешифровать через статистический анализ." +
                        "\nПример ввода: C:\\Users\\projects\\project.txt");
            }
        }
        return methodPath;
    }
    public static String getNewFileName(String oldFileName) {
        int dotIndex = oldFileName.lastIndexOf(".");
        String newFileName = oldFileName.substring(0, dotIndex) + "DecryptedWithStatisticAnalys" + oldFileName.substring(dotIndex);
        return newFileName;
    }
    public static void bubbleSort(int[] numbers, String[] letters) {
        boolean isSorted = true;
        int temp;
        String tmp;

        while (isSorted) {
            isSorted = false;
            for (int i = 0; i < numbers.length - 1; i++) {
                if (numbers[i] < numbers[i + 1]) {
                    temp = numbers[i];
                    numbers[i] = numbers[i + 1];
                    numbers[i + 1] = temp;

                    tmp = letters[i];
                    letters[i] = letters[i+1];
                    letters[i+1] = tmp;

                    isSorted = true;
                }
            }
        }
    }

}
