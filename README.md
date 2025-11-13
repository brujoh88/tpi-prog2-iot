# TPI Programación 2 - Sistema de Gestión de Dispositivos IoT

## Descripción

Sistema de gestión de dispositivos IoT (Internet of Things) con configuración de red asociada. Implementa una relación 1→1 unidireccional entre `DispositivoIoT` y `ConfiguracionRed`, utilizando el patrón DAO y Service Layer con JDBC.

Este proyecto se integra con el Trabajo Final Integrador de Base de Datos I, reutilizando el modelo de datos ya diseñado, probado y validado.

**Video demostración del TFI de BD I**: https://www.youtube.com/watch?v=Pw-BVHe8esg&t=635s

## Integrantes

| Nombre | Email | Rol |
|--------|-------|-----|
| **Gustavo Tiseira** | gustavotiseira@gmail.com | Líder técnico / Integrador |
| **David Vergara** | david.e.vergara2025@gmail.com | Desarrollador |
| **Mauricio López** | rinaldi.el@hotmail.com | Desarrollador |

## Requisitos

- **Java 21** o superior
- **MySQL 8.0** o superior
- **Apache Ant** (para compilación)
- **MySQL Connector/J 8.0.33** (incluido en `lib/`)

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/TPI-Programacion2.git
cd TPI-Programacion2
```

### 2. Crear la Base de Datos

**Opción A: Usando MySQL desde línea de comandos**
```bash
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/data.sql
```

**Opción B: Usando Docker (si tienes el contenedor corriendo)**
```bash
docker exec -i mysql_tfi mysql -u root -pmi_password_seguro < sql/schema.sql
docker exec -i mysql_tfi mysql -u root -pmi_password_seguro < sql/data.sql
```

### 3. Configurar Conexión

Crear archivo `config.properties` en la raíz del proyecto (o copiar desde `config.properties.example`):

```properties
db.url=jdbc:mysql://localhost:3306/iot?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=tu_password_aqui
db.driver=com.mysql.cj.jdbc.Driver
```

### 4. Compilar y Ejecutar

**Compilar el proyecto:**
```bash
ant compile
```

**Ejecutar la aplicación:**
```bash
ant run
```

**Limpiar archivos compilados:**
```bash
ant clean
```

**Crear JAR ejecutable:**
```bash
ant jar
```

## Arquitectura del Proyecto

### Estructura de Paquetes

```
src/
├── config/          # DatabaseConnection (Singleton)
├── entities/        # DispositivoIoT, ConfiguracionRed
├── dao/             # GenericDao, DAOs concretos con PreparedStatement
├── service/         # GenericService, Services con transacciones
├── exceptions/      # Excepciones personalizadas
├── util/            # Validator, InputHelper
└── main/            # Main, AppMenu
```

### Patrones Implementados

- **DAO (Data Access Object)**: Separación de lógica de acceso a datos
- **Service Layer**: Lógica de negocio y gestión de transacciones
- **Singleton**: DatabaseConnection
- **Strategy**: GenericDao/GenericService

## Funcionalidades

### Gestión de Dispositivos IoT

- ✅ Crear dispositivo (con o sin configuración de red)
- ✅ Leer dispositivo por ID
- ✅ Listar todos los dispositivos activos
- ✅ Buscar por serial (campo único)
- ✅ Buscar por ubicación
- ✅ Actualizar información del dispositivo
- ✅ Eliminar lógico (baja lógica)

### Gestión de Configuraciones de Red

- ✅ Crear configuración de red
- ✅ Leer configuración por ID
- ✅ Listar todas las configuraciones
- ✅ Buscar por IP
- ✅ Actualizar configuración
- ✅ Eliminar lógico

### Operaciones Avanzadas

- ✅ **Crear dispositivo + configuración en transacción atómica** (commit/rollback)
- ✅ Ver estadísticas del sistema

### Validaciones Implementadas

- Serial único y formato `XXX-XXXX` (ej: `SER-A001`)
- Formato IPv4 válido con regex
- Coherencia DHCP vs IP estática
- Integridad referencial 1→1
- Longitud de campos
- Conversión automática a mayúsculas (serial, modelo)

## Base de Datos

### Modelo de Datos

**Relación 1→1 Unidireccional:**
- `DispositivoIoT` (Clase A) → `ConfiguracionRed` (Clase B)
- Implementada mediante FK única en `ConfiguracionRed.dispositivo_id`

**Tablas:**

| Tabla | Campos | Constraints |
|-------|--------|-------------|
| `DispositivoIoT` | id, eliminado, serial, modelo, ubicacion, firmwareVersion | PK, UNIQUE(serial) |
| `ConfiguracionRed` | id, eliminado, ip, mascara, gateway, dnsPrimario, dhcpHabilitado, dispositivo_id | PK, UNIQUE(ip), UNIQUE(dispositivo_id), FK |

**Vista:**
- `Vista_Inventario_Red_Activo`: combina ambas tablas (solo registros activos)

### Scripts SQL

- `sql/schema.sql`: Creación de BD, tablas, constraints, índices y vista
- `sql/data.sql`: 10 registros de prueba validados

## Flujo de Uso

1. Ejecutar `ant run` o `java -cp "build:lib/*" main.Main`
2. El sistema verifica la conexión a la base de datos
3. Seleccionar opción del menú principal
4. Seguir las instrucciones en pantalla
5. Todas las operaciones usan transacciones con commit/rollback

## Manejo de Transacciones

Ejemplo de transacción atómica (creación de dispositivo + configuración):

```java
Connection conn = null;
try {
    conn = DatabaseConnection.getConnection();
    conn.setAutoCommit(false);

    // 1. Crear ConfiguracionRed
    configuracionDao.crear(configuracion, conn);

    // 2. Crear DispositivoIoT asociado
    dispositivoDao.crear(dispositivo, conn);

    // 3. Commit si todo OK
    conn.commit();

} catch (Exception e) {
    // Rollback en caso de error
    if (conn != null) conn.rollback();
    throw e;

} finally {
    if (conn != null) {
        conn.setAutoCommit(true);
        conn.close();
    }
}
```

## Manejo de Errores SQL

El sistema mapea códigos de error MySQL a excepciones de negocio:

| Código | Tipo | Excepción |
|--------|------|-----------|
| 1062 | Duplicate entry | `DuplicateEntityException` |
| 1452 | Foreign key fails | `EntityNotFoundException` |
| 3819 | Check constraint | `ValidationException` |
| 1213 | Deadlock | `ConcurrencyException` |

## Relación con TFI de Base de Datos I

Este proyecto reutiliza completamente el modelo de datos diseñado en el TFI de BD I:

- **Volumen de datos**: Base con más de 81,920 registros de prueba disponibles
- **Scripts validados**: Todos los scripts SQL fueron probados en el TFI anterior
- **Vista reutilizada**: `Vista_Inventario_Red_Activo`
- **Constraints probados**: PRIMARY KEY, FOREIGN KEY, UNIQUE, CHECK, índices

## Tecnologías

- Java 21
- JDBC (MySQL Connector/J 8.0.33)
- MySQL 8.0
- Apache Ant (gestión de compilación)

## Capturas de Pantalla

### Menú Principal
```
═══════════════════════════════════════════════════════════
    MENÚ PRINCIPAL
