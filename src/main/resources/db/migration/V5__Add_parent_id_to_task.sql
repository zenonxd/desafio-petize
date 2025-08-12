ALTER TABLE tb_task ADD COLUMN parent_id BIGINT;


ALTER TABLE tb_task ADD CONSTRAINT fk_task_parent
FOREIGN KEY (parent_id) REFERENCES tb_task(id);