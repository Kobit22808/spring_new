# Используем официальный образ JDK
FROM eclipse-temurin:21-jdk AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл pom.xml
COPY pom.xml .

# Устанавливаем Maven
RUN apt-get update && apt-get install -y maven

# Загружаем зависимости
RUN mvn dependency:go-offline

# Копируем остальную часть приложения
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Используем другой базовый образ для выполнения приложения
FROM eclipse-temurin:21-jre

# Устанавливаем рабочую директорию для исполняемого приложения
WORKDIR /app

# Копируем собранный .jar файл из предыдущего этапа
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar

# Указываем, как запускать приложение
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
# build phase
COPY . /app/.

RUN --mount=type=cache,id=s/4a811ef9-94ae-4b09-80f2-6b651b0ae09f-m2/repository,target=/app/.m2/repository \
    mvn dependency:tree && \
    mvn -DoutputFile=target/mvn-dependency-list.log -DskipTests clean dependency:list install