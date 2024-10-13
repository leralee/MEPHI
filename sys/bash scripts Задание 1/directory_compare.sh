#!/bin/bash

# Проверяем, переданы ли два аргумента
if [ "$#" -ne 2 ]; then
  echo "Использование: $0 <directory1> <directory2>"
  exit 1
fi

DIR1="$1"
DIR2="$2"

# Проверяем, существуют ли директории
if [ ! -d "$DIR1" ]; then
  echo "Ошибка: '$DIR1' не является директорией или не существует."
  exit 1
fi

if [ ! -d "$DIR2" ]; then
  echo "Ошибка: '$DIR2' не является директорией или не существует."
  exit 1
fi

# Создаем временные файлы для хранения списка файлов
TEMP_FILE1=$(mktemp)
TEMP_FILE2=$(mktemp)

# Находим все файлы и поддиректории в указанных директориях, удаляем корневую часть пути, сортируем и сохраняем в временные файлы
find "$DIR1" -mindepth 1 -type f | sed "s|^$DIR1/||" | sort > "$TEMP_FILE1"
find "$DIR2" -mindepth 1 -type f | sed "s|^$DIR2/||" | sort > "$TEMP_FILE2"

# Сравниваем файлы и сохраняем результат в файл "directory_comparison.txt"
diff "$TEMP_FILE1" "$TEMP_FILE2" > directory_comparison.txt

# Удаляем временные файлы
rm "$TEMP_FILE1" "$TEMP_FILE2"

echo "Сравнение завершено. Результаты сохранены в 'directory_comparison.txt'."