═══════════════════════════════════════════════════════════
  1. Gestión de Dispositivos IoT
  2. Gestión de Configuraciones de Red
  3. Operaciones Avanzadas
  4. Salir
```

### Transacción Atómica
```
  Iniciando transacción...
[DispositivoIoTService] ConfiguracionRed creada con ID: 11
[DispositivoIoTService] DispositivoIoT creado con ID: 11
[DispositivoIoTService] Transacción completada exitosamente
✅ ¡Transacción completada exitosamente!
```

## Testing

### Pruebas Manuales Realizadas

- ✅ CRUD completo de DispositivoIoT
- ✅ CRUD completo de ConfiguracionRed
- ✅ Transacción atómica con commit
- ✅ Transacción con rollback (simulando error)
- ✅ Validación de duplicados (serial, IP)
- ✅ Validación de formatos (IP, serial, firmware)
- ✅ Baja lógica de entidades
- ✅ Búsquedas por campos únicos

### Consultas SQL Útiles

```sql
-- Ver todos los dispositivos con su configuración
SELECT * FROM Vista_Inventario_Red_Activo;

-- Verificar relación 1:1
SELECT COUNT(*) AS total, COUNT(DISTINCT dispositivo_id) AS unicos
FROM ConfiguracionRed WHERE eliminado = FALSE;

-- Ver dispositivos sin configuración
SELECT * FROM DispositivoIoT d
LEFT JOIN ConfiguracionRed c ON d.id = c.dispositivo_id
WHERE d.eliminado = FALSE AND c.id IS NULL;
```

## Estructura del Repositorio

```
TPI-Programacion2/
├── README.md                    # Este archivo
├── build.xml                    # Configuración de Ant
├── config.properties.example    # Plantilla de configuración
├── .gitignore                   # Archivos ignorados por Git
├── sql/
│   ├── schema.sql               # Creación de BD y tablas
│   └── data.sql                 # Datos de prueba
├── lib/
│   └── mysql-connector-j-8.0.33.jar
└── src/
    ├── config/
    ├── entities/
    ├── dao/
    ├── service/
    ├── exceptions/
    ├── util/
    └── main/
```

## Video Demostrativo

🎥 **[Enlace al video]** - PENDIENTE

Contenido del video (10-15 minutos):
1. Presentación del equipo
2. Demostración CRUD completa
3. Explicación de código (entities, dao, service, menu)
4. Demostración de transacción con rollback
5. Verificación en base de datos

## Trabajo en Equipo

### División de Tareas

**Fase 1 - Gustavo Tiseira:**
- Configuración inicial del proyecto
- Capa `config`: DatabaseConnection
- Capa `entities`: DispositivoIoT, ConfiguracionRed
- Capa `dao`: GenericDao, DAOs concretos

**Fase 2 - David Vergara:**
- Capa `exceptions`: Excepciones personalizadas
- Capa `util`: Validator, InputHelper
- Capa `service`: Lógica de negocio con transacciones

**Fase 3 - Mauricio López:**
- Scripts SQL: schema.sql, data.sql
- Capa `main`: Main, AppMenu
- Documentación: README.md

### Commits por Autor

```bash
# Ver estadísticas de commits
git shortlog -sn --all

# Ver commits por autor
git log --author="Gustavo" --oneline
git log --author="David" --oneline
git log --author="Mauricio" --oneline
```

## Licencia

Proyecto académico - UTN FRVM - Tecnicatura Universitaria en Programación

## Contacto

Para consultas sobre el proyecto:
- Gustavo Tiseira: gustavotiseira@gmail.com
- David Vergara: david.e.vergara2025@gmail.com
- Mauricio López: rinaldi.el@hotmail.com

---

**Última actualización**: Noviembre 2024

**Versión**: 1.0.0
