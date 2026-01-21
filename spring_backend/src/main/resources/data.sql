--USERS

-- you can user gen_random_uuid () to generate random IDs, use this only to generate testdata


insert into users (id, email,first_name,last_name, password)
values  ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'admin@example.com', 'James','Bond', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6' ), -- Password: 1234
        ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'user@example.com', 'Tyler','Durden', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'), -- Password: 1234
        ('31774049-72ca-4345-8dce-e27955485f52', 'default@example.com', 'Jamal','Wilson', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6') -- Password: 1234
 ON CONFLICT DO NOTHING;


--ROLES
INSERT INTO role(id, name)
VALUES ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', 'DEFAULT'),
('ab505c92-7280-49fd-a7de-258e618df074', 'ADMIN'),
('c6aee32d-8c35-4481-8b3e-a876a39b0c02', 'USER')
ON CONFLICT DO NOTHING;

--AUTHORITIES
INSERT INTO authority(id, name)
VALUES ('2ebf301e-6c61-4076-98e3-2a38b31daf86', 'USER_CREATE'),
       ('76d2cbf6-5845-470e-ad5f-2edb9e09a868', 'USER_READ'),
       ('21c942db-a275-43f8-bdd6-d048c21bf5ab', 'USER_DEACTIVATE'),
       ('0f6fcc30-ddba-4504-8c17-a3dc81e1f868', 'USER_MODIFY')
    ON CONFLICT DO NOTHING;

--assign roles to users
insert into users_role (users_id, role_id)
values ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1'),
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1'),
       ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'ab505c92-7280-49fd-a7de-258e618df074'),
       ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('31774049-72ca-4345-8dce-e27955485f52', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1')
 ON CONFLICT DO NOTHING;

--assign authorities to roles
INSERT INTO role_authority(role_id, authority_id)
VALUES ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', '2ebf301e-6c61-4076-98e3-2a38b31daf86'),
       ('ab505c92-7280-49fd-a7de-258e618df074', '76d2cbf6-5845-470e-ad5f-2edb9e09a868'),
       ('ab505c92-7280-49fd-a7de-258e618df074', '0f6fcc30-ddba-4504-8c17-a3dc81e1f868'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '21c942db-a275-43f8-bdd6-d048c21bf5ab')
    ON CONFLICT DO NOTHING;

--ENTRIES
INSERT INTO list_entry(id, title, text, importance, user_id, created_at)
VALUES
    -- User 1 Entries (0d8fa44c-54fd-4cd0-ace9-2a7da57992de)
    ('a1b2c3d4-e5f6-47a8-b9c0-d1e2f3a4b5c6', 'Project Deadline', 'Finish the API documentation by Friday.', 'HIGH', '0d8fa44c-54fd-4cd0-ace9-2a7da57992de', now()),
    ('b2c3d4e5-f6a7-48b9-c0d1-e2f3a4b5c6d7', 'Grocery List', 'Milk, eggs, and coffee beans.', 'LOW', '0d8fa44c-54fd-4cd0-ace9-2a7da57992de', now()),
    ('c3d4e5f6-a7b8-49c9-d0e1-f2a3b4c5d6e7', 'Team Sync', 'Discussion about the new sprint goals.', 'MEDIUM', '0d8fa44c-54fd-4cd0-ace9-2a7da57992de', now()),

    -- User 2 Entries (ba804cb9-fa14-42a5-afaf-be488742fc54)
    ('d4e5f6a7-b8c9-4ad0-e1f2-a3b4c5d6e7f8', 'Security Update', 'Patch the production server vulnerabilities.', 'HIGH', 'ba804cb9-fa14-42a5-afaf-be488742fc54', now()),
    ('e5f6a7b8-c9d0-4be1-f2a3-b4c5d6e7f8a9', 'Idea Scrapbook', 'Maybe build a CLI tool for this database?', 'MEDIUM', 'ba804cb9-fa14-42a5-afaf-be488742fc54', now()),
    ('f6a7b8c9-d0e1-4cf2-a3b4-c5d6e7f8a9b0', 'Personal Reminder', 'Call the dentist for an appointment.', 'LOW', 'ba804cb9-fa14-42a5-afaf-be488742fc54', now())
    ON CONFLICT DO NOTHING;