-- Safe database structure update script
-- Execution time: 2025-01-08

-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS receivable_account;
DROP TABLE IF EXISTS contract;
DROP TABLE IF EXISTS unit;
DROP TABLE IF EXISTS floor;
DROP TABLE IF EXISTS building;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS tenant;

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Project table - designed based on Vue interface fields
CREATE TABLE project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    project_type ENUM('COMPLEX', 'COMMERCIAL_DISTRICT', 'HOTEL', 'APARTMENT', 'OFFICE') NOT NULL,
    management_org VARCHAR(100) NOT NULL,
    rent_bill_company VARCHAR(100) NOT NULL,
    rent_bill_bank_account VARCHAR(100),
    city VARCHAR(50) NOT NULL,
    address VARCHAR(200) NOT NULL,
    project_manager VARCHAR(50),
    contact_phone VARCHAR(20) NOT NULL,
    status TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_project_name (project_name),
    INDEX idx_project_type (project_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Building table - designed based on Vue interface fields
CREATE TABLE building (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    building_name VARCHAR(100) NOT NULL,
    building_code VARCHAR(50) NOT NULL,
    project_id BIGINT NOT NULL,
    building_area DECIMAL(12,2),
    rent_area DECIMAL(12,2),
    property_area DECIMAL(12,2),
    usable_area DECIMAL(12,2),
    remark TEXT,
    status TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    INDEX idx_project_id (project_id),
    INDEX idx_building_name (building_name),
    INDEX idx_building_code (building_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Floor table - designed based on Vue interface fields
CREATE TABLE floor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    floor_name VARCHAR(100) NOT NULL,
    floor_code VARCHAR(50) NOT NULL,
    building_id BIGINT NOT NULL,
    remark TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (building_id) REFERENCES building(id) ON DELETE CASCADE,
    INDEX idx_building_id (building_id),
    INDEX idx_floor_name (floor_name),
    INDEX idx_floor_code (floor_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Unit table - designed based on Vue interface fields
CREATE TABLE unit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_name VARCHAR(100) NOT NULL,
    unit_code VARCHAR(50) NOT NULL,
    floor_id BIGINT NOT NULL,
    unit_status ENUM('RENTABLE', 'SELF_USE', 'PUBLIC_USE', 'LEASE_BACK', 'DISABLED', 'SELF_RENTAL') DEFAULT 'RENTABLE',
    unit_purpose VARCHAR(100),
    building_area DECIMAL(10,2),
    rent_area DECIMAL(10,2),
    is_multi_tenant BOOLEAN DEFAULT FALSE,
    remark TEXT,
    status TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (floor_id) REFERENCES floor(id) ON DELETE CASCADE,
    INDEX idx_floor_id (floor_id),
    INDEX idx_unit_name (unit_name),
    INDEX idx_unit_code (unit_code),
    INDEX idx_unit_status (unit_status),
    INDEX idx_status (status),
    UNIQUE KEY uk_unit_code (unit_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Tenant table - keep original structure
CREATE TABLE tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    id_card VARCHAR(50),
    address VARCHAR(500),
    tenant_type VARCHAR(20) DEFAULT 'INDIVIDUAL',
    credit_rating VARCHAR(10) DEFAULT 'A',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant_name (tenant_name),
    INDEX idx_phone (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Contract table - keep original structure
CREATE TABLE contract (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_number VARCHAR(100) NOT NULL UNIQUE,
    project_id BIGINT NOT NULL,
    unit_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL,
    contract_type VARCHAR(20) DEFAULT 'RENT',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    monthly_rent DECIMAL(10,2),
    deposit DECIMAL(10,2),
    total_amount DECIMAL(12,2),
    payment_method VARCHAR(50),
    payment_cycle VARCHAR(20) DEFAULT 'MONTHLY',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    notes TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (unit_id) REFERENCES unit(id) ON DELETE CASCADE,
    FOREIGN KEY (tenant_id) REFERENCES tenant(id) ON DELETE CASCADE,
    INDEX idx_contract_number (contract_number),
    INDEX idx_project_id (project_id),
    INDEX idx_unit_id (unit_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_status (status),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Receivable account table - keep original structure
CREATE TABLE receivable_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL,
    bill_period VARCHAR(20) NOT NULL,
    bill_type VARCHAR(20) DEFAULT 'RENT',
    amount DECIMAL(10,2) NOT NULL,
    paid_amount DECIMAL(10,2) DEFAULT 0.00,
    outstanding_amount DECIMAL(10,2) NOT NULL,
    due_date DATE NOT NULL,
    payment_date DATE,
    status VARCHAR(20) DEFAULT 'PENDING',
    late_fee DECIMAL(8,2) DEFAULT 0.00,
    notes TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (contract_id) REFERENCES contract(id) ON DELETE CASCADE,
    FOREIGN KEY (tenant_id) REFERENCES tenant(id) ON DELETE CASCADE,
    INDEX idx_contract_id (contract_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_bill_period (bill_period),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date),
    UNIQUE KEY uk_contract_period_type (contract_id, bill_period, bill_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert test data
-- Insert project data
INSERT INTO project (project_name, project_type, management_org, rent_bill_company, rent_bill_bank_account, city, address, project_manager, contact_phone) VALUES
('Cloud Smart Building', 'OFFICE', 'Cloud Property Management', 'Cloud Rent Company', '6228480402564890018', 'Shenzhen', 'Nanshan District Technology Park', 'Manager Zhang', '13800138001'),
('Smart Business Plaza', 'COMMERCIAL_DISTRICT', 'Smart Property Management', 'Smart Rent Management Company', '6228480402564890019', 'Guangzhou', 'Tianhe District Zhujiang New Town', 'Manager Li', '13800138002'),
('Technology Innovation Park', 'COMPLEX', 'Innovation Park Property', 'Innovation Park Rent Company', '6228480402564890020', 'Beijing', 'Haidian District Zhongguancun', 'Manager Wang', '13800138003');

-- Insert building data
INSERT INTO building (building_name, building_code, project_id, building_area, rent_area, property_area, usable_area, remark) VALUES
('Building A', 'A001', 1, 15000.00, 12000.00, 14000.00, 11000.00, 'Main building'),
('Building B', 'B001', 1, 12000.00, 10000.00, 11000.00, 9000.00, 'Secondary building'),
('Building C', 'C001', 2, 20000.00, 18000.00, 19000.00, 17000.00, 'Commercial building'),
('Building D', 'D001', 3, 25000.00, 22000.00, 24000.00, 21000.00, 'Office building');

-- Insert floor data
INSERT INTO floor (floor_name, floor_code, building_id, remark) VALUES
('Floor 1', 'F001', 1, 'First floor lobby'),
('Floor 2', 'F002', 1, 'Second floor office area'),
('Floor 3', 'F003', 1, 'Third floor office area'),
('Floor 1', 'F001', 2, 'Building B first floor'),
('Floor 2', 'F002', 2, 'Building B second floor'),
('Floor 1', 'F001', 3, 'Building C commercial floor'),
('Floor 2', 'F002', 3, 'Building C office floor'),
('Floor 1', 'F001', 4, 'Building D lobby'),
('Floor 2', 'F002', 4, 'Building D office area'),
('Floor 3', 'F003', 4, 'Building D meeting area');

-- Insert unit data
INSERT INTO unit (unit_name, unit_code, floor_id, unit_status, unit_purpose, building_area, rent_area, is_multi_tenant, remark) VALUES
('A101', 'A101', 1, 'RENTABLE', 'OFFICE', 120.50, 100.00, FALSE, 'Building A Floor 1 Unit 01'),
('A102', 'A102', 1, 'RENTABLE', 'OFFICE', 150.00, 130.00, FALSE, 'Building A Floor 1 Unit 02'),
('A201', 'A201', 2, 'RENTABLE', 'OFFICE', 200.00, 180.00, TRUE, 'Building A Floor 2 Unit 01'),
('A202', 'A202', 2, 'SELF_USE', 'OFFICE', 180.00, 160.00, FALSE, 'Building A Floor 2 Unit 02'),
('A301', 'A301', 3, 'RENTABLE', 'OFFICE', 220.00, 200.00, FALSE, 'Building A Floor 3 Unit 01'),
('B101', 'B101', 4, 'RENTABLE', 'OFFICE', 100.00, 85.00, FALSE, 'Building B Floor 1 Unit 01'),
('B201', 'B201', 5, 'RENTABLE', 'OFFICE', 150.00, 130.00, FALSE, 'Building B Floor 2 Unit 01'),
('C101', 'C101', 6, 'RENTABLE', 'RETAIL', 300.00, 280.00, FALSE, 'Building C commercial unit'),
('C201', 'C201', 7, 'RENTABLE', 'OFFICE', 250.00, 220.00, FALSE, 'Building C office unit'),
('D101', 'D101', 8, 'PUBLIC_USE', 'OFFICE', 500.00, 450.00, FALSE, 'Building D lobby');

-- Insert tenant data
INSERT INTO tenant (tenant_name, contact_person, phone, email, id_card, address, tenant_type, credit_rating) VALUES
('Shenzhen Technology Co Ltd', 'Mr Zhang', '13800138001', 'zhang@tech.com', '91440300123456789X', 'Nanshan District Shenzhen', 'COMPANY', 'A'),
('Guangzhou Trading Company', 'Mr Li', '13800138002', 'li@trade.com', '91440100987654321Y', 'Tianhe District Guangzhou', 'COMPANY', 'B'),
('Beijing Innovation Enterprise', 'Mr Wang', '13800138003', 'wang@innovation.com', '91110100456789123Z', 'Haidian District Beijing', 'COMPANY', 'A'),
('Individual Tenant Zhang San', 'Zhang San', '13800138004', 'zhangsan@email.com', '440300199001011234', 'Futian District Shenzhen', 'INDIVIDUAL', 'A');

-- Insert contract data
INSERT INTO contract (contract_number, project_id, unit_id, tenant_id, contract_type, start_date, end_date, monthly_rent, deposit, total_amount, payment_method, payment_cycle, status) VALUES
('HT2025001', 1, 1, 1, 'RENT', '2025-01-01', '2025-12-31', 8000.00, 16000.00, 96000.00, 'Bank Transfer', 'MONTHLY', 'ACTIVE'),
('HT2025002', 1, 3, 2, 'RENT', '2025-01-01', '2026-12-31', 15000.00, 30000.00, 360000.00, 'Bank Transfer', 'QUARTERLY', 'ACTIVE'),
('HT2025003', 2, 8, 3, 'RENT', '2025-02-01', '2027-01-31', 25000.00, 50000.00, 600000.00, 'Bank Transfer', 'MONTHLY', 'ACTIVE'),
('HT2025004', 1, 6, 4, 'RENT', '2025-01-15', '2025-07-14', 6000.00, 12000.00, 36000.00, 'Cash', 'MONTHLY', 'ACTIVE');

-- Insert receivable account data
INSERT INTO receivable_account (contract_id, tenant_id, bill_period, bill_type, amount, paid_amount, outstanding_amount, due_date, status) VALUES
(1, 1, '2025-01', 'RENT', 8000.00, 8000.00, 0.00, '2025-01-31', 'PAID'),
(1, 1, '2025-02', 'RENT', 8000.00, 0.00, 8000.00, '2025-02-28', 'PENDING'),
(2, 2, '2025-Q1', 'RENT', 45000.00, 45000.00, 0.00, '2025-03-31', 'PAID'),
(2, 2, '2025-Q2', 'RENT', 45000.00, 0.00, 45000.00, '2025-06-30', 'PENDING'),
(3, 3, '2025-02', 'RENT', 25000.00, 0.00, 25000.00, '2025-02-28', 'PENDING'),
(4, 4, '2025-01', 'RENT', 6000.00, 6000.00, 0.00, '2025-01-31', 'PAID');