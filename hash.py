import hashlib
from typing import List, Union

import requests


class HashTable:

    def __init__(self, size: int, methods: int = 1):
        self.__size = size
        self.__methods = methods
        self.__count: int = 0
        self.__table: List[Union[tuple[str, str, bool], None]] = [None] * size

    @property
    def size(self) -> int:
        return self.__size

    @property
    def table(self) -> List[Union[tuple[str, str, bool], None]]:
        return self.__table

    @property
    def count(self) -> int:
        return self.__count

    @property
    def get_methods(self) -> int:
        return self.__methods

    def hash1(self, key: str) -> int:
        return int(hashlib.sha256(key.encode('utf-8')).hexdigest(), 16) % self.size  # хеширование

    def hash2(self, key: str) -> int:
        return 1 + (int(hashlib.md5(key.encode('utf-8')).hexdigest(), 16) % (self.size - 1))

    def is_contain(self, key: str) -> bool:
        """Проверяет, содержится ли ключ в таблице."""
        index = self.hash1(key)
        for _ in range(self.size):
            if self.__table[index] is None:  # Пустая ячейка — ключа точно нет
                return False
            if self.__table[index][0] == key:  # Найден ключ
                return True
            index = self.__solver(index, key)
        return False

    def __solver(self, index: int, key: str) -> int:
        if self.get_methods == 1:
            return (index + 1) % self.size
        else:
            return (index + self.hash2(key)) % self.size

    def insert(self, key: str, value: str) -> None:
        if self.count == self.size:
            raise Exception("HashTable is full")

        index = self.hash1(key)
        for _ in range(self.size):
            if self.table[index] is None or (self.table[index][2] is False):  # Свободная или удалённая ячейка
                self.__table[index] = (key, value, True)
                self.__count += 1
                return
            elif self.table[index][0] == key:
                self.__table[index] = (key, value, True)  # Обновляем значение
                return
            index = self.__solver(index, key)

    def remove(self, key: str) -> None:
        if not (self.is_contain(key)):
            raise Exception("Not found")
        index = self.hash1(key)
        for _ in range(self.size):
            if self.table[index][0] == key and self.table[index][2]:  # Найден ключ
                self.__table[index] = (self.table[index][0], self.table[index][1], False)  # Пометить как "удалённую"
                self.__count -= 1
                return
            index = self.__solver(index, key)

    def print_table(self) -> None:
        print("\nХэш-таблица:")
        for i, entry in enumerate(self.table):
            if entry is None:
                print(f"Индекс {i}: None")
            elif entry[2]:  # Занятая ячейка
                print(f"Индекс {i}:")
                print(f"  URL: {entry[0]}")
                print(f"  HTML: {entry[1].replace('\n', ' ')[:100]}... (длина: {len(entry[1])} символов)")
            else:  # Удалённая ячейка
                print(f"Индекс {i}: (Удалено)")


def fetch_html(url: str) -> Union[str, None]:
    try:
        response = requests.get(url)
        if response.status_code == 200:
            return response.text
        else:
            print(f"Ошибка: Не удалось получить доступ к странице (код {response.status_code}).")
    except requests.exceptions.RequestException as e:
        print(f"Ошибка: {e}")
