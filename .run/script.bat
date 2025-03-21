@echo off
setlocal

:: Obtener la ruta del repositorio y corregir barras
for /f "delims=" %%A in ('git rev-parse --show-toplevel') do set REPO_ROOT=%%A
set REPO_PATH=%REPO_ROOT:/=\%

:: Definir la ruta del archivo de propiedades
set ARCHETYPE_PROPERTIES_PATH=src\test\resources\projects\it-basic\archetype.properties
set PROPERTIES_PATH=%1
if "%PROPERTIES_PATH%"=="" set PROPERTIES_PATH=%REPO_PATH%\%ARCHETYPE_PROPERTIES_PATH%
set PROPERTIES_PATH_FULL=%REPO_PATH%\%PROPERTIES_PATH:/=\%

echo REPO_PATH: %REPO_PATH%
echo PROPERTIES_PATH: %PROPERTIES_PATH%
echo PROPERTIES_PATH_FULL: %PROPERTIES_PATH_FULL%

:: Verificar si el archivo de propiedades existe
if not exist "%PROPERTIES_PATH_FULL%" (
    echo El archivo %PROPERTIES_PATH_FULL% no existe.
    exit /b 1
)

:: Propiedades obligatorias
set mandatory_properties=groupId artifactId version package appName inceptionYear

:: Leer el archivo .properties y almacenar las claves y valores
for /F "tokens=1,2 delims==" %%A in (%PROPERTIES_PATH%) do (
    set %%A=%%B
)

:: Verificar si las propiedades obligatorias están definidas
for %%P in (%mandatory_properties%) do (
    if not defined %%P (
        echo La propiedad '%%P' es obligatoria pero no está definida en '%PROPERTIES_PATH_FULL%'.
        exit /b 1
    )
)



:: Obtener el año actual y expandir las variables antes de construir el comando
for /f "tokens=3 delims=/ " %%A in ("%date%") do set CURRENT_YEAR=%%A
if not defined inceptionYear set inceptionYear=%CURRENT_YEAR%

:: Expandir todas las variables necesarias antes de su uso
set FINAL_GROUP_ID=%groupId%
set FINAL_ARTIFACT_ID=%artifactId%
set FINAL_VERSION=%version%
set FINAL_APP_NAME=%appName%
set FINAL_INCEPTION_YEAR=%inceptionYear%
set FINAL_PACKAGE=%package%
set FINAL_REPO_PATH=%REPO_PATH%
set FINAL_NEW_PROJECT_PATH=%REPO_PATH%\..%FINAL_ARTIFACT_ID%

:: Borrar proyecto existente
if exist "%FINAL_REPO_PATH%\..\%FINAL_ARTIFACT_ID%" (
    echo Eliminando directorio existente: %FINAL_NEW_PROJECT_PATH%
    rmdir /s /q "%FINAL_REPO_PATH%\..\%FINAL_ARTIFACT_ID%"
)

:: Escribir el comando en el archivo
set CREATE_ARCHETYPE_COMMAND=%REPO_PATH%\.run\create_archetype_command.bat
(
echo @echo off
echo echo Compilando archetype && ^
echo mvn clean install -U && ^
echo echo Incluyendo archetype en catálogo de Maven && ^
echo mvn archetype:update-local-catalog && ^
echo echo Generando proyecto a partir de archetype
echo mvn archetype:generate -B ^^
echo   -DarchetypeCatalog=local ^^
echo   -DarchetypeGroupId=dev.corusoft ^^
echo   -DarchetypeArtifactId=backend-maven-archetype ^^
echo   -DarchetypeVersion=0.1-SNAPSHOT ^^
echo   -DgroupId=%FINAL_GROUP_ID% ^^
echo   -DartifactId=%FINAL_ARTIFACT_ID% ^^
echo   -Dversion=%FINAL_VERSION% ^^
echo   -Dpackage=%FINAL_PACKAGE% ^^
echo   -DappName=%FINAL_APP_NAME% ^^
echo   -DinceptionYear=%FINAL_INCEPTION_YEAR% ^^
echo   -DoutputDirectory=%FINAL_REPO_PATH%\.. ^^
echo   -Dgoals=compile
echo @echo on
) > "%CREATE_ARCHETYPE_COMMAND%"
echo Comando Maven para generar archetype guardado en '%CREATE_ARCHETYPE_COMMAND%'

:: Ejecutar el script generado
call "%CREATE_ARCHETYPE_COMMAND%"

endlocal
