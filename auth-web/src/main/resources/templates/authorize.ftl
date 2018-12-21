<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Approval</title>
    <link rel="stylesheet" type="text/css"
          href="webjars/bootstrap/css/bootstrap.min.css" />
    <script type="text/javascript" src="webjars/jquery/jquery.min.js"></script>
    <script type="text/javascript"
            src="webjars/bootstrap/js/bootstrap.min.js"></script>
    <link rel="icon" type="image/png" href="resources/favicon.ico" />
</head>
<body>
<div class="container">
    <h2>Please Confirm</h2>
    <p>
        Do you authorize "${authorizationRequest.clientId}" at "${authorizationRequest.redirectUri}" to access your protected resources
        with scope ${authorizationRequest.scope?join(", ")}?
    </p>
    <div class="col-sm-12">
        <form id="confirmationForm" name="confirmationForm" action="../oauth/authorize" method="post">
            <input name="user_oauth_approval" value="true" type="hidden"/>
            <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button class="btn btn-primary" type="submit">Approve</button>
        </form>
    </div>

    <div class="col-sm-12">
        <form id="denialForm" name="denialForm" action="../oauth/authorize" method="post">
            <input name="user_oauth_approval" value="false" type="hidden"/>
            <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button name="deny" type="submit" class="btn btn-default">Deny</button>
        </form>
    </div>

    <div class="clearfix"></div>
</div>
</body>
</html>