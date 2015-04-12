-- Create table
create table QUARTZ_SCHEDULE_JOB
(
  id              NUMBER not null,
  job_name        VARCHAR2(64) not null,
  job_status      CHAR(1) not null,
  job_group       VARCHAR2(64) not null,
  cron_expression VARCHAR2(32) not null,
  bean_class      VARCHAR2(64),
  bean_id         VARCHAR2(32),
  method_name     VARCHAR2(32) not null,
  create_time     DATE,
  update_time     DATE
)
 
-- Add comments to the columns 
comment on column QUARTZ_SCHEDULE_JOB.job_status
  is '1.start 2.stop';
-- Create/Recreate primary, unique and foreign key constraints 
alter table QUARTZ_SCHEDULE_JOB
  add constraint QUARTZ_PK_ID primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
