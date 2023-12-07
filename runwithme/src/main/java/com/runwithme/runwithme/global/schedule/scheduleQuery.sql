use runwithme;
show tables;

SELECT * FROM user;
SELECT * FROM challenge;
SELECT * FROM run_record;

INSERT INTO challenge(check_yn, cost, date_end, date_start, description, goal_amount, goal_days, goal_type, img_seq, manager_seq, max_member, name, password, reg_time, time_end, time_start, now_member, image_seq) 
		VALUES(false, 0, '2023-10-16', '2023-10-13', '', 60, 3, 'distance', null, 1, 6, '샛벼리', null, '2023-10-13', '2023-10-16', '2023-10-16', 1, null);
INSERT INTO challenge(check_yn, cost, date_end, date_start, description, goal_amount, goal_days, goal_type, img_seq, manager_seq, max_member, name, password, reg_time, time_end, time_start, now_member, image_seq) 
		VALUES(false, 0, '2023-10-17', '2023-10-13', '', 60, 3, 'distance', null, 1, 6, '샛벼리', null, '2023-10-13', '2023-10-16', '2023-10-16', 1, null);
INSERT INTO challenge(check_yn, cost, date_end, date_start, description, goal_amount, goal_days, goal_type, img_seq, manager_seq, max_member, name, password, reg_time, time_end, time_start, now_member, image_seq) 
		VALUES(false, 0, '2023-10-17', '2023-10-13', 'test', 60, 3, 'distance', null, 1, 6, '샛벼리', null, '2023-10-13', '2023-10-16', '2023-10-16', 1, null);
        
INSERT INTO run_record(avg_speed, calorie, challenge_seq, end_time, img_seq, reg_time, running_distance, running_time, start_time, user_seq, image_seq, weekly)
		VALUES(0, 0, 1, 0, 1, '2023-10-17', null, null, null, 1, null, 1);

-- date_format(NOW(), '%Y-%m-%d')