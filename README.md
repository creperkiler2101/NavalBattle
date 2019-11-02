# NavalBattle

## Введение
В данном проекте нам группой надо создать игру «Морской Бой». Для этого используется язык программирования Java и база данных MySQL.
<br>
В игре можно:
* Зарегистрировать аккаунт
* Зайти на него
* Играть в сам морской бой с другим игроком
* Посмотреть повторы своих игр
* Прокачивать свое звание, побеждая в играх
* Терять свое звание, проигрывая в играх

Используемые библиотеки
* JOGL - OpenGL библеотека для Java
* Json Simple - Простая библеотека для перевода объектов в JSON и наоборот

Дополнительно, для упрощения писания кода, был создан небольшой движок.

## База данных
База данных используется для хранения информации об аккаунтах, званиях и играх.
![UML Schema](https://github.com/creperkiler2101/NavalBattle/blob/master/UML Schema.png)

## Игровой процесс
Как только вам нашло игру, вы можете выбрать - принять ее или отклонить.
Если один из игроков отклонил игру, то меню выбора закроется у обоих и они опять смогут начать поиск.
Если оба игрока нажали принять, то их перекинет на сцену с игрой.
<br>
После попадания на игровую сцену, нужно расставить корабли и после появится кнопка Go, при нажатии на которую на сервер отправляется сообщение о вашей готовности, а так же информация об кораблях.
* Вы можете скрыть свои корабли, нажав кнопку hide. При повторном нажатии они снова будут видны.
* Как только оба игрока будут готовы, сервер случайным образом выберит того, кто первый ходит.
* Чтобы походить, надо нажать на вражеское (Правое) поле на любую из клеток, на которую не было совершенно нажатия до этого.
  * Если вы не походили за 15 секунд, ход передается оппоненту.
  * Если вы промахнулись, ход передается оппоненту.
  * Если вы попали, вы можете продолжать ходить.
* Как только корабль будет потоплен, он отобразится на поле и все клетки вокруг него будут помечены как открытые.
  * Как только все корабли одного из игроков будут потоплены, отобразится окно с именем победителя и кнопкой выхода.
В это время сервер добавляет победителю 100 опыта, отнимает проигравшему 100 опыта и так же сохраняет игру в базу данных.
* Если один из игроков по какой либо причине выйдет раньше конца игры, второго игрока выкинет в главное меню.

## Повторы игр
Нажав на кнопку replays в главном меню, вы попадете к списку сыгранных вами игр.
Для скролла списка используется клавиши "W" и "S".
Вы можете посмотреть любую игру в любой момент.
В повторе игры будут повторятся ходы с такой же задержкой, с которых вы и ваш противних ходили во время игры. Все корабли сразу отображены. Вы можете выйти в любой момент нажав на кнопку exit.

## Создатели
* Руслан Кудрявцев - Программист.
* Никита Ефремов – Дизайнер, Тестировщик.
* Кирилл Лавров – Тестировщик.
