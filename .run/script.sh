#!/bin/sh
REPO_PATH=$(git rev-parse --show-toplevel)
ARCHETYPE_PROPERTIES_PATH=${REPO_PATH}/src/test/resources/projects/it-basic/archetype.properties
PROPERTIES_PATH="${1:-$ARCHETYPE_PROPERTIES_PATH}"

# Verificar si el archivo de propiedades existe
if [ ! -f "$PROPERTIES_PATH" ]; then
  echo "El archivo $PROPERTIES_PATH no existe."
  exit 1
fi

# Leer las propiedades desde el archivo archetype.properties
source $PROPERTIES_PATH

# Propiedades obligatorias para crear el proyecto
mandatory_properties="groupId artifactId version package appName inceptionYear"
inceptionYear="${inceptionYear:-$(date +%Y)}"

# Comprobar que las propiedades obligatorias están definidas en el archetype.properties
for property in $mandatory_properties; do
  value=$(eval echo \$$property)
  if [ -z "$value" ]; then
    echo "La propiedad '$property' es obligatoria pero no está definida en '${PROPERTIES_PATH}'."
    exit 1
  fi
done

# Construir el comando Maven con las propiedades reemplazadas
mvn_command="#!/bin/sh

mvn install -U
mvn archetype:update-local-catalog
cd ${REPO_PATH}/..
rm -rf $artifactId
mvn archetype:generate -B                             \\
  -DarchetypeCatalog=local                            \\
  -DarchetypeGroupId=dev.corusoft                     \\
  -DarchetypeArtifactId=backend-maven-archetype       \\
  -DarchetypeVersion=0.1-SNAPSHOT                     \\
  -DgroupId=$groupId                                  \\
  -DartifactId=$artifactId                            \\
  -Dversion=$version                                  \\
  -DappName=$appName                                  \\
  -DinceptionYear=$inceptionYear
"

# Escribir el comando generado en el archivo de salida
create_archetype_command=${REPO_PATH}/.run/create_archetype_command.sh
echo "$mvn_command" > $create_archetype_command

# Informar al usuario sobre la ubicación del archivo generado
echo "Comando maven para generar archetype guardado en '$create_archetype_command'"
printf "\n"

sh $create_archetype_command