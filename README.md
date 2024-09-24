# MoviApp

MoviApp es una aplicación de distribución de alimentos que permite a los usuarios realizar compras de productos y gestionar el despacho a domicilio. La aplicación incluye características como autenticación de usuarios, cálculo automático de tarifas de despacho, y monitoreo de temperatura para garantizar la cadena de frío de productos sensibles.

## Características

- **Registro y autenticación**: Los usuarios pueden registrarse e iniciar sesión utilizando cuentas de Google.
- **Gestión de pedidos**: Los usuarios pueden realizar pedidos de productos y calcular automáticamente las tarifas de despacho según las reglas de negocio.
- **Monitoreo de temperatura**: La aplicación incluye un sistema para verificar la temperatura de los productos que deben mantenerse congelados.
- **Interfaz amigable**: Un diseño limpio y funcional que facilita la navegación y el uso de la aplicación.

## Requisitos

- Android Studio
- Kotlin
- Dependencias de Firebase:
  - Firebase Authentication
  - Firebase Realtime Database
  - Firebase Cloud Messaging (para notificaciones)

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Shadownlook/MoviApp.git

Abre el proyecto en Android Studio.

Configura Firebase:

Crea un nuevo proyecto en Firebase Console.
Agrega la configuración de tu aplicación en el archivo google-services.json.
Asegúrate de habilitar las bases de datos y la autenticación necesarias.
Sincroniza el proyecto con Gradle.

Ejecuta la aplicación en un emulador o dispositivo físico.

Uso
Al iniciar la aplicación, los usuarios pueden registrarse o iniciar sesión.
Después de iniciar sesión, los usuarios pueden realizar pedidos y ver el menú de productos disponibles.
La aplicación calculará automáticamente la tarifa de despacho según las reglas definidas.
Si se realizan pedidos de productos que requieren control de temperatura, se emitirá una alarma si la temperatura supera el límite establecido.
Contribución
Las contribuciones son bienvenidas. Si deseas contribuir a este proyecto, por favor sigue estos pasos:

Haz un fork del repositorio.
Crea una nueva rama (git checkout -b feature-nueva).
Realiza tus cambios y haz un commit (git commit -m 'Añadir nueva característica').
Envía tus cambios a tu repositorio (git push origin feature-nueva).
Abre un Pull Request.
Licencia
Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más información.

Contacto
Si tienes preguntas o sugerencias, no dudes en abrir un issue en el repositorio o contactar al autor.
