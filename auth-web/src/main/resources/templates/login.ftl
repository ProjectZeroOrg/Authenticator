<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Login</title>
	<link rel="stylesheet" type="text/css"
		  href="webjars/bootstrap/css/bootstrap.min.css" />
	<script type="text/javascript" src="webjars/jquery/jquery.min.js"></script>
	<script type="text/javascript"
			src="webjars/bootstrap/js/bootstrap.min.js"></script>
	<link rel="icon" type="image/png" href="resources/favicon.ico" />
</head>
<body>
	<div class="container">
	    <#if RequestParameters['logout']??>
          <div class="alert alert-info">
        	  You have been logged out.
          </div>
        </#if>
        <#if RequestParameters['error']??>
        <div class="alert alert-danger">
        	There was a problem logging in. Please try again.
        </div>
        </#if>

		<h1 class="col-sm-12">Login</h1>
		<form role="form" action="login" method="post">
			<div class="row col-sm-6">
				<div class="form-group">
					<label for="username" class="col-sm-3">Username</label> 
					<span class="col-sm-9"><input type="text" class="form-control" id="username" name="username" /></span>
				</div>
                <br/>
				<div class="form-group">
					<label for="password" class="col-sm-3">Password</label> 
					<span class="col-sm-9"><input type="password" class="form-control" id="password" name="password" /></span>
				</div>

				<div class="col-sm-12">
					<input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<button type="submit" class="btn btn-default">Login</button>
				</div>
			</div>
		</form>
		<div class="clearfix"></div>
        <br/>
        <br/>
		<div class="col-sm-12">
	        <form role="form" action="signin/facebook" method="post">
	            <input type="hidden" name="scope" value="public_profile" />
	            <button type="submit" class="btn btn-primary">Login using Facebook</button>
	        </form>
		</div>
		
		
	</div>
</body>
</html>