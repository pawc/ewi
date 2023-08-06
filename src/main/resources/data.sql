INSERT INTO `machine` (`id`, `is_active`, `name`, `description`) VALUES
	('C1', 1, 'Ciagnik 1', 'test ciagnik'),
	('W2', 1, 'Wozek', 'testowy wozek');

INSERT INTO `fuel_consumption_standard` (`id`, `is_used_for_heating`, `unit`, `val`, `machine_id`) VALUES
    (1, 1, 'ON/H', 1.23, 'C1'),
    (2, 0, 'L/H', 4.56, 'C1'),
    (3, 1, 'ON/H', 4.21, 'W2'),
    (4, 0, 'C', 1.2, 'W2');

INSERT INTO `document` (`number`, `date`, `kilometers`, `kilometers_trailer`, `machine_id`) VALUES
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


INSERT INTO `category` (`name`, `is_carried_over`) VALUES
	('Ciągniki', 1);

INSERT INTO `kilometers` (`id`, `month_val`, `year_val`, `val`, `machine_id`) VALUES
	(1, 4, 2022, 120, 'C1'),
	(2, 4, 2022, 50, 'W2');

INSERT INTO `machine_category` (`machine_id`, `category_name`) VALUES
	('C1', 'Ciągniki');

INSERT INTO `fuel_initial_state` (`id`, `month_val`, `year_val`, `val`, `fuel_consumption_standard_id`) VALUES
	(1, 4, 2022, 2.4, 1),
	(2, 4, 2022, 1.2, 2),
	(3, 4, 2022, 5.5, 3),
	(4, 4, 2022, 12.1, 4);

INSERT INTO `fuel_consumption` (`id`, `heating`, `val`, `refueled`, `document_number`, `fuel_consumption_standard_id`) VALUES
	(1, 1, 1.1, 0, '1/04/2022/C1', 1),
	(2, 0, 2.1, 11, '1/04/2022/C1', 2),
	(3, 1, 1.2, 4, '2/04/2022/C1', 1),
	(4, 0, 2.2, 8, '2/04/2022/C1', 2),
	(5, 2.5, 1.3, 5, '3/04/2022/C1', 1),
	(6, 0, 2.3, 10, '3/04/2022/C1', 2),
	(7, 1.2, 1.4, 11, '4/04/2022/C1', 1),
	(8, 0, 2.4, 20, '4/04/2022/C1', 2),
	(9, 0, 1.5, 0, '5/04/2022/C1', 1),
	(10, 0, 2.5, 20, '5/04/2022/C1', 2),
	(11, 2, 1.6, 15, '6/04/2022/C1', 1),
	(12, 0, 2.6, 5, '6/04/2022/C1', 2),
	(13, 5, 1.7, 7, '7/04/2022/C1', 1),
	(14, 0, 2.7, 11, '7/04/2022/C1', 2),
	(15, 4, 1.8, 0, '8/04/2022/C1', 1),
	(16, 0, 2.8, 10, '8/04/2022/C1', 2),
	(17, 1, 1.9, 0, '9/04/2022/C1', 1),
	(18, 0, 2.9, 7, '9/04/2022/C1', 2),
	(19, 1, 2, 2, '10/04/2022/C1', 1),
	(20, 0, 3, 20, '10/04/2022/C1', 2),
	(21, 1, 1.11, 1, '1/04/2022/W2', 3),
	(22, 0, 2.22, 2, '1/04/2022/W2', 4),
	(23, 10, 1.22, 8, '2/04/2022/W2', 3),
	(24, 0, 2.22, 4, '2/04/2022/W2', 4),
	(25, 1, 1.33, 20, '3/04/2022/W2', 3),
	(26, 0, 2.33, 8, '3/04/2022/W2', 4),
	(27, 1, 1.44, 10, '4/04/2022/W2', 3),
	(28, 0, 2.44, 11, '4/04/2022/W2', 4),
	(29, 10, 1.55, 20, '5/04/2022/W2', 3),
	(30, 0, 2.55, 1, '5/04/2022/W2', 4),
	(31, 2, 1.55, 1, '6/04/2022/W2', 3),
	(32, 0, 2.55, 2, '6/04/2022/W2', 4),
	(33, 0, 1.77, 5, '7/04/2022/W2', 3),
	(34, 0, 2.77, 1, '7/04/2022/W2', 4),
	(35, 1, 2.9, 2, '1/05/2022/C1', 1),
	(36, 0, 1.9, 3, '1/05/2022/C1', 2),
	(37, 10, 2.8, 30, '2/05/2022/C1', 1),
	(38, 0, 1.8, 20, '2/05/2022/C1', 2),
	(39, 1, 2.7, 2, '3/05/2022/C1', 1),
	(40, 0, 1.7, 20, '3/05/2022/C1', 2),
	(41, 7, 2.5, 13, '4/05/2022/C1', 1),
	(42, 0, 1.5, 20, '4/05/2022/C1', 2),
	(43, 5, 2.2, 10, '5/05/2022/C1', 1),
	(44, 0, 1.1, 4, '5/05/2022/C1', 2),
	(45, 1, 1.23, 10, '1/05/2022/W2', 3),
	(46, 0, 2.23, 20, '1/05/2022/W2', 4),
	(47, 1, 2.45, 10, '2/05/2022/W2', 3),
	(48, 0, 3.21, 0, '2/05/2022/W2', 4),
	(49, 1, 7.41, 50, '3/05/2022/W2', 3),
	(50, 0, 8.12, 3, '3/05/2022/W2', 4),
	(51, 1, 4.11, 11, '4/05/2022/W2', 3),
	(52, 0, 2.11, 20, '4/05/2022/W2', 4),
	(53, 10, 1.59, 5, '5/05/2022/W2', 3),
	(54, 0, 7.53, 2, '5/05/2022/W2', 4);