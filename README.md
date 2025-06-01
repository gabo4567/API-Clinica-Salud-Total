# Clínica Salud Total - API REST

Proyecto desarrollado como parte de la materia **Metodología de Sistemas I** de la **Tecnicatura Universitaria en Programación** en **ITG - UTN Resistencia**.

## 🧑‍💻 Autor

- **Juan Gabriel Pared**
- 2° año
- Profesor: **Marcos Sosa**

---

## 📌 Descripción

Esta API RESTful gestiona el funcionamiento de la clínica “Salud Total”. Permite administrar pacientes, profesionales, especialidades, turnos médicos, reportes administrativos y consultas desde el sitio web.

La arquitectura está pensada para ser modular, extensible e integrable fácilmente con frontends web o móviles.

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **MySQL**
- **Maven / Gradle**
- **Lombok**
- **Swagger**
- **JUnit + MockMvc**

---

## 📚 Módulos implementados

1. **Gestión de Pacientes**
2. **Gestión de Profesionales**
3. **Gestión de Especialidades**
4. **Gestión de Turnos**
5. **Reportes**
6. **Área de contacto web**

---

## 🔗 Principales Endpoints REST

### 📁 Pacientes – `/api/pacientes`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/` | Registrar nuevo paciente |
| `GET` | `/` | Listar todos los pacientes |
| `GET` | `/{id}` | Buscar paciente por ID |
| `GET` | `/dni/{dni}` | Buscar paciente por DNI |
| `GET` | `/nombre/{nombre}` | Buscar pacientes por nombre |
| `GET` | `/apellido/{apellido}` | Buscar pacientes por apellido |
| `PUT` | `/{id}` | Actualizar datos de paciente |
| `DELETE` | `/{id}` | Eliminar paciente |

---

### 📅 Turnos – `/api/turnos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/paciente/{id}/futuros` | Listar turnos futuros de un paciente |
| `GET` | `/paciente/{id}/pasados` | Listar turnos pasados de un paciente |
| `GET` | `/{id}` | Obtener turno por ID |
| `POST` | `/` | Crear nuevo turno |
| `PUT` | `/{id}` | Actualizar turno existente |
| `DELETE` | `/{id}` | Eliminar turno |
| `GET` | `/profesional/{id}` | Turnos de un profesional |
| `GET` | `/estado/{id}` | Turnos por estado |
| `GET` | `/fecha?fecha=YYYY-MM-DD` | Turnos en una fecha específica |
| `GET` | `/profesional/{id}/disponibilidad?fecha=YYYY-MM-DD` | Turnos de un profesional en una fecha (ver disponibilidad) |

---

### 📊 Reportes – `/api/turnos/reportes`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/turnos-atendidos?profesionalId=1&fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD` | Cantidad de turnos atendidos por profesional |
| `GET` | `/turnos-cancelados-reprogramados?fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD` | Reporte de turnos cancelados y reprogramados |
| `GET` | `/tasa-cancelacion-por-especialidad?fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD` | Tasa de cancelación por especialidad |

---

## ⚙️ Requisitos previos

- JDK 17+
- IntelliJ IDEA
- MySQL 8+
- Maven o Gradle
- Postman o Swagger para pruebas
