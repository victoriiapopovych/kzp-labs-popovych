package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Лабораторна робота № 1.
 * Варіант 18 — «Метеостанція».
 */
public final class Main {

    private static final Path DEFAULT_INPUT = Path.of("data", "input.csv");
    private static final Path DEFAULT_OUTPUT = Path.of("out", "report.txt");
    private static final int FIELD_COUNT = 5;

    private Main() {
    }

    public static void main(String[] args) {
        Path input = DEFAULT_INPUT;
        Path output = DEFAULT_OUTPUT;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help" -> {
                    printHelp();
                    return;
                }

                case "--input" -> {
                    if (i + 1 >= args.length) {
                        System.err.printf(
                                "Помилка: після --input потрібно вказати шлях.%n");
                        return;
                    }

                    input = Path.of(args[++i]);
                }

                case "--output" -> {
                    if (i + 1 >= args.length) {
                        System.err.printf(
                                "Помилка: після --output потрібно вказати шлях.%n");
                        return;
                    }

                    output = Path.of(args[++i]);
                }

                default -> {
                    System.err.printf(
                            "Помилка: невідомий аргумент \"%s\".%n",
                            args[i]);
                    printHelp();
                    return;
                }
            }
        }

        processFile(input, output);
    }

    private static void processFile(Path input, Path output) {
        final List<String> lines;

        try {
            lines = Files.readAllLines(input, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.printf(
                    "Не вдалося прочитати файл \"%s\": %s%n",
                    input,
                    exception.getMessage());
            return;
        }

        int validCount = 0;
        double minTemperature = Double.POSITIVE_INFINITY;
        double totalHumidity = 0.0;
        double maxWind = Double.NEGATIVE_INFINITY;

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            int lineNumber = index + 1;

            if (line.isBlank()) {
                printSkippedLine(lineNumber, "порожній рядок");
                continue;
            }

            // -1 зберігає порожнє останнє поле після розділення.
            String[] fields = line.split(";", -1);

            if (fields.length != FIELD_COUNT) {
                printSkippedLine(
                        lineNumber,
                        "очікується 5 полів, отримано %d"
                                .formatted(fields.length));
                continue;
            }

            String date = fields[0].trim();

            if (date.isEmpty()) {
                printSkippedLine(
                        lineNumber,
                        "дата не може бути порожньою");
                continue;
            }

            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                printSkippedLine(
                        lineNumber,
                        "дата повинна мати формат YYYY-MM-DD");
                continue;
            }

            try {
                double temperature =
                        Double.parseDouble(fields[1].trim());
                double humidity =
                        Double.parseDouble(fields[2].trim());
                double pressure =
                        Double.parseDouble(fields[3].trim());
                double wind =
                        Double.parseDouble(fields[4].trim());

                if (!Double.isFinite(temperature)
                        || !Double.isFinite(humidity)
                        || !Double.isFinite(pressure)
                        || !Double.isFinite(wind)) {

                    printSkippedLine(
                            lineNumber,
                            "числові значення мають бути скінченними");
                    continue;
                }

                if (humidity < 0.0) {
                    printSkippedLine(
                            lineNumber,
                            "вологість не може бути від'ємною");
                    continue;
                }

                if (pressure < 0.0) {
                    printSkippedLine(
                            lineNumber,
                            "тиск не може бути від'ємним");
                    continue;
                }

                if (wind < 0.0) {
                    printSkippedLine(
                            lineNumber,
                            "швидкість вітру не може бути від'ємною");
                    continue;
                }

                // Статистику оновлюємо тільки після повної перевірки запису.
                validCount++;
                minTemperature = Math.min(minTemperature, temperature);
                totalHumidity += humidity;
                maxWind = Math.max(maxWind, wind);

            } catch (NumberFormatException exception) {
                printSkippedLine(
                        lineNumber,
                        "температура, вологість, тиск і вітер "
                                + "повинні бути числами");
            }
        }

        if (validCount == 0) {
            String report = String.format(
                    Locale.ROOT,
                    "Метеостанція — варіант 18%n"
                            + "Коректних записів: 0%n"
                            + "Показники не обчислено: "
                            + "немає коректних записів.%n");

            System.out.print(report);
            writeReport(output, report);
            return;
        }

        double averageHumidity = totalHumidity / validCount;

        String report = String.format(
                Locale.ROOT,
                "Метеостанція — варіант 18%n"
                        + "Коректних записів: %d%n"
                        + "Мінімальна температура: %.2f%n"
                        + "Середня вологість: %.2f%n"
                        + "Найбільша швидкість вітру: %.2f%n",
                validCount,
                minTemperature,
                averageHumidity,
                maxWind);

        System.out.print(report);
        writeReport(output, report);
    }

    private static void writeReport(Path output, String report) {
        try {
            Path parent = output.getParent();

            // Для "report.txt" батьківського каталогу може не бути.
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(
                    output,
                    report,
                    StandardCharsets.UTF_8);

            System.out.printf(
                    "Звіт записано у: %s%n",
                    output);

        } catch (IOException exception) {
            System.err.printf(
                    "Не вдалося записати звіт \"%s\": %s%n",
                    output,
                    exception.getMessage());
        }
    }

    private static void printSkippedLine(
            int lineNumber,
            String reason) {

        System.err.printf(
                "Пропущено рядок %d: %s%n",
                lineNumber,
                reason);
    }

    private static void printHelp() {
        System.out.printf(
                "Лабораторна робота № 1, варіант 18 — Метеостанція%n"
                        + "%n"
                        + "Використання:%n"
                        + "  java ua.lpnu.kzp.Main [параметри]%n"
                        + "%n"
                        + "Параметри:%n"
                        + "  --help            показати цю довідку%n"
                        + "  --input <файл>    вхідний UTF-8 файл%n"
                        + "  --output <файл>   файл звіту%n"
                        + "%n"
                        + "За замовчуванням:%n"
                        + "  input:  data/input.csv%n"
                        + "  output: out/report.txt%n");
    }
}