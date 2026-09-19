# Лабораторна робота №1

## Варіант
18 — Метеостанція

## Призначення програми
Консольна Java-програма читає погодні спостереження з текстового файла, перевіряє коректність записів, пропускає помилкові рядки та формує підсумковий звіт.

## Формат вхідних даних
Кожний рядок має формат:

date;temperature;humidity;pressure;wind

Приклад:

2026-09-01;18.5;62.0;1013.2;3.4

## Результати
Програма обчислює:
- кількість коректних записів;
- мінімальну температуру;
- середню вологість;
- найбільшу швидкість вітру.

## Файл за замовчуванням
Вхід:
data/input.csv

Вихід:
out/report.txt

## Запуск

Компіляція:

javac -encoding UTF-8 -d target/classes src/main/java/ua/lpnu/kzp/Main.java

Звичайний запуск:

java -cp target/classes ua.lpnu.kzp.Main

Довідка:

java -cp target/classes ua.lpnu.kzp.Main --help

Явне задання вхідного і вихідного файла:

java -cp target/classes ua.lpnu.kzp.Main --input data/input.csv --output out/custom-report.txt


## Maven

Проєкт використовує Maven Wrapper, тому для збірки не потрібна окрема локальна версія Maven.

На macOS та Ubuntu замість `.\mvnw.cmd` використовується `./mvnw`.

Запуск тестів:

```powershell
.\mvnw.cmd test
```

Перевірка тестів і статичного аналізу SpotBugs:

```powershell
.\mvnw.cmd verify
```

Створення виконуваного JAR:

```powershell
.\mvnw.cmd package
```

Після успішної збірки виконуваний JAR створюється у каталозі `target`.

Запуск JAR:

```powershell
java -jar target/lab01-1.0.0.jar
```

Виведення довідки:

```powershell
java -jar target/lab01-1.0.0.jar --help
```