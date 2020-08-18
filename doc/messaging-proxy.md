### Обмен сообщениями

----

### Прокси-сервер

Имеем ROUTER сокет к клиентам.
Имеем ROUTER сокет к воркерам.

----

#### Получаем от клиента

Первая часть сообщения - identity клиента (автоматически добавляется ROUTER сокетом)
Вторая часть сообщения - 1 байт, тип сообщения.

Тип 11: Запрос на вычисление функции.

 - ID запроса (генерируется клиентом)
 - Аргумент запроса (байтовый блок)
 - Название функции (строка)
 
Тип 21: Запрос на получение структуры схемы для функции.

 - Название функции (строка)
 
Тип 31: Сообщение о том, что ответ на запрос получен
 
 - ID запроса
 
----

#### Отправляем клиенту

Первая часть сообщения - identity клиента (автоматически удаляется ROUTER сокетом)
Вторая часть сообщения - 1 байт, тип сообщения

Тип 11: Ответ на запрос - завершен успешно

 - ID запроса
 - Результат запроса (байтовый блок)
 
Тип 12: Ответ на запрос - ошибка RemoteFunctionException (см. файл Exceptions.kt)

- ID запроса
- Строка remoteException

Тип 13: Ответ на запрос - ошибка RemoteDecodingException (см. файл Exceptions.kt)

- ID запроса
- Строка remoteArgSchemeStructure

Тип 14: Ответ на запрос - ошибка RemoteEncodingException (см. файл Exceptions.kt)

- ID запроса
- Строка resultString
- Строка remoteResultSchemeStructure

Тип 15: Ответ на запрос - ошибка UnsupportedFunctionNameException
- ID запроса
- Строка functionName
 
Тип 21: Ответ на запрос схемы - функция поддерживается

 - Название функции
 - Структура схемы аргумента (строка)
 - Структура схемы результата (строка)
 
Тип 22: Ответ на запрос схемы - функция не поддерживается

 - Название функции
 
Тип 31: Сообщение о том, что запрос клиента получен.

 - ID запроса (16 байтов)
 
----

#### Получаем от воркера

Первая часть сообщения - identity воркера (автоматически добавляется ROUTER сокетом)
Вторая часть сообщения - 1 байт, тип сообщения.

Тип 11: Ответ на запрос - завершен успешно

 - ID запроса
 - Результат запроса (байтовый блок)
 
Тип 12: Ответ на запрос - ошибка RemoteFunctionException (см. файл Exceptions.kt)

- ID запроса
- Строка remoteException

Тип 13: Ответ на запрос - ошибка RemoteDecodingException (см. файл Exceptions.kt)

- ID запроса
- Строка remoteArgSchemeStructure

Тип 14: Ответ на запрос - ошибка RemoteEncodingException (см. файл Exceptions.kt)

- ID запроса
- Строка resultString
- Строка remoteResultSchemeStructure
 
Тип 31: Сообщение о том, что запрос получен.

 - ID запроса (16 байтов)
 
Тип 41: Heart beat 

 - Нет дополнительных частей

Тип 51: Запрос воркера на подключение к прокси и передача всех схем

- Кол-во функций (4 байта)
- Далее для каждой из функций одна часть - название, вторая часть - структура схемы аргумента, третья часть - структура схемы результата

----

#### Отправляем воркеру

Первая часть сообщения - identity воркера (автоматически удаляется ROUTER сокетом)
Вторая часть сообщения - 1 байт, тип сообщения

Тип 11: Запрос на вычисление функции.

 - ID запроса
 - Аргумент запроса (байтовый блок)
 - Название функции (строка)
 
Тип 31: Сообщение о том, что ответ на запрос получен.
 
 - ID запроса
 
Тип 41: Сообщение о том, что одна из схем воркера отличается от тех, которые уже есть у других воркеров
(ошибка DifferentSchemesException).

 - Название функции
 - Структура схемы аргумента у других воркеров
 - Структура схемы результата у других воркеров

----