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


INSERT INTO `guestbookdb`.`survey`(`user_id`, `event_id`, `submission_date_time`, `response_01`, `response_02`, `response_03`, `response_04`, `response_05`, `response_06`, `response_07`, `response_08`, `response_09`, `response_10`
) VALUES (5, 1, '2017-07-03 12:00:00', 5, 6, 4, 9, 7, 9, 8, 4, 5, 6);
INSERT INTO `guestbookdb`.`survey`(`user_id`, `event_id`, `submission_date_time`, `response_01`, `response_02`, `response_03`, `response_04`, `response_05`, `response_06`, `response_07`, `response_08`, `response_09`, `response_10`
) VALUES (6, 1, '2017-07-05 12:00:00', 6, 2, 9, 9, 8, 6, 6, 5, 7, 5);
INSERT INTO `guestbookdb`.`survey`(`user_id`, `event_id`, `submission_date_time`, `response_01`, `response_02`, `response_03`, `response_04`, `response_05`, `response_06`, `response_07`, `response_08`, `response_09`, `response_10`
) VALUES (7, 2, '2017-07-03 14:00:00', 3, 7, 9, 7, 7, 6, 6, 7, 9, 9);
INSERT INTO `guestbookdb`.`survey`(`user_id`, `event_id`, `submission_date_time`, `response_01`, `response_02`, `response_03`, `response_04`, `response_05`, `response_06`, `response_07`, `response_08`, `response_09`, `response_10`
) VALUES (5, 3, '2017-07-06 12:00:00', 8, 7, 4, 5, 5, 4, 9, 6, 5, 5);
