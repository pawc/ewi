

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Zrzucanie danych dla tabeli ewidb.maszyna: ~2 rows (około)
/*!40000 ALTER TABLE `maszyna` DISABLE KEYS */;
INSERT INTO `maszyna` (`id`, `aktywna`, `nazwa`, `opis`) VALUES
	('C1', 1, 'Ciągnik 1', 'test ciągnik'),
	('W2', 1, 'Wózek', 'testowy wózek');
/*!40000 ALTER TABLE `maszyna` ENABLE KEYS */;


-- Zrzucanie danych dla tabeli ewidb.norma: ~4 rows (około)
/*!40000 ALTER TABLE `norma` DISABLE KEYS */;
INSERT INTO `norma` (`id`, `czy_ogrzewanie`, `jednostka`, `wartosc`, `maszyna_id`) VALUES
	(1, 1, 'ON/H', 1.23, 'C1'),
	(2, 0, 'L/H', 4.56, 'C1'),
	(3, 1, 'ON/H', 4.21, 'W2'),
	(4, 0, 'C', 1.2, 'W2');
/*!40000 ALTER TABLE `norma` ENABLE KEYS */;


-- Zrzucanie danych dla tabeli ewidb.dokument: ~27 rows (około)
/*!40000 ALTER TABLE `dokument` DISABLE KEYS */;
INSERT INTO `dokument` (`numer`, `data`, `kilometry`, `kilometry_przyczepa`, `maszyna_id`) VALUES
	('1/04/2022/C1', '2022-04-01', 10, 10, 'C1'),
	('1/04/2022/W2', '2022-04-20', 10, 9, 'W2'),
	('1/05/2022/C1', '2022-05-01', 15, 1, 'C1'),
	('1/05/2022/W2', '2022-05-01', 12, 12, 'W2'),
	('10/04/2022/C1', '2022-04-10', 13, 13, 'C1'),
	('2/04/2022/C1', '2022-04-02', 11, 11, 'C1'),
	('2/04/2022/W2', '2022-04-21', 8, 8, 'W2'),
	('2/05/2022/C1', '2022-05-02', 12, 10, 'C1'),
	('2/05/2022/W2', '2022-05-02', 7, 8, 'W2'),
	('3/04/2022/C1', '2022-04-03', 0, 0, 'C1'),
	('3/04/2022/W2', '2022-04-22', 13, 12, 'W2'),
	('3/05/2022/C1', '2022-05-03', 3, 12, 'C1'),
	('3/05/2022/W2', '2022-05-03', 17, 13, 'W2'),
	('4/04/2022/C1', '2022-04-04', 5, 4, 'C1'),
	('4/04/2022/W2', '2022-04-23', 13, 3, 'W2'),
	('4/05/2022/C1', '2022-05-05', 7, 8, 'C1'),
	('4/05/2022/W2', '2022-05-04', 12, 13, 'W2'),
	('5/04/2022/C1', '2022-04-05', 1.4, 21.2, 'C1'),
	('5/04/2022/W2', '2022-04-24', 22, 11, 'W2'),
	('5/05/2022/C1', '2022-05-06', 21, 10, 'C1'),
	('5/05/2022/W2', '2022-05-05', 13, 2, 'W2'),
	('6/04/2022/C1', '2022-04-06', 7, 7, 'C1'),
	('6/04/2022/W2', '2022-04-25', 13, 12, 'W2'),
	('7/04/2022/C1', '2022-04-07', 18.5, 18, 'C1'),
	('7/04/2022/W2', '2022-04-26', 12, 13, 'W2'),
	('8/04/2022/C1', '2022-04-08', 18, 18, 'C1'),
	('9/04/2022/C1', '2022-04-09', 19, 19, 'C1');
/*!40000 ALTER TABLE `dokument` ENABLE KEYS */;


-- Zrzucanie danych dla tabeli ewidb.kategoria: ~1 rows (około)
/*!40000 ALTER TABLE `kategoria` DISABLE KEYS */;
INSERT INTO `kategoria` (`nazwa`, `przenoszona_na_kolejny_okres`) VALUES
	('Ciągniki', 0);
/*!40000 ALTER TABLE `kategoria` ENABLE KEYS */;


-- Zrzucanie danych dla tabeli ewidb.kilometry: ~2 rows (około)
/*!40000 ALTER TABLE `kilometry` DISABLE KEYS */;
INSERT INTO `kilometry` (`id`, `miesiac`, `rok`, `wartosc`, `maszyna_id`) VALUES
	(5, 4, 2022, 120, 'C1'),
	(6, 4, 2022, 50, 'W2');
/*!40000 ALTER TABLE `kilometry` ENABLE KEYS */;


-- Zrzucanie danych dla tabeli ewidb.maszyna_kategorie: ~1 rows (około)
/*!40000 ALTER TABLE `maszyna_kategorie` DISABLE KEYS */;
INSERT INTO `maszyna_kategorie` (`maszyna_id`, `kategorie_nazwa`) VALUES
	('C1', 'Ciągniki');
/*!40000 ALTER TABLE `maszyna_kategorie` ENABLE KEYS */;


