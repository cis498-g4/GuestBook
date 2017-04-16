

INSERT INTO `guestbookdb`.`user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) VALUES (0, 'Perwaize', 'Ahmed', 'PerwaizeAhmed2013@u.northwestern.edu', '8e52c6ae8abc5d8506fffebd5a7773c5535495d5'); -- sha-1 of 'cis498'
INSERT INTO `guestbookdb`.`user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) VALUES (0, 'Matt', 'Granum', 'MatthewGranum2017@u.northwestern.edu', '8e52c6ae8abc5d8506fffebd5a7773c5535495d5');
INSERT INTO `guestbookdb`.`user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) VALUES (0, 'Scott', 'Langnas', 'ScottLangnas2018@u.northwestern.edu', '8e52c6ae8abc5d8506fffebd5a7773c5535495d5');
INSERT INTO `guestbookdb`.`user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) VALUES (0, 'Mike', 'Molenda', 'm-molenda@northwestern.edu', '8e52c6ae8abc5d8506fffebd5a7773c5535495d5');
INSERT INTO `guestbookdb`.`user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) VALUES (1, 'Albert', 'Einstein', 'albert@cis498.com', '8e52c6ae8abc5d8506fffebd5a7773c5535495d5');
INSERT INTO `guestbookdb`.`user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) VALUES (1, 'Marie', 'Curie', 'marie@cis498.com', '8e52c6ae8abc5d8506fffebd5a7773c5535495d5');
INSERT INTO `guestbookdb`.`user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) VALUES (1, 'Isaac', 'Newton', 'isaac@cis498.com', '8e52c6ae8abc5d8506fffebd5a7773c5535495d5');
INSERT INTO `guestbookdb`.`user`(`user_type_id`, `first_name`, `last_name`, `email`, `password`) VALUES (1, 'Stephen', 'Hawking', 'stephen@cis498.com', '8e52c6ae8abc5d8506fffebd5a7773c5535495d5');


INSERT INTO `guestbookdb`.`event`(`event_name`, `start_date_time`, `end_date_time`, `presenter_id`, `registration_code`, `open_registration`, `mandatory_survey`, `capacity`) VALUES ('Building Webpages', '2017-07-01 10:00:00', '2017-07-01 11:00:00', 1, 'WEBD1111', TRUE, TRUE, 100);
INSERT INTO `guestbookdb`.`event`(`event_name`, `start_date_time`, `end_date_time`, `presenter_id`, `registration_code`, `open_registration`, `mandatory_survey`, `capacity`) VALUES ('Web App Hosting', '2017-07-02 10:00:00', '2017-07-02 11:00:00', 2, 'WEBD2222', TRUE, FALSE, 75);
INSERT INTO `guestbookdb`.`event`(`event_name`, `start_date_time`, `end_date_time`, `presenter_id`, `registration_code`, `open_registration`, `mandatory_survey`, `capacity`) VALUES ('MySQL Databases', '2017-07-01 13:00:00', '2017-07-01 14:00:00', 3, 'DATA3333', FALSE, FALSE, 25);
INSERT INTO `guestbookdb`.`event`(`event_name`, `start_date_time`, `end_date_time`, `presenter_id`, `registration_code`, `open_registration`, `mandatory_survey`, `capacity`) VALUES ('Coding in Java', '2017-07-02 13:00:00', '2017-07-02 14:00:00', 4, 'JAVA4444', FALSE, FALSE, 20);


INSERT INTO `guestbookdb`.`event_attendance`(`user_id`, `event_id`, `attendance_status_id`) VALUES (5, 1, 2);
INSERT INTO `guestbookdb`.`event_attendance`(`user_id`, `event_id`, `attendance_status_id`) VALUES (6, 1, 2);
INSERT INTO `guestbookdb`.`event_attendance`(`user_id`, `event_id`, `attendance_status_id`) VALUES (7, 2, 2);
INSERT INTO `guestbookdb`.`event_attendance`(`user_id`, `event_id`, `attendance_status_id`) VALUES (5, 3, 2);
INSERT INTO `guestbookdb`.`event_attendance`(`user_id`, `event_id`, `attendance_status_id`) VALUES (8, 1, 1);


INSERT INTO `guestbookdb`.`survey`(`user_id`, `event_id`, `submission_date_time`) VALUES (5, 1, '2017-07-03 12:00:00');
INSERT INTO `guestbookdb`.`survey`(`user_id`, `event_id`, `submission_date_time`) VALUES (6, 1, '2017-07-05 12:00:00');
INSERT INTO `guestbookdb`.`survey`(`user_id`, `event_id`, `submission_date_time`) VALUES (7, 2, '2017-07-03 14:00:00');
INSERT INTO `guestbookdb`.`survey`(`user_id`, `event_id`, `submission_date_time`) VALUES (5, 3, '2017-07-06 12:00:00');


INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 1, 5);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 2, 6);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 3, 4);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 4, 9);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 5, 7);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 6, 9);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 7, 8);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 8, 4);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 9, 5);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (1, 10, 6);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 1, 6);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 2, 2);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 3, 9);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 4, 9);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 5, 8);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 6, 6);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 7, 6);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 8, 5);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 9, 7);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (2, 10, 5);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 1, 3);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 2, 7);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 3, 9);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 4, 7);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 5, 7);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 6, 6);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 7, 6);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 8, 7);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 9, 9);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (3, 10, 9);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 1, 8);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 2, 7);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 3, 4);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 4, 5);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 5, 5);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 6, 4);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 7, 9);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 8, 6);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 9, 5);
INSERT INTO `guestbookdb`.`survey_response`(`survey_id`, `question_id`, `response`) VALUES (4, 10, 5);
