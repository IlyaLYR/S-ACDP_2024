import hashlib

import requests


class HashTable:
    def __init__(self, capacity):
        self.capacity = capacity
        self.table = [None] * capacity
        self.size = 0

    def len(self):
        return self.size

    def contains(self, key):
        index = self._hash(key) % self.capacity
        if self.table[index] is not None:
            for k, _ in self.table[index]:
                if k == key:
                    return True
        return False

    def insert(self, key, value):
        index = self._hash(key) % self.capacity
        if self.table[index] is None:
            self.table[index] = [(key, value)]
        else:
            for i, (k, v) in enumerate(self.table[index]):
                if k == key:
                    self.table[index][i] = (key, value)
                    return
            self.table[index].append((key, value))
            self.size += 1

    def get(self, key):
        index = self._hash(key) % self.capacity
        if self.table[index] is not None:
            for k, v in self.table[index]:
                if k == key:
                    return v
        return None

    def get_html(self, url):
        if self.contains(url):
            print("html из кэша")
            return self.get(url)
        else:
            response = requests.get(url)
            if response.status_code == 200:
                html = response.text
                self.insert(url, html)
                return html
            else:
                return f"Ошибка: Не удалось получить доступ к странице по адресу {url}"

    def _hash(self, key):
        return int(hashlib.sha256(key.encode('utf-8')).hexdigest(), 16)

    def remove(self, key):
        index = self._hash(key) % self.capacity
        if self.table[index] is not None:
            for i, (k, _) in enumerate(self.table[index]):
                if k == key:
                    del self.table[index][i]
                    self.size -= 1
                    if len(self.table[index]) == 0:
                        self.table[index] = None

    def print_table(self):
        print("Хэш-таблица:")
        for i in range(self.capacity):
            if self.table[i] is not None:
                print(f"Индекс {i}: {[(k, hashlib.sha256(v.encode('utf-8')).hexdigest()) for k, v in self.table[i]]}") # Изменение вывода
            else:
                print(f"Индекс {i}: None")

# Тест
table = HashTable(5) # Маленькая хэш-таблица, чтобы вызвать коллизии

table.insert("apple", "яблоко")
table.insert("apple", "яблоко")
table.insert("banana", "банан")
table.insert("apricot", "абрикос")
table.insert("apple1", "яблоко1")

# Выведем хэш-таблицу:
table.print_table()
