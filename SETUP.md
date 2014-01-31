Настройка окружения
===================

Необходимые технологии
----------------------
1. **Oracle jdk7.0_51** [(link)](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) - JAVA
2. **Apache Maven 3.1.1** [(link)](http://maven.apache.org/download.cgi) - Сборка проекта, менеджер зависимостей java библиотек
3. **Bower 1.2.8** *опционально* [(link)](http://bower.io/) - Менеджер зависимостей фронтенда (javascript, css)
4. **PostgresSql 9.2** [(link)](http://www.postgresql.org/download/) - База данных
5. **Flyway 2.3.1** *скачивается автоматически скриптом* [(link)](http://flywaydb.org/getstarted/download.html) - Репозиторий базы данных.
6. **Apache tomcat 7.x** [(link)](http://tomcat.apache.org/download-70.cgi) - Вебсервер.

Настройка базы данных
----------------------
1. Создать пользователя **pricegsmowner** с паролем **pricegsm5266** с правами **супервизора**
2. Создать базу данных **pricegsm2**, назначить владельцем **pricegsmowner**.
3. Инициализировать flyway, выполнив команду <br/>
`flyway/flyway.sh init`
<br/>
4. Мигририровать данные из flyway <br/>
`flyway/flyway.sh migrate`

Сборка проекта
--------------

1. Выполнить команду maven <br/>
`mvn clean package`

Настройка Tomcat
----------------

изменить файл **CATALINA_HOME/conf/server.xml**, добавив **URIEncoding="UTF-8"** в коннектор

`<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" URIEncoding="UTF-8"/>`

Вход в систему
--------------

Клиент:
<br/>
seller@pricegsm.com 12345
<br/>
buyer@pricegsm.com 12345

Админ:
<br/>
admin@pricegsm.com 12345

Запуск парсера вручную
----------------------

[http://localhost:8080/pricegsm/exchange](http://localhost:8080/pricegsm/exchange) - текущие курсы валют.
<br/>
[http://localhost:8080/pricegsm/yandex](http://localhost:8080/pricegsm/yandex) - парсер яндекс маркета.