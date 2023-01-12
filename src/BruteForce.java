import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

public class BruteForce {
    private static String path = "";
    private static int key;

    public static void bruteforce() throws IOException {

        path = pathForBruteforce();

        //создание нового файла
        String outputPath = getNewFileName(path);
        if (Files.notExists(Path.of(outputPath))) {
            Files.createFile(Path.of(outputPath));
        }

        try (FileReader readerForTest = new FileReader(path);
             FileReader reader = new FileReader(path);
             FileWriter writer = new FileWriter(outputPath)) {

            //мапа для ключ-совпадения, лист для часто использующихся слов
            Map<Integer, Integer> keysAndMatches = new HashMap<>();
            List<String> frequentlyUsedWords = frequentlyUsedWords();

            //лист с основными буквами и символами
            //same as in Encryption-Decryption
            String letters = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя.,\":;-!? ";
            char[] chars = letters.toCharArray();
            List<Character> charsList = new ArrayList<>();
            for (char ch : chars) {
                charsList.add(ch);
            }

            //маленький буффер для тестов ключей, счетчик для совпадений, копия буффера для возвращения буффера к исходному тексту до расшифровки
            char[] smallBufferForTests = new char[10000];
            int testSize = readerForTest.read(smallBufferForTests);
            int counter = 0;
            char[] copyOfBufferForTest = Arrays.copyOf(smallBufferForTests, smallBufferForTests.length);

            //выбирается ключ
            //75 is a magical number, it must be stored in static final variable with reasonable name
            for (int i = 0; i < 75; i++) {
                key = i;

                //буффер дешифруется по текущему ключу
                for (int j = 0; j < testSize; j++) {
                    char letter = smallBufferForTests[j];
                    int lettersInt = (int) letter;

                    if (charsList.contains(letter) && lettersInt != 10) {
                        int indexLetter = letters.indexOf(smallBufferForTests[j]);
                        int newIndex = newIndex(key, indexLetter, letters);
                        smallBufferForTests[j] = letters.charAt(newIndex);
                    }
                }

                //перевели буффер в строку для стринговых методов и удобной работы
                String stringWithDecryptedBuffer = new String(smallBufferForTests);

                //сравниваем с самыми часто используемыми словами
                counter = checkMatchesWithFrequentlyUsedWords(stringWithDecryptedBuffer, frequentlyUsedWords, counter);
                //сравниваем по правилу "точка - пробел - большая буква"
                counter = checkDotSpaceUpperLetter(stringWithDecryptedBuffer, counter);

                //вывод общей информации, запись в мапу ключ-совпадения, возврат маленького буффера к первоначальному виду до дешифровки для следующего ключа, обнуление счетчика для следующего ключа
                System.out.println("По ключу " + key + " количество совпадений: " + counter);
                keysAndMatches.put(key, counter);
                smallBufferForTests = Arrays.copyOf(copyOfBufferForTest, copyOfBufferForTest.length);
                counter = 0;
            }

            int rightKey = rightKey(keysAndMatches);
            System.out.println("\nПравильный ключ: "+rightKey);

            //запись всего расшифрованного текста с подобранным ключом в новый файл
            char[] buffer = new char[128000];
            while (reader.ready()) {
                int size = reader.read(buffer);
                for (int i = 0; i < size; i++) {
                    char letter = buffer[i];
                    int lettersInt = (int) letter;

                    if (charsList.contains(letter) && lettersInt != 10) {
                        int indexLetter = letters.indexOf(buffer[i]);
                        int newIndex = newIndex(rightKey, indexLetter, letters);
                        buffer[i] = letters.charAt(newIndex);
                    }
                }
                writer.write(buffer, 0, size);
            }

        }
    }

