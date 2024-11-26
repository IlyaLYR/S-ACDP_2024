import hashlib
from typing import List, Union

import requests


class HashTable:
    """Хеш-таблица на открытой адресации
    """

    def __init__(self, size: int, methods: int = 1):
        """Инициализация таблицы

        Args:
            size (int): размер таблицы (>0)
            methods (int, optional): Метод открытой адресации
                1 - Линейное хеширование
                2 - Двойное хеширование
            . Defaults to 1.
        """
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
        """Хеш-функция 1

        Args:
            key (str): ключ, который необходимо зашифровать

        Returns:
            int: индекс
        """
        return int(hashlib.sha256(key.encode('utf-8')).hexdigest(), 16) % self.size

    def hash2(self, key: str) -> int:
        """Хеш-функция 2

        Args:
            key (str): ключ, который необходимо зашифровать

        Returns:
            int: индекс
        """
        h2 = int(hashlib.md5(key.encode('utf-8')).hexdigest(), 16) % self.size
        return 1 + h2 if h2 != 0 else 1

    def is_contain(self, key: str) -> bool:
        """Метод проверки вхождения ключа в таблице

        Args:
            key (str): ключ

        Returns:
            bool: результат
        """
        index = self.hash1(key)
        for _ in range(self.size):
            if self.__table[index] is None:  # Пустая ячейка — ключа точно нет
                return False
            if self.__table[index][0] == key and self.__table[index][2]:  # Найден ключ и он активен
                return True
            index = self.__solver(index, key)
        return False

    def __solver(self, index: int, key: str) -> int:
        """Вспомогательный метод выборы типа хеширования

        Args:
            index (int): индекс
            key (str): ключ

        Returns:
            int: индекс в таблице
        """
        if self.get_methods == 1:
            return (index + 1) % self.size
        else:
            return (index + self.hash2(key)) % self.size

    def insert(self, key: str, value: str) -> None:
        """Вставка элемента в таблицу

        Args:
            key (str): ключ
            value (str): значение
        """
        if self.count == self.size:
            print("Хеш-таблица -> заполнена!")
            return
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
        """Удаление элемента из таблицы

        Args:
            key (str): _description_
        """
        if not self.is_contain(key):
            print("Ключ не найден!")
            return
        index = self.hash1(key)
        for _ in range(self.size):
            if self.table[index][0] == key and self.table[index][2]:  # Найден ключ
                self.__table[index] = (self.table[index][0], self.table[index][1], False)  # Пометить как "удалённую"
                self.__count -= 1
                return
            index = self.__solver(index, key)

    def print_table(self) -> None:
        """Красивый и понятный вывод таблицы
        """
        print("\nХэш-таблица:")
        for i, entry in enumerate(self.table):
            if entry is None:
                print(f"Индекс {i}: Статус: свободно")
            elif entry[2]:  # Занятая ячейка
                print(f"Индекс {i}: Статус: занято")
                print(f"  URL: {entry[0]}")
                print(f"  HTML: {entry[1].replace('\n', ' ')[:100]}... (длина: {len(entry[1])} символов)")
            else:  # Удалённая ячейка
                print(f"Индекс {i}: Статус: удалено")
        print(f"Коэффициент заполненности ={self.count / self.size}")


def fetch_html(url: str) -> Union[str, None]:
    """Вспомогательный метод для получения HTML

    Args:
        url (str): URL страницы

    Returns:
        Union[str, None]: html в виде строк
    """
    try:
        response = requests.get(url)
        if response.status_code == 200:
            return response.text
        else:
            print(f"Ошибка: Не удалось получить доступ к странице (код {response.status_code}).")
    except requests.exceptions.RequestException as e:
        print(f"Ошибка: {e}")
