insert into employee(id, name, age, managerid) values (NEXT VALUE FOR emp_seq, 'Samrat', 30, null);
insert into employee(id, name, age, managerid) values (NEXT VALUE FOR emp_seq, 'Vipul', 26, select id from employee where name='Samrat');
insert into employee(id, name, age, managerid) values (NEXT VALUE FOR emp_seq, 'Dev', 26, select id from employee where name='Samrat');