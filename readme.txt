PARAMETRY URUCHOMIENIA
Serwer
<port> <liczba_graczy> <początkowa_ilość_pieniędzy> <ante>

Klient
<port>


PROTOKÓŁ KOMUNIKACJI
Nazwa robocza: Simple Card Protocol

BEGIN - rozpoczęcie gry

ID <id> - przypisanie id do gracza

START <money> - przekazanie początkowej ilości pieniędzy

RAISE <money> - podbicie stawki

CALL - dopasowanie stawki

FOLD - opuszczenie gry

CHECK - sprawdzenie

CARD <suite><rank>  - przekazanie karty

BET <player> <bet_type> <bet_amount:optional> - przekazanie akcji gracza

CONTINUE - rozpoczęcie kolejnej rundy

QUIT - zakończenie gry