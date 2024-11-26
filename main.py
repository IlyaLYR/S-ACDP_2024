from hash import HashTable, fetch_html

# Инициализация словаря
cache = HashTable(4)

while True:
    url = input("Введите URL страницы (или 'exit' для выхода): ")
    if url.lower() == 'exit':
        break
    if url.startswith("remove -u"):
        url_remove = url[len("remove -u "):]
        cache.remove(url_remove)
        cache.print_table()  # Печать таблицы
        continue
    html = fetch_html(url)

    if html:
        if cache.is_contain(url):
            print(f"{url} загружен из Хэш-таблицы.")
        else:
            cache.insert(url, html)
            print(f"{url} добавлен в кэш.")
    else:
        print("Не удалось загрузить страницу.")
    cache.print_table()  # Печать таблицы
