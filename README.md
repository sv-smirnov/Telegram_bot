# Task&Time Telegram Bot 

### Описание проекта:
Task&Time Telegram Bot позволяет команде отслеживать работу над задачами в чате с ботом в Telegram.
### Возможности Task&Time Telegram Bot: 

  + Создание задач с указанием исполнителя как по умолчанию за текущий день, так и с выбором определенной даты
  + Хранение данных пользователей
  + Отправление нотификаций - напоминаний о добавлении задач
  + Запрос истории собственных внесенных задач за определенную дату
  + Запрос данных о всех задачах, внесенных командой как по умолчанию за текущий день, так и с выбором определенной даты


***
### Технологии:
 + Telegram Bot API
 + Java 11
 + Spring boot
 + Postgres
 + JdbcTemplate/Hibernate
 + Maven

***
### Начало работы с ботом:
Для работы с  Task&Time Telegram Bot необходимо иметь аккаунт в Telegram.

Найти бота можно по ссылке **https://t.me/Task_Time_Bot**

Для начала работы с ботом нужно отправить команду **/start**, для этого можно вопользоваться меню:
![menu](/bot-menu.jpg "Меню бота")
![menu](/start.jpg "Меню бота")
***

### Основные команды:
Ниже представлен полный список команд для работы с ботом, этот список выдается командой **/help**: 
* **/help** - выдаст список поддерживаемых команд; 
* **/task HH:MM TASK_DESCRIPTION** - Укажите выполненную работу в формате: /task ЧЧ:MM ЗАДАНИЕ 
* **/date YYYY:MM:DD HH:MM TASK_DESCRIPTION** - Укажите выполненную работу в формате: /date ГГГГ-ММ-ДД ЧЧ:MM ЗАДАНИЕ 
* **/history YYYY:MM:DD** - Просмотреть свою историю заданий за определенную дату в формате: /history ГГГГ-ММ-ДД 
* **/history_btw YYYY:MM:DD YYYY:MM:DD** - Просмотреть свою историю заданий за диапазон дат в формате: /history_btw ГГГГ-ММ-ДД ГГГГ-ММ-ДД
* **/team_activity** - Просмотреть историю заданий команды за текущий день 
* **/team_activity YYYY:MM:DD** - Просмотреть историю заданий команды за определенную дату в формате: /team_activity ГГГГ-ММ-ДД]

Пример использования команды **/task HH:MM TASK_DESCRIPTION** для добавления новой задачи:

![task](/task.jpg "новое задание")


Пример использования команды **/team_activity** для запроса истории задач, выполненных командой за текущий день:

![team](/team.jpg "активность команды")

Пример использования команды **/history YYYY:MM:DD** для запроса истории задач, выполненных пользователем за текущий день:

![history](/history.jpg "история")

***
В боте настроена отправка напоминаний о внесении задач пользователям, сообщение происходит всем зарегистрированным пользователям с понедельника по пятницу. 

![notification](/notification.jpg "напоминание")
