# TPI Programación 2 - Sistema de Gestión de Dispositivos IoT

## Descripción

Sistema de gestión de dispositivos IoT (Internet of Things) con configuración de red asociada. Implementa una relación 1→1 unidireccional entre `DispositivoIoT` y `ConfiguracionRed`, utilizando el patrón DAO y Service Layer con JDBC.

Este proyecto se integra con el Trabajo Final Integrador de Base de Datos I, reutilizando el modelo de datos ya diseñado, probado y validado.

---

## 🎥 Video Demostrativo del TPI

> **⚠️ IMPORTANTE:** Este video es parte de la entrega del TPI y muestra el funcionamiento completo del sistema.

**🔗 [Ver Video en YouTube](https://youtu.be/szscB3yAgzI)**

**Contenido del video:**
- ✅ Presentación del equipo y arquitectura del proyecto
- ✅ Demostración completa del CRUD de Dispositivos IoT
- ✅ Demostración completa del CRUD de Configuraciones de Red
- ✅ Transacciones atómicas (commit y rollback)
- ✅ Explicación del código (entities, dao, service, menu)
- ✅ Verificación de datos en MySQL

**Video demostración del TFI de BD I**: https://www.youtube.com/watch?v=Pw-BVHe8esg

---

## 📚 Documentación del Proyecto

> **Todos los documentos técnicos están disponibles en la carpeta [`docs/`](docs/)**

### Guía Rápida de Documentación

| 📄 Documento | 🎯 Cuándo Consultarlo | 📍 Ubicación |
|--------------|----------------------|--------------|
| **[Informe Técnico](docs/INFORME_TECNICO.pdf)** | Para entender decisiones de diseño, arquitectura detallada, transacciones ACID, conclusiones y referencias completas | `docs/INFORME_TECNICO.pdf` |
| **[Diagrama UML Completo](docs/UML-completo.pdf)** | Para ver todas las clases con atributos, métodos, tipos de datos y relaciones detalladas | `docs/UML-completo.pdf` |
| **[Diagrama UML Simplificado](docs/UML-simplificado.pdf)** | Para entender la arquitectura general del sistema de un vistazo | `docs/UML-simplificado.pdf` |

### Contenido del Informe Técnico (8 páginas)

El informe incluye análisis completo de:

1. **Introducción**: Descripción del dominio IoT y objetivos del TPI
2. **Diseño**: Modelo de datos, decisiones de relación 1:1 con FK única, justificaciones técnicas
3. **Arquitectura**: Explicación detallada de capas (config, entities, dao, service, main)
4. **Persistencia**: Patrón de transacciones, garantías ACID, manejo de errores SQL
5. **Validaciones**: Reglas de negocio y validaciones por entidad
6. **Pruebas**: Casos de prueba, evidencias de rollback, consultas SQL ejecutadas
7. **Conclusiones**: Aprendizajes, dificultades, mejoras futuras, trabajo en equipo
8. **Referencias**: Documentación técnica, patrones, herramientas, y uso de IA (Claude Code)

---

## Integrantes

| Nombre | Email | Rol |
|--------|-------|-----|
| **Gustavo Tiseira** | gustavotiseira@gmail.com | Líder técnico / Integrador |
| **David Vergara** | david.e.vergara2025@gmail.com | Desarrollador |
| **Mauricio López** | rinaldi.el@hotmail.com | Desarrollador |

## Requisitos

- **Java 21** o superior
- **MySQL 8.0** o superior (o Docker para contenedor MySQL)
- **Apache Ant** (para compilación desde línea de comandos)
- **MySQL Connector/J 8.0.33** (incluido en `lib/`)
- **NetBeans IDE 21+** (opcional, recomendado para estudiantes)
- **Docker** (opcional, recomendado para facilitar la configuración de MySQL)

## Instalación y Configuración

> **💡 Tip:** Se recomienda usar Docker para MySQL. Simplifica la configuración y garantiza un entorno consistente entre todos los desarrolladores.

### 1. Clonar el repositorio

```bash
git clone https://github.com/brujoh88/tpi-prog2-iot.git
cd tpi-prog2-iot
```

### 2. Configurar MySQL con Docker (Recomendado)

#### Levantar el contenedor MySQL

```bash
docker run -d \
  --name mysql_tfi \
  -e MYSQL_ROOT_PASSWORD=mi_password_seguro \
  -e MYSQL_DATABASE=iot \
  -p 3306:3306 \
  mysql:8.0
```

#### ⏱️ **IMPORTANTE: Tiempo de Inicialización**

El contenedor de MySQL tarda aproximadamente **30-60 segundos** en estar completamente listo después de ejecutar el comando anterior. Durante este tiempo, MySQL está:
- Inicializando el sistema de archivos InnoDB
- Creando la base de datos `iot`
- Configurando usuarios y permisos
- Preparando el servidor para conexiones

**Espera hasta ver este mensaje en los logs:**
```
[Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.42'  socket: '/var/run/mysqld/mysqld.sock'  port: 3306
```

#### Verificar que MySQL está listo

```bash
# Ver los logs del contenedor
docker logs mysql_tfi

# Verificar que responde a conexiones
docker exec mysql_tfi mysqladmin ping -uroot -pmi_password_seguro
# Debe mostrar: mysqld is alive
```

#### Ejecutar scripts SQL

Una vez que el contenedor esté listo:

```bash
# Crear las tablas y estructura
docker exec -i mysql_tfi mysql -uroot -pmi_password_seguro iot < sql/schema.sql

# Cargar datos de prueba
docker exec -i mysql_tfi mysql -uroot -pmi_password_seguro iot < sql/data.sql
```

#### Comandos útiles de Docker

```bash
# Ver el estado del contenedor
docker ps -a | grep mysql_tfi

# Detener el contenedor
docker stop mysql_tfi

# Iniciar el contenedor (si ya existe)
docker start mysql_tfi

# Eliminar el contenedor (perderás los datos)
docker rm -f mysql_tfi

# Conectarse a MySQL desde línea de comandos
docker exec -it mysql_tfi mysql -uroot -pmi_password_seguro iot
```

### 3. Crear la Base de Datos (Alternativa sin Docker)

Si prefieres usar MySQL instalado localmente:

```bash
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/data.sql
```

### 4. Configurar Conexión

Crear archivo `config.properties` en la raíz del proyecto (o copiar desde `config.properties.example`):

```properties
db.url=jdbc:mysql://localhost:3306/iot?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=mi_password_seguro
db.driver=com.mysql.cj.jdbc.Driver
```

**Nota:** Si usas Docker, asegúrate de que el password coincida con el del contenedor (`mi_password_seguro` en el ejemplo).

### 5. Compilar y Ejecutar

El proyecto puede ejecutarse de **3 formas diferentes**. Se recomienda usar NetBeans IDE para mayor comodidad:

#### Opción 1: NetBeans IDE (Recomendado para estudiantes)

El proyecto está completamente configurado para NetBeans. Solo necesitas:

1. **Abrir el proyecto:**
   - Menú: `File → Open Project`
   - Navegar hasta la carpeta `tpi-prog2-iot`
   - NetBeans detectará automáticamente el proyecto

2. **Ejecutar la aplicación:**
   - Presiona `F6` o clic derecho en el proyecto → `Run`
   - NetBeans compilará y ejecutará automáticamente

3. **Limpiar y construir:**
   - Presiona `Shift + F11` o clic derecho → `Clean and Build`

4. **Solo compilar:**
   - Presiona `F11` o clic derecho → `Build`

**Nota:** NetBeans usa el archivo `build.xml` (Apache Ant) internamente, por lo que toda la configuración ya está lista.

#### Opción 2: Apache Ant (Línea de comandos)

Si prefieres usar la terminal o no tienes NetBeans:

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

#### Opción 3: Java directo (Sin Ant ni NetBeans)

Para ejecutar directamente con Java:

```bash
# Compilar manualmente
javac -d build -cp "lib/*" src/**/*.java

# Ejecutar
java -cp "build:lib/*" main.Main
```

## Herramientas de Desarrollo

Este proyecto es compatible con múltiples IDEs y herramientas:

### IDEs Recomendados

- **NetBeans IDE** (Recomendado): Configuración completa incluida en `nbproject/`
  - Descarga: https://netbeans.apache.org/
  - Versión recomendada: NetBeans 21 o superior
  - Soporte nativo para proyectos Ant

- **IntelliJ IDEA**: Compatible mediante importación de proyecto Ant
  - File → New → Project from Existing Sources → Seleccionar `build.xml`

- **Eclipse**: Compatible mediante plugin Ant
  - File → Import → Existing Ant Buildfile

- **Visual Studio Code**: Compatible con extensiones de Java
  - Extension Pack for Java (Microsoft)
  - Debugger for Java

### Herramientas de Línea de Comandos

- **Apache Ant**: Sistema de construcción incluido (`build.xml`)
- **Java JDK 21**: Compilador y runtime
- **Git**: Control de versiones

## Arquitectura del Proyecto

> **📐 Ver diagramas visuales en [`docs/UML-completo.pdf`](docs/UML-completo.pdf) y [`docs/UML-simplificado.pdf`](docs/UML-simplificado.pdf)**
>
> **📖 Explicación detallada de arquitectura y decisiones de diseño en [`docs/INFORME_TECNICO.pdf`](docs/INFORME_TECNICO.pdf) (Secciones 2, 3 y 4)**

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

1. Ejecutar la aplicación (usando NetBeans con `F6`, `ant run`, o `java -cp "build:lib/*" main.Main`)
2. El sistema verifica la conexión a la base de datos
3. Seleccionar opción del menú principal
4. Seguir las instrucciones en pantalla
5. Todas las operaciones usan transacciones con commit/rollback

## Solución de Problemas (Troubleshooting)

### Error: "Communications link failure"

**Problema:**
```
[DatabaseConnection] Error al obtener conexión: Communications link failure
The last packet sent successfully to the server was 0 milliseconds ago.
```

**Causas comunes:**

1. **MySQL aún no está listo** (contenedor Docker recién creado)
   - **Solución:** Espera 30-60 segundos y vuelve a intentar
   - Verifica con: `docker logs mysql_tfi | grep "ready for connections"`

2. **El contenedor no está corriendo**
   - **Solución:** `docker start mysql_tfi`
   - Verifica con: `docker ps | grep mysql_tfi`

3. **Puerto 3306 ocupado por otra instancia de MySQL**
   - **Solución:** Detén el otro MySQL o cambia el puerto del contenedor:
     ```bash
     docker run -d --name mysql_tfi -e MYSQL_ROOT_PASSWORD=mi_password_seguro -e MYSQL_DATABASE=iot -p 3307:3306 mysql:8.0
     # Actualiza config.properties: db.url=jdbc:mysql://localhost:3307/iot...
     ```

4. **Credenciales incorrectas en config.properties**
   - **Solución:** Verifica que `db.password` coincida con `MYSQL_ROOT_PASSWORD` del contenedor

5. **Base de datos 'iot' no existe**
   - **Solución:** Ejecuta los scripts SQL:
     ```bash
     docker exec -i mysql_tfi mysql -uroot -pmi_password_seguro iot < sql/schema.sql
     ```

### Error: "Access denied for user 'root'@'localhost'"

**Problema:** La contraseña en `config.properties` no coincide con la del servidor MySQL.

**Solución:**
```bash
# Si usas Docker, verifica la contraseña del contenedor
docker inspect mysql_tfi | grep MYSQL_ROOT_PASSWORD

# Actualiza config.properties con la contraseña correcta
```

### El contenedor se detiene inmediatamente después de crearlo

**Problema:** El contenedor de MySQL se crea pero se detiene de inmediato.

**Solución:**
```bash
# Ver los logs para identificar el error
docker logs mysql_tfi

# Causas comunes: puerto ya en uso, permisos insuficientes
# Eliminar y recrear el contenedor
docker rm -f mysql_tfi
docker run -d --name mysql_tfi -e MYSQL_ROOT_PASSWORD=mi_password_seguro -e MYSQL_DATABASE=iot -p 3306:3306 mysql:8.0
```

## Manejo de Transacciones

> **📖 Para entender garantías ACID, patrón de transacciones y manejo de errores SQL, consulta [`docs/INFORME_TECNICO.pdf`](docs/INFORME_TECNICO.pdf) (Sección 4)**

Ejemplo de transacción atómica (creación de dispositivo + configuración):

```java
Connection conn = null;
try {
    conn = DatabaseConnection.getConnection();
    conn.setAutoCommit(false);

    // 1. Crear DispositivoIoT PRIMERO para obtener su ID
    dispositivoDao.crear(dispositivo, conn);

    // 2. Setear el dispositivo_id en ConfiguracionRed
    configuracion.setDispositivoId(dispositivo.getId());

    // 3. Crear ConfiguracionRed con el dispositivo_id válido
    configuracionDao.crear(configuracion, conn);

    // 4. Commit si todo OK
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
├── docs/                        # 📚 Documentación técnica del TPI
│   ├── INFORME_TECNICO.pdf      # Informe completo (8 páginas)
│   ├── UML-completo.pdf         # Diagrama UML detallado
│   └── UML-simplificado.pdf     # Diagrama UML de alto nivel
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

> 🎥 **Ver la sección "Video Demostrativo del TPI" al inicio de este README**
>
> 🔗 **[Ver Video en YouTube](https://youtu.be/szscB3yAgzI)**

## Trabajo en Equipo

> **📖 Ver conclusiones sobre colaboración y uso de herramientas en [`docs/INFORME_TECNICO.pdf`](docs/INFORME_TECNICO.pdf) (Secciones 7.4 y 8.5)**

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

Proyecto académico - UTN - Tecnicatura Universitaria en Programación

## Contacto

Para consultas sobre el proyecto:
- Gustavo Tiseira: gustavotiseira@gmail.com
- David Vergara: david.e.vergara2025@gmail.com
- Mauricio López: rinaldi.el@hotmail.com

---

**Última actualización**: Noviembre 2025

**Versión**: 1.0.0
