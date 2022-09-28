# ************************************************************
# Antares - SQL Client
# Version 0.5.12
# 
# https://antares-sql.app/
# https://github.com/antares-sql/antares
# 
# Host: 127.0.0.1 (Arch Linux 10.9.2)
# Database: pers_test
# Generation time: 2022-09-27T14:53:37+03:00
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table question
# ------------------------------------------------------------

DROP TABLE IF EXISTS `question`;

CREATE TABLE `question` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;

INSERT INTO `question` (`id`, `text`, `created_on`) VALUES
	(1, "I prefer one-on-one conversations to group activities", "2022-09-26 13:58:06"),
	(2, "I often prefer to express myself in writing", "2022-09-26 13:58:19"),
	(3, "I enjoy solitude", "2022-09-26 13:58:37"),
	(4, "I seem to care about wealth, fame, and status less than my peers", "2022-09-26 13:58:45"),
	(5, "I dislike small talk, but I enjoy talking in-depth about topics that matter to me", "2022-09-26 13:58:53"),
	(6, "People tell me that I’m a good listener", "2022-09-26 13:59:02"),
	(7, "I’m not a big risk-taker", "2022-09-26 14:00:18"),
	(8, "I enjoy work that allows me to “dive in” with few interruptions", "2022-09-26 14:00:27"),
	(9, "I like to celebrate birthdays on a small scale, with only one or two close friends or family members", "2022-09-26 14:00:36"),
	(10, "People describe me as “soft-spoken” or “mellow", "2022-09-26 14:00:44"),
	(11, "I prefer not to show or discuss my work with others until it’s finished", "2022-09-26 14:01:22"),
	(12, "I dislike conflict", "2022-09-26 14:01:30"),
	(13, "I do my best work on my own", "2022-09-26 14:01:37"),
	(14, "I tend to think before I speak", "2022-09-26 14:01:45"),
	(15, "I feel drained after being out and about, even if I’ve enjoyed myself", "2022-09-26 14:01:52"),
	(16, "I often let calls go through to voice-mail", "2022-09-26 14:02:09"),
	(17, "If I had to choose, I’d prefer a weekend with absolutely nothing to do to one with too many things scheduled", "2022-09-26 14:02:17"),
	(18, "I don’t enjoy multi-tasking", "2022-09-26 14:02:26"),
	(19, "I can concentrate easily", "2022-09-26 14:02:33"),
	(20, "In classroom situations, I prefer lectures to seminars", "2022-09-26 14:02:42");

/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of views
# ------------------------------------------------------------

# Creating temporary tables to overcome VIEW dependency errors


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

# Dump completed on 2022-09-27T14:53:37+03:00
