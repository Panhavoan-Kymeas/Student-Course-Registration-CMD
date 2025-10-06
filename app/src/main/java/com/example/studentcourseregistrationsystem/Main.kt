package com.example.studentcourseregistrationsystem

import com.example.studentcourseregistrationsystem.models.Student
import com.example.studentcourseregistrationsystem.models.Course
import com.example.studentcourseregistrationsystem.models.Enrollment

fun main() {
    val studentList = mutableListOf<Student>()
    val courseList = mutableListOf<Course>()
    val enrollmentList = mutableListOf<Enrollment>()

    while (true) {
        println("=== Student Course Registration System ===")
        println("1. Register new student")
        println("2. Register new course")
        println("3. Enroll student in course")
        println("4. View each course students")
        println("5. View all students")
        println("6. View all courses")
        println("7. Exit")
        println()
        println("HOW TO USE:")
        println("- Type a number (1-7) to select an option")
        println("- Press enter to confirm your choice")
        println("- Type 7 or 'exit' to quit the application")
        println()

        print("Your Selection: ")
        val input = readLine()?.trim()

        if (input.equals("exit", ignoreCase = true)) {
            println("Exiting the program. Goodbye!")
            break
        }

        val userOption = input?.toIntOrNull()
        if (userOption == null) {
            println("Invalid input. Please enter a number (1-7) or type 'exit'.")
            continue
        }

        when (userOption) {
            1 -> registerStudent(studentList)
            2 -> registerCourse(courseList)
            3 -> enrollStudent(studentList, courseList, enrollmentList)
            4 -> viewCourseStudents(studentList, courseList, enrollmentList)
            5 -> printStudents(studentList)
            6 -> printCourses(courseList)
            7 -> {
                println("Exiting the program. Goodbye!")
                break
            }
            else -> println("Invalid option. Please select a number from 1 to 7.")
        }
    }
}
fun registerStudent(studentList: MutableList<Student>) {
    val name = readRequiredField("Please enter student name: ")
    print("Please enter student email (optional): ")
    val email = readLine()
    val major = readRequiredField("Please enter student major: ")
    val studentID = generateStudentID(studentList)

    val newStudent = Student(studentID, name, email, major)
    studentList.add(newStudent)
    println("Student registered successfully\n")
}

fun registerCourse(courseList: MutableList<Course>) {
    val courseID = readRequiredField("Please enter course unique identifier: ")
    val courseName = readRequiredField("Please enter course name: ")
    val credits = readRequiredField("Please enter course credits: ").toInt()

    val newCourse = Course(courseID, courseName, credits)
    courseList.add(newCourse)
    println("Course registered successfully\n")
}

fun enrollStudent(studentList: List<Student>, courseList: List<Course>, enrollmentList: MutableList<Enrollment>) {
    var studentID: String
    var courseID: String

    do {
        studentID = readRequiredField("Please enter the student id to enroll: ")
        if (studentList.none { it.id == studentID }) println("Student ID not found. Try again.")
    } while (studentList.none { it.id == studentID })

    do {
        courseID = readRequiredField("Please enter course id to enroll: ")
        if (courseList.none { it.courseID == courseID }) println("Course ID not found. Try again.")
    } while (courseList.none { it.courseID == courseID })

    if (enrollmentList.any { it.studentID == studentID && it.courseID == courseID }) {
        println("Student is already enrolled in this course.")
        return
    }

    enrollmentList.add(Enrollment(studentID, courseID))
    val student = studentList.find { it.id == studentID }!!
    val course = courseList.find { it.courseID == courseID }!!
    println("Enrolled ${student.name} in ${course.courseName} successfully!\n")
}

fun viewCourseStudents(studentList: List<Student>, courseList: List<Course>, enrollmentList: List<Enrollment>) {
    println("== Course List ==")
    courseList.forEach { println("CourseID: ${it.courseID}, CourseName: ${it.courseName}") }

    var courseID: String
    do {
        courseID = readRequiredField("Please enter course id to view course students: ")
        if (courseList.none { it.courseID == courseID }) println("Course ID not found. Try again.")
    } while (courseList.none { it.courseID == courseID })

    val enrolledStudents = enrollmentList
        .filter { it.courseID == courseID }
        .mapNotNull { enrollment -> studentList.find { it.id == enrollment.studentID } }

    if (enrolledStudents.isEmpty()) {
        println("No students are enrolled in this course.\n")
        return
    }

    println("Students enrolled in ${courseList.find { it.courseID == courseID }!!.courseName}:")
    println("%-10s %-20s %-25s %-15s".format("ID", "Name", "Email", "Major"))
    println("=".repeat(70))
    enrolledStudents.forEach {
        println("%-10s %-20s %-25s %-15s".format(it.id, it.name, it.email ?: "-", it.major))
    }
    println()
}

fun printStudents(studentList: List<Student>) {
    println("== Student List ==")
    println("%-10s %-20s %-25s %-15s".format("ID", "Name", "Email", "Major"))
    println("=".repeat(70))
    studentList.forEach {
        println("%-10s %-20s %-25s %-15s".format(it.id, it.name, it.email ?: "-", it.major))
    }
    println()
}

fun printCourses(courseList: List<Course>) {
    println("== Course List ==")
    println("%-10s %-30s %-10s".format("ID", "Course Name", "Credits"))
    println("=".repeat(55))
    courseList.forEach {
        println("%-10s %-30s %-10d".format(it.courseID, it.courseName, it.credits))
    }
    println()
}

fun generateStudentID(studentList: List<Student>): String {
    val nextId = studentList.size + 1
    return "ST%03d".format(nextId)
}

fun readRequiredField(prompt: String): String {
    var input: String?
    do {
        print(prompt)
        input = readLine()
        if (input.isNullOrBlank()) println("This field cannot be empty. Please try again.")
    } while (input.isNullOrBlank())
    return input
}
