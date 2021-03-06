Основы работы в сети
====================

Стек сетевых протоколов
-----------------------

**Стек протоколов** - иерархически организованный набор сетевых протоколов, 
достаточный для организации взаимодействия узлов в сети. 

Протоколы работают в сети одновременно, значит работа протоколов 
должна быть организована так, чтобы не возникало конфликтов или незавершённых операций. 

**Сетевая модель OSI**

**O**pen **S**ystems **I**nterconnection basic reference model = 
базовая эталонная модель взаимодействия открытых систем = **ЭМВОС**
* **(7)** Прикладной (application): HTTP, FTP, SMTP
* **(6)** Представительский (представления) (presentation): ASCII, EBCDIC, JPEG
* **(5)** Сеансовый (session): RPC, PAP
* **(4)** Транспортный (transport): TCP, UDP, SCTP
* **(3)** Сетевой (network): IPv4, IPv6, IPsec, AppleTalk
* **(2)** Канальный (data link): PPP, IEEE 802.2, Ethernet, DSL, ARP, L2TP
* **(1)** Физический (physical): USB, витая пара, коаксиальный кабель, оптический кабель

Протокол TCP/IP
---------------

**TCP/IP**
* **T**ransmission **C**ontrol **P**rotocol  
* **I**nternet **P**rotocol 

https://ru.wikipedia.org/wiki/TCP/IP

Архитектура «клиент-сервер»
---------------------------
**Client–Server** - архитектура, в которой задания или сетевая нагрузка 
распределены между поставщиками услуг (**серверами**), и заказчиками услуг (**клиентами**).

**Сервер** пассивен, он ожидает запросов от клиента и выполняет их.

**Клиент** подключается к серверу.

https://ru.wikipedia.org/wiki/%D0%9A%D0%BB%D0%B8%D0%B5%D0%BD%D1%82_%E2%80%94_%D1%81%D0%B5%D1%80%D0%B2%D0%B5%D1%80

Создание простейшего сетевого чата
----------------------------------

Надежность передачи данных по сети. Сериализация передаваемых объектов
----------------------------------------------------------------------

Передача объектов и файлов по сети
----------------------------------
