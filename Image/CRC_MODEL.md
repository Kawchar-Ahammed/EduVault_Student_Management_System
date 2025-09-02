# **EduVault Academic Management System**
## **CRC (Class-Responsibility-Collaboration) Model**

---

## **CRC Cards Overview**

CRC (Class-Responsibility-Collaboration) modeling helps identify the classes, their responsibilities, and how they collaborate with other classes in the EduVault system.

---

## **1. Model Classes**

### **User Class**
```
┌─────────────────────────────────────────────────────────┐
│                         User                            │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Store user authentication credentials                 │
│ • Manage user profile information                       │
│ • Validate login credentials                            │
│ • Track user activity and sessions                      │
│ • Enforce role-based permissions                        │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • Student (inheritance)                                 │
│ • Faculty (inheritance)                                 │
│ • AuthenticationService (validation)                    │
│ • UserDAO (data persistence)                            │
│ • AuditLog (activity tracking)                          │
└─────────────────────────────────────────────────────────┘
```

### **Student Class**
```
┌─────────────────────────────────────────────────────────┐
│                       Student                           │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Maintain academic profile and records                 │
│ • Calculate and update CGPA                             │
│ • Manage course enrollments                             │
│ • Track academic progress                               │
│ • Handle profile updates                                │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • User (parent class)                                   │
│ • Enrollment (course registration)                      │
│ • Assessment (grade records)                            │
│ • Attendance (class attendance)                         │
│ • StudentDAO (data operations)                          │
│ • GradeCalculationService (CGPA calculation)            │
└─────────────────────────────────────────────────────────┘
```

### **Faculty Class**
```
┌─────────────────────────────────────────────────────────┐
│                       Faculty                           │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Manage assigned courses                               │
│ • Enter and grade student assessments                   │
│ • Mark student attendance                               │
│ • Generate course reports                               │
│ • Communicate with students                             │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • User (parent class)                                   │
│ • Course (teaching assignment)                          │
│ • Assessment (grading)                                  │
│ • Attendance (marking)                                  │
│ • FacultyDAO (data operations)                          │
│ • EmailService (communication)                          │
└─────────────────────────────────────────────────────────┘
```

### **Course Class**
```
┌─────────────────────────────────────────────────────────┐
│                        Course                           │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Store course information and metadata                 │
│ • Manage student enrollments                            │
│ • Track enrollment capacity                             │
│ • Maintain course statistics                            │
│ • Validate enrollment eligibility                       │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • Faculty (instructor assignment)                       │
│ • Student (through Enrollment)                          │
│ • Enrollment (registration management)                  │
│ • Assessment (course assessments)                       │
│ • CourseDAO (data persistence)                          │
│ • ReportService (statistics)                            │
└─────────────────────────────────────────────────────────┘
```

### **Assessment Class**
```
┌─────────────────────────────────────────────────────────┐
│                      Assessment                         │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Store individual assessment data                      │
│ • Calculate grade percentages                           │
│ • Determine letter grades                               │
│ • Validate grade entries                                │
│ • Track assessment history                              │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • Student (assessment owner)                            │
│ • Course (assessment belongs to)                        │
│ • Faculty (grading faculty)                             │
│ • GradeCalculationService (calculations)                │
│ • AssessmentDAO (data operations)                       │
│ • EmailService (grade notifications)                    │
└─────────────────────────────────────────────────────────┘
```

---

## **2. Controller Classes**

### **AdminController Class**
```
┌─────────────────────────────────────────────────────────┐
│                   AdminController                       │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Handle administrative operations                      │
│ • Manage user accounts (create/update/delete)           │
│ • Oversee system configuration                          │
│ • Generate administrative reports                       │
│ • Monitor system performance                            │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • UserDAO (user management)                             │
│ • StudentDAO (student administration)                   │
│ • FacultyDAO (faculty management)                       │
│ • CourseDAO (course administration)                     │
│ • ReportService (report generation)                     │
│ • SystemSettings (configuration)                        │
└─────────────────────────────────────────────────────────┘
```

### **FacultyController Class**
```
┌─────────────────────────────────────────────────────────┐
│                  FacultyController                      │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Handle faculty-specific operations                    │
│ • Manage course content and grading                     │
│ • Process attendance marking                            │
│ • Generate grade reports                                │
│ • Facilitate student communication                      │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • Faculty (faculty profile)                             │
│ • CourseDAO (course operations)                         │
│ • AssessmentDAO (grading operations)                    │
│ • AttendanceDAO (attendance operations)                 │
│ • EmailService (notifications)                          │
│ • GradeCalculationService (grade calculations)          │
└─────────────────────────────────────────────────────────┘
```

### **StudentController Class**
```
┌─────────────────────────────────────────────────────────┐
│                  StudentController                      │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Handle student-specific operations                    │
│ • Manage profile updates                                │
│ • Display academic records                              │
│ • Show grade and attendance information                 │
│ • Generate student reports                              │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • Student (student profile)                             │
│ • StudentDAO (profile operations)                       │
│ • EnrollmentDAO (enrollment records)                    │
│ • AssessmentDAO (grade records)                         │
│ • AttendanceDAO (attendance records)                    │
│ • ReportService (transcript generation)                 │
└─────────────────────────────────────────────────────────┘
```

---

## **3. Data Access Object (DAO) Classes**

### **UserDAO Class**
```
┌─────────────────────────────────────────────────────────┐
│                       UserDAO                           │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Perform CRUD operations on User data                  │
│ • Handle user authentication queries                    │
│ • Manage user role assignments                          │
│ • Validate user credentials                             │
│ • Track user activity logs                              │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • DatabaseConnection (database access)                  │
│ • User (data model)                                     │
│ • AuthenticationService (credential validation)         │
│ • AdminController (user management)                     │
│ • AuditLog (activity tracking)                          │
└─────────────────────────────────────────────────────────┘
```

