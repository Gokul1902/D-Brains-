 <!-- <!DOCTYPE html> -->
<html lang="en">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>Sign Up Form by Colorlib</title>

	<!-- Font Icon -->
	<link rel="stylesheet" href="fonts/material-icon/css/material-design-iconic-font.min.css">

	<!-- Main css -->
	<link rel="stylesheet" href="login.css">
</head>
<header>
	
	<div class="label">
<img src="logo-removebg-preview (1).png"/>	</div>
<a href="Instruction.html">Instructions</a>
</header>

<body>

	<div class="main">
		<section class="sign-in">
			<div class="container">
				<div class="signin-content">
					<div class="signin-image">
						<figure>
							<img src="Computer_login-pana.svg" alt="sing up image">
						</figure>
					</div>

					<div class="signin-form">
					
						<h2 class="form-title">Mysql Login</h2>
						<form method="post" action="checker" class="register-form" id="login-form">
							<div class="form-group">
								<label for="username"><i class="zmdi zmdi-account material-icons-name"></i></label>
								<input type="text" name="username" id="username" placeholder="Username" />
							</div>
							<div class="form-group">
								<label for="password"><i class="zmdi zmdi-lock"></i></label> <input type="password"
									name="password" id="password" placeholder="Password" />
							</div>
							<div class="form-group form-button">
								<input type="submit" name="signin" id="signin" class="form-submit" value="Log in" />
								
							</div>
							
						</form>

					</div>
				</div>
			</div>
		</section>

	</div>

	<!-- JS -->
	<script src="vendor/jquery/jquery.min.js"></script>
</body>


</html> 