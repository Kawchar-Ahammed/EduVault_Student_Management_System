-- ==========================================
-- COMPLETE ACADEMIC MANAGEMENT SYSTEM DATABASE (UPDATED AND UPGRADED)
-- Enhanced comprehensive SQL file for PushpoAcademicDB
-- Execute this entire script to set up the complete upgraded database
-- ==========================================

-- Create Database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'PushpoAcademicDB')
BEGIN
    CREATE DATABASE PushpoAcademicDB;
END
GO

USE PushpoAcademicDB;
GO

-- Table Name (Development Query - Commented Out)
-- SELECT name AS TableName
-- FROM sys.tables
-- ORDER BY name;

-- ==========================================
-- DROP EXISTING OBJECTS (For Clean Setup)
-- ==========================================

-- Drop all foreign key constraints first to avoid dependency issues
DECLARE @sql NVARCHAR(MAX) = '';
SELECT @sql = @sql + 'ALTER TABLE [' + SCHEMA_NAME(schema_id) + '].[' + OBJECT_NAME(parent_object_id) + '] DROP CONSTRAINT [' + name + '];' + CHAR(13)
FROM sys.foreign_keys;
EXEC sp_executesql @sql;

-- Drop Tables in correct order (child tables first, then parent tables)
IF OBJECT_ID('CourseAttendanceConfig', 'U') IS NOT NULL DROP TABLE CourseAttendanceConfig;
IF OBJECT_ID('PopupNotificationQueue', 'U') IS NOT NULL DROP TABLE PopupNotificationQueue;
IF OBJECT_ID('PDFReportTemplates', 'U') IS NOT NULL DROP TABLE PDFReportTemplates;
IF OBJECT_ID('DeviceManagement', 'U') IS NOT NULL DROP TABLE DeviceManagement;
IF OBJECT_ID('AIPerformancePredictions', 'U') IS NOT NULL DROP TABLE AIPerformancePredictions;
IF OBJECT_ID('MarkEntryConfigurations', 'U') IS NOT NULL DROP TABLE MarkEntryConfigurations;
IF OBJECT_ID('StudentListFilters', 'U') IS NOT NULL DROP TABLE StudentListFilters;
IF OBJECT_ID('SendMarksWorkflow', 'U') IS NOT NULL DROP TABLE SendMarksWorkflow;
IF OBJECT_ID('AttendanceUpdateLogs', 'U') IS NOT NULL DROP TABLE AttendanceUpdateLogs;
IF OBJECT_ID('IndividualEmailLogs', 'U') IS NOT NULL DROP TABLE IndividualEmailLogs;
IF OBJECT_ID('TestSchedules', 'U') IS NOT NULL DROP TABLE TestSchedules;
IF OBJECT_ID('AssignmentSchedules', 'U') IS NOT NULL DROP TABLE AssignmentSchedules;
IF OBJECT_ID('MarkSubmissionWorkflow', 'U') IS NOT NULL DROP TABLE MarkSubmissionWorkflow;
IF OBJECT_ID('ReportGeneration', 'U') IS NOT NULL DROP TABLE ReportGeneration;
IF OBJECT_ID('DataBackups', 'U') IS NOT NULL DROP TABLE DataBackups;
IF OBJECT_ID('ExportLogs', 'U') IS NOT NULL DROP TABLE ExportLogs;
IF OBJECT_ID('StudentLoginLogs', 'U') IS NOT NULL DROP TABLE StudentLoginLogs;
IF OBJECT_ID('PerformanceComparisons', 'U') IS NOT NULL DROP TABLE PerformanceComparisons;
IF OBJECT_ID('EmailLogs', 'U') IS NOT NULL DROP TABLE EmailLogs;
IF OBJECT_ID('AssignmentSubmissions', 'U') IS NOT NULL DROP TABLE AssignmentSubmissions;
IF OBJECT_ID('DashboardAnalytics', 'U') IS NOT NULL DROP TABLE DashboardAnalytics;
IF OBJECT_ID('UserSessions', 'U') IS NOT NULL DROP TABLE UserSessions;
IF OBJECT_ID('AuditTrail', 'U') IS NOT NULL DROP TABLE AuditTrail;
IF OBJECT_ID('PerformanceAnalytics', 'U') IS NOT NULL DROP TABLE PerformanceAnalytics;
IF OBJECT_ID('MarkEntrySessions', 'U') IS NOT NULL DROP TABLE MarkEntrySessions;
IF OBJECT_ID('CoursePrerequisites', 'U') IS NOT NULL DROP TABLE CoursePrerequisites;
IF OBJECT_ID('CourseSections', 'U') IS NOT NULL DROP TABLE CourseSections;
IF OBJECT_ID('EmailTemplates', 'U') IS NOT NULL DROP TABLE EmailTemplates;
IF OBJECT_ID('Notifications', 'U') IS NOT NULL DROP TABLE Notifications;
IF OBJECT_ID('AttendanceReports', 'U') IS NOT NULL DROP TABLE AttendanceReports;
IF OBJECT_ID('Assessments', 'U') IS NOT NULL DROP TABLE Assessments;
IF OBJECT_ID('Attendance', 'U') IS NOT NULL DROP TABLE Attendance;
IF OBJECT_ID('CourseEnrollments', 'U') IS NOT NULL DROP TABLE CourseEnrollments;
IF OBJECT_ID('Courses', 'U') IS NOT NULL DROP TABLE Courses;
IF OBJECT_ID('UserRoles', 'U') IS NOT NULL DROP TABLE UserRoles;
IF OBJECT_ID('Faculty', 'U') IS NOT NULL DROP TABLE Faculty;
IF OBJECT_ID('Students', 'U') IS NOT NULL DROP TABLE Students;
IF OBJECT_ID('Programs', 'U') IS NOT NULL DROP TABLE Programs;
IF OBJECT_ID('Users', 'U') IS NOT NULL DROP TABLE Users;
IF OBJECT_ID('AcademicSessions', 'U') IS NOT NULL DROP TABLE AcademicSessions;
IF OBJECT_ID('SystemSettings', 'U') IS NOT NULL DROP TABLE SystemSettings;
IF OBJECT_ID('GradeScale', 'U') IS NOT NULL DROP TABLE GradeScale;
IF OBJECT_ID('Roles', 'U') IS NOT NULL DROP TABLE Roles;
IF OBJECT_ID('Departments', 'U') IS NOT NULL DROP TABLE Departments;

-- Drop Functions and Procedures
IF OBJECT_ID('GetStudentCGPA', 'FN') IS NOT NULL DROP FUNCTION GetStudentCGPA;
IF OBJECT_ID('CheckAttendanceEligibility', 'FN') IS NOT NULL DROP FUNCTION CheckAttendanceEligibility;
IF OBJECT_ID('CalculateAttendanceMarks', 'P') IS NOT NULL DROP PROCEDURE CalculateAttendanceMarks;
IF OBJECT_ID('CalculateFinalGrade', 'P') IS NOT NULL DROP PROCEDURE CalculateFinalGrade;
IF OBJECT_ID('SendMarkNotifications', 'P') IS NOT NULL DROP PROCEDURE SendMarkNotifications;

-- Drop Triggers
IF OBJECT_ID('tr_Assessments_CalculateGrade', 'TR') IS NOT NULL DROP TRIGGER tr_Assessments_CalculateGrade;
IF OBJECT_ID('tr_Attendance_UpdateMarks', 'TR') IS NOT NULL DROP TRIGGER tr_Attendance_UpdateMarks;
IF OBJECT_ID('tr_Assessments_NotifyStudents', 'TR') IS NOT NULL DROP TRIGGER tr_Assessments_NotifyStudents;

-- Drop Views
IF OBJECT_ID('StudentPerformanceSummary', 'V') IS NOT NULL DROP VIEW StudentPerformanceSummary;
IF OBJECT_ID('FacultyCourseLoad', 'V') IS NOT NULL DROP VIEW FacultyCourseLoad;
IF OBJECT_ID('RecentNotifications', 'V') IS NOT NULL DROP VIEW RecentNotifications;
GO

-- ==========================================
-- CREATE TABLES
-- ==========================================

--------------------------------------------------
-- 0. Roles Table
--------------------------------------------------
CREATE TABLE Roles (
    roleId INT IDENTITY(1,1) PRIMARY KEY,
    roleName NVARCHAR(50) UNIQUE NOT NULL
);
GO

-- Insert roles for the system
INSERT INTO Roles (roleName) VALUES
('ADMIN'),    -- System administrator role
('FACULTY'),  -- Faculty member role
('STUDENT');  -- Student role
GO

--------------------------------------------------
-- 1. Departments Table
--------------------------------------------------
CREATE TABLE Departments (
    departmentId INT IDENTITY(1,1) PRIMARY KEY,
    departmentName NVARCHAR(100) UNIQUE NOT NULL
);
GO

-- Insert academic departments
INSERT INTO Departments (departmentName) VALUES
('Computer Science and Telecommunication Engineering'), -- CSTE Department
('English'),                                            -- English Department
('Applied Chemistry and Chemical Engineering'),        -- ACCE Department
('Electrical and Electronic Engineering'),             -- EEE Department
('Applied Mathematics'),
('Business Administration'),                -- BBA/MBA Department
('Pharmacy'),                              -- Pharmacy Department
('Genetic Engineering and Biotechnology'),  -- GEB Department
('Environmental Science and Disaster Management'), -- ESDM Department
('Law and Justice');                           -- Law Department
GO

--------------------------------------------------
-- 2. Programs Table (Normalized)
--------------------------------------------------
CREATE TABLE Programs (
    programId INT IDENTITY(1,1) PRIMARY KEY,
    programName NVARCHAR(150) UNIQUE NOT NULL,
    degreeType NVARCHAR(20) CHECK (degreeType IN ('BSc','MSc','PhD','BA','BBA','MBA','MA','BPharm','LLB')),
    departmentId INT NOT NULL,
    createdDate DATETIME DEFAULT GETDATE(),
    createdBy INT NULL,
    updatedDate DATETIME NULL,
    updatedBy INT NULL,
    FOREIGN KEY (departmentId) REFERENCES Departments(departmentId) ON DELETE NO ACTION
);
GO

INSERT INTO Programs (programName, degreeType, departmentId) VALUES
('BSc Computer Science and Telecommunication Engineering', 'BSc', 1), -- CSTE BSc
('BSc Electrical and Electronic Engineering', 'BSc', 4),                -- EEE BSc
('BSc Applied Chemistry and Chemical Engineering', 'BSc', 3),           -- ACCE BSc
('BA in English', 'BA', 2),                                           -- English BA
('BSc in Applied Mathematics', 'BSc', 5);                                -- Applied Math BSc
GO

-- Add more academically realistic programs
INSERT INTO Programs (programName, degreeType, departmentId) VALUES
('BBA in Business Administration', 'BBA', 6), -- Business Administration
('Bachelor of Pharmacy', 'BPharm', 7),        -- Pharmacy
('BSc in Genetic Engineering and Biotechnology', 'BSc', 8), -- GEBT
('BSc in Environmental Science and Disaster Management', 'BSc', 9), -- ESDM
('LLB in Law and Justice', 'LLB', 10);        -- Law and Justice
GO

