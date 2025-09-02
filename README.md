# 🎓 EduVault Academic Management System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-19+-blue.svg)](https://openjfx.io/)
[![SQL Server](https://img.shields.io/badge/SQL%20Server-2019+-red.svg)](https://www.microsoft.com/sql-server)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 🌟 Project Overview

**EduVault** is a comprehensive **desktop application** designed to streamline academic management processes in educational institutions. Built using **JavaFX** for the user interface and **Microsoft SQL Server** for robust data management, this system provides a complete solution for managing students, faculty, courses, grades, and administrative tasks.

### 🎯 **What This Project Does**
- **Student Management**: Registration, profile management, course enrollment, and academic tracking
- **Faculty Operations**: Course assignments, grade entry, attendance tracking, and student performance monitoring  
- **Administrative Control**: User management, system configuration, reporting, and oversight
- **Grade Management**: Comprehensive assessment tracking with automated calculations and notifications
- **Communication**: Integrated email notifications for important academic updates

### 🏗️ **Why This Project Matters**
This system addresses real-world challenges in academic administration by providing:
- **Centralized Data Management** - All academic information in one secure location
- **Automated Processes** - Reduces manual work through automated grade calculations and notifications
- **Role-Based Access** - Secure access control ensuring data privacy and appropriate permissions
- **Scalable Architecture** - MVC design pattern allows for easy maintenance and feature expansion

## 📋 Table of Contents
- [Features](#-key-features)
- [Technology Stack](#️-technology-stack)
- [Prerequisites](#-prerequisites)
- [Environment Setup](#-environment-setup)
- [Installation Guide](#-installation-guide)
- [Database Setup](#️-database-setup)
- [Running the Project](#-running-the-project)
- [User Guide](#-user-guide)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [Troubleshooting](#-troubleshooting)
- [Future Enhancements](#-future-enhancements)

## ✨ Key Features

### 🔐 **Authentication & Security**
- **Multi-Role Login System**: Separate interfaces for Admin, Faculty, and Students
- **Secure Password Management**: Encrypted password storage and validation
- **Session Management**: Automatic logout and session tracking
- **Role-Based Permissions**: Access control based on user roles

### 👨‍🎓 **Student Features**
- **Profile Management**: View and update personal information
- **Course Enrollment**: Browse available courses and enroll with prerequisite checking
- **Grade Tracking**: Real-time access to grades and academic performance
- **Transcript Generation**: Download official academic transcripts
- **CGPA Calculation**: Automated cumulative grade point average computation

### 👨‍🏫 **Faculty Features**
- **Course Management**: View assigned courses and enrolled students
- **Grade Entry**: Enter grades for various assessment types (Quiz, Assignment, Midterm, Final)
- **Grade Publishing**: Publish final grades with automatic student notifications
- **Attendance Tracking**: Mark and monitor student attendance
- **Performance Analytics**: View class statistics and student performance trends

### 🛡️ **Administrative Features**
- **User Management**: Create and manage student and faculty accounts
- **Course Setup**: Configure courses, prerequisites, and enrollment limits
- **System Reporting**: Generate comprehensive reports on system usage
- **Email Configuration**: Manage automated email notifications
- **Data Backup**: Database backup and recovery options

## 🛠️ Technology Stack

| **Component** | **Technology** | **Version** | **Purpose** |
|---------------|----------------|-------------|-------------|
| **Programming Language** | Java | 17+ | Core application development |
| **UI Framework** | JavaFX | 19+ | Desktop user interface |
| **Database** | Microsoft SQL Server | 2019+ | Data persistence and management |
| **Architecture** | MVC Pattern | - | Code organization and maintainability |
| **Email Service** | JavaMail API | 1.6+ | Email notifications |
| **JDBC Driver** | mssql-jdbc | 12.8.1 | Database connectivity |
| **Build System** | Manual compilation | - | Project building (Maven/Gradle optional) |

## 📋 Prerequisites

Before setting up the project, ensure you have the following installed on your system:

### **Required Software**
1. **Java Development Kit (JDK) 17 or higher**
   - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Verify installation: `java -version` and `javac -version`

2. **JavaFX SDK 19 or higher**
   - Download from: [OpenJFX](https://openjfx.io/openjfx/install/)
   - Extract to a directory (e.g., `C:\javafx-sdk-19`)

3. **Microsoft SQL Server**
   - Options: SQL Server Developer Edition (free) or SQL Server Express
   - Download from: [Microsoft SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-downloads)
   - Alternative: Use SQL Server Management Studio (SSMS) for easier management

4. **Git** (for version control)
   - Download from: [Git Downloads](https://git-scm.com/downloads)

### **Optional but Recommended**
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Database Tool**: SQL Server Management Studio (SSMS) or Azure Data Studio
- **Email Account**: Gmail or any SMTP-enabled email for testing notifications

## 🌍 Environment Setup

### **1. Set Java Environment Variables**

**Windows:**
```bash
# Add to System PATH
C:\Program Files\Java\jdk-17\bin

# Set JAVA_HOME
JAVA_HOME=C:\Program Files\Java\jdk-17
```

**macOS/Linux:**
```bash
# Add to ~/.bashrc or ~/.zshrc
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

### **2. Configure JavaFX**
Create an environment variable for JavaFX:
```bash
# Windows
JAVAFX_HOME=C:\javafx-sdk-19

# macOS/Linux
export JAVAFX_HOME=/path/to/javafx-sdk-19
```

### **3. SQL Server Configuration**
1. **Start SQL Server Service**
2. **Enable SQL Server Authentication** (if not already enabled)
3. **Create a dedicated database user** (recommended for security)
4. **Note down server name, port, and credentials**

## 🚀 Installation Guide

### **Step 1: Clone the Repository**
```bash
# Clone the project
git clone https://github.com/Kawchar-Ahammed/EduVault_Student_Management_System.git

# Navigate to project directory
cd EduVault_Student_Management_System
```

### **Step 2: Verify Project Structure**
Ensure your project has the following structure:
```
EduVault_Student_Management_System/
├── src/
│   ├── applications/
│   ├── controllers/
│   ├── model/
│   ├── dao/
│   ├── util/
│   └── sql/
├── lib/
├── bin/ (will be created during compilation)
└── README.md
```

### **Step 3: Check Dependencies**
Verify that all JAR files are present in the `lib/` directory:
- `javafx-base.jar`
- `javafx-controls.jar`
- `javafx-fxml.jar`
- `javafx-graphics.jar`
- `mssql-jdbc-12.8.1.jre8.jar`
- `javax.mail.jar`

## 🗄️ Database Setup

### **Step 1: Create Database**
1. **Open SQL Server Management Studio (SSMS)**
2. **Connect to your SQL Server instance**
3. **Create a new database:**
```sql
CREATE DATABASE EduVault_DB;
```

### **Step 2: Execute Database Script**
1. **Open the SQL script**: `src/sql/COMPLETE_ACADEMIC_SYSTEM.sql`
2. **Execute the script** in SSMS against the `EduVault_DB` database
3. **Verify tables creation**: Ensure all tables are created successfully

### **Step 3: Configure Database Connection**
Edit the file `src/util/DatabaseConnection.java`:

```java
// Update these values according to your SQL Server setup
private static final String CONNECTION_STRING = 
    "jdbc:sqlserver://localhost:1433;databaseName=EduVault_DB;encrypt=true;trustServerCertificate=true";
private static final String USERNAME = "your_sql_username";
private static final String PASSWORD = "your_sql_password";
```

**Common Connection String Variations:**
```java
// For SQL Server with Windows Authentication
"jdbc:sqlserver://localhost:1433;databaseName=EduVault_DB;integratedSecurity=true;"

// For SQL Server Express
"jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=EduVault_DB;encrypt=true;trustServerCertificate=true"

// For remote SQL Server
"jdbc:sqlserver://your-server-ip:1433;databaseName=EduVault_DB;encrypt=true;trustServerCertificate=true"
```

### **Step 4: Test Database Connection**
Create a simple test to verify connectivity:
```java
// Test in DatabaseConnection.java main method
public static void main(String[] args) {
    try {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn != null) {
            System.out.println("Database connection successful!");
            conn.close();
        }
    } catch (SQLException e) {
        System.out.println("Database connection failed: " + e.getMessage());
    }
}
```

## ▶️ Running the Project

### **Method 1: Command Line Compilation and Execution**

**Step 1: Compile the Project**
```bash
# Navigate to project root directory
cd /path/to/EduVault_Student_Management_System

# Create bin directory if it doesn't exist
mkdir bin

# Compile all Java files
javac -cp "lib/*" -d bin src/applications/*.java src/controllers/*.java src/model/*.java src/dao/*.java src/util/*.java
```

**Step 2: Run the Application**
```bash
# Run the main application
java -cp "bin;lib/*" applications.Main

# For macOS/Linux (use colon instead of semicolon)
java -cp "bin:lib/*" applications.Main
```

### **Method 2: Using an IDE**

**IntelliJ IDEA:**
1. **Open Project**: File → Open → Select project directory
2. **Configure Libraries**: File → Project Structure → Libraries → Add JARs from `lib/` folder
3. **Set Main Class**: Run → Edit Configurations → Main class: `applications.Main`
4. **Run**: Click the green play button or press Shift+F10

**Eclipse:**
1. **Import Project**: File → Import → Existing Projects into Workspace
2. **Add Libraries**: Right-click project → Properties → Java Build Path → Libraries → Add JARs
3. **Run Configuration**: Run → Run Configurations → Java Application → New
4. **Set Main Class**: `applications.Main`

**VS Code:**
1. **Open Folder**: File → Open Folder → Select project directory
2. **Install Extensions**: Java Extension Pack
3. **Configure Classpath**: Add `lib/*` to classpath in `.vscode/settings.json`
4. **Run**: Press F5 or use Run and Debug panel

### **Method 3: Batch/Shell Scripts**

**Windows (compile.bat):**
```batch
@echo off
echo Compiling EduVault...
javac -cp "lib/*" -d bin src/applications/*.java src/controllers/*.java src/model/*.java src/dao/*.java src/util/*.java
echo Compilation complete!
pause
```

**Windows (run.bat):**
```batch
@echo off
echo Starting EduVault Academic Management System...
java -cp "bin;lib/*" applications.Main
pause
```

**macOS/Linux (compile.sh):**
```bash
#!/bin/bash
echo "Compiling EduVault..."
javac -cp "lib/*" -d bin src/applications/*.java src/controllers/*.java src/model/*.java src/dao/*.java src/util/*.java
echo "Compilation complete!"
```

**macOS/Linux (run.sh):**
```bash
#!/bin/bash
echo "Starting EduVault Academic Management System..."
java -cp "bin:lib/*" applications.Main
```

## 👥 User Guide

### **Default Login Credentials**
For testing purposes, use these default credentials:

| **Role** | **Username** | **Password** | **Description** |
|----------|--------------|--------------|-----------------|
| Administrator | `admin` | `admin123` | Full system access |
| Faculty | `faculty` | `faculty123` | Course and grade management |
| Student | `student` | `student123` | Academic records and enrollment |

### **Administrator Workflow**
1. **Login** with admin credentials
2. **User Management**:
   - Create new faculty accounts
   - Create new student accounts
   - Manage user permissions
3. **System Configuration**:
   - Set up courses and curriculum
   - Configure system settings
   - Manage email settings
4. **Reporting**:
   - Generate system usage reports
   - View academic performance statistics
   - Export data for analysis

### **Faculty Workflow**
1. **Login** with faculty credentials
2. **Course Management**:
   - View assigned courses
   - See enrolled students list
   - Access course materials
3. **Grade Management**:
   - Enter grades for different assessments
   - Calculate weighted grades
   - Publish final grades
4. **Student Monitoring**:
   - Track student attendance
   - Monitor academic performance
   - Generate progress reports

### **Student Workflow**
1. **Login** with student credentials
2. **Academic Records**:
   - View current grades
   - Check CGPA and academic standing
   - Download transcripts
3. **Course Enrollment**:
   - Browse available courses
   - Check prerequisites
   - Enroll in courses
4. **Profile Management**:
   - Update personal information
   - Change password
   - Set notification preferences

## 📁 Project Structure

```
EduVault_Student_Management_System/
├── 📂 src/                           # Source code directory
│   ├── 📂 applications/              # Main application entry point
│   │   └── Main.java                 # Application launcher
│   ├── 📂 controllers/               # MVC Controllers
│   │   ├── AdminController.java      # Administrative operations
│   │   ├── FacultyController.java    # Faculty-specific functions
│   │   ├── StudentController.java    # Student-specific functions
│   │   └── LoginController.java      # Authentication handling
│   ├── 📂 model/                     # Data models/entities
│   │   ├── User.java                 # Base user model
│   │   ├── Student.java              # Student entity
│   │   ├── Faculty.java              # Faculty entity
│   │   ├── Course.java               # Course entity
│   │   ├── Assessment.java           # Assessment/grade entity
│   │   └── Enrollment.java           # Enrollment relationship
│   ├── 📂 dao/                       # Data Access Objects
│   │   ├── UserDAO.java              # User database operations
│   │   ├── StudentDAO.java           # Student database operations
│   │   ├── FacultyDAO.java           # Faculty database operations
│   │   ├── CourseDAO.java            # Course database operations
│   │   └── AssessmentDAO.java        # Assessment database operations
│   ├── 📂 util/                      # Utility classes
│   │   ├── DatabaseConnection.java   # Database connection management
│   │   ├── EmailService.java         # Email notification service
│   │   └── PasswordUtil.java         # Password encryption utilities
│   └── 📂 sql/                       # Database scripts
│       ├── COMPLETE_ACADEMIC_SYSTEM.sql  # Full database schema
│       └── sample_data.sql           # Sample data for testing
├── 📂 lib/                           # External libraries
│   ├── javafx-base.jar
│   ├── javafx-controls.jar
│   ├── javafx-fxml.jar
│   ├── javafx-graphics.jar
│   ├── javafx-media.jar
│   ├── javafx-swing.jar
│   ├── javafx-web.jar
│   ├── mssql-jdbc-12.8.1.jre8.jar
│   └── javax.mail.jar
├── 📂 bin/                           # Compiled class files (auto-generated)
├── 📂 docs/                          # Documentation (to be added)
│   ├── SOFTWARE_ENGINEERING_PROJECT_REPORT.md
│   ├── USE_CASE_MODELS.md
│   └── UML_DIAGRAMS/
├── 📂 resources/                     # Application resources
│   ├── images/                       # UI images and icons
│   └── fxml/                         # FXML layout files (if used)
├── 📄 README.md                      # This file
├── 📄 LICENSE                        # Project license
├── 📄 .gitignore                     # Git ignore rules
└── 📄 CHANGELOG.md                   # Version history
```

### **Key Architectural Components**

**MVC Architecture:**
- **Model**: Data entities representing real-world objects (Student, Faculty, Course)
- **View**: JavaFX user interface components
- **Controller**: Business logic and user interaction handling

**Data Access Layer:**
- **DAO Pattern**: Separates database operations from business logic
- **Connection Management**: Centralized database connection handling
- **Transaction Management**: Ensures data consistency

**Service Layer:**
- **Email Service**: Handles all email notifications
- **Security Utilities**: Password encryption and session management
- **Validation Services**: Input validation and business rule enforcement

## 🤝 Contributing

We welcome contributions from developers of all skill levels! Here's how you can contribute to EduVault:

### **Getting Started**
1. **Fork the Repository**
   ```bash
   # Click "Fork" on GitHub, then clone your fork
   git clone https://github.com/YOUR_USERNAME/EduVault_Student_Management_System.git
   cd EduVault_Student_Management_System
   ```

2. **Set Up Development Environment**
   - Follow the installation guide above
   - Ensure all tests pass before making changes
   - Set up your IDE with proper formatting rules

3. **Create a Feature Branch**
   ```bash
   # Create and switch to a new branch for your feature
   git checkout -b feature/your-feature-name
   ```

### **Development Guidelines**

**Code Style:**
- **Java Naming Conventions**: Use camelCase for variables/methods, PascalCase for classes
- **Indentation**: Use 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters per line
- **Documentation**: Add JavaDoc comments for all public methods and classes

**Example Code Style:**
```java
/**
 * Retrieves student information by ID
 * @param studentId The unique identifier for the student
 * @return Student object containing all student details
 * @throws SQLException if database connection fails
 */
public Student getStudentById(String studentId) throws SQLException {
    String query = "SELECT * FROM Students WHERE student_id = ?";
    // Implementation here
}
```

**Database Guidelines:**
- **Always use prepared statements** to prevent SQL injection
- **Include transaction management** for multi-table operations
- **Handle exceptions properly** with meaningful error messages
- **Close resources** in finally blocks or use try-with-resources

**UI Guidelines:**
- **Consistent styling** across all JavaFX components
- **Responsive design** that works on different screen sizes
- **User-friendly error messages** and validation feedback
- **Keyboard navigation support** for accessibility

### **Types of Contributions**

**🐛 Bug Fixes**
- Fix existing functionality issues
- Improve error handling
- Resolve performance problems

**✨ New Features**
- Add new functionality
- Enhance existing features
- Improve user experience

**📚 Documentation**
- Improve code comments
- Update README or guides
- Add API documentation

**🧪 Testing**
- Add unit tests
- Create integration tests
- Improve test coverage

**🎨 UI/UX Improvements**
- Enhance user interface design
- Improve accessibility
- Add new themes or styling

### **Pull Request Process**

1. **Update Documentation**
   - Update README.md if needed
   - Add/update code comments
   - Document any new features

2. **Test Your Changes**
   ```bash
   # Compile and test your changes
   javac -cp "lib/*" -d bin src/**/*.java
   java -cp "bin;lib/*" applications.Main
   ```

3. **Commit Your Changes**
   ```bash
   # Add your changes
   git add .
   
   # Commit with a descriptive message
   git commit -m "Add feature: student attendance tracking
   
   - Added attendance marking functionality for faculty
   - Created attendance report generation
   - Updated database schema with attendance table
   - Added unit tests for attendance operations"
   ```

4. **Push and Create Pull Request**
   ```bash
   # Push your branch
   git push origin feature/your-feature-name
   
   # Go to GitHub and create a Pull Request
   ```

5. **Pull Request Template**
   ```markdown
   ## Description
   Brief description of what this PR does
   
   ## Type of Change
   - [ ] Bug fix
   - [ ] New feature
   - [ ] Documentation update
   - [ ] Performance improvement
   
   ## Testing
   - [ ] All existing tests pass
   - [ ] New tests added for new functionality
   - [ ] Manual testing completed
   
   ## Screenshots (if applicable)
   Add screenshots of UI changes
   
   ## Checklist
   - [ ] Code follows project style guidelines
   - [ ] Self-review completed
   - [ ] Documentation updated
   - [ ] No merge conflicts
   ```

### **Code Review Process**
- All submissions require review before merging
- Reviewers will check for code quality, functionality, and adherence to guidelines
- Address feedback promptly and professionally
- Be open to suggestions and improvements

## 🔧 Troubleshooting

### **Common Issues and Solutions**

#### **1. Compilation Errors**

**Problem**: `error: package javafx.application does not exist`
```
Solution:
- Verify JavaFX is installed and properly configured
- Check JAVAFX_HOME environment variable
- Ensure JavaFX JARs are in the lib/ directory
- Use proper classpath in compilation command
```

**Problem**: `error: package java.sql does not exist`
```
Solution:
- Verify JDK version (should be 8 or higher)
- Check JAVA_HOME environment variable
- Ensure mssql-jdbc JAR is in classpath
```

#### **2. Database Connection Issues**

**Problem**: `SQLException: Login failed for user`
```
Solution:
1. Verify SQL Server is running
2. Check username/password in DatabaseConnection.java
3. Ensure SQL Server Authentication is enabled
4. Verify database name exists
5. Check firewall settings for SQL Server port (1433)
```

**Problem**: `SQLException: The TCP/IP connection to the host has failed`
```
Solution:
1. Enable TCP/IP protocol in SQL Server Configuration Manager
2. Restart SQL Server service
3. Check if SQL Server Browser service is running
4. Verify connection string format
```

#### **3. Runtime Errors**

**Problem**: `NoClassDefFoundError` at runtime
```
Solution:
1. Verify all JAR files are in lib/ directory
2. Check classpath includes all required libraries
3. Ensure bin/ directory contains compiled classes
4. Recompile the entire project
```

**Problem**: Email notifications not working
```
Solution:
1. Check email configuration in EmailService.java
2. Verify SMTP settings (Gmail: smtp.gmail.com:587)
3. For Gmail, enable "Less secure app access" or use App Passwords
4. Check firewall settings for SMTP ports
```

#### **4. Performance Issues**

**Problem**: Slow database queries
```
Solution:
1. Check database indexes on frequently queried columns
2. Optimize SQL queries in DAO classes
3. Implement connection pooling
4. Monitor SQL Server performance
```

**Problem**: UI freezing during operations
```
Solution:
1. Move long-running operations to background threads
2. Use JavaFX Task and Platform.runLater()
3. Implement progress indicators
4. Optimize database operations
```

### **Debug Mode**
To enable debug mode for troubleshooting:

```java
// Add to Main.java
public static void main(String[] args) {
    // Enable debug logging
    System.setProperty("java.util.logging.config.file", "logging.properties");
    
    // Your existing code
    launch(args);
}
```

### **Logging Configuration**
Create `logging.properties` for detailed logging:
```properties
# Global logging level
.level = INFO

# Console handler
java.util.logging.ConsoleHandler.level = ALL
java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter

# File handler
java.util.logging.FileHandler.level = ALL
java.util.logging.FileHandler.pattern = logs/eduvault_%g.log
java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter
```

## 🚀 Future Enhancements

### **Planned Features (Roadmap)**

#### **Phase 1: Core Improvements**
- [ ] **Enhanced Security**
  - Two-factor authentication (2FA)
  - Password complexity requirements
  - Session timeout management
  - Audit trail for all user actions

- [ ] **UI/UX Enhancements**
  - Modern Material Design interface
  - Dark/Light theme toggle
  - Responsive design for different screen sizes
  - Accessibility improvements (screen reader support)

#### **Phase 2: Advanced Features**
- [ ] **Reporting and Analytics**
  - Advanced reporting dashboard
  - Data visualization charts
  - Export to PDF/Excel
  - Automated report scheduling

- [ ] **Communication Features**
  - In-app messaging system
  - Announcements and notifications
  - Calendar integration
  - Mobile app notifications

#### **Phase 3: Integration and Scalability**
- [ ] **External Integrations**
  - Learning Management System (LMS) integration
  - Payment gateway for fee management
  - Library system integration
  - Biometric attendance system

- [ ] **Cloud and Mobile**
  - Web-based version
  - Mobile application (Android/iOS)
  - Cloud deployment options
  - Real-time synchronization

### **Technology Upgrades**
- **Migration to Spring Boot** for better enterprise features
- **REST API development** for mobile app integration
- **Microservices architecture** for better scalability
- **Docker containerization** for easier deployment

### **Contributing to Future Features**
If you're interested in working on any of these features:
1. **Create an issue** on GitHub describing the feature
2. **Discuss the implementation** with maintainers
3. **Follow the contribution guidelines** above
4. **Submit a pull request** with your implementation

## 📞 Support and Contact

### **Getting Help**
- **GitHub Issues**: [Create an issue](https://github.com/Kawchar-Ahammed/EduVault_Student_Management_System/issues) for bugs or feature requests
- **Discussions**: Use GitHub Discussions for questions and community support
- **Documentation**: Check this README and inline code documentation

### **Project Maintainer**
**Kawchar Ahammed**
- **GitHub**: [@Kawchar-Ahammed](https://github.com/Kawchar-Ahammed)
- **Email**: kawchar.cse@example.com
- **LinkedIn**: [Kawchar Ahammed](https://linkedin.com/in/kawchar-ahammed)

### **Community**
- **Discord**: [EduVault Community](https://discord.gg/eduvault) (Coming Soon)
- **Slack**: [EduVault Workspace](https://eduvault.slack.com) (Coming Soon)

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

### **What this means:**
- ✅ **Commercial use** - You can use this project for commercial purposes
- ✅ **Modification** - You can modify and adapt the code
- ✅ **Distribution** - You can distribute the original or modified code
- ✅ **Private use** - You can use this project privately
- ❗ **Liability** - The authors are not liable for any damages
- ❗ **Warranty** - No warranty is provided

## 🙏 Acknowledgments

- **JavaFX Community** for excellent documentation and examples
- **Microsoft** for SQL Server and comprehensive documentation
- **Open Source Contributors** for various libraries and tools used
- **Academic Institutions** for providing real-world requirements and feedback
- **Beta Testers** who helped identify and resolve issues

---

<div align="center">

### 🌟 **Star this repository if you find it helpful!** 🌟

**Built with ❤️ for the academic community**

*Last updated: September 2025*

</div>
#   E d u V a u l t _ S t u d e n t _ M a n a g e m e n t _ S y s t e m  
 #   E d u V a u l t _ S t u d e n t _ M a n a g e m e n t _ S y s t e m  
 