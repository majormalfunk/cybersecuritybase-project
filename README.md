# Cyber Security Base -project

This project is built on the Course Project I, forked from https://github.com/cybersecuritybase/cybersecuritybase-project.

The aim of the project was to create a web application that has at least five different flaws from the OWASP top ten list and then point out these flaws and also to provide steps to fix them.

There are two kinds of users in the web application derived from the base project. The web application enables Administrators to create new user accounts and view user registrations to the event. The web application is just for demonstration purposes so there is no feature to create new user accounts other way than with an administrator account. Precreated regular users are allowed to signup for the event once the user account has been created. Upon signing up to the event a regular user needs to give their name, address and bank account. Users should only see their own signup information and only an administrator should be able to grant or deny administrator privileges to other users.

Using the web application: build it and launch it. By default it runs in port 8080 in localhost. (http://localhost:8080)

These users are precreated in CustomUserDetailsService.java:

* admin/admin (administrator)
* donald/donald (signed up user)
* daisy/123456 (signed up user)
* newbie/password (not signed up user)

A GitHub repository of the web application can be found at: https://github.com/majormalfunk/cybersecuritybase-project. This report can also be found as the README.md of the repository.

I have detected some alarming security risks in the web application and would now like to point them out. Here are the most obvious issues identified as some of OWASP Top 10 Application Security Risks:

## APPLICATION SECURITY RISK 1:

Issue: OWASP A2:2017 Broken Authentication

Details: The web applications allows to create weak or well-known passwords, and it doesn’t invalidate HttpSession at logout

Steps to reproduce:
1. Open the software
2. Signup as administrator with admin/admin (weak and well-known)

or

1. Open the software
2. Signup as any user
3. Log out user
4. Open software again and you’ll be signed in as the previous user (HttpSession is not invalidated at logout)

Steps to fix:

Implement weak-password checks such as testing new or changed passwords against a list of the top 10000 worst passwords
Fix in SecurityConfiguration.java to invalidateHttpSession(true) instead of false.

## APPLICATION SECURITY RISK 2:

Issue: OWASP A3:2017 Sensitive Data Exposure

Details: In the web applications bank account details are not encrypted in the database, broken access control allows users to see others users’ bank account details (See also risk 3: OWASP A5:2017)

Steps to reproduce:

1. Open the source code

You can see that there is no encryption for bank details in SignupController.java or SignUp.java

Steps to fix:

1. In Signup.java implement encryption in setBankAccount() and decryption in getBankAccount() of bank details.

Details: Http protocol is used instead of Https so data is passed to the browser in plain text

Steps to fix:

1. Enforce use of Https in SecurityConfiguration.java

## APPLICATION SECURITY RISK 3:

Issue: OWASP A5:2017 Broken Access Control

Details: Any user who guesses another user’s username can view their details by browsing to a correct url

Steps to reproduce:

1. Login as user donald/donald
2. Alter the URL to http://localhost:8080/done/daisy/
3. You can see the details of user daisy

or

1. Login as daisy/123456
2. Alter the URL to http://localhost:8080/admin
3. Grant admin privileges to user daisy
4. You can see all users’ details and you can even deny administrator privileges from user admin.

Steps to fix:

Enforce a check against authentication in SignupController.java methods loadAdmin(…), updatePrivileges(…), newAccount(…) and showRegistration(…) that a normal user is allowed to see only his own details and that only an admin is allowed to grant admin privileges and create new users.
Remove usernames from URL routes and use only authentication to identify user

## APPLICATION SECURITY RISK 4:

Issue: OWASP A7: Cross-Site Scripting (XSS)

Details: Input is not sanitized before storing in database allowing cross-site scripting with e.g. JavaScript.

Steps to reproduce:

1. Login as newbie/password or create a new user with admin/admin and log in with that.
2. Enter some javascript e.g. <script>alert(”Foobar!”)</script> in detail information and submit.
3. The alert should be shown every time the details are loaded on to the screen.

Steps to fix:

Replace utext with text in done.html and sanitize inputs before saving to database.

## APPLICATION SECURITY RISK 5:

Issue: OWASP A9: Using Components with Known Vulnerabilities

Details: The software is using Spring Boot 1.4.2 that has known vulnerabilities see e.g. CVE details in https://www.cvedetails.com/cve/CVE-2017-8046/

Steps to fix:

1. Use a recent version of Spring Boot (and other components). The latest can be found in https://spring.io/projects/spring-boot

## APPLICATION SECURITY RISK 6:

Issue: OWASP A10:2017 Insufficient Logging and Monitoring

Details: No security log is written of important security related events allowing breaches go unnoticed.

Steps to reproduce:

1. Login as donald/donald
2. Alter the URL to http://localhost:8080/admin
3. Grant administrator privileges to a user
4. You can see that there is no logging entry of elevated privileges

Steps to fix:

Add logging of important security events in easily readable format. At least all the changes in administrator privileges.