### **StudentDAO Class**
```
┌─────────────────────────────────────────────────────────┐
│                     StudentDAO                          │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Perform CRUD operations on Student data               │
│ • Calculate and update CGPA                             │
│ • Manage academic records                               │
│ • Handle bulk student operations                        │
│ • Generate student statistics                           │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • DatabaseConnection (database access)                  │
│ • Student (data model)                                  │
│ • AdminController (student management)                  │
│ • StudentController (profile operations)                │
│ • GradeCalculationService (CGPA calculation)            │
│ • ReportService (student reports)                       │
└─────────────────────────────────────────────────────────┘
```

### **AssessmentDAO Class**
```
┌─────────────────────────────────────────────────────────┐
│                   AssessmentDAO                         │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Perform CRUD operations on Assessment data            │
│ • Calculate weighted final grades                       │
│ • Manage grade publication status                       │
│ • Generate grade statistics                             │
│ • Handle bulk grade operations                          │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • DatabaseConnection (database access)                  │
│ • Assessment (data model)                               │
│ • FacultyController (grade entry)                       │
│ • StudentController (grade viewing)                     │
│ • GradeCalculationService (calculations)                │
│ • EmailService (grade notifications)                    │
└─────────────────────────────────────────────────────────┘
```

---

## **4. Service Classes**

### **AuthenticationService Class**
```
┌─────────────────────────────────────────────────────────┐
│                AuthenticationService                    │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Authenticate user login credentials                   │
│ • Authorize user operations                             │
│ • Manage user sessions                                  │
│ • Handle password operations                            │
│ • Enforce security policies                             │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • UserDAO (credential validation)                       │
│ • User (authentication data)                            │
│ • SessionManager (session handling)                     │
│ • PasswordSecurity (password operations)                │
│ • AuditLog (security events)                            │
└─────────────────────────────────────────────────────────┘
```

### **GradeCalculationService Class**
```
┌─────────────────────────────────────────────────────────┐
│              GradeCalculationService                    │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Calculate weighted grade averages                     │
│ • Determine letter grades                               │
│ • Compute GPA and CGPA                                  │
│ • Validate grade entries                                │
│ • Apply grading policies                                │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • Assessment (grade data)                               │
│ • Student (CGPA calculation)                            │
│ • AssessmentDAO (grade operations)                      │
│ • GradeScale (grading standards)                        │
│ • Course (weightage configurations)                     │
└─────────────────────────────────────────────────────────┘
```

### **EmailService Class**
```
┌─────────────────────────────────────────────────────────┐
│                    EmailService                         │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Send email notifications                              │
│ • Generate email templates                              │
│ • Handle bulk email operations                          │
│ • Track email delivery status                           │
│ • Manage email configurations                           │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • Student (recipient information)                       │
│ • Faculty (sender information)                          │
│ • Assessment (grade notifications)                      │
│ • NotificationService (message queuing)                 │
│ • SystemSettings (email configuration)                  │
└─────────────────────────────────────────────────────────┘
```

### **ReportService Class**
```
┌─────────────────────────────────────────────────────────┐
│                   ReportService                         │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Generate academic transcripts                         │
│ • Create performance reports                            │
│ • Produce attendance summaries                          │
│ • Generate statistical analytics                        │
│ • Export reports in various formats                     │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • StudentDAO (student data)                             │
│ • CourseDAO (course data)                               │
│ • AssessmentDAO (grade data)                            │
│ • AttendanceDAO (attendance data)                       │
│ • Student (profile information)                         │
│ • Course (course details)                               │
└─────────────────────────────────────────────────────────┘
```

---

## **5. Utility Classes**

### **DatabaseConnection Class**
```
┌─────────────────────────────────────────────────────────┐
│                 DatabaseConnection                      │
├─────────────────────────────────────────────────────────┤
│ RESPONSIBILITIES:                                       │
│ • Manage database connections                           │
│ • Handle transaction management                         │
│ • Provide connection pooling                            │
│ • Ensure connection security                            │
│ • Monitor connection performance                        │
├─────────────────────────────────────────────────────────┤
│ COLLABORATIONS:                                         │
│ • All DAO classes (database access)                     │
│ • SystemSettings (connection configuration)             │
│ • AuditLog (connection monitoring)                      │
│ • SecurityManager (connection security)                 │
└─────────────────────────────────────────────────────────┘
```

---

## **CRC Model Summary**

### **Key Design Patterns Identified**
1. **Model-View-Controller (MVC)**: Clear separation between model, controller, and view layers
2. **Data Access Object (DAO)**: Abstraction of data persistence operations
3. **Service Layer**: Business logic encapsulation
4. **Singleton Pattern**: DatabaseConnection class ensures single connection instance
5. **Observer Pattern**: Notification system for grade updates

### **Primary Collaborations**
- **Controllers ↔ DAOs**: Controllers delegate data operations to DAOs
- **Services ↔ Models**: Services perform business logic operations on models
- **DAOs ↔ DatabaseConnection**: All data access goes through database connection
- **Models ↔ Services**: Models use services for complex calculations
- **Controllers ↔ Services**: Controllers use services for business operations

### **Responsibility Distribution**
- **Models**: Data representation and basic business rules
- **Controllers**: User interface logic and request handling
- **DAOs**: Data persistence and retrieval
- **Services**: Business logic and complex operations
- **Utilities**: Common functionality and system resources

This CRC model provides a clear understanding of class responsibilities and collaborations in the EduVault system, facilitating effective object-oriented design and implementation.
