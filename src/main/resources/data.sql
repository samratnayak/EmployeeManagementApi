insert into employee(id, name, age, managerid) values (NEXT VALUE FOR emp_seq, 'John', 30, null);
insert into employee(id, name, age, managerid) values (NEXT VALUE FOR emp_seq, 'Vipul', 27, select id from employee where name='John');
insert into employee(id, name, age, managerid) values (NEXT VALUE FOR emp_seq, 'Dave', 26, select id from employee where name='John');
insert into employee(id, name, age, managerid) values (NEXT VALUE FOR emp_seq, 'Will', 25, select id from employee where name='Dave');
insert into employee(id, name, age, managerid) values (NEXT VALUE FOR emp_seq, 'Kailash', 23, select id from employee where name='Will');