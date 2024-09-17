-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 14 Eyl 2024, 10:09:03
-- Sunucu sürümü: 10.4.28-MariaDB
-- PHP Sürümü: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `online_exam_system`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `answers`
--

CREATE TABLE `answers` (
  `id` bigint(20) NOT NULL,
  `answer_text` varchar(255) NOT NULL,
  `is_correct` bit(1) DEFAULT NULL,
  `receiving_points` int(11) DEFAULT NULL,
  `exam_id` bigint(20) NOT NULL,
  `question_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `departments`
--

CREATE TABLE `departments` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `departments`
--

INSERT INTO `departments` (`id`, `name`) VALUES
(1, 'Yazılım Mühendisliği'),
(2, 'Bilgisayar Mühendisliği');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `exams`
--

CREATE TABLE `exams` (
  `id` bigint(20) NOT NULL,
  `duration` int(11) NOT NULL,
  `end_datetime` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL,
  `start_datetime` datetime(6) NOT NULL,
  `department_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `exams`
--

INSERT INTO `exams` (`id`, `duration`, `end_datetime`, `title`, `start_datetime`, `department_id`, `user_id`) VALUES
(2, 35, '2025-11-13 17:02:00.000000', 'Mobil Programlama', '2024-09-13 15:04:00.000000', 1, 4),
(3, 25, '2025-12-13 20:08:00.000000', 'Mobil 2', '2025-01-13 15:07:00.000000', 1, 4),
(4, 20, '2024-09-13 15:10:00.000000', 'Mobil 3', '2024-09-13 15:09:00.000000', 1, 4),
(5, 5, '2024-09-13 20:29:00.000000', 'ssdf', '2024-09-13 20:23:00.000000', 1, 4);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `exam_participations`
--

CREATE TABLE `exam_participations` (
  `id` bigint(20) NOT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `is_active` varchar(255) DEFAULT NULL,
  `points` int(11) NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `exam_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `questions`
--

CREATE TABLE `questions` (
  `id` bigint(20) NOT NULL,
  `correct_answer` varchar(255) DEFAULT NULL,
  `option1` varchar(255) DEFAULT NULL,
  `option2` varchar(255) DEFAULT NULL,
  `option3` varchar(255) DEFAULT NULL,
  `option4` varchar(255) DEFAULT NULL,
  `option5` varchar(255) DEFAULT NULL,
  `points` int(11) NOT NULL,
  `question_text` varchar(255) NOT NULL,
  `question_type` varchar(255) NOT NULL,
  `exam_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `questions`
--

INSERT INTO `questions` (`id`, `correct_answer`, `option1`, `option2`, `option3`, `option4`, `option5`, `points`, `question_text`, `question_type`, `exam_id`) VALUES
(1, 'android (google), ios (apple), windows phone (microsoft), ark os, harmony os (huawei)', NULL, NULL, NULL, NULL, NULL, 50, ' Günümüzde en çok tercih edilen mobil işletim sistemlerinden 3 tanesinin adını yazınız.', 'klasik', 2),
(2, 'Image', 'Image', 'Label', 'Button', 'Text', 'Screen', 25, 'App Lab ‘da ekrana resim eklemek için hangi bileşen kullanılır?', 'coktan_secme', 2),
(3, 'yanlis', NULL, NULL, NULL, NULL, NULL, 25, ' “text Input” öğesinde ile birlikte kullanılan getText() komutu ile sayısal değer okunur.', 'dogru_yanlis', 2),
(4, 'dogru', NULL, NULL, NULL, NULL, NULL, 100, ' JavaScript ile blok kod ve metin kodlarla bir uygulama yapılır ve paylaşılabilir.', 'dogru_yanlis', 3),
(5, 'asdasd', NULL, NULL, NULL, NULL, NULL, 100, ' JavaScript ile blok kod ve metin kodlarla bir uygulama yapılır ve paylaşılabilir.', 'klasik', 4),
(6, 'dsfsdf', NULL, NULL, NULL, NULL, NULL, 100, 'sdfds', 'klasik', 5);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `students`
--

CREATE TABLE `students` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `student_number` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `teachers`
--

CREATE TABLE `teachers` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `teacher_number` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `department_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `users`
--

INSERT INTO `users` (`id`, `email`, `is_active`, `name`, `number`, `password`, `role`, `surname`, `department_id`) VALUES
(1, 'ahmet@outlook.com', b'1', 'Ahmet', '2112101051', '$2a$10$8Hu.Eac9T1gkwRAMFUBqqOvsUPVA5FoFc/WQnNCuhQdKvRsf7X5ZK', 'ADMIN', 'Tekman', NULL),
(3, 'ahmet7582@outlook.com', b'1', 'Ahmet', '12345678910', '$2a$10$jeknqGo6u4aCYRIlKG.Zwuaw9zC5sv1QaM8uoGN43lyw1KGSCNqRS', 'STUDENT', 'Tekman', 1),
(4, 'ardatekman38@gmail.com', b'1', 'Arda', '12345678911', '$2a$10$eC14uTV9IfpSHwFv/e8wwOujMxmhB3Un0d7LtxsD5zpU1/D0xdP8G', 'TEACHER', 'Tekman', NULL);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `answers`
--
ALTER TABLE `answers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKm0pc3y2c8era03pfi3fu2gmie` (`exam_id`),
  ADD KEY `FK3erw1a3t0r78st8ty27x6v3g1` (`question_id`),
  ADD KEY `FK5bp3d5loftq2vjn683ephn75a` (`user_id`);

--
-- Tablo için indeksler `departments`
--
ALTER TABLE `departments`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `exams`
--
ALTER TABLE `exams`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKinp3vh2mmm2lc1m5rsu8yrbfd` (`department_id`),
  ADD KEY `FKi63cpl1xkgy32iq68ru4ypjn4` (`user_id`);

--
-- Tablo için indeksler `exam_participations`
--
ALTER TABLE `exam_participations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKdb4jll9n059h4wwxoxjfny74p` (`exam_id`),
  ADD KEY `FKs7ggxemn0vbv4ugdlnw382ydb` (`user_id`);

--
-- Tablo için indeksler `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKrk78bmt53fns7np8casqa3q44` (`exam_id`);

--
-- Tablo için indeksler `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `teachers`
--
ALTER TABLE `teachers`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsbg59w8q63i0oo53rlgvlcnjq` (`department_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `answers`
--
ALTER TABLE `answers`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Tablo için AUTO_INCREMENT değeri `departments`
--
ALTER TABLE `departments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Tablo için AUTO_INCREMENT değeri `exams`
--
ALTER TABLE `exams`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Tablo için AUTO_INCREMENT değeri `exam_participations`
--
ALTER TABLE `exam_participations`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Tablo için AUTO_INCREMENT değeri `questions`
--
ALTER TABLE `questions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Tablo için AUTO_INCREMENT değeri `students`
--
ALTER TABLE `students`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `teachers`
--
ALTER TABLE `teachers`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `answers`
--
ALTER TABLE `answers`
  ADD CONSTRAINT `FK3erw1a3t0r78st8ty27x6v3g1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`),
  ADD CONSTRAINT `FK5bp3d5loftq2vjn683ephn75a` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKm0pc3y2c8era03pfi3fu2gmie` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`);

--
-- Tablo kısıtlamaları `exams`
--
ALTER TABLE `exams`
  ADD CONSTRAINT `FKi63cpl1xkgy32iq68ru4ypjn4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKinp3vh2mmm2lc1m5rsu8yrbfd` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`);

--
-- Tablo kısıtlamaları `exam_participations`
--
ALTER TABLE `exam_participations`
  ADD CONSTRAINT `FKdb4jll9n059h4wwxoxjfny74p` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`),
  ADD CONSTRAINT `FKs7ggxemn0vbv4ugdlnw382ydb` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Tablo kısıtlamaları `questions`
--
ALTER TABLE `questions`
  ADD CONSTRAINT `FKrk78bmt53fns7np8casqa3q44` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`);

--
-- Tablo kısıtlamaları `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FKsbg59w8q63i0oo53rlgvlcnjq` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
