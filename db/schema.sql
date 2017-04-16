-- BUILD SCHEMA

DROP TABLE IF EXISTS `guestbookdb`.`event_attendance`;
DROP TABLE IF EXISTS `guestbookdb`.`attendance_status`;
DROP TABLE IF EXISTS `guestbookdb`.`survey_response`;
DROP TABLE IF EXISTS `guestbookdb`.`survey`;
DROP TABLE IF EXISTS `guestbookdb`.`survey_question`;
DROP TABLE IF EXISTS `guestbookdb`.`event`; 
DROP TABLE IF EXISTS `guestbookdb`.`user`; 
DROP TABLE IF EXISTS `guestbookdb`.`user_type`; 

CREATE TABLE `guestbookdb`.`user_type` (
    `user_type_id` TINYINT NOT NULL,
    `user_type` VARCHAR(32),
    PRIMARY KEY (`user_type_id`)
);

CREATE TABLE `guestbookdb`.`user` (
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `user_type_id` TINYINT NOT NULL DEFAULT 0,
    `first_name` VARCHAR(64) NOT NULL,
    `last_name` VARCHAR(64) NOT NULL,
    `email` VARCHAR(256) NOT NULL,
    `password` CHAR(40) NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE (`email`),
    FOREIGN KEY `fk_user$user_type` (`user_type_id`)
        REFERENCES `guestbookdb`.`user_type`(`user_type_id`)
        ON DELETE RESTRICT
);
 
CREATE TABLE `guestbookdb`.`event` (
    `event_id` INT NOT NULL AUTO_INCREMENT,
    `event_name` VARCHAR(256) NOT NULL,
    `start_date_time` DATETIME NOT NULL,
    `end_date_time` DATETIME NOT NULL,
    `presenter_id` INT NOT NULL,
    `open_registration` BOOLEAN NOT NULL DEFAULT FALSE,
    `registration_code` CHAR(8),
    `mandatory_survey` BOOLEAN NOT NULL DEFAULT FALSE,
    `capacity` INT,
    PRIMARY KEY (`event_id`),
    FOREIGN KEY `fk_event$user` (`presenter_id`) 
        REFERENCES `guestbookdb`.`user`(`user_id`)
        ON DELETE RESTRICT
);

CREATE TABLE `guestbookdb`.`survey` (
    `survey_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `event_id` INT NOT NULL,
    `submission_date_time` DATETIME NOT NULL,
    PRIMARY KEY (`survey_id`),
    FOREIGN KEY `fk_survey$user` (`user_id`)
        REFERENCES `guestbookdb`.`user`(`user_id`)
        ON DELETE NO ACTION,
    FOREIGN KEY `fk_survey$event` (`event_id`)
        REFERENCES `guestbookdb`.`event`(`event_id`)
        ON DELETE CASCADE,
    UNIQUE KEY `uk_user_id$event_id` (`user_id`, `event_id`)
);

CREATE TABLE `guestbookdb`.`survey_question` (
    `question_id` TINYINT NOT NULL,
    `question_name` VARCHAR(32) NOT NULL,
    `question_text` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`question_id`)
);
 
CREATE TABLE `guestbookdb`.`survey_response` (
    `survey_id` INT NOT NULL,
    `question_id` TINYINT NOT NULL,
    `response` TINYINT,
    PRIMARY KEY (`survey_id`, `question_id`),
    FOREIGN KEY `fk_survey_response$survey` (`survey_id`) 
        REFERENCES `guestbookdb`.`survey`(`survey_id`)
        ON DELETE CASCADE,
    FOREIGN KEY `fk_survey_response$survey_question` (`question_id`)
        REFERENCES `guestbookdb`.`survey_question`(`question_id`)
        ON DELETE RESTRICT
);

CREATE TABLE `guestbookdb`.`attendance_status` (
    `attendance_status_id` TINYINT NOT NULL,
    `attendance_status` VARCHAR(32),
    PRIMARY KEY (`attendance_status_id`)
);

CREATE TABLE `guestbookdb`.`event_attendance` (
    `event_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `attendance_status_id` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`user_id`, `event_id`),
    FOREIGN KEY `fk_attendance_status$event` (`event_id`) 
        REFERENCES `guestbookdb`.`event`(`event_id`)
        ON DELETE CASCADE,
    FOREIGN KEY `fk_event_attendance$user` (`user_id`) 
        REFERENCES `guestbookdb`.`user`(`user_id`)
        ON DELETE CASCADE,
    FOREIGN KEY `fk_event_attendance$attendance_status` (`attendance_status_id`)
        REFERENCES `guestbookdb`.`attendance_status`(`attendance_status_id`)
        ON DELETE RESTRICT
);

-- INSERT DEFAULT ENUM VALUES

INSERT INTO `guestbookdb`.`user_type`(`user_type_id`, `user_type`) VALUES (0, 'ORGANIZER');
INSERT INTO `guestbookdb`.`user_type`(`user_type_id`, `user_type`) VALUES (1, 'GUEST');

INSERT INTO `guestbookdb`.`attendance_status`(`attendance_status_id`, `attendance_status`) VALUES (0, 'NOT_ATTENDED');
INSERT INTO `guestbookdb`.`attendance_status`(`attendance_status_id`, `attendance_status`) VALUES (1, 'SIGNED_IN');
INSERT INTO `guestbookdb`.`attendance_status`(`attendance_status_id`, `attendance_status`) VALUES (2, 'ATTENDED');

INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (1, 'event_overall', 'Overall opinion of the event');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (2, 'event_venue', 'Opinion of the event venue');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (3, 'pres_overall', 'Overall opinion of the presenter');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (4, 'pres_quality', 'Quality of the presentation');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (5, 'pres_knowledge', 'Presenter\'s knowledge of the material');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (6, 'pres_engage', 'Presenter\'s ability to engage your interest in the material');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (7, 'material_overall', 'Quality of the material presented');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (8, 'repeat_likelihood', 'How likely are you to attend another event by this presenter?');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (9, 'event_rec_likelihood', 'How likely are you to recommend this event to a friend?');
INSERT INTO `guestbookdb`.`survey_question`(`question_id`, `question_name`, `question_text`) VALUES (10, 'pres_rec_likelihood', 'How likely are you to recommend this presenter to a friend?');