--------------------------------------------------
-- 3. Users Table
--------------------------------------------------
CREATE TABLE Users (
    userId INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(100) NOT NULL, -- store hashed passwords
    email NVARCHAR(100) UNIQUE NOT NULL,
    fullName NVARCHAR(100) NOT NULL,
    phoneNumber NVARCHAR(20),
    departmentId INT NULL,
    isActive BIT DEFAULT 1,
    createdDate DATETIME DEFAULT GETDATE(),
    createdBy INT NULL,
    updatedDate DATETIME NULL,
    updatedBy INT NULL,
    lastLoginDate DATETIME,
    profilePicture NVARCHAR(500),
    address NVARCHAR(MAX),
    dateOfBirth DATE,
    FOREIGN KEY (departmentId) REFERENCES Departments(departmentId) ON DELETE SET NULL
);

-- Create index separately (cannot be inline with table creation in SQL Server)
CREATE INDEX IX_Users_Username ON Users(username);
GO

-- Insert system users
INSERT INTO Users (username, password, email, fullName, phoneNumber, departmentId)
VALUES
('admin', 'admin123', 'kawcharahammed16@gmail.com', 'System Admin', '+8801000000000', NULL), -- Admin user
('faculty1', 'faculty123', 'faculty1@nstu.edu.bd', 'Dr. Nazia Majadi', '+8801000000001', 1), -- CSTE Faculty
('faculty2', 'faculty123', 'faculty2@nstu.edu.bd', 'Dr. Nabil', '+8801000000002', 2), -- English Faculty
-- New faculty for each department
('faculty3', 'faculty123', 'faculty3@nstu.edu.bd', 'Dr. Ayesha Rahman', '+8801000000003', 3), -- ACCE
('faculty4', 'faculty123', 'faculty4@nstu.edu.bd', 'Dr. Tanvir Hasan', '+8801000000004', 4), -- EEE
('faculty5', 'faculty123', 'faculty5@nstu.edu.bd', 'Dr. Farhana Sultana', '+8801000000005', 5), -- Applied Math
('faculty6', 'faculty123', 'faculty6@nstu.edu.bd', 'Dr. Mahmudul Haque', '+8801000000006', 6), -- BBA/MBA
('faculty7', 'faculty123', 'faculty7@nstu.edu.bd', 'Dr. Nusrat Jahan', '+8801000000007', 7), -- Pharmacy
('faculty8', 'faculty123', 'faculty8@nstu.edu.bd', 'Dr. Shamsul Alam', '+8801000000008', 8), -- GEB
('faculty9', 'faculty123', 'faculty9@nstu.edu.bd', 'Dr. Rashed Chowdhury', '+8801000000009', 9), -- ESDM
('faculty10', 'faculty123', 'faculty10@nstu.edu.bd', 'Dr. Laila Begum', '+8801000000010', 10), -- Law
('faculty11', 'faculty123', 'faculty11@nstu.edu.bd', 'Dr. Imran Hossain', '+8801000000011', 1), -- CSTE (extra)
('faculty12', 'faculty123', 'faculty12@nstu.edu.bd', 'Dr. Sultana Parvin', '+8801000000012', 2), -- English (extra)

-- Real CSTE Students from ID PDF (16th Batch, 2020-21 Session)
('kawchar0116', 'student123', 'kawchar0116@student.nstu.edu.bd', 'Kawchar Ahammed', '+8801712345001', 1), -- ID: 2101020
('sajedul0116', 'student123', 'sajedul0116@student.nstu.edu.bd', 'Sajedul Karim', '+8801712345002', 1), -- ID: 2101027
('redoyan0118', 'student123', 'redoyan0118@student.nstu.edu.bd', 'Md. Redoyan Islam', '+8801712345003', 1), -- ID: 2301007

-- Batch 16 (2020-21)
('borhan0116', 'student123', 'borhan0116@student.nstu.edu.bd', 'Mohammad Borhan Uddin', '+8801712345010', 1),
('safkat0116', 'student123', 'safkat0116@student.nstu.edu.bd', 'Safkat Islam Niloy', '+8801712345011', 2),
('rabiul0116', 'student123', 'rabiul0116@student.nstu.edu.bd', 'Rabiul Hassan', '+8801712345012', 3),
('tasrik0116', 'student123', 'tasrik0116@student.nstu.edu.bd', 'Md. Tasrik', '+8801712345013', 4),
('sajedul0116b', 'student123', 'sajedul0116b@student.nstu.edu.bd', 'Sajedul Karim', '+8801712345014', 5),
('mohammad', 'student123', 'mohammad@student.nstu.edu.bd', 'Mohammad Raihan', '+8801712345015', 6),
('eshan0116', 'student123', 'eshan0116@student.nstu.edu.bd', 'Md. Abdur Rahman', '+8801712345016', 7),
('akil0116', 'student123', 'akil0116@student.nstu.edu.bd', 'Md. Sadman Akil', '+8801712345017', 8),
('joy0116', 'student123', 'joy0116@student.nstu.edu.bd', 'Joy Kumar Bhuiyan', '+8801712345018', 9),
('zobaier0116', 'student123', 'zobaier0116@student.nstu.edu.bd', 'Abdullah Al Zobaier', '+8801712345019', 10),
('soikot0116', 'student123', 'soikot0116@student.nstu.edu.bd', 'Abul Hayat Soikot', '+8801712345020', 1),
('mahbob0116', 'student123', 'mahbob0116@student.nstu.edu.bd', 'Mahbob Hasan Sazib', '+8801712345021', 2),

-- Batch 17 (2021-22)
('tasin0117', 'student123', 'tasin0117@student.nstu.edu.bd', 'Tasin Islam', '+8801712345022', 3),
('tareq0117', 'student123', 'tareq0117@student.nstu.edu.bd', 'Md. Tareq Hossan', '+8801712345023', 4),
('niloy0117', 'student123', 'niloy0117@student.nstu.edu.bd', 'Niloy Mutsuddy', '+8801712345024', 5),
('yeasin0117', 'student123', 'yeasin0117@student.nstu.edu.bd', 'Md. Yeasin Arafat', '+8801712345025', 6),
('sazzad0117', 'student123', 'sazzad0117@student.nstu.edu.bd', 'Sazzad Ul Arafin', '+8801712345026', 7),
('yeasinb0117', 'student123', 'yeasinb0117@student.nstu.edu.bd', 'Yeasin Arafat', '+8801712345027', 8),

-- Batch 18 (2022-23)
('nishad0118', 'student123', 'nishad0118@student.nstu.edu.bd', 'Md. Nishad Ahmed', '+8801712345101', 1),
('faraha0118', 'student123', 'faraha0118@student.nstu.edu.bd', 'Faraha Hossain Chowdhury', '+8801712345102', 2),
('billal0118', 'student123', 'billal0118@student.nstu.edu.bd', 'Md. Billal Hossain', '+8801712345103', 3),
('anika0118', 'student123', 'anika0118@student.nstu.edu.bd', 'Tasnima Akter Anika', '+8801712345104', 4),
('safin0118', 'student123', 'safin0118@student.nstu.edu.bd', 'Sadman Safin', '+8801712345105', 5);
GO

--------------------------------------------------
-- 3.1 UserRoles Table (Bridge for Multi-Roles)
--------------------------------------------------
CREATE TABLE UserRoles (
    userId INT NOT NULL,
    roleId INT NOT NULL,
    PRIMARY KEY(userId, roleId),
    FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE,
    FOREIGN KEY (roleId) REFERENCES Roles(roleId) ON DELETE NO ACTION
);
GO

--------------------------------------------------
-- Assign roles to users (sorted by role groups)
--------------------------------------------------

-- Admin user
INSERT INTO UserRoles (userId, roleId)
VALUES
(1, 1); -- Admin (System Admin)

-- Faculties (roleId = 2)
INSERT INTO UserRoles (userId, roleId)
VALUES
(2, 2),  -- Faculty - Dr. Nazia Majadi (CSTE)
(3, 2),  -- Faculty - Dr. Nabil (English)
(4, 2),  -- Faculty - Dr. Ayesha Rahman (ACCE)
(5, 2),  -- Faculty - Dr. Tanvir Hasan (EEE)
(6, 2),  -- Faculty - Dr. Farhana Sultana (Applied Math)
(7, 2),  -- Faculty - Dr. Mahmudul Haque (BBA/MBA)
(8, 2),  -- Faculty - Dr. Nusrat Jahan (Pharmacy)
(9, 2),  -- Faculty - Dr. Shamsul Alam (GEB)
(10, 2), -- Faculty - Dr. Rashed Chowdhury (ESDM)
(11, 2), -- Faculty - Dr. Laila Begum (Law)
(12, 2), -- Faculty - Dr. Imran Hossain (CSTE Extra)
(13, 2); -- Faculty - Dr. Sultana Parvin (English Extra)

-- Students (roleId = 3)
INSERT INTO UserRoles (userId, roleId)
VALUES
(14, 3), -- Kawchar Ahammed (CSTE, Batch 16)
(15, 3), -- Sajedul Karim (CSTE, Batch 16)
(16, 3), -- Md. Redoyan Islam (CSTE, Batch 18)

-- Batch 16 Students
(17, 3), -- Mohammad Borhan Uddin
(18, 3), -- Safkat Islam Niloy
(19, 3), -- Rabiul Hassan
(20, 3), -- Md. Tasrik
(21, 3), -- Sajedul Karim (duplicate ID)
(22, 3), -- Mohammad Raihan
(23, 3), -- Md. Abdur Rahman
(24, 3), -- Md. Sadman Akil
(25, 3), -- Joy Kumar Bhuiyan
(26, 3), -- Abdullah Al Zobaier
(27, 3), -- Abul Hayat Soikot
(28, 3), -- Mahbob Hasan Sazib

-- Batch 17 Students
(29, 3), -- Tasin Islam
(30, 3), -- Md. Tareq Hossan
(31, 3), -- Niloy Mutsuddy
(32, 3), -- Md. Yeasin Arafat
(33, 3), -- Sazzad Ul Arafin
(34, 3), -- Yeasin Arafat (duplicate)

-- Batch 18 Students
(35, 3), -- Md. Nishad Ahmed
(36, 3), -- Faraha Hossain Chowdhury
(37, 3), -- Md. Billal Hossain
(38, 3), -- Tasnima Akter Anika
(39, 3); -- Sadman Safin

GO


