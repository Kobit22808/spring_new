# Используем официальный образ JDK 21
FROM eclipse-temurin:21 AS build

# Устанавливаем Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

# Устанавливаем рабочую директорию
WORKDIR /demo

# Копируем pom.xml
COPY pom.xml .

# Загружаем зависимости
RUN mvn dependency:go-offline

# Копируем остальную часть приложения
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Проверяем, что .jar файл создан
RUN ls -al /demo/target 

# Создаем исполняемый образ
FROM eclipse-temurin:21-jre

# Устанавливаем рабочую директорию для исполняемого приложения
WORKDIR /demo

# Копируем собранный .jar файл из предыдущего этапа
COPY --from=build /demo/target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar

# Указываем, как запускать приложение
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]