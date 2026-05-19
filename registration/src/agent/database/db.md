CREATE TABLE roles (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE training_centers (
    id BIGINT PRIMARY KEY IDENTITY(1,1),

    code VARCHAR(20) UNIQUE NOT NULL,
    name NVARCHAR(255) NOT NULL,

    address NVARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),

    created_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY IDENTITY(1,1),

    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,

    full_name NVARCHAR(100) NOT NULL,

    gender VARCHAR(10)
        CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),

    date_of_birth DATE,

    role_id BIGINT NOT NULL,

    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_users_roles
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
);

CREATE TABLE students (
    id BIGINT PRIMARY KEY IDENTITY(1,1),

    user_id BIGINT UNIQUE NOT NULL,

    student_code VARCHAR(20) UNIQUE NOT NULL,

    major NVARCHAR(100),
    class_name VARCHAR(50),

    gpa DECIMAL(3,2),

    training_center_id BIGINT NOT NULL,

    CONSTRAINT FK_students_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT FK_students_training_centers
        FOREIGN KEY (training_center_id)
        REFERENCES training_centers(id)
);

CREATE TABLE lecturers (
    id BIGINT PRIMARY KEY IDENTITY(1,1),

    user_id BIGINT UNIQUE NOT NULL,

    lecturer_code VARCHAR(20) UNIQUE NOT NULL,

    department NVARCHAR(100),
    academic_rank NVARCHAR(100),

    training_center_id BIGINT NOT NULL,

    CONSTRAINT FK_lecturers_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT FK_lecturers_training_centers
        FOREIGN KEY (training_center_id)
        REFERENCES training_centers(id)
);

CREATE TABLE subjects (
    id BIGINT PRIMARY KEY IDENTITY(1,1),

    code VARCHAR(20) UNIQUE NOT NULL,
    name NVARCHAR(255) NOT NULL,

    credits INT NOT NULL,

    description NVARCHAR(MAX),

    created_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE course_sections (
    id BIGINT PRIMARY KEY IDENTITY(1,1),

    subject_id BIGINT NOT NULL,

    section_code VARCHAR(30) UNIQUE NOT NULL,

    semester VARCHAR(20) NOT NULL,
    academic_year VARCHAR(20) NOT NULL,

    max_students INT DEFAULT 0,

    training_center_id BIGINT,
    lecturer_id BIGINT,

    created_at DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_course_sections_subjects
        FOREIGN KEY (subject_id)
        REFERENCES subjects(id),

    CONSTRAINT FK_course_sections_training_centers
        FOREIGN KEY (training_center_id)
        REFERENCES training_centers(id),

    CONSTRAINT FK_course_sections_lecturers
        FOREIGN KEY (lecturer_id)
        REFERENCES lecturers(id)
);

CREATE TABLE admins (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    user_id BIGINT UNIQUE NOT NULL,
    training_center_id BIGINT NOT NULL,
    CONSTRAINT FK_admins_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT FK_admins_training_centers
        FOREIGN KEY (training_center_id)
        REFERENCES training_centers(id)
);

CREATE TABLE refresh_tokens (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    token VARCHAR(512) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    expires_at DATETIME NOT NULL,
    revoked BIT DEFAULT 0,
    CONSTRAINT FK_refresh_tokens_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE enrollments (
    student_id BIGINT,
    course_section_id BIGINT,

    enrolled_at DATETIME DEFAULT GETDATE(),

    PRIMARY KEY(student_id, course_section_id),

    CONSTRAINT FK_enrollments_students
        FOREIGN KEY (student_id)
        REFERENCES students(id),

    CONSTRAINT FK_enrollments_course_sections
        FOREIGN KEY (course_section_id)
        REFERENCES course_sections(id)
);


