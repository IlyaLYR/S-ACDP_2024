from typing import Union

import requests

from hash import HashTable, fetch_html

# Инициализация словаря
cache = HashTable(4)

while True:
    url = input("Введите URL страницы (или 'exit' для выхода): ")
    if url.lower() == 'exit':
        break
    html = fetch_html(url)

    if html:
        if cache.is_contain(html):
            print(f"{url} загружен из Хэш-таблицы.")
        else:
            cache.insert(url, html)
            print(f"{url} добавлен в кэш.")
    else:
        print("Не удалось загрузить страницу.")
    cache.print_table()  # Печать таблицы



#  https://www.google.com/
#  https://www.wikipedia.org/
#  https://www.python.org/
#  https://www.google.com/search?q=secret_information
