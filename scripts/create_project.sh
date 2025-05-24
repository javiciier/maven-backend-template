#!/usr/bin/env bash

mvnd archetype:generate -B \
  -DarchetypeCatalog=local  \
  -DarchetypeGroupId=dev.corusoft \
  -DarchetypeArtifactId=backend-maven-archetype \
  -DarchetypeVersion=0.1-SNAPSHOT \
  -DinceptionYear="$(date +%Y)"  \
  -Dversion=0.1-SNAPSHOT  \
  # Fill the following parameters
  -DgroupId=  \       # E.g.: my.project
  -DartifactId= \     # E.g.: project-name
  -DappName=          # E.g.: my-application-backend
