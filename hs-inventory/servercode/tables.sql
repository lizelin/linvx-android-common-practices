prompt PL/SQL Developer import file
prompt Created on 2016-05-08 by lizelin
set feedback off
set define off
prompt Dropping PSS_INVENTORY_ACCOUNT...
drop table PSS_INVENTORY_ACCOUNT cascade constraints;
prompt Dropping PSS_INVENTORY_ACC_REF_WH...
drop table PSS_INVENTORY_ACC_REF_WH cascade constraints;
prompt Creating PSS_INVENTORY_ACCOUNT...
create table PSS_INVENTORY_ACCOUNT
(
  vc2account     VARCHAR2(40) not null,
  vc2password    VARCHAR2(64),
  vc2name        VARCHAR2(32),
  vc2enabledflag CHAR(1)
)
;
alter table PSS_INVENTORY_ACCOUNT
  add primary key (VC2ACCOUNT);

prompt Creating PSS_INVENTORY_ACC_REF_WH...
create table PSS_INVENTORY_ACC_REF_WH
(
  vc2guid          VARCHAR2(10) not null,
  vc2account       VARCHAR2(40),
  vc2warehouseguid VARCHAR2(40),
  vc2warehousetype VARCHAR2(40),
  vc2warehousename VARCHAR2(200)
)
;
alter table PSS_INVENTORY_ACC_REF_WH
  add primary key (VC2GUID);

set feedback on
set define on
prompt Done.
