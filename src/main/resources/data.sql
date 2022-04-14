-- Zrzucanie danych dla tabeli ewidb.maszyna: ~2 rows (około)
/*!40000 ALTER TABLE `maszyna` DISABLE KEYS */;
INSERT INTO `maszyna` (`id`, `aktywna`, `nazwa`, `opis`) VALUES
	('C1', 1, 'Ciągnik', 'opis testowego ciągnika'),
	('W1', 1, 'Wózek', 'testowy opis wózka');
/*!40000 ALTER TABLE `maszyna` ENABLE KEYS */;

-- Zrzucanie danych dla tabeli ewidb.dokument: ~20 rows (około)
/*!40000 ALTER TABLE `dokument` DISABLE KEYS */;
INSERT INTO `dokument` (`numer`, `data`, `kilometry`, `kilometry_przyczepa`, `maszyna_id`) VALUES
	('1/04/2022/C1', '2022-04-01', 10, 1, 'C1'),
	('1/04/2022/W1', '2022-04-14', 14, 14, 'W1'),
	('10/04/2022/C1', '2022-04-10', 17.2, 10, 'C1'),
	('10/04/2022/W1', '2022-04-23', 31, 13, 'W1'),
	('2/04/2022/C1', '2022-04-02', 20, 2, 'C1'),
	('2/04/2022/W1', '2022-04-15', 15, 15, 'W1'),
	('3/04/2022/C1', '2022-04-03', 30, 3, 'C1'),
	('3/04/2022/W1', '2022-04-16', 16, 16, 'W1'),
	('4/04/2022/C1', '2022-04-04', 21, 4, 'C1'),
	('4/04/2022/W1', '2022-04-17', 17, 17, 'W1'),
	('5/04/2022/C1', '2022-04-05', 0, 0, 'C1'),
	('5/04/2022/W1', '2022-04-18', 18, 18, 'W1'),
	('6/04/2022/C1', '2022-04-06', 27.5, 6, 'C1'),
	('6/04/2022/W1', '2022-04-19', 9, 19, 'W1'),
	('7/04/2022/C1', '2022-04-07', 41, 7, 'C1'),
	('7/04/2022/W1', '2022-04-20', 20, 20, 'W1'),
	('8/04/2022/C1', '2022-04-08', 20.3, 8, 'C1'),
	('8/04/2022/W1', '2022-04-21', 11, 11, 'W1'),
	('9/04/2022/C1', '2022-04-09', 19, 9, 'C1'),
	('9/04/2022/W1', '2022-04-22', 12, 12, 'W1');
/*!40000 ALTER TABLE `dokument` ENABLE KEYS */;

-- Zrzucanie danych dla tabeli ewidb.kategoria: ~1 rows (około)
/*!40000 ALTER TABLE `kategoria` DISABLE KEYS */;
INSERT INTO `kategoria` (`nazwa`, `przenoszona_na_kolejny_okres`) VALUES
	('Sprzęt', 0);
/*!40000 ALTER TABLE `kategoria` ENABLE KEYS */;

-- Zrzucanie danych dla tabeli ewidb.kilometry: ~2 rows (około)
/*!40000 ALTER TABLE `kilometry` DISABLE KEYS */;
INSERT INTO `kilometry` (`id`, `miesiac`, `rok`, `wartosc`, `maszyna_id`) VALUES
	(5, 4, 2022, 55, 'C1'),
	(28, 4, 2022, 3, 'W1');
/*!40000 ALTER TABLE `kilometry` ENABLE KEYS */;

-- Zrzucanie danych dla tabeli ewidb.maszyna_kategorie: ~2 rows (około)
/*!40000 ALTER TABLE `maszyna_kategorie` DISABLE KEYS */;
INSERT INTO `maszyna_kategorie` (`maszyna_id`, `kategorie_nazwa`) VALUES
	('C1', 'Sprzęt'),
	('W1', 'Sprzęt');
/*!40000 ALTER TABLE `maszyna_kategorie` ENABLE KEYS */;

-- Zrzucanie danych dla tabeli ewidb.norma: ~3 rows (około)
/*!40000 ALTER TABLE `norma` DISABLE KEYS */;
INSERT INTO `norma` (`id`, `czy_ogrzewanie`, `jednostka`, `wartosc`, `maszyna_id`) VALUES
	(1, 1, 'ON/H', 11.5, 'C1'),
	(2, 0, 'L/H', 2.3, 'C1'),
	(26, 0, 'T/H', 1.1, 'W1');
/*!40000 ALTER TABLE `norma` ENABLE KEYS */;

-- Zrzucanie danych dla tabeli ewidb.stan: ~3 rows (około)
/*!40000 ALTER TABLE `stan` DISABLE KEYS */;
INSERT INTO `stan` (`id`, `miesiac`, `rok`, `wartosc`, `norma_id`) VALUES
	(3, 4, 2022, 10, 1),
	(4, 4, 2022, 20, 2),
	(27, 4, 2022, 5, 26);
/*!40000 ALTER TABLE `stan` ENABLE KEYS */;

-- Zrzucanie danych dla tabeli ewidb.zuzycie: ~30 rows (około)
/*!40000 ALTER TABLE `zuzycie` DISABLE KEYS */;
INSERT INTO `zuzycie` (`id`, `ogrzewanie`, `wartosc`, `zatankowano`, `dokument_numer`, `norma_id`) VALUES
	(6, 1, 3.1, 30, '1/04/2022/C1', 1),
	(7, 0, 2.1, 0, '1/04/2022/C1', 2),
	(8, 3, 3.2, 40, '2/04/2022/C1', 1),
	(9, 0, 2.2, 1, '2/04/2022/C1', 2),
	(10, 3, 3.3, 40, '3/04/2022/C1', 1),
	(11, 0, 2.3, 0, '3/04/2022/C1', 2),
	(12, 4, 3.4, 41.5, '4/04/2022/C1', 1),
	(13, 0, 2.4, 1, '4/04/2022/C1', 2),
	(14, 0, 3.5, 100, '5/04/2022/C1', 1),
	(15, 0, 2.5, 50, '5/04/2022/C1', 2),
	(16, 0, 3.5, 0, '6/04/2022/C1', 1),
	(17, 0, 2.6, 0, '6/04/2022/C1', 2),
	(18, 7, 3.7, 57, '7/04/2022/C1', 1),
	(19, 0, 2.7, 0, '7/04/2022/C1', 2),
	(20, 1, 3.8, 65.4, '8/04/2022/C1', 1),
	(21, 0, 2.8, 2, '8/04/2022/C1', 2),
	(22, 1, 1.9, 1, '9/04/2022/C1', 1),
	(23, 0, 2.9, 1.3, '9/04/2022/C1', 2),
	(24, 1.1, 4.12, 22.98, '10/04/2022/C1', 1),
	(25, 0, 2.12, 2.4, '10/04/2022/C1', 2),
	(29, 0, 1.1, 0, '1/04/2022/W1', 26),
	(30, 0, 1.2, 0, '2/04/2022/W1', 26),
	(31, 0, 1.3, 5, '3/04/2022/W1', 26),
	(32, 0, 1.4, 0, '4/04/2022/W1', 26),
	(33, 0, 1.5, 0, '5/04/2022/W1', 26),
	(34, 0, 1.6, 0, '6/04/2022/W1', 26),
	(35, 0, 2, 6.5, '7/04/2022/W1', 26),
	(36, 0, 2.1, 0, '8/04/2022/W1', 26),
	(37, 0, 2.2, 0, '9/04/2022/W1', 26),
	(38, 0, 2.3, 2, '10/04/2022/W1', 26);