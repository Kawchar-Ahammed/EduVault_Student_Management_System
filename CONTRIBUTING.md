# Contributing to EduVault Academic Management System

First off, thank you for considering contributing to EduVault! It's people like you that make EduVault such a great tool for academic management.

## Table of Contents
- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Process](#development-process)
- [Style Guidelines](#style-guidelines)
- [Pull Request Process](#pull-request-process)
- [Community](#community)

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code.

### Our Pledge
- **Be welcoming** to newcomers and experienced contributors alike
- **Be respectful** of differing viewpoints and experiences
- **Accept constructive criticism** gracefully
- **Focus on what is best** for the community and project
- **Show empathy** towards other community members

### Unacceptable Behavior
- Harassment, discrimination, or offensive comments
- Personal attacks or trolling
- Publishing others' private information without permission
- Any conduct that would be inappropriate in a professional setting

## Getting Started

### Prerequisites
Before you begin contributing, make sure you have:
- Java JDK 17 or higher installed
- JavaFX SDK 19 or higher
- Microsoft SQL Server (or SQL Server Express)
- Git for version control
- An IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

### Setting Up Your Development Environment

1. **Fork and Clone**
   ```bash
   # Fork the repository on GitHub, then:
   git clone https://github.com/YOUR_USERNAME/EduVault_Student_Management_System.git
   cd EduVault_Student_Management_System
   ```

2. **Set Up the Database**
   - Follow the database setup instructions in README.md
   - Create a test database for development

3. **Configure Your IDE**
   - Import the project
   - Add all JAR files from `lib/` to your classpath
   - Set the main class to `applications.Main`

4. **Test Your Setup**
   ```bash
   # Compile the project
   javac -cp "lib/*" -d bin src/**/*.java
   
   # Run the application
   java -cp "bin;lib/*" applications.Main
   ```

## How Can I Contribute?

### 🐛 Reporting Bugs

**Before Submitting a Bug Report:**
- Check the [existing issues](https://github.com/Kawchar-Ahammed/EduVault_Student_Management_System/issues)
- Try to reproduce the issue with the latest version
- Collect relevant information (OS, Java version, database version)

**Bug Report Template:**
```markdown
**Describe the Bug**
A clear and concise description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '...'
3. Scroll down to '...'
4. See error

**Expected Behavior**
A clear description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Environment:**
- OS: [e.g., Windows 10, macOS 12.0, Ubuntu 20.04]
- Java Version: [e.g., JDK 17.0.2]
- JavaFX Version: [e.g., 19.0.2]
- SQL Server Version: [e.g., SQL Server 2019]

**Additional Context**
Add any other context about the problem here.
```

### ✨ Suggesting Features

**Before Submitting a Feature Request:**
- Check if the feature already exists
- Look through existing feature requests
- Consider if this feature would benefit the broader community

**Feature Request Template:**
```markdown
**Is your feature request related to a problem?**
A clear description of what the problem is.

**Describe the solution you'd like**
A clear and concise description of what you want to happen.

**Describe alternatives you've considered**
Other solutions or features you've considered.

**Additional context**
Add any other context, mockups, or examples about the feature request.
```

### 💻 Code Contributions

We welcome code contributions! Here are some areas where you can help:

**Good First Issues:**
- UI improvements and bug fixes
- Documentation improvements
- Adding input validation
- Improving error messages
- Writing unit tests

**Medium Complexity:**
- New features (attendance tracking, reporting)
- Performance optimizations
- Security enhancements
- Database query optimizations

**Advanced Contributions:**
- Architecture improvements
- Integration with external systems
- Mobile app development
- Web-based version

## Development Process

### Branch Naming Conventions
- `feature/feature-name` - New features
- `bugfix/issue-description` - Bug fixes
- `hotfix/critical-issue` - Critical fixes
- `docs/documentation-update` - Documentation updates
- `refactor/component-name` - Code refactoring

### Commit Message Guidelines

**Format:**
```
type(scope): brief description

Detailed explanation (if needed)

- Additional notes
- Breaking changes
- Issue references
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code formatting (no logic changes)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

**Examples:**
```bash
feat(student): add course enrollment validation

- Check prerequisites before enrollment
- Validate course capacity limits
- Add user feedback for enrollment status

Closes #123
```

```bash
fix(database): resolve connection pool exhaustion

- Implement proper connection closing
- Add connection timeout configuration
- Update DAO classes to use try-with-resources

Fixes #456
```

## Style Guidelines

### Java Code Style

**Naming Conventions:**
```java
// Classes: PascalCase
public class StudentController { }

// Methods and variables: camelCase
public void enrollStudent() { }
private String studentName;

// Constants: UPPER_SNAKE_CASE
private static final String DEFAULT_PASSWORD = "student123";

// Packages: lowercase
package applications.controllers;
```

**Code Formatting:**
```java
// Indentation: 4 spaces (no tabs)
public class Example {
    private String name;
    
    public void method() {
        if (condition) {
            // Do something
        } else {
            // Do something else
        }
    }
}

// Line length: Maximum 120 characters
// Braces: Opening brace on same line
// Spacing: Space after keywords, around operators
```

**Documentation:**
```java
/**
 * Enrolls a student in a specific course
 * 
 * @param studentId The unique identifier for the student
 * @param courseId The unique identifier for the course
 * @return true if enrollment was successful, false otherwise
 * @throws SQLException if database operation fails
 * @throws IllegalArgumentException if parameters are invalid
 */
public boolean enrollStudent(String studentId, String courseId) 
        throws SQLException, IllegalArgumentException {
    // Implementation
}
```

### Database Guidelines

**SQL Style:**
```sql
-- Use uppercase for SQL keywords
SELECT s.student_id, s.full_name, c.course_name
FROM Students s
INNER JOIN Enrollments e ON s.student_id = e.student_id
INNER JOIN Courses c ON e.course_id = c.course_id
WHERE s.is_active = 1
ORDER BY s.full_name;
```

**Prepared Statements:**
```java
// Always use prepared statements
String query = "SELECT * FROM Students WHERE student_id = ? AND semester = ?";
try (PreparedStatement stmt = connection.prepareStatement(query)) {
    stmt.setString(1, studentId);
    stmt.setInt(2, semester);
    // Execute query
}
```

### UI Guidelines

**JavaFX Best Practices:**
```java
// Use FXML for complex layouts
// Keep controllers focused on logic, not UI setup
// Use CSS for styling instead of inline styles
// Implement proper event handling

// Example controller
@FXML
private TextField usernameField;

@FXML
private void handleLogin(ActionEvent event) {
    String username = usernameField.getText().trim();
    if (username.isEmpty()) {
        showError("Username is required");
        return;
    }
    // Handle login logic
}
```

## Pull Request Process

### Before Submitting

1. **Test Your Changes**
   ```bash
   # Compile and run
   javac -cp "lib/*" -d bin src/**/*.java
   java -cp "bin;lib/*" applications.Main
   
   # Test all affected functionality
   # Run any existing unit tests
   ```

2. **Update Documentation**
   - Update README.md if needed
   - Add or update code comments
   - Update CHANGELOG.md

3. **Check Code Quality**
   - Follow the style guidelines above
   - Remove any debug code or temporary changes
   - Ensure no merge conflicts

### Pull Request Template

```markdown
## Description
Brief description of what this PR does and why.

## Type of Change
- [ ] Bug fix (non-breaking change that fixes an issue)
- [ ] New feature (non-breaking change that adds functionality)
- [ ] Breaking change (fix or feature that causes existing functionality to change)
- [ ] Documentation update
- [ ] Performance improvement
- [ ] Code refactoring

## How Has This Been Tested?
Describe the tests you ran and provide instructions to reproduce.

## Screenshots (if applicable)
Add screenshots of any UI changes.

## Checklist
- [ ] My code follows the project's style guidelines
- [ ] I have performed a self-review of my code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
- [ ] Any dependent changes have been merged and published

## Related Issues
Closes #(issue number)
```

### Review Process

1. **Automated Checks**
   - Code compiles successfully
   - No obvious security issues
   - Follows basic style guidelines

2. **Manual Review**
   - Code quality and readability
   - Functionality and correctness
   - Performance implications
   - Security considerations

3. **Testing**
   - Reviewer tests the changes locally
   - Verifies the fix/feature works as expected
   - Checks for any regressions

4. **Approval and Merge**
   - At least one maintainer approval required
   - All conversations resolved
   - CI checks pass (when implemented)

## Community

### Communication Channels

- **GitHub Issues**: For bug reports and feature requests
- **GitHub Discussions**: For questions and general discussion
- **Pull Requests**: For code review and collaboration

### Getting Help

**For Contributors:**
- Check the README.md for setup instructions
- Look through existing issues and PRs
- Ask questions in GitHub Discussions
- Reach out to maintainers via GitHub

**For Users:**
- Create an issue for bugs or problems
- Use GitHub Discussions for general questions
- Check the documentation first

### Recognition

Contributors will be recognized in:
- CHANGELOG.md for significant contributions
- README.md contributors section
- GitHub contributor statistics
- Special mentions in release notes

### Mentorship

**New to Open Source?**
- Look for issues labeled "good first issue"
- Don't hesitate to ask questions
- Start with documentation or simple bug fixes
- Join our community discussions

**Experienced Contributors:**
- Help mentor newcomers
- Review pull requests
- Suggest improvements to processes
- Share knowledge in discussions

## Thank You!

Your contributions make EduVault better for everyone. Whether you're fixing bugs, adding features, improving documentation, or helping other users, every contribution is valuable and appreciated.

Happy coding! 🎓✨
