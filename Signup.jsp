
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="Signup.css">
    <title>Login</title>
</head>
<body>
    <div class="container" id="container">
        <div class="form-container sign-up-container">
            <form method="post">
            <div id="logo" class="logo"><img src="Screenshot_2023-02-25_at_10.44.16_PM-removebg-preview.png"/></div>
                <h1>Create Account</h1>
                <!-- <div class="social-container">
                    <a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
                    <a href="#" class="social"><i class="fab fa-google-plus-g"></i></a>
                    <a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
                </div> -->
                <!-- <span>or use your email for registration</span> -->
              <input id="signname" type="text" name="signname" placeholder="name" required autocomplete="off">
        <input id="signpass" type="password" name="signpass" placeholder="password" required autocomplete="off">
        <input id="signmail" type="email" name="signmail" placeholder="Email" required autocomplete="off">
                <button name="type" onclick="loginFun(this)" type="button" value="sign up">sign up</button>
                 <div id="signwrong">
        <%
        HttpSession mess = request.getSession();
        String signMessage = (String)mess.getAttribute("signmessage");
        if(signMessage!=null){
        	out.println(signMessage);
        }
        %></div>
            </form>
        </div>
        <div class="form-container sign-in-container">
         <form method="post">
                <div id="logo" class="logo"><img src="logo-removebg-preview (1).png"/></div>
                <h1>Sign in</h1>
                <!-- <div class="social-container">
                    <a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
                    <a href="#" class="social"><i class="fab fa-google-plus-g"></i></a>
                    <a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
                </div> -->
                <!-- <span>or use your account</span> -->
               <input id="logname" type="text" name="logname" placeholder="name" required autocomplete="off">
        		<input id="logpass" type="password" name="logpass" placeholder="password" required autocomplete="off">
        		<button name="type" onclick="loginFun(this);return false" type="button" value="login">login</button>
        		 <div id="logwrong">
        <%
        
        String logmessage = (String)mess.getAttribute("logmessage");
        if(logmessage!=null){
        	out.println(logmessage);
        }
        %>
        </div>
            </form>
        </div>
        <div class="overlay-container">
            <div class="overlay">
                <div class="overlay-panel overlay-left">
                    <h1>Welcome Back!</h1>
                    <p>To keep connected with us please login with your personal info</p>
                    <button class="ghost" id="signIn">Sign In</button>
                </div>
                <div class="overlay-panel overlay-right">
                    <h1>Hello, Friend!</h1>
                    <p>Enter your personal details and start journey with us</p>
                    <button class="ghost" id="signUp">Sign Up</button>
                </div>
            </div>
        </div>
    </div>
</body>
  <script>
  const signUpButton = document.getElementById('signUp');
  const signInButton = document.getElementById('signIn');
  const container = document.getElementById('container');

  signUpButton.addEventListener('click', () => {
  	container.classList.add("right-panel-active");
  });

  signInButton.addEventListener('click', () => {
  	container.classList.remove("right-panel-active");
  });

	let user = document.getElementById("username");
  document.cookie = "username=";

  function loginFun(ele) {
	let user1="";
	let pass="";
    const xhttp = new XMLHttpRequest();

     
     
      console.log("Hello")

 

      xhttp.onreadystatechange = function () {
                if (xhttp.readyState == 4) {
                    if (xhttp.status == 200) {
    	  var data = JSON.parse(xhttp.responseText);
          console.log(data)
          
          if (data["loginStatus"] == "success") {
           document.cookie = "username="+user1;
              location.href = "Mainwork.html";
              console.log("run");
          }
          else {
        	  location.href = "Signup.jsp";
          }
        }
    }
      }

      console.log(ele.value)
      if(ele.value=="sign up"){
    	  user1 = document.getElementById("signname").value;
          pass = document.getElementById("signpass").value; 
          let mail = document.getElementById("signmail").value;
          console.log("user"+user1);
          console.log("pass"+pass);
          console.log("mail"+mail);

          xhttp.open("POST", "Signpage?signname="+user1+"&signpass="+pass+"&type="+ele.value+"&signmail="+mail);
      }
      else{
    	  user1 = document.getElementById("logname").value;
          pass = document.getElementById("logpass").value;
          console.log("user"+user1);
          console.log("pass"+pass);
          xhttp.open("POST", "Signpage?logname="+user1+"&logpass="+pass+"&type="+ele.value);
      }

      xhttp.send();
      return false;

      return false;
  }

  </script>
</html>