--------------------------------------------------
-- 4. Students Table
--------------------------------------------------
CREATE TABLE Students (
    studentId NVARCHAR(20) PRIMARY KEY,
    userId INT NOT NULL UNIQUE,
    batch INT NOT NULL,
    semester INT NOT NULL,
    session NVARCHAR(10) NOT NULL,
    programId INT NOT NULL, -- normalized FK
    cgpa DECIMAL(4,2) DEFAULT 0.00,
    status NVARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','GRADUATED','DROPPED')),
    totalCredits INT DEFAULT 0,
    enrollmentDate DATE DEFAULT GETDATE(),
    guardianName NVARCHAR(100),
    guardianPhone NVARCHAR(20),
    emergencyContact NVARCHAR(20),
    bloodGroup NVARCHAR(5),
    createdDate DATETIME DEFAULT GETDATE(),
    createdBy INT NULL,
    updatedDate DATETIME NULL,
    updatedBy INT NULL,
    FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE,
    FOREIGN KEY (programId) REFERENCES Programs(programId) ON DELETE NO ACTION
);

-- Create indexes separately for Students table
CREATE INDEX IX_Students_Batch ON Students(batch);
CREATE INDEX IX_Students_Semester ON Students(semester);
CREATE INDEX IX_Students_Session ON Students(session);
GO

-- Insert student records
INSERT INTO Students 
(studentId, userId, batch, semester, session, programId, cgpa, totalCredits, guardianName, guardianPhone, emergencyContact, bloodGroup)
VALUES
-- Batch 16 (2020-21)
('MUH2101020M', 14,  16, 7, '2020-21', 1, 3.07, 45, 'Kazi Humayoun Kabir', '+8801712345001', '+8801912345001', 'B+'), -- CSTE
('ASH2101027M', 15,  16, 7, '2020-21', 2, 3.30, 47, 'Engr. Wilson Ahmed', '+8801712345002', '+8801912345002', 'A+'), -- EEE
('MUH2301007M', 16,  16, 7, '2020-21', 1, 3.20, 45, 'Md. Lee Rahman', '+8801712345003', '+8801912345003', 'B+'), -- CSTE
('BOR2101030M', 17, 16, 7, '2020-21', 1, 3.10, 44, 'Borhan Father', '+8801712345010', '+8801912345010', 'O+'),
('SAF2101031M', 18, 16, 7, '2020-21', 2, 3.15, 46, 'Safkat Father', '+8801712345011', '+8801912345011', 'A+'),
('RAB2101032M', 19, 16, 7, '2020-21', 3, 3.20, 48, 'Rabiul Father', '+8801712345012', '+8801912345012', 'B+'),
('TAS2101033M', 20, 16, 7, '2020-21', 4, 3.05, 43, 'Tasrik Father', '+8801712345013', '+8801912345013', 'O-'),
('SAJ2101034M', 21, 16, 7, '2020-21', 5, 3.25, 49, 'Sajedul Father', '+8801712345014', '+8801912345014', 'A-'),
('MOH2101035M', 22, 16, 7, '2020-21', 6, 3.00, 42, 'Mohammad Father', '+8801712345015', '+8801912345015', 'B-'),
('ESH2101036M', 23, 16, 7, '2020-21', 7, 3.18, 47, 'Eshan Father', '+8801712345016', '+8801912345016', 'A+'),
('AKI2101037M', 24, 16, 7, '2020-21', 8, 3.12, 45, 'Akil Father', '+8801712345017', '+8801912345017', 'O+'),
('JOY2101038M', 25, 16, 7, '2020-21', 9, 3.22, 48, 'Joy Father', '+8801712345018', '+8801912345018', 'A+'),
('ZOB2101039M', 26, 16, 7, '2020-21', 10, 3.09, 44, 'Zobaier Father', '+8801712345019', '+8801912345019', 'B+'),
('SOI2101040M', 27, 16, 7, '2020-21', 1, 3.11, 45, 'Soikot Father', '+8801712345020', '+8801912345020', 'O-'),
('MAH2101041M', 28, 16, 7, '2020-21', 2, 3.13, 46, 'Mahbob Father', '+8801712345021', '+8801912345021', 'A+'),

-- Batch 17 (2021-22)
('TAS2201042M', 29, 17, 5, '2021-22', 3, 3.17, 30, 'Tasin Father', '+8801712345022', '+8801912345022', 'B+'),
('TAR2201043M', 30, 17, 5, '2021-22', 4, 3.19, 32, 'Tareq Father', '+8801712345023', '+8801912345023', 'O+'),
('NIL2201044M', 31, 17, 5, '2021-22', 5, 3.21, 34, 'Niloy Father', '+8801712345024', '+8801912345024', 'A-'),
('YEA2201045M', 32, 17, 5, '2021-22', 6, 3.23, 36, 'Yeasin Father', '+8801712345025', '+8801912345025', 'B-'),
('SAZ2201046M', 33, 17, 5, '2021-22', 7, 3.25, 38, 'Sazzad Father', '+8801712345026', '+8801912345026', 'AB+'),
('YEB2201047M', 34, 17, 5, '2021-22', 8, 3.27, 40, 'YeasinB Father', '+8801712345027', '+8801912345027', 'O+'),

-- Batch 18 (2022-23)
('NIS2301048M', 35, 18, 2, '2022-23', 1, 3.30, 20, 'Nishad Father', '+8801712345101', '+8801912345101', 'A+'),
('FAR2301049M', 36, 18, 2, '2022-23', 2, 3.32, 22, 'Faraha Father', '+8801712345102', '+8801912345102', 'B+'),
('BIL2301050M', 37, 18, 2, '2022-23', 3, 3.34, 24, 'Billal Father', '+8801712345103', '+8801912345103', 'O+'),
('ANI2301051M', 38, 18, 2, '2022-23', 4, 3.36, 26, 'Anika Father', '+8801712345104', '+8801912345104', 'A-'),
('SAF2301052M', 39, 18, 2, '2022-23', 5, 3.38, 28, 'Safin Father', '+8801712345105', '+8801912345105', 'B-');
GO


--------------------------------------------------
-- 5. Faculty Table
--------------------------------------------------
CREATE TABLE Faculty (
    facultyId NVARCHAR(20) PRIMARY KEY,
    userId INT ,
    departmentId INT NOT NULL,
    designation NVARCHAR(50) NOT NULL,
    specialization NVARCHAR(200),
    joinDate DATE DEFAULT GETDATE(),
    officeRoom NVARCHAR(20),
    officeHours NVARCHAR(100),
    researchInterests NVARCHAR(MAX),
    status NVARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','STUDY_LEAVE','ON_LEAVE','RETIRED')),
    createdDate DATETIME DEFAULT GETDATE(),
    createdBy INT NULL,
    updatedDate DATETIME NULL,
    updatedBy INT NULL,
    FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE SET NULL,
    FOREIGN KEY (departmentId) REFERENCES Departments(departmentId),
    INDEX IX_Faculty_Designation (designation)
);
GO


-- Only insert faculty records with valid userId from Users table (2–13)
INSERT INTO Faculty 
(facultyId, userId, departmentId, designation, specialization, officeRoom, officeHours, researchInterests)
VALUES
('CSTE101', 2, 1, 'Associate Professor', 'Artificial Intelligence', 'CSTE401', 'Mon-Wed 10:00-12:00', 'Machine Learning, Neural Networks'), -- Dr. Nazia Majadi
('ENG105', 3, 2, 'Associate Professor', 'English Literature', 'ENG201', 'Tue-Thu 14:00-16:00', 'Postmodern Literature, Critical Theory'), -- Dr. Nabil
('ACCE201', 4, 3, 'Assistant Professor', 'Financial Accounting', 'ACCE301', 'Mon-Fri 09:00-11:00', 'Corporate Finance, Auditing'), -- Dr. Ayesha Rahman
('EEE301', 5, 4, 'Associate Professor', 'Power Systems', 'EEE401', 'Tue-Thu 13:00-15:00', 'Renewable Energy, Smart Grid'), -- Dr. Tanvir Hasan
('MATH401', 6, 5, 'Professor', 'Applied Mathematics', 'MATH501', 'Mon-Wed 11:00-13:00', 'Differential Equations, Mathematical Modeling'), -- Dr. Farhana Sultana
('BBA501', 7, 6, 'Associate Professor', 'Management Studies', 'BBA601', 'Wed-Fri 14:00-16:00', 'Strategic Management, Organizational Behavior'), -- Dr. Mahmudul Haque
('PHARM601', 8, 7, 'Assistant Professor', 'Pharmaceutical Sciences', 'PHARM701', 'Mon-Thu 10:00-12:00', 'Drug Development, Clinical Pharmacy'), -- Dr. Nusrat Jahan
('GEB701', 9, 8, 'Professor', 'Environmental Science', 'GEB801', 'Tue-Fri 15:00-17:00', 'Climate Change, Environmental Management'), -- Dr. Shamsul Alam
('ESDM801', 10, 9, 'Associate Professor', 'Sustainable Development', 'ESDM901', 'Mon-Wed 08:00-10:00', 'Environmental Policy, Green Technology'), -- Dr. Rashed Chowdhury
('LAW901', 11, 10, 'Professor', 'Constitutional Law', 'LAW1001', 'Thu-Fri 16:00-18:00', 'Human Rights, Legal Research'), -- Dr. Laila Begum
('CSTE102', 12, 1, 'Assistant Professor', 'Software Engineering', 'CSTE402', 'Tue-Thu 11:00-13:00', 'Web Development, Database Systems'), -- Dr. Imran Hossain
('ENG106', 13, 2, 'Lecturer', 'Applied Linguistics', 'ENG202', 'Mon-Fri 12:00-14:00', 'Language Acquisition, Sociolinguistics'); -- Dr. Sultana Parvin

-- Note: These faculty IDs can now be used in Courses table
--------------------------------------------------
-- 6. Courses Table
--------------------------------------------------
CREATE TABLE Courses (
    courseId INT IDENTITY(1,1) PRIMARY KEY,
    courseCode NVARCHAR(20) UNIQUE NOT NULL,
    courseName NVARCHAR(200) NOT NULL,
    description NVARCHAR(MAX),
    credits INT NOT NULL DEFAULT 3,
    creditHours INT NOT NULL DEFAULT 3,
    semester INT NOT NULL,
    batch INT NOT NULL,
    session NVARCHAR(10) NOT NULL,
	programId INT NOT NULL, -- links course to program
    facultyId NVARCHAR(20) NULL,
    maxStudents INT DEFAULT 50,
    enrolledStudents INT DEFAULT 0,
    isActive BIT DEFAULT 1,
    status NVARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','ARCHIVED')),
    classroom NVARCHAR(50),
    syllabus NVARCHAR(MAX),
    attendanceMarks DECIMAL(5,2) DEFAULT 5.00,  -- 5%
    ctMarks DECIMAL(5,2) DEFAULT 20.00,          -- 20%
    finalMarks DECIMAL(5,2) DEFAULT 75.00,       -- 75% (Total = 100%)
	totalClasses INT DEFAULT 24,
    attendanceWeight DECIMAL(5,2) DEFAULT 5.00,
    createdDate DATETIME DEFAULT GETDATE(),
    createdBy INT NULL,
    updatedDate DATETIME NULL,
    updatedBy INT NULL,
	FOREIGN KEY (programId) REFERENCES Programs(programId) ON DELETE NO ACTION,
    FOREIGN KEY (facultyId) REFERENCES Faculty(facultyId) ON DELETE SET NULL
);

