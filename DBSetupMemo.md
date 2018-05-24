#Database Setup Memo
The database server is MariaDB.
##Dev instance
###Users
####dbadmin
          Username: dbadmin
          Password: adminpass
     Database Name: authdb
    Connection URL: mysql://authdb-dev:3306/

The dbadmin is granted all privileges on authdb. It is used for schema initialization and db migration.
```
sh-4.2$ mysql -u root
Welcome to the MariaDB monitor.  Commands end with ; or \g.
Your MariaDB connection id is 40
Server version: 10.2.8-MariaDB MariaDB Server

Copyright (c) 2000, 2017, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]> show grants for dbadmin;
+--------------------------------------------------------------------------------------------------------+
| Grants for dbadmin@%                                                                                   |
+--------------------------------------------------------------------------------------------------------+
| GRANT USAGE ON *.* TO 'dbadmin'@'%' IDENTIFIED BY PASSWORD '*2C6396ADEEF1AF865672D48735C0E3EC8B1A9CEC' |
| GRANT ALL PRIVILEGES ON `authdb`.* TO 'dbadmin'@'%'                                                    |
+--------------------------------------------------------------------------------------------------------+
2 rows in set (0.00 sec)

MariaDB [(none)]>
```
####dbuser    
The dbuser is created with less privileges - CRUD operations only. It is used by the applications.
```
ariaDB [(none)]> CREATE USER 'dbuser' IDENTIFIED BY 'dbpass';
Query OK, 0 rows affected (0.00 sec)

MariaDB [(none)]> GRANT SELECT, INSERT, UPDATE, DELETE ON `authdb`.* TO 'dbuser'@'%' IDENTIFIED BY 'dbpass';
Query OK, 0 rows affected (0.00 sec)

MariaDB [(none)]> FLUSH PRIVILEGES;
Query OK, 0 rows affected (0.00 sec)

MariaDB [(none)]> show grants for dbuser;
+-------------------------------------------------------------------------------------------------------+
| Grants for dbuser@%                                                                                   |
+-------------------------------------------------------------------------------------------------------+
| GRANT USAGE ON *.* TO 'dbuser'@'%' IDENTIFIED BY PASSWORD '*9FB2126F7514B6AF42B20E9E4B8E839B72E31396' |
| GRANT SELECT, INSERT, UPDATE, DELETE ON `authdb`.* TO 'dbuser'@'%'                                    |
+-------------------------------------------------------------------------------------------------------+
2 rows in set (0.00 sec)

MariaDB [(none)]>
```

###Connection
AuthDB-dev is exposed on 3306 with an ingress service and VirtualBox Network port forwarding.
####Expose ingress service
```
Microsoft Windows [Version 10.0.16299.371]
(c) 2017 Microsoft Corporation. All rights reserved.

c:\SAPDevelop\mdev>oc login -u system:admin
Logged into "https://192.168.99.100:8443" as "system:admin" using existing credentials.

You have access to the following projects and can switch between them with 'oc project <projectname>':

    default
    kube-public
    kube-system
  * myproject
    openshift
    openshift-infra
    openshift-node
    openshift-web-console
    project-zero

Using project "myproject".

c:\SAPDevelop\mdev>oc project project-zero
Now using project "project-zero" on server "https://192.168.99.100:8443".

c:\SAPDevelop\mdev>oc expose dc authdb-dev --type=LoadBalancer --name=authdb-dev-ingress
service "authdb-dev-ingress" exposed
```
There is no need to expose the authdb-dev-ingress service itself. It is done with VirtualBox port forwarding. Anyway for furure reference:
```
c:\SAPDevelop\mdev>oc export svc authdb-dev-ingress
apiVersion: v1
kind: Service
metadata:
  creationTimestamp: null
  labels:
    app: authdb
    env: dev
  name: authdb-dev-ingress
spec:
  deprecatedPublicIPs:
  - 172.29.39.37
  externalIPs:
  - 172.29.39.37
  ports:
  - nodePort: 31199
    port: 3306
    protocol: TCP
    targetPort: 3306
  selector:
    name: authdb-dev
  sessionAffinity: None
  type: LoadBalancer
status:
  loadBalancer: {}
```

####Port forwarding
A port forwarding rule is added to VirtualBox Network Settings with host port 3306 to guest port 31199 (or whichever port is assigned to the authdb-dev-ingress service).

####Local development
Applications datasources are defined as:
```
spring:
    datasource:
        url: jdbc:mariadb://${AUTH_DB_SERVER:authdb-dev}:${AUTH_DB_PORT:3306}/authdb
        username: ${AUTH_DB_USER:dbuser}
        password: ${AUTH_DB_PASS:dbpass}
        driver-class-name: org.mariadb.jdbc.Driver
```
and
```
spring:
    datasource:
        url: jdbc:mariadb://${AUTH_DB_SERVER:authdb-dev}:${AUTH_DB_PORT:3306}/authdb
        username: ${AUTH_DB_ADMIN_USER:dbadmin}
        password: ${AUTH_DB_ADMIN_PASS:adminpass}
        driver-class-name: org.mariadb.jdbc.Driver
```
The easiest way to work is to add to the etc/hosts file

        46.249.93.103   authdb-dev
        
Another option is to override the default configurations with environment variables - AUTH_DB_SERVER=46.249.93.103.

###First authdb-init log
```$xslt
alter table user_roles drop foreign key FKj9553ass9uctjrmh0gkqsmv0d
alter table user_roles drop foreign key FK55itppkw3i07do3h7qoclqd4k
drop table if exists role
drop table if exists user
drop table if exists user_roles
create table role (id bigint not null auto_increment, name varchar(255) not null, primary key (id)) ENGINE=InnoDB
create table user (id bigint not null auto_increment, active bit not null, password varchar(255), username varchar(255) not null, primary key (id)) ENGINE=InnoDB
create table user_roles (user_id bigint not null, roles_id bigint not null) ENGINE=InnoDB
alter table role add constraint UK_8sewwnpamngi6b1dwaa88askk unique (name)
alter table user add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username)
alter table user_roles add constraint FKj9553ass9uctjrmh0gkqsmv0d foreign key (roles_id) references role (id)
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id)

select role0_.id as id1_0_, role0_.name as name2_0_ from role role0_ where role0_.name=?
insert into role (name) values (?)
select role0_.id as id1_0_, role0_.name as name2_0_ from role role0_ where role0_.name=?
insert into role (name) values (?)
insert into user (active, password, username) values (?, ?, ?)
insert into user_roles (user_id, roles_id) values (?, ?)
insert into user_roles (user_id, roles_id) values (?, ?)
```

##Prod instance
TBD