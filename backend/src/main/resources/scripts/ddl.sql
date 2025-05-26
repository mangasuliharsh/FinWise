-- 1. Create the database
CREATE DATABASE IF NOT EXISTS FinWise;
USE FinWise;

-- 2. Users Table
CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `username` varchar(50) NOT NULL,
                         `email` varchar(100) NOT NULL,
                         `password` varchar(255) NOT NULL,
                         `first_name` varchar(50) NOT NULL,
                         `last_name` varchar(50) NOT NULL,
                         `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                         `last_updated_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         `role` varchar(20) DEFAULT 'USER',
                         `account_non_expired` tinyint(1) DEFAULT '1',
                         `account_non_locked` tinyint(1) DEFAULT '1',
                         `credentials_non_expired` tinyint(1) DEFAULT '1',
                         `enabled` tinyint(1) DEFAULT '1',
                         `image_url` varchar(255) DEFAULT NULL,
                         `is_new_user` tinyint(1) DEFAULT '1',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `username` (`username`),
                         UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 3. Family Profiles Table
CREATE TABLE family_profiles (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 user_id BIGINT NOT NULL,
                                 family_size INT NOT NULL,
                                 monthly_income DECIMAL(15,2) NOT NULL,
                                 monthly_expenses DECIMAL(15,2) NOT NULL,
                                 location VARCHAR(100),
                                 risk_tolerance VARCHAR(10) CHECK (risk_tolerance IN ('LOW', 'MEDIUM', 'HIGH')) DEFAULT 'MEDIUM',
                                 created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 4. Children Table
CREATE TABLE children (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          family_profile_id BIGINT NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          date_of_birth DATE NOT NULL,
                          current_education_level VARCHAR(50),
                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (family_profile_id) REFERENCES family_profiles(id) ON DELETE CASCADE
);

-- 5. Education Plans Table
CREATE TABLE education_plans (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 child_id BIGINT NOT NULL,
                                 plan_name VARCHAR(100) NOT NULL,
                                 education_level VARCHAR(50) NOT NULL,
                                 institution_type VARCHAR(50),
                                 estimated_start_year INT NOT NULL,
                                 estimated_end_year INT NOT NULL,
                                 estimated_total_cost DECIMAL(15,2) NOT NULL,
                                 current_savings DECIMAL(15,2) DEFAULT 0,
                                 monthly_contribution DECIMAL(15,2) DEFAULT 0,
                                 inflation_rate DECIMAL(5,2) DEFAULT 6.00,
                                 notes TEXT,
                                 created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (child_id) REFERENCES children(id) ON DELETE CASCADE
);

-- 6. Marriage Plans Table
CREATE TABLE marriage_plans (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                family_profile_id BIGINT NOT NULL,
                                plan_name VARCHAR(100) NOT NULL,
                                for_name VARCHAR(100),
                                relationship VARCHAR(50),
                                estimated_year INT NOT NULL,
                                estimated_total_cost DECIMAL(15,2) NOT NULL,
                                current_savings DECIMAL(15,2) DEFAULT 0,
                                monthly_contribution DECIMAL(15,2) DEFAULT 0,
                                inflation_rate DECIMAL(5,2) DEFAULT 6.00,
                                notes TEXT,
                                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                FOREIGN KEY (family_profile_id) REFERENCES family_profiles(id) ON DELETE CASCADE
);

-- 7. Marriage Expense Categories Table
CREATE TABLE marriage_expense_categories (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             marriage_plan_id BIGINT NOT NULL,
                                             category_name VARCHAR(100) NOT NULL,
                                             estimated_cost DECIMAL(15,2) NOT NULL,
                                             priority VARCHAR(10) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')) DEFAULT 'MEDIUM',
                                             notes TEXT,
                                             created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                             FOREIGN KEY (marriage_plan_id) REFERENCES marriage_plans(id) ON DELETE CASCADE
);

-- 8. Savings Plans Table
CREATE TABLE savings_plans (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               family_profile_id BIGINT NOT NULL,
                               plan_name VARCHAR(100) NOT NULL,
                               goal_amount DECIMAL(15,2) NOT NULL,
                               current_amount DECIMAL(15,2) DEFAULT 0,
                               monthly_contribution DECIMAL(15,2) DEFAULT 0,
                               target_completion_date DATE,
                               purpose VARCHAR(100),
                               priority VARCHAR(10) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')) DEFAULT 'MEDIUM',
                               notes TEXT,
                               created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (family_profile_id) REFERENCES family_profiles(id) ON DELETE CASCADE
);

-- 9. Investment Options Table
CREATE TABLE investment_options (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(100) NOT NULL,
                                    type VARCHAR(50) NOT NULL,
                                    risk_level VARCHAR(10) CHECK (risk_level IN ('LOW', 'MEDIUM', 'HIGH')) NOT NULL,
                                    expected_annual_return DECIMAL(5,2) NOT NULL,
                                    min_investment_period INT,
                                    description TEXT,
                                    is_active BOOLEAN DEFAULT TRUE,
                                    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 10. Investments Table
CREATE TABLE investments (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             savings_plan_id BIGINT NOT NULL,
                             investment_option_id BIGINT NOT NULL,
                             amount DECIMAL(15,2) NOT NULL,
                             start_date DATE NOT NULL,
                             maturity_date DATE,
                             current_value DECIMAL(15,2),
                             status VARCHAR(20) CHECK (status IN ('ACTIVE', 'MATURED', 'WITHDRAWN')) DEFAULT 'ACTIVE',
                             created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (savings_plan_id) REFERENCES savings_plans(id) ON DELETE CASCADE,
                             FOREIGN KEY (investment_option_id) REFERENCES investment_options(id)
);

-- 11. Financial Transactions Table
CREATE TABLE financial_transactions (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        user_id BIGINT NOT NULL,
                                        transaction_type VARCHAR(20) CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'ADJUSTMENT')) NOT NULL,
                                        amount DECIMAL(15,2) NOT NULL,
                                        transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        description TEXT,
                                        related_plan_id BIGINT,
                                        related_plan_type VARCHAR(20),
                                        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 12. Notifications Table
CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               title VARCHAR(100) NOT NULL,
                               message TEXT NOT NULL,
                               is_read BOOLEAN DEFAULT FALSE,
                               notification_type VARCHAR(20) NOT NULL,
                               created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 13. Education Cost References Table
CREATE TABLE education_cost_references (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           education_level VARCHAR(50) NOT NULL,
                                           institution_type VARCHAR(50) NOT NULL,
                                           region VARCHAR(50),
                                           average_annual_cost DECIMAL(15,2) NOT NULL,
                                           year INT NOT NULL,
                                           data_source VARCHAR(255),
                                           created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 14. Marriage Cost References Table
CREATE TABLE marriage_cost_references (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          category VARCHAR(50) NOT NULL,
                                          region VARCHAR(50),
                                          average_cost DECIMAL(15,2) NOT NULL,
                                          year INT NOT NULL,
                                          data_source VARCHAR(255),
                                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 15. Economic Indicators Table
CREATE TABLE economic_indicators (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     indicator_name VARCHAR(50) NOT NULL,
                                     value DECIMAL(10,4) NOT NULL,
                                     year INT NOT NULL,
                                     month INT,
                                     data_source VARCHAR(255),
                                     created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 16. Reports Table
CREATE TABLE reports (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         report_name VARCHAR(100) NOT NULL,
                         report_type VARCHAR(50) NOT NULL,
                         generated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         file_path VARCHAR(255),
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_user_id ON family_profiles(user_id);
CREATE INDEX idx_family_profile_id ON children(family_profile_id);
CREATE INDEX idx_child_id ON education_plans(child_id);
CREATE INDEX idx_family_marriage ON marriage_plans(family_profile_id);
CREATE INDEX idx_family_savings ON savings_plans(family_profile_id);