-- Create index separately
CREATE INDEX IX_Courses_Code ON Courses(courseCode);
GO

-- Insert Courses (corrected facultyIds)
INSERT INTO Courses 
(courseCode, courseName, description, credits, creditHours, semester, batch, session, programId, facultyId, maxStudents, classroom)
VALUES
-- CSTE
('CSTE1201', 'Data Structures', 'Study of algorithms and data structures.', 3, 3, 2, 16, '2020-21', 1, 'CSTE101', 60, 'CSTE401'),
('CSTE1302', 'Database Systems', 'Relational databases, SQL, and normalization.', 3, 3, 3, 16, '2020-21', 1, 'CSTE101', 60, 'CSTE402'),
('CSTE1403', 'Operating Systems', 'Concepts of modern operating systems.', 3, 3, 4, 17, '2021-22', 1, 'CSTE101', 60, 'CSTE403'),
('CSTE1504', 'Software Engineering', 'Software development lifecycle and methodologies.', 3, 3, 5, 17, '2021-22', 1, 'CSTE102', 60, 'CSTE501'),
('CSTE1605', 'Computer Networks', 'Network protocols and architectures.', 3, 3, 6, 17, '2021-22', 1, 'CSTE101', 60, 'CSTE403'),
('CSTE1706', 'Artificial Intelligence', 'AI concepts and machine learning basics.', 3, 3, 7, 16, '2020-21', 1, 'CSTE101', 50, 'CSTE401'),

-- English
('ENG2202', 'Modern English Poetry', 'Exploration of modern poetry and its movements.', 3, 3, 4, 16, '2020-21', 4, 'ENG105', 40, 'ENG201'),
('ENG2101', 'Introduction to Linguistics', 'Fundamentals of linguistics and language structure.', 3, 3, 2, 17, '2021-22', 4, 'ENG105', 40, 'ENG202'),
('ENG2303', 'World Literature', 'Comparative study of world literature.', 3, 3, 3, 17, '2021-22', 4, 'ENG106', 40, 'ENG301'),
('ENG2404', 'Creative Writing', 'Techniques in creative writing and composition.', 3, 3, 4, 17, '2021-22', 4, 'ENG106', 30, 'ENG301'),

-- ACCE
('ACCE1101', 'Physical Chemistry', 'Basic concepts of physical chemistry.', 3, 3, 1, 16, '2020-21', 3, 'ACCE201', 50, 'ACCE101'),
('ACCE1202', 'Organic Chemistry', 'Introduction to organic compounds and reactions.', 3, 3, 2, 17, '2021-22', 3, 'ACCE201', 50, 'ACCE102'),
('ACCE1303', 'Analytical Chemistry', 'Quantitative and qualitative analysis methods.', 3, 3, 3, 17, '2021-22', 3, 'ACCE201', 50, 'ACCE201'),
('ACCE1404', 'Chemical Engineering', 'Process design and chemical plant operations.', 4, 4, 4, 16, '2020-21', 3, 'ACCE201', 45, 'ACCE301'),

-- EEE
('EEE1101', 'Circuit Theory', 'Fundamentals of electrical circuits.', 3, 3, 1, 16, '2020-21', 2, 'EEE301', 55, 'EEE101'),
('EEE1202', 'Electronics I', 'Semiconductor devices and applications.', 3, 3, 2, 17, '2021-22', 2, 'EEE301', 55, 'EEE102'),
('EEE1303', 'Power Systems', 'Electric power generation and transmission.', 4, 4, 3, 17, '2021-22', 2, 'EEE301', 50, 'EEE301'),
('EEE1404', 'Control Systems', 'Automatic control theory and applications.', 3, 3, 4, 16, '2020-21', 2, 'EEE301', 50, 'EEE301'),

-- Applied Mathematics
('MATH1101', 'Calculus I', 'Differential and integral calculus.', 3, 3, 1, 16, '2020-21', 5, 'MATH401', 40, 'MATH101'),
('MATH1202', 'Linear Algebra', 'Vector spaces, matrices, and linear transformations.', 3, 3, 2, 17, '2021-22', 5, 'MATH401', 40, 'MATH102'),
('MATH1303', 'Differential Equations', 'Ordinary and partial differential equations.', 3, 3, 3, 17, '2021-22', 5, 'MATH401', 40, 'MATH201'),
('MATH1404', 'Statistics', 'Probability theory and statistical methods.', 3, 3, 4, 16, '2020-21', 5, 'MATH401', 45, 'MATH201'),

-- Business Administration
('BBA1101', 'Principles of Management', 'Introduction to management concepts.', 3, 3, 1, 16, '2020-21', 6, 'BBA501', 60, 'BBA101'),
('BBA1202', 'Financial Accounting', 'Basics of financial accounting and reporting.', 3, 3, 2, 17, '2021-22', 6, 'BBA501', 60, 'BBA102'),
('BBA1303', 'Marketing Management', 'Marketing strategies and consumer behavior.', 3, 3, 3, 17, '2021-22', 6, 'BBA501', 60, 'BBA401'),
('BBA1404', 'Strategic Management', 'Corporate strategy and competitive analysis.', 3, 3, 4, 16, '2020-21', 6, 'BBA501', 55, 'BBA401'),

-- Pharmacy
('PHAR1101', 'Pharmaceutical Chemistry', 'Chemistry for pharmacy students.', 3, 3, 1, 16, '2020-21', 7, 'PHARM601', 45, 'PHAR101'),
('PHAR1202', 'Pharmacology I', 'Introduction to pharmacology.', 3, 3, 2, 17, '2021-22', 7, 'PHARM601', 45, 'PHAR102'),
('PHAR1303', 'Pharmaceutical Technology', 'Drug formulation and delivery systems.', 4, 4, 3, 17, '2021-22', 7, 'PHARM601', 40, 'PHAR301'),
('PHAR1404', 'Clinical Pharmacy', 'Patient care and medication management.', 3, 3, 4, 16, '2020-21', 7, 'PHARM601', 40, 'PHAR301'),

-- Genetic Engineering and Biotechnology
('GEB1101', 'Genetics', 'Principles of genetics and heredity.', 3, 3, 1, 16, '2020-21', 8, 'GEB701', 35, 'GEB101'),
('GEB1202', 'Molecular Biology', 'Structure and function of biomolecules.', 3, 3, 2, 17, '2021-22', 8, 'GEB701', 35, 'GEB102'),
('GEB1303', 'Biotechnology', 'Applications of biotechnology in industry.', 4, 4, 3, 17, '2021-22', 8, 'GEB701', 35, 'GEB201'),
('GEB1404', 'Genetic Engineering', 'Recombinant DNA technology and applications.', 4, 4, 4, 16, '2020-21', 8, 'GEB701', 30, 'GEB201'),

-- Environmental Science and Disaster Management
('ESDM1101', 'Environmental Science', 'Introduction to environmental science.', 3, 3, 1, 16, '2020-21', 9, 'ESDM801', 40, 'ESDM101'),
('ESDM1202', 'Disaster Management', 'Principles and practices of disaster management.', 3, 3, 2, 17, '2021-22', 9, 'ESDM801', 40, 'ESDM102'),
('ESDM1303', 'Climate Change', 'Climate science and adaptation strategies.', 3, 3, 3, 17, '2021-22', 9, 'ESDM801', 40, 'ESDM401'),
('ESDM1404', 'Environmental Impact Assessment', 'EIA methodologies and case studies.', 4, 4, 4, 16, '2020-21', 9, 'ESDM801', 35, 'ESDM401'),

-- Law and Justice
('LAW1101', 'Introduction to Law', 'Basic concepts of law and legal systems.', 3, 3, 1, 16, '2020-21', 10, 'LAW901', 30, 'LAW101'),
('LAW1202', 'Constitutional Law', 'Study of constitutional law and governance.', 3, 3, 2, 17, '2021-22', 10, 'LAW901', 30, 'LAW102'),
('LAW1303', 'Criminal Law', 'Principles of criminal law and procedure.', 3, 3, 3, 17, '2021-22', 10, 'LAW901', 30, 'LAW301'),
('LAW1404', 'Human Rights Law', 'International human rights and civil liberties.', 3, 3, 4, 16, '2020-21', 10, 'LAW901', 25, 'LAW301');


--------------------------------------------------
-- 7. Course Enrollments Table
--------------------------------------------------
CREATE TABLE CourseEnrollments (
    enrollmentId INT IDENTITY(1,1) PRIMARY KEY,
    studentId NVARCHAR(20) NOT NULL,
    courseId INT NOT NULL,
    enrollmentDate DATE DEFAULT GETDATE(),
    status NVARCHAR(20) DEFAULT 'ENROLLED' CHECK (status IN ('ENROLLED','DROPPED','COMPLETED','FAILED')),
    grade NVARCHAR(5),
    gradePoints DECIMAL(3,2),
    courseAttendancePercentageRequired DECIMAL(5,2) DEFAULT 0.00 CHECK (courseAttendancePercentageRequired BETWEEN 0 AND 100),
    finalMarks DECIMAL(5,2) DEFAULT 0.00,
    isActive BIT DEFAULT 1,
    createdDate DATETIME DEFAULT GETDATE(),
    createdBy INT NULL,
    updatedDate DATETIME NULL,
    updatedBy INT NULL,
    FOREIGN KEY (studentId) REFERENCES Students(studentId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE,
    UNIQUE(studentId, courseId)
);
GO

-- Insert course enrollment records using courseCode to get actual courseId
INSERT INTO CourseEnrollments (studentId, courseId, status, courseAttendancePercentageRequired, finalMarks)
VALUES
-- Batch 16 CSTE Students
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 'ENROLLED', 90, 0),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 'ENROLLED', 85, 0),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1706'), 'ENROLLED', 88, 0),

-- Batch 16 EEE Students  
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 'ENROLLED', 85, 0),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='EEE1202'), 'ENROLLED', 87, 0),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 'ENROLLED', 92, 0),

