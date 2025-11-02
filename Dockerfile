### Primeira fase compila a aplicação e cria o .jar

# Instalando a imagem docker com o java 21 e maven
FROM maven:3.9.7-eclipse-temurin-21-alpine AS builder

# Definindo o app como diretório de trabalho
WORKDIR /app

# Copiando as dependências do pom.xml
COPY pom.xml .

# Baixando as dependências do projeto
RUN mvn dependency:go-offline

# Copiando o código fonte da aplicação
COPY src ./src

# Executando o build da aplicação
RUN mvn clean package -DskipTests

### Segunda fase cria a imagem final para rodar a aplicação

# Instalando a imagem docker com o java 17 mais leve
FROM amazoncorretto:25.0.0-alpine3.22

# Definindo o app como diretório de trabalho
WORKDIR /app

# Copia APENAS o arquivo .jar gerado no estágio anterior e nomeando como app.jar
COPY --from=builder /app/target/projeto-nuvem-0.0.1-SNAPSHOT.jar app.jar

# Informa ao docker para escutar a porta 8080
EXPOSE 8080

# Comandos para iniciar a aplicação
ENTRYPOINT [ "java", "-jar", "app.jar" ]