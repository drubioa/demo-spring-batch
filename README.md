# Demo Spring Batch con JPA y H2 

Prueba de concepto de Spring Batch con un ItemReader y ItemWriter que se conecta mediante JPA a una bd en h2. Se incluye script para generar bd con registros de prueba.

## Pre-requisitos

Java 8 y Maven.


## Construcción

```bash
mvn clean install
mvn spring-boot:run
```

## Despliegue

Para acceder a la base de datos 


|Url|Usuario|Contraseña|
|---|-------|----------|
|http://localhost:8080|sa|sa|



