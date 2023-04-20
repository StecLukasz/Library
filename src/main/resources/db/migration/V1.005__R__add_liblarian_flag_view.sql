GRANT SELECT ON newhr.pos_employee TO books;
GRANT SELECT ON newhr.pos_group TO books;

DROP VIEW IF EXISTS newhr_employee_view;
CREATE VIEW newhr_employee_view
            (id, username, first_name, first_name_pl, last_name, last_name_pl, email, department, employment_form, manager_id, role) AS
SELECT e.id,
       e.login,
       e.first_name,
       e.first_name_pl,
       e.last_name,
       e.last_name_pl,
       e.email,
       pg.name,
       e.employment_form,
       e.manager_id,
       CASE
           WHEN e.flag_senior_director IS TRUE THEN 'DIRECTOR'
           WHEN e.flag_hr IS TRUE and pg.name = 'Administration' THEN 'HR'
           WHEN e.flag_manager IS TRUE THEN 'MANAGER'
           WHEN e.flag_hr IS TRUE THEN 'HR'
           WHEN e.flag_librarian IS TRUE THEN 'LIBRARIAN'

           ELSE 'USER'
           END AS role
FROM newhr.pos_employee e
         JOIN newhr.pos_group pg ON pg.id = e.group_id;

ALTER TABLE newhr_employee_view
    OWNER TO skills;


grant select on newhr_employee_view to dmichna;

grant select on newhr_employee_view to backup;




