# 📱 Yoinks Mobile (Android)

Aplicación nativa para Android de **Yoinks** diseñada en **Kotlin** y **Jetpack Compose**.
Descarga música en **320 kbps (CBR)** y videos con metadatos y carátulas integradas.

---

## ⚡ Compilación Automática en la Nube con GitHub Actions (0 GB en tu PC)

No necesitas instalar **Android Studio** ni descargar SDKs pesados. GitHub compilará el archivo `.apk` gratis en sus servidores.

### Paso 1: Crea un repositorio nuevo en GitHub
1. Entra a tu cuenta en [github.com/new](https://github.com/new).
2. Ponle de nombre al repositorio: `yoinks-android`.
3. Puedes dejarlo en **Público** o **Privado**.
4. Haz clic en **Create repository** (no marques la casilla de "Add a README file").

---

### Paso 2: Sube el código a tu repositorio
Abre una terminal (PowerShell o CMD) dentro de esta carpeta `yoinks-android` y ejecuta (reemplaza `TU_USUARIO` por tu usuario de GitHub):

```sh
git init
git add .
git commit -m "Initial commit Yoinks Android"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/yoinks-android.git
git push -u origin main
```

*(O simplemente ejecuta el archivo `Subir_a_GitHub.bat` que te dejamos en esta carpeta y te pedirá la URL).*

---

### Paso 3: Descarga el APK directamente a tu Celular
1. Ve a tu repositorio en GitHub desde tu computadora o directamente desde el navegador de tu teléfono.
2. Toca en la pestaña **Actions** (arriba).
3. Verás la tarea **Build Android APK** ejecutándose en verde (~2 a 3 minutos).
4. Haz clic en la ejecución terminada y baja hasta la sección **Artifacts**.
5. Descarga **`Yoinks-App-Debug`** (es un archivo `.zip` que contiene tu instalador **`.apk`**).
6. ¡Ábrelo en tu teléfono y dale a **Instalar**!

---

## ✨ Características de la App en Android
- **Compartir directamente desde YouTube:** Abre cualquier video en YouTube, dale a "Compartir", elige Yoinks y comenzará a procesarlo.
- **Calidad de Estudio:** Soporta presets de MP3 en 320 kbps con ID3 Tags y carátulas automáticas.
- **Interfaz Moderna:** Diseño oscuro Material 3 con Jetpack Compose.
- **Colecciones y Playlists:** Soporte para enlaces directos y listas.