-- Batch 18 CSTE Students
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 'ENROLLED', 92, 0),
('NIS2301048M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 'ENROLLED', 88, 0),

-- More Batch 16 Students
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 'ENROLLED', 85, 0),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 'ENROLLED', 87, 0),
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), 'ENROLLED', 90, 0),
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1303'), 'ENROLLED', 88, 0),
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 'ENROLLED', 83, 0),
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1404'), 'ENROLLED', 85, 0),
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 'ENROLLED', 92, 0),
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1404'), 'ENROLLED', 89, 0),
('MOH2101035M', (SELECT courseId FROM Courses WHERE courseCode='BBA1101'), 'ENROLLED', 88, 0),
('MOH2101035M', (SELECT courseId FROM Courses WHERE courseCode='BBA1404'), 'ENROLLED', 90, 0),
('ESH2101036M', (SELECT courseId FROM Courses WHERE courseCode='PHAR1101'), 'ENROLLED', 87, 0),
('ESH2101036M', (SELECT courseId FROM Courses WHERE courseCode='PHAR1404'), 'ENROLLED', 89, 0),
('AKI2101037M', (SELECT courseId FROM Courses WHERE courseCode='GEB1101'), 'ENROLLED', 91, 0),
('AKI2101037M', (SELECT courseId FROM Courses WHERE courseCode='GEB1404'), 'ENROLLED', 88, 0),
('JOY2101038M', (SELECT courseId FROM Courses WHERE courseCode='ESDM1101'), 'ENROLLED', 86, 0),
('JOY2101038M', (SELECT courseId FROM Courses WHERE courseCode='ESDM1404'), 'ENROLLED', 90, 0),
('ZOB2101039M', (SELECT courseId FROM Courses WHERE courseCode='LAW1101'), 'ENROLLED', 85, 0),
('ZOB2101039M', (SELECT courseId FROM Courses WHERE courseCode='LAW1404'), 'ENROLLED', 88, 0),

-- Batch 17 Students
('TAS2201042M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1202'), 'ENROLLED', 89, 0),
('TAS2201042M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1303'), 'ENROLLED', 87, 0),
('TAR2201043M', (SELECT courseId FROM Courses WHERE courseCode='EEE1202'), 'ENROLLED', 86, 0),
('TAR2201043M', (SELECT courseId FROM Courses WHERE courseCode='EEE1303'), 'ENROLLED', 88, 0),
('NIL2201044M', (SELECT courseId FROM Courses WHERE courseCode='MATH1202'), 'ENROLLED', 91, 0),
('NIL2201044M', (SELECT courseId FROM Courses WHERE courseCode='MATH1303'), 'ENROLLED', 89, 0),
('YEA2201045M', (SELECT courseId FROM Courses WHERE courseCode='BBA1202'), 'ENROLLED', 87, 0),
('YEA2201045M', (SELECT courseId FROM Courses WHERE courseCode='BBA1303'), 'ENROLLED', 90, 0),
('SAZ2201046M', (SELECT courseId FROM Courses WHERE courseCode='PHAR1202'), 'ENROLLED', 88, 0),
('SAZ2201046M', (SELECT courseId FROM Courses WHERE courseCode='PHAR1303'), 'ENROLLED', 86, 0),
('YEB2201047M', (SELECT courseId FROM Courses WHERE courseCode='GEB1202'), 'ENROLLED', 92, 0),
('YEB2201047M', (SELECT courseId FROM Courses WHERE courseCode='GEB1303'), 'ENROLLED', 89, 0),

-- Cross-departmental enrollments (general electives)
('SOI2101040M', (SELECT courseId FROM Courses WHERE courseCode='ENG2101'), 'ENROLLED', 85, 0),
('MAH2101041M', (SELECT courseId FROM Courses WHERE courseCode='ENG2404'), 'ENROLLED', 87, 0),
('FAR2301049M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 'ENROLLED', 90, 0),
('BIL2301050M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 'ENROLLED', 88, 0),
('ANI2301051M', (SELECT courseId FROM Courses WHERE courseCode='BBA1101'), 'ENROLLED', 89, 0),
('SAF2301052M', (SELECT courseId FROM Courses WHERE courseCode='ESDM1101'), 'ENROLLED', 86, 0);
GO


--------------------------------------------------
-- 8  Assessment Table (Replaces CTMarks)
-- Stores student scores for different assessments like CT, Quiz, Assignment, Midterm, Final, Presentation, Project, Thesis, Viva
--------------------------------------------------
CREATE TABLE Assessments (
    assessmentId INT IDENTITY(1,1) PRIMARY KEY,
    studentId NVARCHAR(20) NOT NULL,
    courseId INT NOT NULL,
    sectionId INT NULL, -- Link to course section
    markSessionId INT NULL, -- Link to mark entry session
    facultyId NVARCHAR(20) NOT NULL,
    assessmentName NVARCHAR(200) NOT NULL,
    assessmentType NVARCHAR(50) NOT NULL CHECK (assessmentType IN ('Assignment','Quiz','CT','Midterm','Final','Presentation','Project','Thesis','Viva')),
    marks DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    totalMarks DECIMAL(5,2) NOT NULL,
    percentage AS (CASE WHEN totalMarks > 0 THEN (marks/totalMarks)*100 ELSE 0 END) PERSISTED,
    weightage DECIMAL(5,2) NOT NULL,
    weightedMarks AS (CASE WHEN totalMarks > 0 THEN (marks/totalMarks)*weightage ELSE 0 END) PERSISTED,
    grade NVARCHAR(5),
    gradePoints DECIMAL(3,2),
    assessmentDate DATE,
    submittedDate DATETIME DEFAULT GETDATE(),
    isAbsent BIT DEFAULT 0,
    isPublished BIT DEFAULT 0,
    publishedDate DATETIME NULL,
    emailSent BIT DEFAULT 0,
    emailSentDate DATETIME NULL,
    remarks NVARCHAR(MAX),
    isActive BIT DEFAULT 1,
    createdDate DATETIME DEFAULT GETDATE(),
    createdBy INT NULL,
    updatedDate DATETIME NULL,
    updatedBy INT NULL,
    FOREIGN KEY (studentId) REFERENCES Students(studentId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE,
    --FOREIGN KEY (sectionId) REFERENCES CourseSections(sectionId) ON DELETE SET NULL,
    FOREIGN KEY (facultyId) REFERENCES Faculty(facultyId) ON DELETE NO ACTION
);

-- Create indexes separately for Assessments table
CREATE INDEX IX_Assessments_Student ON Assessments(studentId);
CREATE INDEX IX_Assessments_Course ON Assessments(courseId);
CREATE INDEX IX_Assessments_Faculty ON Assessments(facultyId);
CREATE INDEX IX_Assessments_Type ON Assessments(assessmentType);
CREATE INDEX IX_Assessments_Date ON Assessments(assessmentDate);
CREATE INDEX IX_Assessments_Published ON Assessments(isPublished);
CREATE INDEX IX_Assessments_EmailSent ON Assessments(emailSent);

-- PERFORMANCE OPTIMIZATION: Add indexes for computed columns
CREATE INDEX IX_Assessments_Percentage ON Assessments(percentage);
CREATE INDEX IX_Assessments_WeightedMarks ON Assessments(weightedMarks);
CREATE INDEX IX_Assessments_Grade ON Assessments(grade) WHERE grade IS NOT NULL;

-- PERFORMANCE OPTIMIZATION: Add composite indexes for common query patterns
CREATE INDEX IX_Assessments_Student_Course_Type ON Assessments(studentId, courseId, assessmentType) INCLUDE (marks, percentage, grade);
CREATE INDEX IX_Assessments_Course_Published ON Assessments(courseId, isPublished) INCLUDE (studentId, marks, percentage);
GO

-- ===============================
-- Insert Sample Data into Assessments Table
-- All IDs dynamically fetched from Courses table
-- ===============================

-- 1. Class Test (CT)
INSERT INTO Assessments (studentId, courseId, facultyId, assessmentName, assessmentType, marks, totalMarks, weightage, assessmentDate, remarks)
VALUES
-- Data Structures CT
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'CT-1', 'CT', 15, 20, 20, '2024-09-20', 'Average performance'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'CT-1', 'CT', 17, 20, 20, '2024-09-20', 'Good understanding'),
('NIS2301048M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'CT-1', 'CT', 18, 20, 20, '2024-09-20', 'Excellent work'),

-- Modern English Poetry CT
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'CT-1', 'CT', 17, 20, 20, '2024-09-20', 'Good understanding'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'CT-1', 'CT', 18, 20, 20, '2024-09-20', 'Excellent work'),
('BIL2301050M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'CT-1', 'CT', 16, 20, 20, '2024-09-20', 'Satisfactory performance'),

-- Circuit Theory CT
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'CT-1', 'CT', 14, 20, 20, '2024-09-15', 'Need improvement'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'CT-1', 'CT', 16, 20, 20, '2024-09-15', 'Good progress'),

-- Physical Chemistry CT
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ACCE1101'), 'CT-1', 'CT', 18, 20, 20, '2024-09-18', 'Excellent understanding'),

-- Calculus I CT
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'CT-1', 'CT', 15, 20, 20, '2024-09-22', 'Average performance'),
('FAR2301049M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'CT-1', 'CT', 17, 20, 20, '2024-09-22', 'Good work');

-- 2. Quiz
INSERT INTO Assessments (studentId, courseId, facultyId, assessmentName, assessmentType, marks, totalMarks, weightage, assessmentDate, remarks)
VALUES
-- Data Structures Quiz
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Quiz 1', 'Quiz', 8, 10, 2, '2024-09-10', 'Well done'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Quiz 1', 'Quiz', 9, 10, 2, '2024-09-10', 'Excellent'),
('NIS2301048M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Quiz 1', 'Quiz', 7, 10, 2, '2024-09-10', 'Good effort'),

-- Database Systems Quiz
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1302'), 'Quiz 1', 'Quiz', 9, 10, 2, '2024-09-12', 'Excellent SQL skills'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1302'), 'Quiz 1', 'Quiz', 8, 10, 2, '2024-09-12', 'Good understanding'),

-- English Poetry Quiz
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Quiz 1', 'Quiz', 7, 10, 2, '2024-09-10', 'Satisfactory'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Quiz 1', 'Quiz', 9, 10, 2, '2024-09-10', 'Excellent analysis'),
('BIL2301050M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Quiz 1', 'Quiz', 8, 10, 2, '2024-09-10', 'Good interpretation'),

-- Management Quiz
('MOH2101035M', (SELECT courseId FROM Courses WHERE courseCode='BBA1303'), 
 (SELECT facultyId FROM Courses WHERE courseCode='BBA1303'), 'Quiz 1', 'Quiz', 8, 10, 2, '2024-09-14', 'Good business concepts'),
('ANI2301051M', (SELECT courseId FROM Courses WHERE courseCode='BBA1303'), 
 (SELECT facultyId FROM Courses WHERE courseCode='BBA1303'), 'Quiz 1', 'Quiz', 9, 10, 2, '2024-09-14', 'Excellent understanding');

-- 3. Assignments
INSERT INTO Assessments (studentId, courseId, facultyId, assessmentName, assessmentType, marks, totalMarks, weightage, assessmentDate, remarks)
VALUES
-- Programming Assignments
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Binary Tree Implementation', 'Assignment', 18, 20, 10, '2024-09-15', 'Good algorithm implementation'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Binary Tree Implementation', 'Assignment', 19, 20, 10, '2024-09-15', 'Excellent coding style'),
('NIS2301048M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Binary Tree Implementation', 'Assignment', 16, 20, 10, '2024-09-15', 'Functional but needs optimization'),

-- Database Design Assignment
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1302'), 'Library Management System', 'Assignment', 17, 20, 10, '2024-09-20', 'Good ER design'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1302'), 'Library Management System', 'Assignment', 18, 20, 10, '2024-09-20', 'Excellent normalization'),

