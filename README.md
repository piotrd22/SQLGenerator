# SQLGenerator

The application uses OpenAi API (or Azure OpenAi API) and allows to generate SQL queries from natural language.

The application allows you to configure several database connections by adding data sources in the application.yml configuration file. Supported databases at the moment are PostgreSQL, Oracle, MySQL.

A small demo is available by running the application and containers from the docker folder. You can then go to http://localhost:8080 and use the chat. Just select the appropriate database, schema and ask a question like "Give me 10 users who have min 10 orders" and the application should return the necessary SQL query.

The technologies used are Java 17, Spring and the new Spring AI module