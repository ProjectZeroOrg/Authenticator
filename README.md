# Authenticator
Authenticator provides SSO authentication and authorization of users against a database server 
or external identity provider (facebook). It issues JWT tokens for both. 

## Modules
Authenticator contains 3 modules auth-web, auth-lib and extras. 

The auth-web is an OAuth2 Authorization server, that supports all the grant types 
but is intended to be used with authorization code. 
The auth-lib module is a common library.

The extras module has a database initialization application and 2 sample application. 
The authdb-init should to be run once in order to create the authdb database schema and a superuser account. 
Running it again would recreate the database.
The sample-ui and sample-resource are "Hello world" examples of an OAuth2 client and a resource server,
that connect with the Authenticator's auth-web server.

## Configuration and environment variables
The Authenticator's web application has a number of configurations in its [application.yml](auth-web/src/main/resources/application.yml).
Their default values are appropriate for local development and most can be overridden with environment variables. Here is the list:

| ENV var             | default            | comment                                                                             |
| :------------------ |:-------------------| :-----------------------------------------------------------------------------------|
| AUTH_SERVER_PORT    | 9999               | Good for local dev. In openshift it should be changed to 8080.                      |
| AUTH_APP_CONTEXT_PATH | /uaa             | Good for local dev. In openshift it should be changed to just /.                    |
| AUTH_DB_SERVER      | authdb-dev         | It is fine like this. Should be changed to authdb-prod in the prod configuration.   |
| AUTH_DB_PORT        | 3306               | No need to change.                                                                  |
| AUTH_DB_ADMIN_USER  | dbadmin            | No need to change.                                                                  |
| AUTH_DB_ADMIN_PASS  | adminpass          | Definitely should be changed in the prod configuration.                             |
| AUTH_DB_USER        | dbuser             | No need to change.                                                                  |
| AUTH_DB_PASS        | dbpass             | Definitely should be changed in the prod configuration.                             |
| CLIENT_APP_ID       | Challenge Me       | No need to change.                                                                  |
| CLIENT_APP_SECRET   | challengeMeSecret  | Definitely should be changed in the prod configuration.                             |
| FACEBOOK_APP_ID     | 2054424358161118   | No need to change.                                                                  |
| FACEBOOK_APP_SECRET | 6ea2786127fb5219755d814e298d62dc | It will be letter changed for both prod and dev configurations.       |
| JWT_PRIVATE_KEY     |                    | Definitely should be changed in the prod configuration.                             |
| JWT_PUBLIC_KEY      |                    | Goes together with the private key.                                                 |

The pair of private and public keys is generated with: 
```ssh-keygen -t rsa -b 4096 -f jwtRS256.key```
with no password.