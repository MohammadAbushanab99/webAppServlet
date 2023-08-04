<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="webapp.GradingDAO" %>

<html>

<head>
<title>Instructor Page</title>
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
        <p><strong>Instructor ID: ${instructorId}</strong></p>
        <p><strong>Name: ${instructorName}</strong></p>
    </div>
    <div class="container">
        <h2>Select an option:</h2>
        <ul class="options">
            <li><a href="#" class="option-btn" onclick="showContent('courseInfoContainer')">See My Courses Information</a></li>
            <li><a href="#" class="option-btn" onclick="showContent('gradesAndStudentsContainer')">See Grades and My Students Information</a></li>
            <!-- Add other options as needed -->
        </ul>
        <a href="logout" class="logout-btn">Log Out</a>
        <div id="courseInfoContainer" class="result-container" style="display: none;">
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
        </div>
        <div id="gradesAndStudentsContainer" class="result-container" style="display: block;">
            <!-- Show grades and students information here -->
            <h3>Select a Course:</h3>
             <form id="saveRemarksForm" method="post" action="/instructor">
            <form id="gradesAndStudentsForm">
                <select name="selectedCourseId" id="courseSelect">
                    <c:forEach items="${courseList}" var="course">
                        <option value="${course.getId()}">${course.getCourseName()}</option>
                    </c:forEach>
                </select>
                <input type="submit" value="Show Course Grades and Students Information">
            </form>

            <h3>Selected Course Information:</h3>
            <p><strong>Course ID:</strong> ${selectedCourseId}</p>
            <p><strong>Course Name:</strong> ${selectedCourseName}</p>
            <h3>Students and Remarks:</h3>
            <table>
                <tr>
                    <th>Student ID</th>
                    <th>Student Name</th>
                    <th>${remarks1}</th>
                    <th>${remarks2}</th>
                    <th>Final Grade</th>
                    <th>Total Grade</th>
                </tr>
                     <form id="saveRemarksForm" method="post" action="/instructor">
                <c:forEach items="${studentsList}" var="student">
                    <tr>
                        <td>${student.getStudentId()}</td>
                        <td>${student.getName()}</td>
                        <td>
                            <c:choose>
                                <c:when test="${selectedCourse.getExamType() == 1}">
                                    <input type="text" id="midExam_${student.getStudentId()}" name="midExam_${student.getStudentId()}" value="${student.getMidExam()}">
                                </c:when>
                                <c:otherwise>
                                    <input type="text" id="firstExam_${student.getStudentId()}" value="${student.getFirstExam()}">
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${selectedCourse.getExamType() == 1}">
                                    <input type="text" id="quizzes_${student.getStudentId()}" value="${student.getQuizzes()}">
                                </c:when>
                                <c:otherwise>
                                    <input type="text" id="secondExam_${student.getStudentId()}" value="${student.getSecondExam()}">
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <input type="text" id="finalExam_${student.getStudentId()}" value="${student.getFinalExam()}">
                        </td>
                        <td>
                            <input type="text" id="totalGrade_${student.getStudentId()}" value="${student.getTotalGrade()}">
                        </td>
                        <td>


                             <input type="hidden" name="selectedStudentId" value="${student.getStudentId()}">
                             <button type="submit" onclick="saveRemarks('${student.getStudentId()}')">Save Remarks</button>
                              </form>

                        </td>
                    </tr>

                </c:forEach>
            </table>
        </div>
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


       async function saveRemarks(studentId) {

        console.log("Student ID:", studentId);

             inputPrefix = "midExam_";
            const midExamInputId = inputPrefix + studentId;
            const element = document.getElementById(midExamInputId);
            if (element) {
             console.log("yese");            }
             else {
               console.log("no");
            }
            console.log("Student ID:", midExamInputId);
                const midExam = document.getElementById(midExamInputId).value;
                  inputPrefix = "quizzes_";
                 const quizExamInputId = inputPrefix + studentId;
                const quizzes = document.getElementById(quizExamInputId).value;
               inputPrefix = "firstExam_";
               const firstExamInputId = inputPrefix + studentId;
               const firstExamElement = document.getElementById(firstExamInputId);
               const firstExam = firstExamElement ? firstExamElement.value : 0;
                  inputPrefix = "secondExam_";
                  const secondExamInputId = inputPrefix + studentId;
                   const secondExamElement = document.getElementById(firstExamInputId);
                  const secondExam = secondExamElement ? secondExamElement.value : 0;
                  inputPrefix = "finalExam_";
                  const finalInputId = inputPrefix + studentId;
                const finalExam = document.getElementById(finalInputId).value;
                  inputPrefix = "totalGrade_";
                  const totalInputId = inputPrefix + studentId;
                const totalGrade = document.getElementById(totalInputId).value;
                        const data = {
                            selectedStudentId: studentId,
                            midExam: midExam,
                            quizzes: quizzes,
                            firstExam: firstExam,
                            secondExam: secondExam,
                            finalExam: finalExam,
                            totalGrade: totalGrade,
                        };
                                            console.log(data);
                  // Send the data to the server using AJAX with POST method
                  fetch("/instructor", {
                      method: "POST",
                      headers: {
                          "Content-Type": "application/json"
                      },
                      body: JSON.stringify(data)
                  })
                  .then(response => response.json())
                  .then(result => {
                      if (result.success) {
                          alert("Remarks saved successfully!");
                      } else {
                          alert("Error while saving remarks.");
                      }
                  })
                  .catch(error => {
                      console.error("Error:", error);
                      alert("An error occurred while processing the request.");
                  });
            }


    </script>
</body>
</html>