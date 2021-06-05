#!/bin/bash

function help() {
  echo ""
  echo "Usage: ./services.sh COMMAND"
  echo ""
  echo "This scripts helps with building and running local app stack"
  echo ""
  echo "Commands:"
  printf "  build\t\tPerform clean build of all services\n"
  printf "  run\t\tPerform clean build, recreate and run all containers in docker-compose\n"
  printf "  stop\t\tStop and remove all running containers\n"
}

case "$1" in
"build")
  gradle clean build
  ;;
"run")
  gradle clean build
  docker-compose up -d --build
  ;;
"stop")
  docker-compose down
  ;;
*)
  help
  ;;
esac