    private static String pathForBruteforce() {
        //text in var
        System.out.println("\nВведите полный путь к файлу с текстом, к которому нужно подобрать ключ дешифровки." +
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
                    //text in var
                    System.out.println("Введите полный путь к существующему файлу с текстом, который нужно дешифровать.");
                }
            } catch (InvalidPathException e) {
                //text in var
                System.out.println("\nВведите полный путь к файлу с текстом, к которому нужно подобрать ключ дешифровки." +
                        "\nПример ввода: C:\\Users\\projects\\project.txt");
            }
        }
        return methodPath;
    }
    public static String getNewFileName(String oldFileName) {
        int dotIndex = oldFileName.lastIndexOf(".");
        //inline
        String newFileName = oldFileName.substring(0, dotIndex) + "Bruteforced" + oldFileName.substring(dotIndex);
        return newFileName;
    }
    public static List<String> frequentlyUsedWords() {
        List<String> frequentlyWords = new ArrayList<>();
        //better to load this words from file to list; if want to hardcode, can be used List.of(...)
        frequentlyWords.add("и");
        frequentlyWords.add("в");
        frequentlyWords.add("не");
        frequentlyWords.add("на");
        frequentlyWords.add("я");
        frequentlyWords.add("быть");
        frequentlyWords.add("он");
        frequentlyWords.add("с");
        frequentlyWords.add("что");
        frequentlyWords.add("а");
        frequentlyWords.add("по");
        frequentlyWords.add("это");
        frequentlyWords.add("она");
        frequentlyWords.add("этот");
        frequentlyWords.add("к");
        frequentlyWords.add("но");
        frequentlyWords.add("они");
        frequentlyWords.add("мы");
        frequentlyWords.add("как");
        frequentlyWords.add("из");
        frequentlyWords.add("у");
        frequentlyWords.add("который");
        frequentlyWords.add("то");
        frequentlyWords.add("за");
        frequentlyWords.add("свой");
        frequentlyWords.add("что");
        frequentlyWords.add("весь");
        frequentlyWords.add("год");
        frequentlyWords.add("от");
        frequentlyWords.add("так");
        frequentlyWords.add("о");
        frequentlyWords.add("для");
        frequentlyWords.add("ты");
        frequentlyWords.add("же");
        frequentlyWords.add("все");
        frequentlyWords.add("тот");
        frequentlyWords.add("мочь");
        frequentlyWords.add("вы");
        frequentlyWords.add("человек");
        frequentlyWords.add("такой");
        frequentlyWords.add("его");
        frequentlyWords.add("сказать");
        frequentlyWords.add("только");
        frequentlyWords.add("или");
        frequentlyWords.add("ещё");
        frequentlyWords.add("бы");
        frequentlyWords.add("себя");
        frequentlyWords.add("один");
        frequentlyWords.add("как");
        frequentlyWords.add("уже");
        frequentlyWords.add("до");
        frequentlyWords.add("время");
        frequentlyWords.add("если");
        frequentlyWords.add("сам");
        frequentlyWords.add("когда");
        frequentlyWords.add("другой");
        frequentlyWords.add("вот");
        frequentlyWords.add("говорить");
        frequentlyWords.add("наш");
        frequentlyWords.add("мой");
        frequentlyWords.add("знать");
        frequentlyWords.add("стать");
        frequentlyWords.add("при");
        frequentlyWords.add("чтобы");
        frequentlyWords.add("дело");
        frequentlyWords.add("жизнь");
        frequentlyWords.add("кто");
        frequentlyWords.add("первый");
        frequentlyWords.add("очень");
        frequentlyWords.add("два");
        frequentlyWords.add("день");
        frequentlyWords.add("её");
        frequentlyWords.add("новый");
        frequentlyWords.add("рука");
        frequentlyWords.add("даже");
        frequentlyWords.add("во");
        frequentlyWords.add("со");
        frequentlyWords.add("раз");
        frequentlyWords.add("где");
        frequentlyWords.add("там");
        frequentlyWords.add("под");
        frequentlyWords.add("можно");
        frequentlyWords.add("ну");
        frequentlyWords.add("какой");
        frequentlyWords.add("после");
        frequentlyWords.add("их");
        frequentlyWords.add("работа");
        frequentlyWords.add("без");
        frequentlyWords.add("самый");
        frequentlyWords.add("потом");
        frequentlyWords.add("надо");
        frequentlyWords.add("хотеть");
        frequentlyWords.add("ли");
        frequentlyWords.add("слово");
        frequentlyWords.add("идти");
        frequentlyWords.add("большой");
        frequentlyWords.add("должен");
        frequentlyWords.add("место");
        frequentlyWords.add("иметь");
        frequentlyWords.add("ничто");
        String str = "то сейчас тут лицо каждый друг нет теперь ни глаз тоже тогда видеть вопрос через да здесь дом да потому сторона какой-то думать сделать " +
                "страна жить чем мир об последний случай голова более делать что-то смотреть ребенок просто конечно сила российский конец перед несколько вид система" +
                "всегда работать между три нет понять пойти часть спросить город дать также никто понимать получить отношение лишь второй именно ваш хотя ни сидеть над" +
                "женщина оказаться русский один взять прийти являться деньги почему вдруг любить стоить почти земля общий ведь машина однако сразу хорошо вода отец высокий" +
                "остаться выйти много проблема начать хороший час это сегодня право совсем нога считать главный решение увидеть дверь казатсья образ псиать история лучший власть закон" +
                "все война бог голос найти поэтому стоять вообще тысяча больше вместе маленький книга некоторый решить любойвозможность результат ночь стол никогда " +
                "имя область молодой пройти например статья оно число компания про государственный полный принять народ никакой советский жена настоящий всякий группа" +
                "развитие процесс суд давать ответить старый условие твой пока средство помнить начало ждать свет пора путь душа куда нужно разный нужный уровень иной форма" +
                "связь уж минута кроме находиться опять многий белый собственный улица черный написать вечер снова основной качество мысль дорога мать действие месяц оставаться" +
                "государство язык любовь взгляд мама играть далекий лежать нельзя век школа подумать уйти цель среди общество посмотреть деятельность";
        String[] strr = str.split(" ");
        for(String s: strr) {
            frequentlyWords.add(s);
        }
        return frequentlyWords;
    }
    public static int newIndex(int key, int currentIndex, String letters) {
        int newIndex = currentIndex - key;
        if (newIndex == letters.length()) {
            return 0;
        }
        if (newIndex < 0) {
            return letters.length() + newIndex;
        }
        if (newIndex > 0) {
            return newIndex;
        }
        return 0;
    }
    public static int rightKey(Map<Integer, Integer> map) {
        int rightKey = 0;
        int maxValue = 0;
        Set<Map.Entry<Integer, Integer>> newSet = map.entrySet();
        for(Map.Entry<Integer, Integer> pair: newSet) {
            int key = pair.getKey();
            int value = pair.getValue();
            if(value > maxValue) {
                maxValue = value;
                rightKey = key;
            }
        }
        return rightKey;
    }
    public static int checkDotSpaceUpperLetter(String str, int counter) {
        //same as in previous alphabet var, it is much duplicated
        String lettersForCheckRule = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя!?:;-.,\" ";
        char[] chars2 = lettersForCheckRule.toCharArray();
        List<Character> listForLettersForCheck = new ArrayList<>();
        for(char c: chars2) {
            listForLettersForCheck.add(c);
        }
        int indexOfDot = str.indexOf(".");
        while(indexOfDot != -1 && indexOfDot < 9998) {
            if(str.charAt(indexOfDot + 1) == ' ' && !listForLettersForCheck.contains(str.charAt(indexOfDot + 2))){
                counter = counter + 1;
            }
            indexOfDot = str.indexOf(".", indexOfDot + 1);
        }
        return counter;
    }
    public static int checkMatchesWithFrequentlyUsedWords(String str, List<String> list, int counter) {
    String[] arrayForDecryptedList = str.split(" ");
    List<String> listWithDecryptedText = new ArrayList<>();
    for (String s: arrayForDecryptedList) {
        listWithDecryptedText.add(s.toLowerCase());
    }
    for(String s: listWithDecryptedText) {
        if(list.contains(s)) {
            counter = counter + 1;
        }
    }
    return counter;
}


}
