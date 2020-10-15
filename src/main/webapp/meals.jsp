<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        .three {
            border-radius: 10px;
            color: #36454A;
            background: -webkit-linear-gradient(top, #A4D3E0, #A4D3E0 50%, #CBE3EB 50%);
            background: -o-linear-gradient(top, #A4D3E0, #A4D3E0 50%, #CBE3EB 50%);
            background: linear-gradient(to top, #A4D3E0, #A4D3E0 50%, #CBE3EB 50%);
            box-shadow: 2px 2px 3px black;
        }
        th {
            font-weight: normal;
            padding: 7px 10px;
        }
        td {
            border-top: 1px solid #FDFFE4;
            padding: 7px 10px;
        }
        tr:nth-child(2n) {
            background: #D7DCE1;
        }
        table {
            font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif, serif;
            font-size: 14px;
            max-width: 70%;
            width: 70%;
            border-collapse: collapse;
            text-align: left;
        }
        .edit:before   { content: "\270E"; }
        .delete:before { content: "\2718";}
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<div>
    <table>
        <thead>
        <tr>
            <%--<th>ID</th>--%>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="mealsTo" scope="request" type="java.util.Collection"/>
        <c:forEach var="mealTo" items = "${requestScope.mealsTo}">
            <tr style="color:  ${mealTo.excess ?  '#ff3e25' : '#33bf49 '}">
                    <%--<td>${meal.getId()}</td>--%>
                <td>
                    <javatime:parseLocalDateTime value="${mealTo.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsed"/>
                    <javatime:format value="${parsed}" style="FS"/>
                </td>

                <td>${mealTo.description}</td>
                <td>${mealTo.calories}</td>

                <td><a class="edit" href="meals?action=edit&id=${mealTo.id}">Edit</a></td>
                <td><a class="delete" href="meals?action=delete&id=${mealTo.id}">Delete</a></td>
            </tr>

        </c:forEach>


        </tbody>
    </table>
</div>

<p><a class="three" href="meals?action=add">Add Meal</a></p>

</body>
</html>