-- Literature Analysis Assignment
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Modern Poetry Analysis', 'Assignment', 16, 20, 10, '2024-09-15', 'Creative analysis'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Modern Poetry Analysis', 'Assignment', 17, 20, 10, '2024-09-15', 'Well structured essay'),
('BIL2301050M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Modern Poetry Analysis', 'Assignment', 15, 20, 10, '2024-09-15', 'Good literary insight'),

-- Chemistry Lab Assignment
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ACCE1101'), 'Spectroscopy Lab Report', 'Assignment', 19, 20, 10, '2024-09-18', 'Excellent lab technique'),

-- Math Problem Set
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'Differential Calculus Problems', 'Assignment', 16, 20, 10, '2024-09-25', 'Good problem solving'),
('FAR2301049M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'Differential Calculus Problems', 'Assignment', 18, 20, 10, '2024-09-25', 'Excellent mathematical reasoning');

-- ===============================
-- Midterm Examinations
-- ===============================

INSERT INTO Assessments (studentId, courseId, facultyId, assessmentName, assessmentType, marks, totalMarks, weightage, assessmentDate, remarks)
VALUES
-- Data Structures Midterm
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Midterm Exam', 'Midterm', 40, 50, 25, '2024-10-10', 'Good problem solving'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Midterm Exam', 'Midterm', 43, 50, 25, '2024-10-10', 'Excellent algorithmic thinking'),
('NIS2301048M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Midterm Exam', 'Midterm', 38, 50, 25, '2024-10-10', 'Satisfactory performance'),

-- English Poetry Midterm
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Midterm Exam', 'Midterm', 38, 50, 25, '2024-10-10', 'Good literary analysis'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Midterm Exam', 'Midterm', 42, 50, 25, '2024-10-10', 'Excellent interpretation'),
('BIL2301050M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Midterm Exam', 'Midterm', 36, 50, 25, '2024-10-10', 'Good effort'),

-- Circuit Theory Midterm
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Midterm Exam', 'Midterm', 35, 50, 25, '2024-10-12', 'Need more practice'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Midterm Exam', 'Midterm', 41, 50, 25, '2024-10-12', 'Good circuit analysis'),

-- Chemistry Midterm
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ACCE1101'), 'Midterm Exam', 'Midterm', 44, 50, 25, '2024-10-08', 'Excellent chemical knowledge'),

-- Math Midterm
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'Midterm Exam', 'Midterm', 39, 50, 25, '2024-10-15', 'Good mathematical skills'),
('FAR2301049M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'Midterm Exam', 'Midterm', 42, 50, 25, '2024-10-15', 'Excellent problem solving');

-- ===============================
-- Final Examinations
-- ===============================

INSERT INTO Assessments (studentId, courseId, facultyId, assessmentName, assessmentType, marks, totalMarks, weightage, assessmentDate, remarks)
VALUES
-- Data Structures Final
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Final Exam', 'Final', 60, 70, 75, '2024-12-15', 'Strong technical skills'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Final Exam', 'Final', 65, 70, 75, '2024-12-15', 'Excellent comprehensive knowledge'),
('NIS2301048M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Final Exam', 'Final', 58, 70, 75, '2024-12-15', 'Good overall performance'),

-- English Poetry Final
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Final Exam', 'Final', 55, 70, 75, '2024-12-15', 'Good overall performance'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Final Exam', 'Final', 58, 70, 75, '2024-12-15', 'Excellent critical thinking'),
('BIL2301050M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Final Exam', 'Final', 52, 70, 75, '2024-12-15', 'Satisfactory analysis'),

-- Database Systems Final
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1302'), 'Final Exam', 'Final', 62, 70, 75, '2024-12-18', 'Excellent database design'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 
 (SELECT facultyId FROM Courses WHERE courseCode='CSTE1302'), 'Final Exam', 'Final', 59, 70, 75, '2024-12-18', 'Good SQL proficiency'),

-- Circuit Theory Final
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Final Exam', 'Final', 48, 70, 75, '2024-12-20', 'Improved significantly'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Final Exam', 'Final', 56, 70, 75, '2024-12-20', 'Good circuit understanding'),

-- Chemistry Final
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='ACCE1101'), 'Final Exam', 'Final', 63, 70, 75, '2024-12-12', 'Outstanding chemistry knowledge'),

-- Math Final
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'Final Exam', 'Final', 54, 70, 75, '2024-12-22', 'Good mathematical reasoning'),
('FAR2301049M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 
 (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'Final Exam', 'Final', 61, 70, 75, '2024-12-22', 'Excellent calculus mastery');

GO


--------------------------------------------------
-- 9. Attendance Table
-- Tracks student attendance for each course session
--------------------------------------------------
CREATE TABLE Attendance (
    attendanceId INT IDENTITY(1,1) PRIMARY KEY,
    studentId NVARCHAR(20) NOT NULL,
    courseId INT NOT NULL,
    sectionId INT NULL, -- Link to course section
    attendanceDate DATE NOT NULL,
    status NVARCHAR(20) NOT NULL CHECK (status IN ('PRESENT', 'ABSENT', 'LATE', 'EXCUSED')),
    classType NVARCHAR(20) DEFAULT 'LECTURE' CHECK (classType IN ('LECTURE','LAB','TUTORIAL','SEMINAR','PRACTICAL','EXAM','PRESENTATION')),
    classNumber INT DEFAULT 1, -- Sequential class number for the course
    facultyId NVARCHAR(20) NOT NULL,
    attendanceMarks DECIMAL(5,2) DEFAULT 0.00, -- Marks for this attendance
    remarks NVARCHAR(MAX),
    isActive BIT DEFAULT 1,
    enteredBy INT NULL,
    enteredDate DATETIME DEFAULT GETDATE(),
    modifiedBy INT NULL,
    modifiedDate DATETIME NULL,
    FOREIGN KEY (studentId) REFERENCES Students(studentId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE,
    --FOREIGN KEY (sectionId) REFERENCES CourseSections(sectionId) ON DELETE SET NULL,
    FOREIGN KEY (facultyId) REFERENCES Faculty(facultyId) ON DELETE NO ACTION,
    FOREIGN KEY (enteredBy) REFERENCES Users(userId) ON DELETE NO ACTION,
    FOREIGN KEY (modifiedBy) REFERENCES Users(userId) ON DELETE NO ACTION,
    UNIQUE(studentId, courseId, attendanceDate, classType) -- Prevent duplicate entries
);

-- Create indexes separately for Attendance table
CREATE INDEX IX_Attendance_Student ON Attendance(studentId);
CREATE INDEX IX_Attendance_Course ON Attendance(courseId);
CREATE INDEX IX_Attendance_Date ON Attendance(attendanceDate);
CREATE INDEX IX_Attendance_Status ON Attendance(status);
GO

-- Full Attendance Data

INSERT INTO Attendance (studentId, courseId, attendanceDate, status, classType, facultyId, remarks)
VALUES
-- Data Structures (CSTE1201)
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-01', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Active participation'),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-02', 'LATE', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Arrived 10 min late'),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-03', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Good engagement'),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-04', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'On time'),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-05', 'ABSENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Medical leave'),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-08', 'PRESENT', 'LAB', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Good lab work'),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-09', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Excellent questions'),
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-10', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Participated in discussion'),

('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-01', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Attentive'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-02', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Good participation'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-03', 'LATE', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Traffic jam'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-04', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'On time'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-05', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Active learner'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-08', 'PRESENT', 'LAB', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Excellent programming'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-09', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Good understanding'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), '2023-09-10', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='CSTE1201'), 'Helpful to peers'),

-- Modern English Poetry (ENG2202)
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-01', 'ABSENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Medical leave'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-02', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Participated in discussion'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-03', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Good contribution'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-04', 'LATE', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Traffic delay'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-05', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Excellent analysis'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-08', 'PRESENT', 'SEMINAR', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Great presentation'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-09', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Insightful comments'),

('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-01', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Enthusiastic participation'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-02', 'EXCUSED', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Family emergency'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-03', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Insightful questions'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-04', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'On time'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-05', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Outstanding contribution'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-08', 'PRESENT', 'SEMINAR', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Excellent analysis'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), '2023-09-09', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ENG2202'), 'Creative interpretation'),

-- Circuit Theory (EEE1101)
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), '2023-09-01', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Attentive'),
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), '2023-09-02', 'PRESENT', 'LAB', (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Good lab skills'),
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), '2023-09-03', 'LATE', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Bus delay'),
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), '2023-09-04', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Improved understanding'),
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), '2023-09-05', 'ABSENT', 'LAB', (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Sick'),
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), '2023-09-08', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='EEE1101'), 'Back on track'),

-- Physical Chemistry (ACCE1101)
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), '2023-09-01', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ACCE1101'), 'Excellent attention'),
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), '2023-09-02', 'PRESENT', 'LAB', (SELECT facultyId FROM Courses WHERE courseCode='ACCE1101'), 'Outstanding lab work'),
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), '2023-09-03', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='ACCE1101'), 'Great questions'),

-- Calculus I (MATH1101)
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), '2023-09-01', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'Good focus'),
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), '2023-09-02', 'PRESENT', 'TUTORIAL', (SELECT facultyId FROM Courses WHERE courseCode='MATH1101'), 'Active problem solving'),

-- Principles of Management (BBA1101)
('MOH2101035M', (SELECT courseId FROM Courses WHERE courseCode='BBA1101'), '2023-09-01', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='BBA1101'), 'Good business sense'),

-- Pharmaceutical Chemistry (PHAR1101)
('ESH2101036M', (SELECT courseId FROM Courses WHERE courseCode='PHAR1101'), '2023-09-01', 'PRESENT', 'LECTURE', (SELECT facultyId FROM Courses WHERE courseCode='PHAR1101'), 'Attentive learner');

GO



--------------------------------------------------
-- 10. AttendanceReports Table
-- Pre-calculated attendance statistics for reporting
--------------------------------------------------
CREATE TABLE AttendanceReports (
    reportId INT IDENTITY(1,1) PRIMARY KEY,
    studentId NVARCHAR(20) NOT NULL,           -- FK to Students
    courseId INT NOT NULL,                      -- FK to Courses
    totalClasses INT DEFAULT 0,                 -- Total classes held
    presentClasses INT DEFAULT 0,               -- Classes attended
    absentClasses INT DEFAULT 0,                -- Absent classes
    lateClasses INT DEFAULT 0,                  -- Late classes
    excusedClasses INT DEFAULT 0,               -- Excused absences
    attendancePercentage AS CAST(
        CASE 
            WHEN totalClasses > 0 THEN (CAST(presentClasses AS FLOAT) / totalClasses) * 100
            ELSE 0 
        END AS DECIMAL(5,2)
    ) PERSISTED,                                -- Auto-calculated field
    academicYear NVARCHAR(20),                  -- e.g., 2024-25
    semester NVARCHAR(10),                      -- e.g., 1st, 2nd
    lastUpdated DATETIME DEFAULT GETDATE(),     -- Last calculation/update timestamp
    isActive BIT DEFAULT 1,                     -- Active flag
    FOREIGN KEY (studentId) REFERENCES Students(studentId) ON DELETE CASCADE,
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE,
    UNIQUE(studentId, courseId)              -- Prevent duplicate reports per course/student
);