-- Zrzucanie danych dla tabeli ewidb.stan: ~4 rows (około)
/*!40000 ALTER TABLE `stan` DISABLE KEYS */;
INSERT INTO `stan` (`id`, `miesiac`, `rok`, `wartosc`, `norma_id`) VALUES
	(1, 4, 2022, 2.4, 1),
	(2, 4, 2022, 1.2, 2),
	(3, 4, 2022, 5.5, 3),
	(4, 4, 2022, 12.1, 4);
/*!40000 ALTER TABLE `stan` ENABLE KEYS */;


-- Zrzucanie danych dla tabeli ewidb.zuzycie: ~54 rows (około)
/*!40000 ALTER TABLE `zuzycie` DISABLE KEYS */;
INSERT INTO `zuzycie` (`id`, `ogrzewanie`, `wartosc`, `zatankowano`, `dokument_numer`, `norma_id`) VALUES
	(11, 1, 1.1, 0, '1/04/2022/C1', 1),
	(12, 0, 2.1, 11, '1/04/2022/C1', 2),
	(13, 1, 1.2, 4, '2/04/2022/C1', 1),
	(14, 0, 2.2, 8, '2/04/2022/C1', 2),
	(15, 2.5, 1.3, 5, '3/04/2022/C1', 1),
	(16, 0, 2.3, 10, '3/04/2022/C1', 2),
	(17, 1.2, 1.4, 11, '4/04/2022/C1', 1),
	(18, 0, 2.4, 20, '4/04/2022/C1', 2),
	(19, 0, 1.5, 0, '5/04/2022/C1', 1),
	(20, 0, 2.5, 20, '5/04/2022/C1', 2),
	(21, 2, 1.6, 15, '6/04/2022/C1', 1),
	(22, 0, 2.6, 5, '6/04/2022/C1', 2),
	(23, 5, 1.7, 7, '7/04/2022/C1', 1),
	(24, 0, 2.7, 11, '7/04/2022/C1', 2),
	(25, 4, 1.8, 0, '8/04/2022/C1', 1),
	(26, 0, 2.8, 10, '8/04/2022/C1', 2),
	(27, 1, 1.9, 0, '9/04/2022/C1', 1),
	(28, 0, 2.9, 7, '9/04/2022/C1', 2),
	(29, 1, 2, 2, '10/04/2022/C1', 1),
	(30, 0, 3, 20, '10/04/2022/C1', 2),
	(31, 1, 1.11, 1, '1/04/2022/W2', 3),
	(32, 0, 2.22, 2, '1/04/2022/W2', 4),
	(33, 10, 1.22, 8, '2/04/2022/W2', 3),
	(34, 0, 2.22, 4, '2/04/2022/W2', 4),
	(35, 1, 1.33, 20, '3/04/2022/W2', 3),
	(36, 0, 2.33, 8, '3/04/2022/W2', 4),
	(37, 1, 1.44, 10, '4/04/2022/W2', 3),
	(38, 0, 2.44, 11, '4/04/2022/W2', 4),
	(39, 10, 1.55, 20, '5/04/2022/W2', 3),
	(40, 0, 2.55, 1, '5/04/2022/W2', 4),
	(41, 2, 1.55, 1, '6/04/2022/W2', 3),
	(42, 0, 2.55, 2, '6/04/2022/W2', 4),
	(43, 0, 1.77, 5, '7/04/2022/W2', 3),
	(44, 0, 2.77, 1, '7/04/2022/W2', 4),
	(45, 1, 2.9, 2, '1/05/2022/C1', 1),
	(46, 0, 1.9, 3, '1/05/2022/C1', 2),
	(47, 10, 2.8, 30, '2/05/2022/C1', 1),
	(48, 0, 1.8, 20, '2/05/2022/C1', 2),
	(49, 1, 2.7, 2, '3/05/2022/C1', 1),
	(50, 0, 1.7, 20, '3/05/2022/C1', 2),
	(51, 7, 2.5, 13, '4/05/2022/C1', 1),
	(52, 0, 1.5, 20, '4/05/2022/C1', 2),
	(53, 5, 2.2, 10, '5/05/2022/C1', 1),
	(54, 0, 1.1, 4, '5/05/2022/C1', 2),
	(55, 1, 1.23, 10, '1/05/2022/W2', 3),
	(56, 0, 2.23, 20, '1/05/2022/W2', 4),
	(57, 1, 2.45, 10, '2/05/2022/W2', 3),
	(58, 0, 3.21, 0, '2/05/2022/W2', 4),
	(59, 1, 7.41, 50, '3/05/2022/W2', 3),
	(60, 0, 8.12, 3, '3/05/2022/W2', 4),
	(61, 1, 4.11, 11, '4/05/2022/W2', 3),
	(62, 0, 2.11, 20, '4/05/2022/W2', 4),
	(63, 10, 1.59, 5, '5/05/2022/W2', 3),
	(64, 0, 7.53, 2, '5/05/2022/W2', 4);
/*!40000 ALTER TABLE `zuzycie` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
