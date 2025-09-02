# Changelog

All notable changes to the EduVault Academic Management System will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Comprehensive README.md with detailed setup instructions
- MIT License for open source distribution
- .gitignore file for clean repository management
- Complete software engineering documentation
- UML diagrams (ER, Class, DFD, Activity, Sequence, CRC)

### Changed
- Improved project structure for better maintainability
- Enhanced documentation for easier contribution

### Security
- Password hashing implementation
- Role-based access control
- SQL injection prevention with prepared statements

## [1.0.0] - 2025-09-02

### Added
- **Core Features**
  - Multi-role authentication system (Admin, Faculty, Student)
  - Student management with profile and enrollment features
  - Faculty course and grade management capabilities
  - Administrative user and system management
  - Email notification service integration

- **Database Features**
  - Complete SQL Server database schema
  - User, Student, Faculty, Course, and Assessment tables
  - Enrollment tracking and grade management
  - Data integrity constraints and relationships

- **User Interface**
  - JavaFX-based desktop application
  - Role-specific dashboards and interfaces
  - Grade entry and viewing capabilities
  - Course enrollment and management

- **Technical Implementation**
  - MVC (Model-View-Controller) architecture
  - DAO (Data Access Object) pattern
  - Connection pooling and database management
  - Error handling and validation

### Technical Specifications
- **Java**: JDK 17+ compatibility
- **JavaFX**: 19+ for modern UI components
- **Database**: Microsoft SQL Server 2019+
- **JDBC**: mssql-jdbc-12.8.1 driver
- **Email**: JavaMail API for notifications

### Security Features
- Secure password storage with hashing
- Session management and timeout
- Role-based access restrictions
- SQL injection prevention
- Input validation and sanitization

## [0.9.0] - 2025-08-15

### Added
- Initial project structure and basic framework
- Database schema design and implementation
- Core model classes (User, Student, Faculty, Course)
- Basic authentication mechanism

### Changed
- Migrated from console-based to JavaFX GUI
- Improved database connection management
- Enhanced error handling throughout the application

## [0.5.0] - 2025-08-01

### Added
- Basic user management functionality
- Simple grade entry and viewing
- Initial database setup scripts
- Core business logic implementation

### Technical Notes
- Established MVC architecture pattern
- Implemented basic DAO layer
- Created initial JavaFX views

---

## Version Numbering

This project follows [Semantic Versioning](https://semver.org/):
- **MAJOR.MINOR.PATCH**
- **MAJOR**: Incompatible API changes
- **MINOR**: Backward-compatible functionality additions
- **PATCH**: Backward-compatible bug fixes

## Change Categories

- **Added**: New features
- **Changed**: Changes in existing functionality
- **Deprecated**: Soon-to-be removed features
- **Removed**: Features removed in this version
- **Fixed**: Bug fixes
- **Security**: Security vulnerability fixes

## Contributing to Changelog

When contributing, please:
1. Add your changes to the [Unreleased] section
2. Follow the established format and categories
3. Include breaking changes in the Changed section
4. Reference issue numbers when applicable
5. Keep descriptions concise but informative
