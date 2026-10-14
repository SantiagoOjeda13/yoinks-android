@echo off
title Subir Yoinks Android a GitHub
cls
echo ========================================================
echo   SUBIR YOINKS ANDROID A GITHUB (Compilador en la Nube)
echo ========================================================
echo.
echo Este script subira el proyecto a tu repositorio de GitHub
echo para que GitHub Actions compile el archivo APK gratis.
echo.

set /p REPO_URL="Pega la URL de tu repositorio de GitHub (ej. https://github.com/usuario/yoinks-android.git): "

if "%REPO_URL%"=="" (
    echo.
    echo ERROR: No ingresaste ninguna URL.
    pause
    exit /b
)

echo.
echo [1/4] Inicializando Git...
git init
git branch -M main

echo [2/4] Agregando archivos...
git add .

echo [3/4] Creando commit...
git commit -m "Compilar Yoinks Android APK via GitHub Actions"

echo [4/4] Conectando y subiendo a GitHub...
git remote remove origin 2>nul
git remote add origin %REPO_URL%
git push -u origin main --force

echo.
echo ========================================================
echo   EXITO: Codigo subido a GitHub!
echo ========================================================
echo Ahora ve a la pestana "Actions" en tu repositorio de GitHub
echo y podras descargar tu archivo APK en 2 minutos.
echo.
pause
