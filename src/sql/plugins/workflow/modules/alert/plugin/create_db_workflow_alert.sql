--
-- Table structure for table task_alert_cf
--
DROP TABLE IF EXISTS task_alert_cf;
CREATE TABLE task_alert_cf(
  id_task INT DEFAULT 0 NOT NULL,
  id_directory INT DEFAULT 0 NOT NULL,
  id_state_after_deadline INT DEFAULT 0 NOT NULL,
  position_directory_entry_date INT DEFAULT 0 NOT NULL,
  nb_days_to_date INT DEFAULT 0 NOT NULL,
  id_retrieval_type SMALLINT DEFAULT 0 NOT NULL,
  PRIMARY KEY (id_task)
);

--
-- Table structure for table task_alert_value
--
DROP TABLE IF EXISTS task_alert;
CREATE TABLE task_alert(
  id_history INT DEFAULT 0 NOT NULL,
  id_task INT DEFAULT 0 NOT NULL,
  reference_date TIMESTAMP DEFAULT NULL NULL,
  PRIMARY KEY (id_history, id_task)
);
