# Distributed databases homework

## 1. Попередні вимоги
- Встановлено Java 25

## 2. Підготовка
### Запуск серверу
```bash
cd server
./mvnw spring-boot:run
```

### Збираємо клієнт
```bash
cd client
./mvnw clean package
```

### Запуск клієнту
```bash
$ java -jar target/hw1-client-0.0.1-SNAPSHOT.jar <counter-type> <parallel-clients> <requests-per-client>
```
`counter-type` - тип лічильника. Може приймати одне із значень:  
    - `in-memory`  
    - `file-based`  
    - `postgres/lost-update`  
    - `postgres/serializable-update-v1`  
    - `postgres/serializable-update-v2`  
    - `postgres/in-place-update`  
    - `postgres/row-level-locking`  
    - `postgres/optimistic-lock`  

Приклад виклику:
```bash
$ java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 2 10000
```

## 3. Завдання 1: Web counter

### 3.1 Запуск тест-кейсів
```bash
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 1 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 2 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 5 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 10 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar file-based 1 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar file-based 2 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar file-based 5 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar file-based 10 10000
```

### 3.2. Результати виконання
| Тип лічильника        | Кількість клієнтів | Кількість запитів на клієнт   | Кінцеве значення  | Час виконання, сек    | Пропускна здатність, запитів/сек    |
| ------------          | ---------          | -------------                 | -------------     | -------------         | -------------                       | 
| in-memory             | 1                  | 10000                         | 10000             | 9.853                 | 1014.89                             |
| in-memory             | 2                  | 10000                         | 20000             | 8.301                 | 2409.45                             |
| in-memory             | 5                  | 10000                         | 50000             | 6.776                 | 7379.20                             |
| in-memory             | 10                 | 10000                         | 100000            | 9.136                 | 10945.31                            |
| file-based            | 1                  | 10000                         | 10000             | 10.399                | 961.62                              |
| file-based            | 2                  | 10000                         | 20000             | 9.502                 | 2104.74                             |
| file-based            | 5                  | 10000                         | 50000             | 16.765                | 2982.41                             |
| file-based            | 10                 | 10000                         | 100000            | 32.673                | 3060.59                             |

## 4. Завдання 2: Postgres massive insert

### 4.1 Запуск тест-кейсів
```bash
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar postgres/lost-update 10 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar postgres/serializable-update-v1 10 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar postgres/serializable-update-v2 10 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar postgres/in-place-update 10 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar postgres/row-level-locking 10 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar postgres/optimistic-lock 10 10000
```

### 4.2 Результати виконання
| Тип лічильника        | Кількість клієнтів | Кількість запитів на клієнт   | Кінцеве значення  | Час виконання, сек    | Пропускна здатність, запитів/сек    |
| ------------          | ---------          | -------------                 | -------------     | -------------         | -------------                       | 
| postgres/lost-update  | 10                 | 10000                         | 10274             | 387.193               | 258.27                              |
| postgres/serializable-update-v1             | 10                  | 10000                         | 13169             | 87.407                   | 1144.07                             |
| postgres/serializable-update-v2             | 10                  | 10000                         | 100000             | 623.044                   | 160.50                             |
| postgres/in-place-update             | 10                  | 10000                         | 100000             | 406.603                   | 245.94                             |
| postgres/row-level-locking             | 10                  | 10000                         | 100000             | 490.472                   | 203.89                             |
| postgres/optimistic-lock             | 10                  | 10000                         | 100000             | 484.152                   | 206.55                             |