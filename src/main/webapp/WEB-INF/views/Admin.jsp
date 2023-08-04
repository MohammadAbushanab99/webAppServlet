<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="webapp.GradingDAO" %>

<html>

<head>
<title>Admin Page</title>
</head>
  <style>
      @import url(https://fonts.googleapis.com/css?family=Roboto:300);
     body, h1, h2, p, ul, li, button {
         margin: 0;
         padding: 0;
         box-sizing: border-box;
     }

     body {
         font-family: Arial, sans-serif;
         background-color: #f0f0f0;
     }

     .header {
         background-color: #007BFF;
         color: #fff;
         padding: 10px;
         text-align: center;
     }

     .header h1 {
         font-size: 24px;
         margin: 0;
     }

     .logout-btn {
         color: #fff;
         text-decoration: none;
         font-size: 16px;
         margin-right: 20px;
         margin-top: 20px;
         float: right;
     }

     .info-container {
         background-color: #fff;
         padding: 20px;
         margin: 20px;
         border-radius: 5px;
         box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
     }

     .options {
         list-style: none;
         padding: 0;
         margin: 20px;
     }

     .option-btn {
         background-color: #007BFF;
         color: #fff;
         border: none;
         padding: 10px 20px;
         border-radius: 5px;
         cursor: pointer;
         margin-bottom: 10px;
     }

     .option-btn:hover {
         background-color: #0056b3;
     }

     .result-container {
         background-color: #fff;
         padding: 20px;
         margin: 20px;
         border-radius: 5px;
         box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
     }

     .result-container p {
         margin-bottom: 10px;
     }

     .result-container table {
         border-collapse: collapse;
         width: 100%;
     }

     .result-container th, .result-container td {
         border: 1px solid #ddd;
         padding: 8px;
         text-align: left;
     }

     .result-container th {
         background-color: #f2f2f2;
     }

     .result-container tr:nth-child(even) {
         background-color: #f2f2f2;
     }

     .result-container tr:hover {
         background-color: #ddd;
     }

        .logout-btn {
            background-color: #dc3545;
            color: #fff;
            border: none;
            padding: 10px 20px;
            cursor: pointer;
            position: absolute;
            top: 10px;
            right: 10px;
        }

        .logout-btn:hover {
            background-color: #555;
        }
      body {
        background: #76b852; /* fallback for old browsers */
        background: rgb(141,194,111);
        background: linear-gradient(90deg, rgba(141,194,111,1) 0%, rgba(118,184,82,1) 50%);
        font-family: "Roboto", sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
      }
    </style>
 <div class="info-container">
        <p><strong>Admin</strong></p>
        <p><strong>Admin ID: ${Admin}</strong></p>

    </div>
    <div class="container">
        <h2>Select an option:</h2>
        <ul class="options">
            <li><a href="#" class="option-btn" onclick="showContent('AddInstructor')">Add Instructor</a></li>
            <li><a href="#" class="option-btn" onclick="showContent('AddCourse')">Add Course</a></li>
             <li><a href="#" class="option-btn" onclick="showContent('AddStudent')">Add Student</a></li>
        </ul>
        <a href="logout" class="logout-btn">Log Out</a>


        <div id="AddInstructor" class="result-container" style="display: none;">
        <h2>Add New Instructor</h2>
        <form action="/Admin" method="post">
        <p>Instructor Name: <input type="text" id="Instructor" name="InstructorName"></p>
        <button type="submit">Submit</button>
        </div>

        <div id="AddCourse" class="result-container" style="display: none;">
        <div id="InstructorList">
             <c:forEach items="${InstructorList}" var="instructorEntry">
                 <c:set var="instructorId" value="${instructorEntry.key}" />
                 <c:set var="instructorName" value="${instructorEntry.value}" />

                 <p>Instructor ID: ${instructorId}</p>
                 <p>Instructor Name: ${instructorName}</p>
                 <hr>
             </c:forEach>


         </div>
                <form action="/Admin" method="post">

                 <p>Course Name : <input type="text" id="Course1" name="CourseName" ></p>
                 <p>Instructor Id : <input type="text" id="Instructor1" name="InstructorIdCourse" ></p>
                 <button type="submit">Submit</button>
                 </form>
        </div>
        <div id="AddStudent" class="result-container" style="display: none;">
            <!-- Show course information here -->
            <h2>Course Information</h2>
            <div id="courseList">
                <c:forEach items="${courseList}" var="course">
                    <p>Course ID: ${course.getId()}</p>
                    <p>Course Name: ${course.getCourseName()}</p>
                    <p>Number of Students: ${course.getNumberOfStudents()}</p>
                    <hr>
                </c:forEach>
            </div>
             <form action="/Admin" method="post">
            <p> Student Name: <input type="text" id="Student" name="StudentName" ></p>
            <p> Student major: <input type="text" id="Student" name="StudentMajor" ></p>
              <p> Course ID: <input type="text" id="Course2" name="selectedCourseId" ></p>
              <button type="submit">Submit</button>
                </form>
        </div>


         <script>
                function showContent(contentId) {
                    const allContainers = document.getElementsByClassName('result-container');
                    for (let i = 0; i < allContainers.length; i++) {
                        allContainers[i].style.display = 'none';
                    }
                    const containerToShow = document.getElementById(contentId);
                    containerToShow.style.display = 'block';
                }

         </script>

</body>
</html>