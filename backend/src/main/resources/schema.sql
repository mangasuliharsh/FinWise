-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist (in correct order to handle foreign key constraints)
DROP TABLE IF EXISTS education_plans;
DROP TABLE IF EXISTS education_plan;
DROP TABLE IF EXISTS child;
DROP TABLE IF EXISTS family_profile;
DROP TABLE IF EXISTS education_cost_references;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create family_profile table
CREATE TABLE family_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    primary_guardian_name VARCHAR(255) NOT NULL,
    primary_guardian_email VARCHAR(255) NOT NULL,
    primary_guardian_phone VARCHAR(20),
    secondary_guardian_name VARCHAR(255),
    secondary_guardian_email VARCHAR(255),
    secondary_guardian_phone VARCHAR(20),
    address VARCHAR(255) NOT NULL,
    annual_income DECIMAL(19,2) NOT NULL,
    onboarding_complete BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create child table
CREATE TABLE child (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    current_education_level VARCHAR(50) NOT NULL,
    family_profile_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (family_profile_id) REFERENCES family_profile(id)
);

-- Create education_plans table
CREATE TABLE education_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL,
    plan_name VARCHAR(255),
    education_level VARCHAR(50) NOT NULL,
    institution_type VARCHAR(50),
    estimated_start_year INT NOT NULL,
    estimated_end_year INT NOT NULL,
    estimated_total_cost DECIMAL(19,2) NOT NULL,
    current_savings DECIMAL(19,2) DEFAULT 0.00,
    monthly_contribution DECIMAL(19,2) DEFAULT 0.00,
    inflation_rate DECIMAL(5,2) DEFAULT 6.00,
    notes TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (child_id) REFERENCES child(id)
);

-- Create education_cost_references table
CREATE TABLE education_cost_references (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    education_level VARCHAR(50) NOT NULL,
    institution_type VARCHAR(50) NOT NULL,
    region VARCHAR(100) NOT NULL,
    average_annual_cost DECIMAL(19,2) NOT NULL,
    year INT NOT NULL,
    data_source VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

