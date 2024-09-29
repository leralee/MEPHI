#!/bin/bash

# Список хостов для проверки
hosts=("192.168.1.1" "google.com" "192.168.1.100" "yahoo.com")

# Инициализация счетчиков
available_count=0
unavailable_count=0

# Цикл для проверки каждого хоста
for host in "${hosts[@]}"; do
  # Выполнение команды ping и проверка результата
  if ping -c 1 -W 1 "$host" > /dev/null; then
    echo "Хост $host доступен"
    ((available_count++))
  else
    echo "Хост $host недоступен"
    ((unavailable_count++))
  fi
done

# Вывод общего количества доступных и недоступных хостов
echo "Доступные хосты: $available_count"
echo "Недоступные хосты: $unavailable_count"
