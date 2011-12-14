--
-- WORKFLOWALERT-2 : Add the possibility to choose the creation date of the record as the reference date
-- WORKFLOWALERT-4 : Add the possibility to choose the modification date of the record as the reference date
--
ALTER TABLE task_alert_cf ADD COLUMN id_retrieval_type SMALLINT DEFAULT 0 NOT NULL;
UPDATE task_alert_cf SET id_retrieval_type = 1;

--
-- WORKFLOWALERT-5 : The reference date should be stored in the module-workflow-alert tables instead of fetching directly at the records
--
ALTER TABLE task_alert ADD COLUMN reference_date TIMESTAMP DEFAULT NULL NULL;
