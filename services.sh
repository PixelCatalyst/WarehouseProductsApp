#!/bin/bash

function help() {
  echo ""
  echo "Usage: ./services.sh COMMAND"
  echo ""
  echo "This scripts helps with building and running local app stack"
  echo ""
  echo "Commands:"
  printf "  build\t\t\tPerform clean build of all services\n"
  printf "  run\t\t\tPerform clean build, recreate and run all containers in docker-compose\n"
  printf "  run-deps\t\t\tRun all dependencies of the main app in docker-compose\n"
  printf "  create-test-users\tRegister test user accounts in the app\n"
  printf "  stop\t\t\tStop and remove all running containers\n"
}

case "$1" in
"build")
  gradle clean build
  ;;
"run")
  gradle clean build
  docker-compose up -d --build
  ;;
"run-deps")
  docker-compose up -d postgres minio-s3
  ;;
"create-test-users")
  printf "Creating test user credentials as read-only: jan.kowalski_ro husky1...\n"
  curl --request POST -S \
    -H "Content-Type: application/json" \
    -d '{"username": "jan.kowalski_ro", "password": "husky1", "roles": ["READ_PRODUCTS", "READ_BULK_PRODUCTS"]}' \
    --url 'localhost:8080/register'

  printf "\nCreating test user credentials as read-write: jan.kowalski_rw hunter2...\n"
  curl --request POST -S \
    -H "Content-Type: application/json" \
    -d '{"username": "jan.kowalski_rw", "password": "hunter2", "roles": ["READ_PRODUCTS", "READ_BULK_PRODUCTS", "WRITE_PRODUCTS"]}' \
    --url 'localhost:8080/register'

  printf "\nCreating users done\n"
  ;;
"stop")
  docker-compose down
  ;;
*)
  help
  ;;
esac