-- Indexes must be created separately
CREATE INDEX IX_AttendanceReports_Student ON AttendanceReports(studentId);
CREATE INDEX IX_AttendanceReports_Course ON AttendanceReports(courseId);
CREATE INDEX IX_AttendanceReports_Percentage ON AttendanceReports(attendancePercentage);
GO

-- Insert Attendance Reports using courseCode to get courseId
INSERT INTO AttendanceReports
(studentId, courseId, totalClasses, presentClasses, absentClasses, lateClasses, excusedClasses, academicYear, semester)
VALUES
-- Data Structures Course Reports
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 8, 6, 1, 1, 0, '2024-25', '7th'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 8, 7, 0, 1, 0, '2024-25', '7th'),
('NIS2301048M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1201'), 8, 7, 1, 0, 0, '2024-25', '2nd'),

-- Database Systems Course Reports
('MUH2101020M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 5, 4, 0, 1, 0, '2024-25', '7th'),
('BOR2101030M', (SELECT courseId FROM Courses WHERE courseCode='CSTE1302'), 5, 4, 1, 0, 0, '2024-25', '7th'),

-- Modern English Poetry Course Reports
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 7, 5, 1, 1, 0, '2024-25', '7th'),
('MUH2301007M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 7, 6, 0, 0, 1, '2024-25', '2nd'),
('BIL2301050M', (SELECT courseId FROM Courses WHERE courseCode='ENG2202'), 7, 6, 1, 0, 0, '2024-25', '2nd'),

-- Circuit Theory Course Reports
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 6, 4, 1, 1, 0, '2024-25', '7th'),
('ASH2101027M', (SELECT courseId FROM Courses WHERE courseCode='EEE1101'), 6, 5, 0, 1, 0, '2024-25', '7th'),

-- Physical Chemistry Course Reports
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1101'), 6, 6, 0, 0, 0, '2024-25', '7th'),

