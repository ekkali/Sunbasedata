<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en" class="h-100">

<head>
   <title>Login</title>

   <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
         integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
           integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
           crossorigin="anonymous"></script>
</head>
<body class="h-100">
<div class="container h-100">
   <div class="row h-100 justify-content-center align-items-center">
      <div class="col-md-4">
         <form id="loginForm" action="/process-login" method="post">
            <h2 class="mb-4">Login</h2>
            <div class="form-group">
               <label for="username">Email:</label>
               <input type="text" class="form-control" id="username" name="username" required>
            </div>
            <div class="form-group">
               <label for="password">Password:</label>
               <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Login</button>
            <a href="/signup">Sign Up</a>
         </form>
      </div>
   </div>
</div>
</body>

</html>

