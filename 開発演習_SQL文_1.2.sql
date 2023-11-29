-- ユーザーの作成・権限付与
ALTER SESSION SET CONTAINER = XEPDB1;  -- 明石さんは不要
CREATE USER console_crud_user IDENTIFIED BY systemsss;
GRANT ALL PRIVILEGES TO console_crud_user;


-- SQL Developerで「console_crud_user」の接続先を作成する。
-- 以降は、「console_crud_user」の接続先に切り替えて実行
-- 部署テーブルの作成
CREATE TABLE department  (
  dept_id NUMBER(1) PRIMARY KEY,
  dept_name VARCHAR2(30 CHAR) NOT NULL
);
-- シーケンスの作成
CREATE SEQUENCE seq_dep NOCACHE;

-- 社員テーブルの作成
CREATE TABLE employee (
  emp_id NUMBER(4) PRIMARY KEY,
  emp_name VARCHAR2(30 CHAR) NOT NULL,
  gender NUMBER(1) NOT NULL,
  birthday DATE NOT NULL,
  dept_id NUMBER(1) NOT NULL REFERENCES department(dept_id)
);
-- シーケンスの作成
CREATE SEQUENCE seq_emp NOCACHE;

-- 部署テーブルへのレコード登録
INSERT INTO department VALUES (seq_dep.NEXTVAL,'営業部');
INSERT INTO department VALUES (seq_dep.NEXTVAL,'経理部');
INSERT INTO department VALUES (seq_dep.NEXTVAL,'総務部');

-- 社員テーブルへのレコード登録
INSERT INTO employee VALUES (seq_emp.NEXTVAL, '鈴木太郎', 1, '1986/10/12', 1);
INSERT INTO employee VALUES (seq_emp.NEXTVAL, '田中二郎', 1, '1979/07/02', 2);
INSERT INTO employee VALUES (seq_emp.NEXTVAL, '渡辺花子', 2, '1988/04/23', 2);
COMMIT;