-- Calculus I Course Reports
('SAJ2101034M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 5, 4, 0, 1, 0, '2024-25', '7th'),
('FAR2301049M', (SELECT courseId FROM Courses WHERE courseCode='MATH1101'), 5, 5, 0, 0, 0, '2024-25', '2nd'),

-- Principles of Management Course Reports
('MOH2101035M', (SELECT courseId FROM Courses WHERE courseCode='BBA1101'), 5, 4, 0, 1, 0, '2024-25', '7th'),
('ANI2301051M', (SELECT courseId FROM Courses WHERE courseCode='BBA1101'), 5, 5, 0, 0, 0, '2024-25', '2nd'),

-- Pharmaceutical Chemistry Course Reports
('ESH2101036M', (SELECT courseId FROM Courses WHERE courseCode='PHAR1101'), 5, 5, 0, 0, 0, '2024-25', '7th'),

-- Additional Reports
('RAB2101032M', (SELECT courseId FROM Courses WHERE courseCode='ACCE1303'), 6, 5, 1, 0, 0, '2024-25', '7th'),
('TAS2101033M', (SELECT courseId FROM Courses WHERE courseCode='EEE1202'), 6, 4, 1, 1, 0, '2024-25', '7th'),
('NIL2201044M', (SELECT courseId FROM Courses WHERE courseCode='MATH1202'), 5, 4, 0, 1, 0, '2024-25', '5th'),
('NIL2201044M', (SELECT courseId FROM Courses WHERE courseCode='MATH1303'), 5, 5, 0, 0, 0, '2024-25', '5th'),
('YEA2201045M', (SELECT courseId FROM Courses WHERE courseCode='BBA1202'), 5, 4, 1, 0, 0, '2023-24', '5th'),
('YEA2201045M', (SELECT courseId FROM Courses WHERE courseCode='BBA1303'), 5, 5, 0, 0, 0, '2023-24', '5th'),
('SAZ2201046M', (SELECT courseId FROM Courses WHERE courseCode='PHAR1202'), 5, 4, 0, 1, 0, '2023-24', '5th'),
('SAZ2201046M', (SELECT courseId FROM Courses WHERE courseCode='PHAR1303'), 5, 5, 0, 0, 0, '2023-24', '5th'),
('YEB2201047M', (SELECT courseId FROM Courses WHERE courseCode='GEB1202'), 5, 5, 0, 0, 0, '2023-24', '5th'),
('YEB2201047M', (SELECT courseId FROM Courses WHERE courseCode='GEB1303'), 5, 4, 1, 0, 0, '2023-24', '5th'),

-- Cross-departmental
('SOI2101040M', (SELECT courseId FROM Courses WHERE courseCode='ENG2101'), 5, 4, 0, 1, 0, '2023-24', '7th'),
('MAH2101041M', (SELECT courseId FROM Courses WHERE courseCode='ENG2404'), 5, 5, 0, 0, 0, '2023-24', '7th'),
('SAF2301052M', (SELECT courseId FROM Courses WHERE courseCode='ESDM1101'), 5, 4, 1, 0, 0, '2023-24', '2nd');
GO

USE PushpoAcademicDB;
--------------------------------------------------
-- 11 Notifications Table
--------------------------------------------------
CREATE TABLE Notifications (
    notificationId INT IDENTITY(1,1) PRIMARY KEY,
    userId INT NOT NULL,
    title NVARCHAR(200) NOT NULL,
    message NVARCHAR(MAX) NOT NULL,
    type NVARCHAR(50) DEFAULT 'GENERAL',
    priority NVARCHAR(20) DEFAULT 'MEDIUM' CHECK (priority IN ('LOW','MEDIUM','HIGH','URGENT')),
    relatedType NVARCHAR(50),
    relatedId INT,
    isRead BIT DEFAULT 0,
    createdDate DATETIME DEFAULT GETDATE(),
    readDate DATETIME,
    isActive BIT DEFAULT 1,
    FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE NO ACTION
);
GO

CREATE INDEX IX_Notifications_User ON Notifications(userId);
CREATE INDEX IX_Notifications_Date ON Notifications(createdDate);
CREATE INDEX IX_Notifications_Type ON Notifications(type);
CREATE INDEX IX_Notifications_Read ON Notifications(isRead);
GO

-- ==========================================
-- COMPLETE NOTIFICATIONS INSERT (ALL TYPES)
-- ==========================================

-- WELCOME notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Welcome to NSTU - Batch 18', 
       'Welcome to Noakhali Science and Technology University! Your academic journey in Computer Science begins here.',
       'GENERAL', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='MUH2301007M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Welcome to EEE Department', 
       'Welcome Faraha! We are excited to have you in our Electrical Engineering program.',
       'GENERAL', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='FAR2301049M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Chemistry Department Welcome',
       'Welcome to Applied Chemistry and Chemical Engineering. Check your lab schedule.',
       'GENERAL', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='RAB2101032M';

-- ACADEMIC notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Semester 7 Registration Complete', 
       'Your course registration for Semester 7 has been successfully completed. Check your enrollment status.',
       'ACADEMIC', 'HIGH', NULL, NULL, 1
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Mid-semester Grade Update', 
       'Your mid-semester grades are now available. Current CGPA: 3.30',
       'ACADEMIC', 'MEDIUM', NULL, NULL, 1
FROM Users WHERE username='BOR2101030M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Academic Standing - Excellent', 
       'Congratulations! You are on the Dean''s List for excellent academic performance.',
       'ACADEMIC', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='NIS2301048M';

-- ASSIGNMENT notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Database Project Due Soon', 
       'Your Library Management System project for Database Systems is due in 3 days.',
       'ASSIGNMENT', 'HIGH', 'Course', 2, 0
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Algorithm Assignment Uploaded', 
       'New assignment on Binary Tree implementation has been uploaded for Data Structures.',
       'ASSIGNMENT', 'HIGH', 'Course', 1, 0
FROM Users WHERE username='TAS2101033M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Chemistry Lab Report Due', 
       'Spectroscopy lab report for Physical Chemistry is due next week.',
       'ASSIGNMENT', 'MEDIUM', 'Course', 11, 0
FROM Users WHERE username='ESH2101036M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Math Problem Set Available', 
       'New calculus problem set is available for download. Due date: Next Friday.',
       'ASSIGNMENT', 'MEDIUM', 'Course', 19, 0
FROM Users WHERE username='SAJ2101034M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Business Case Study Assignment', 
       'Strategic Management case study assignment has been posted. Work in groups of 4.',
       'ASSIGNMENT', 'HIGH', 'Course', 26, 0
FROM Users WHERE username='YEA2201045M';

-- EXAMINATION notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Midterm Schedule Released', 
       'Midterm examination schedule for all courses has been published. Check your timetable.',
       'EXAM', 'HIGH', NULL, NULL, 1
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Final Exam Hall Ticket', 
       'Your final examination hall ticket is ready for download from student portal.',
       'EXAM', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='BOR2101030M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Make-up Exam Scheduled', 
       'Make-up exam for missed Chemistry midterm has been scheduled for next Monday.',
       'EXAM', 'URGENT', 'Course', 11, 0
FROM Users WHERE username='ESH2101036M';

-- GRADE notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Data Structures Final Grade', 
       'Your final grade for Data Structures: A- (3.7 GPA). Excellent performance!',
       'GRADE', 'MEDIUM', 'Course', 1, 0
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Database Systems Grade Published', 
       'Your Database Systems final grade: B+ (3.3 GPA). Good improvement shown.',
       'GRADE', 'MEDIUM', 'Course', 2, 0
FROM Users WHERE username='BOR2101030M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'English Poetry Grade Available', 
       'Modern English Poetry final grade: A (4.0 GPA). Outstanding literary analysis!',
       'GRADE', 'MEDIUM', 'Course', 7, 0
FROM Users WHERE username='NIS2301048M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Circuit Theory Results', 
       'Your Circuit Theory final grade: B (3.0 GPA). Consider extra help for advanced topics.',
       'GRADE', 'MEDIUM', 'Course', 15, 0
FROM Users WHERE username='TAS2101033M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Chemistry Excellence Award', 
       'Congratulations! You received the highest grade in Physical Chemistry this semester.',
       'GRADE', 'HIGH', 'Course', 11, 0
FROM Users WHERE username='RAB2101032M';

-- ATTENDANCE notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Circuit Theory Attendance Warning', 
       'Your attendance in Circuit Theory is 66%. Minimum 75% required for final exam eligibility.',
       'ATTENDANCE', 'URGENT', 'Course', 15, 0
FROM Users WHERE username='TAS2101033M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Data Structures Attendance Good', 
       'Your current attendance in Data Structures is 75%. Keep up the good work!',
       'ATTENDANCE', 'LOW', 'Course', 1, 1
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Perfect Attendance Recognition', 
       'Congratulations on maintaining perfect attendance in Database Systems!',
       'ATTENDANCE', 'MEDIUM', 'Course', 2, 0
FROM Users WHERE username='BOR2101030M';

-- SYSTEM notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Student Portal Maintenance', 
       'Student portal will be under maintenance on Saturday 2-4 AM. Plan accordingly.',
       'SYSTEM', 'MEDIUM', NULL, NULL, 1
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Faculty Portal Update', 
       'New grade entry features are now available in the faculty portal.',
       'SYSTEM', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='RAB2101032M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Database Backup Completed', 
       'Daily database backup completed successfully. All systems operational.',
       'SYSTEM', 'LOW', NULL, NULL, 1
FROM Users WHERE username='MUH2301007M';

-- SCHOLARSHIP / FINANCIAL notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Merit Scholarship Awarded', 
       'Congratulations! You have been awarded a merit scholarship for academic excellence.',
       'FINANCIAL', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='NIS2301048M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Tuition Fee Reminder', 
       'Semester tuition fee payment deadline is approaching. Pay by month end to avoid late fees.',
       'FINANCIAL', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='MUH2301007M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Research Grant Opportunity', 
       'New undergraduate research grant applications are now open for Chemistry students.',
       'FINANCIAL', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='ESH2101036M';

-- EVENT notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Tech Fest 2024 Registration', 
       'Annual Technical Festival registration is now open. Showcase your programming skills!',
       'EVENT', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Career Fair Next Week', 
       'Major tech companies will be visiting campus for recruitment. Update your resume!',
       'EVENT', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='BOR2101030M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Business Plan Competition', 
       'Submit your business plan for the annual entrepreneurship competition. Prize: 50,000 BDT.',
       'EVENT', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='YEA2201045M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Faculty Development Workshop', 
       'Workshop on modern teaching methodologies scheduled for next weekend.',
       'EVENT', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='RAB2101032M';

-- LIBRARY / FACILITY notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Library Book Overdue', 
       'You have 2 overdue books. Please return to avoid fine. Introduction to Algorithms due 5 days ago.',
       'LIBRARY', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'New Books Available', 
       'Latest editions of literature and poetry books are now available in the library.',
       'LIBRARY', 'LOW', NULL, NULL, 0
FROM Users WHERE username='NIS2301048M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Computer Lab Booking', 
       'Programming lab is available for booking during weekends. Reserve your slot online.',
       'FACILITY', 'LOW', NULL, NULL, 0
FROM Users WHERE username='SAJ2101034M';

-- HEALTH / SAFETY notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Health Checkup Reminder', 
       'Annual health checkup for all students is due. Visit campus health center.',
       'HEALTH', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='TAS2101033M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Campus Safety Drill', 
       'Emergency evacuation drill scheduled for tomorrow 3 PM. All must participate.',
       'SAFETY', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='MUH2301007M';

-- COURSE notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Programming Contest Practice', 
       'Extra programming practice sessions for Data Structures students every Thursday.',
       'COURSE', 'LOW', 'Course', 1, 0
FROM Users WHERE username='TAS2101033M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Database Guest Lecture', 
       'Industry expert will deliver guest lecture on NoSQL databases next Tuesday.',
       'COURSE', 'MEDIUM', 'Course', 2, 0
FROM Users WHERE username='MUH2101020M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Poetry Recitation Event', 
       'Voluntary poetry recitation event for English students. Showcase your talent!',
       'COURSE', 'LOW', 'Course', 7, 0
FROM Users WHERE username='NIS2301048M';

-- ADMINISTRATIVE notifications
INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Grade Submission Deadline', 
       'Final grades must be submitted by faculty before the deadline: December 30th.',
       'ADMIN', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='RAB2101032M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Course Evaluation Forms', 
       'Student course evaluation forms are now available. Faculty access granted.',
       'ADMIN', 'MEDIUM', NULL, NULL, 0
FROM Users WHERE username='ESH2101036M';

INSERT INTO Notifications (userId, title, message, type, priority, relatedType, relatedId, isRead)
SELECT userId, 'Annual Report Preparation', 
       'Academic year report preparation is in progress. Department heads please submit data.',
       'ADMIN', 'HIGH', NULL, NULL, 0
FROM Users WHERE username='MUH2301007M';
GO


--------------------------------------------------
-- 12 Email Templates Table
--------------------------------------------------
CREATE TABLE EmailTemplates (
    templateId INT IDENTITY(1,1) PRIMARY KEY,
    templateName NVARCHAR(100) UNIQUE NOT NULL,
    subject NVARCHAR(200) NOT NULL,
    body NVARCHAR(MAX) NOT NULL,
    templateType NVARCHAR(50) NOT NULL,
    variables NVARCHAR(MAX),
    createdBy INT NOT NULL,
    createdDate DATETIME DEFAULT GETDATE(),
    lastModified DATETIME DEFAULT GETDATE(),
    isActive BIT DEFAULT 1,
    FOREIGN KEY (createdBy) REFERENCES Users(userId) ON DELETE NO ACTION
);

-- Indexes outside table
CREATE INDEX IX_EmailTemplates_Type ON EmailTemplates(templateType);
CREATE INDEX IX_EmailTemplates_CreatedBy ON EmailTemplates(createdBy);
CREATE INDEX IX_EmailTemplates_IsActive ON EmailTemplates(isActive);
GO

-- Insert sample email templates
INSERT INTO EmailTemplates (templateName, subject, body, templateType, variables, createdBy)
VALUES
('Assessment Marks Notification',
 'Your Assessment Marks are Published',
 'Hello {{studentName}},<br><br>Your Class Test ({{assessmentName}}) marks for {{courseName}} have been published.<br>Marks Obtained: {{marks}}/{{totalMarks}}<br>Grade: {{grade}}<br><br>Regards,<br>{{facultyName}}',
 'Grade',
 '["studentName","assessmentName","courseName","marks","totalMarks","grade","facultyName"]',
 1),

('Attendance Alert',
 'Attendance Update for {{courseName}}',
 'Dear {{studentName}},<br><br>Your attendance for {{courseName}} as of {{date}} is {{attendancePercentage}}%.<br>Please ensure you maintain the minimum required attendance.<br><br>Regards,<br>{{facultyName}}',
 'Attendance',
'["studentName","courseName","date","attendancePercentage","facultyName"]',
 1),

('Assignment Uploaded',
 'New Assignment: {{assignmentTitle}}',
 'Hello {{studentName}},<br><br>A new assignment "{{assignmentTitle}}" has been uploaded for {{courseName}}.<br>Submission Deadline: {{dueDate}}<br>Check the LMS portal for details.<br><br>Regards,<br>{{facultyName}}',
 'Assignment',
 '["studentName","assignmentTitle","courseName","dueDate","facultyName"]',
 2),

('CT Marks Notification',
 'Class Test Marks Published - {{courseName}}',
 'Dear {{studentName}},<br><br>Your Class Test marks have been published:<br><br><strong>Course:</strong> {{courseName}}<br><strong>Assessment:</strong> {{assessmentName}}<br><strong>Marks Obtained:</strong> {{marks}}/{{totalMarks}}<br><strong>Grade:</strong> {{grade}}<br><strong>Remarks:</strong> {{remarks}}<br><br>Keep up the good work!<br><br>Best regards,<br>{{facultyName}}<br>{{designation}}',
 'Marks',
 '["studentName","courseName","assessmentName","marks","totalMarks","grade","remarks","facultyName","designation"]',
 2),

('Attendance Report',
 'Attendance Summary - {{courseName}}',
 'Dear {{studentName}},<br><br>Your attendance summary for {{courseName}}:<br><br><strong>Total Classes:</strong> {{totalClasses}}<br><strong>Classes Attended:</strong> {{attendedClasses}}<br><strong>Attendance Percentage:</strong> {{attendancePercentage}}%<br><strong>Attendance Marks:</strong> {{attendanceMarks}}/{{maxAttendanceMarks}}<br><strong>Status:</strong> {{attendanceStatus}}<br><br>{{additionalMessage}}<br><br>Regards,<br>{{facultyName}}',
 'Attendance',
 '["studentName","courseName","totalClasses","attendedClasses","attendancePercentage","attendanceMarks","maxAttendanceMarks","attendanceStatus","additionalMessage","facultyName"]',
 2);
GO

--------------------------------------------------
-- 13. Academic Sessions Table
--------------------------------------------------
CREATE TABLE AcademicSessions (
    sessionId INT IDENTITY(1,1) PRIMARY KEY,
    sessionName NVARCHAR(20) UNIQUE NOT NULL, -- e.g., "2023-24", "2024-25"
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    isActive BIT DEFAULT 0,
    isCurrent BIT DEFAULT 0,
    createdDate DATETIME DEFAULT GETDATE(),
    createdBy INT,
    FOREIGN KEY (createdBy) REFERENCES Users(userId) ON DELETE SET NULL,
    CHECK (endDate > startDate)
);
GO

-- Insert academic sessions
INSERT INTO AcademicSessions (sessionName, startDate, endDate, isActive, isCurrent, createdBy)
VALUES
('2020-21', '2020-07-01', '2021-06-30', 1, 0, 1),
('2021-22', '2021-07-01', '2022-06-30', 1, 0, 1),
('2022-23', '2022-07-01', '2023-06-30', 1, 0, 1),
('2023-24', '2023-07-01', '2024-06-30', 1, 1, 1), -- Current session
('2024-25', '2024-07-01', '2025-06-30', 1, 0, 1);
GO


--------------------------------------------------
-- 15. Mark Entry Sessions Table (for tracking mark submission status)
--------------------------------------------------
CREATE TABLE MarkEntrySessions (
    markSessionId INT IDENTITY(1,1) PRIMARY KEY,
    courseId INT NOT NULL,
    sectionId INT NOT NULL,
    facultyId NVARCHAR(20) NOT NULL,
    assessmentType NVARCHAR(50) NOT NULL, -- CT, Quiz, Assignment, Midterm, Final
    assessmentName NVARCHAR(100) NOT NULL,
    totalMarks DECIMAL(5,2) NOT NULL,
    weightage DECIMAL(5,2) NOT NULL,
    entryDate DATE DEFAULT CAST(GETDATE() AS DATE),
    submissionStatus NVARCHAR(20) DEFAULT 'DRAFT' CHECK (submissionStatus IN ('DRAFT','SUBMITTED','PUBLISHED')),
    publishedDate DATETIME NULL,
    emailsSent BIT DEFAULT 0,
    createdBy INT NOT NULL,
    createdDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (courseId) REFERENCES Courses(courseId) ON DELETE CASCADE,
    --FOREIGN KEY (sectionId) REFERENCES CourseSections(sectionId) ON DELETE CASCADE,
    FOREIGN KEY (facultyId) REFERENCES Faculty(facultyId) ON DELETE NO ACTION,
    FOREIGN KEY (createdBy) REFERENCES Users(userId) ON DELETE NO ACTION
);
GO