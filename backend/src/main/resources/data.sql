-- Insert sample family profiles
INSERT INTO family_profile (primary_guardian_name, primary_guardian_email, primary_guardian_phone, address, annual_income, onboarding_complete, created_at, updated_at)
VALUES 
('Rajesh Kumar', 'rajesh.kumar@email.com', '9876543210', 'Bangalore, Karnataka', 1500000.00, true, NOW(), NOW()),
('Priya Patel', 'priya.patel@email.com', '9876543211', 'Mumbai, Maharashtra', 2000000.00, true, NOW(), NOW());

-- Insert sample children
INSERT INTO child (name, date_of_birth, gender, current_education_level, family_profile_id, created_at, updated_at)
VALUES 
('Aarav Kumar', '2018-05-15', 'MALE', 'PRIMARY', 1, NOW(), NOW()),
('Diya Patel', '2019-03-22', 'FEMALE', 'KINDERGARTEN', 1, NOW(), NOW()),
('Arjun Singh', '2017-11-08', 'MALE', 'PRIMARY', 2, NOW(), NOW()),
('Zara Khan', '2020-01-30', 'FEMALE', 'PRESCHOOL', 2, NOW(), NOW());

-- Initial Education Cost References
INSERT INTO education_cost_references (education_level, institution_type, region, average_annual_cost, year, data_source)
VALUES
    ('PRIMARY', 'PUBLIC', 'BANGALORE', 50000.00, 2024, 'Education Ministry Survey'),
    ('PRIMARY', 'PRIVATE', 'BANGALORE', 150000.00, 2024, 'Private School Association'),
    ('SECONDARY', 'PUBLIC', 'BANGALORE', 75000.00, 2024, 'Education Ministry Survey'),
    ('SECONDARY', 'PRIVATE', 'BANGALORE', 200000.00, 2024, 'Private School Association'),
    ('UNDERGRADUATE', 'PUBLIC', 'BANGALORE', 100000.00, 2024, 'University Grants Commission'),
    ('UNDERGRADUATE', 'PRIVATE', 'BANGALORE', 300000.00, 2024, 'Private University Association'),
    ('POSTGRADUATE', 'PUBLIC', 'BANGALORE', 150000.00, 2024, 'University Grants Commission'),
    ('POSTGRADUATE', 'PRIVATE', 'BANGALORE', 400000.00, 2024, 'Private University Association');
