:: Mandatory parameters: groupId, artifactId, appName
mvn archetype:generate -B ^
  -DarchetypeCatalog=local ^
  -DarchetypeGroupId=dev.corusoft ^
  -DarchetypeArtifactId=backend-maven-archetype ^
  -DarchetypeVersion=0.1-SNAPSHOT ^
  -DinceptionYear="%date:~6,4%" ^
  -Dversion=0.1-SNAPSHOT ^
  -DgroupId= ^
  -DartifactId= ^
  -DappName=