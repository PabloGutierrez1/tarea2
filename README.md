# Tarea 2 - Paradigmas de Programación

Sistema de generación de rutinas de ejercicio personalizadas utilizando Java Swing.

## Alcances y Supuestos
- **Frecuencia y exclusión**: Se asume que el sistema genera rutinas personalizadas y que cada ejercicio asignado no se repetirá para el mismo cliente en semanas consecutivas.
- **Datos**: Los ejercicios se cargan dinámicamente desde un archivo CSV.
- **Estructura**: Se implementa una arquitectura desacoplada utilizando el patrón Pub/Sub a través de eventos personalizados (`Escucha` y `Evento`).

## Estructura de Datos
El archivo de datos se encuentra en `data/ejercicios.csv` con el siguiente formato:
```csv
codigo;nombre;tipo;intensidad;tiempoMin;descripcion;ultimaSemanaUsado
```

## Instrucciones de Ejecución

Para compilar y ejecutar el proyecto desde la raíz del mismo, ejecuta los siguientes comandos en tu terminal:

### 1. Compilación
```bash
javac -d out backend/*.java frontend/*.java
```

### 2. Ejecución
```bash
java -cp out frontend.App
```
