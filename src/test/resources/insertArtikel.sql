insert into artikels(naam, aankoopprijs, verkoopprijs, houdbaarheid, soort)
values('testfood', 100, 120, 7, 'F');
insert into artikels(naam, aankoopprijs, verkoopprijs, garantie, soort)
values('testnonfood', 100, 120, 30, 'NF');
insert into kortingen(artikelid, vanafAantal, percentage)
values ((select id from artikels where naam = 'testfood'), 1, 10);