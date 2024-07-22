Iteration 1 Worksheet
=====================

Adding a feature
-----------------
In this iteration we added the feature that allows users to create accounts on our app, and sign in.
Having user accounts will allow us to store information about each user that persists to track their transactions,
groups, wallet, and general information about the user. We starte with a login screen that has an option to create 
an account if you dont already have one. From there you can enter your desired username, first name, last name, and password which will all be stored in our database. Once the account has been create the user is automatically navigated back to the login screen where they can login with their freshly created account.

Links
- [User Story: Create Account](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/12)
- [User Story: Account Login](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/13)
- [Feature: Personal Acount](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/1)
- [Test: LoginTest](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/blob/master/app/src/test/java/com/comp3350/webudget/business/Login_test.java)
- [Test: SignupTest](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/blob/master/app/src/test/java/com/comp3350/webudget/business/SignupLogicTest.java)
- [Merge Commit](nothingYet)


Exceptional code
----------------

Here is where we used some [Exceptional Code](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/blob/master/app/src/main/java/com/comp3350/webudget/business/LoginLogic.java).
In this code we throw an InvalidLoginException if you attempt to login with an invalid username or password. The exception is handled gracefully by the UI alerting the user to the invalid input that was provided.


Branching
----------

Please view our [branching strategy](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/blob/master/Documentation/Branching_Strategy.md).
Please view our [branching graph](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/blob/master/Documentation/branching_graph.PNG) image


SOLID
-----

Here is the link to the [SOLID Violation](https://code.cs.umanitoba.ca/3350-winter-2021-a01/3350-winter2021-a01-lingual-g5/-/issues/63) we found in group 5s repo.

Agile Planning
--------------

In this iteration, we pushed back [accessing user wallet](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/4), [viewing personal caldenar](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/8), and [persistant storage](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/2) features to iteration 2. We did this because during planning in iteration 0 we got ambitious about iteration 1 and assumed we would have a database up. Since we did not get the database up we were unable to implement these features that relied on it. No descriptions about features or stories have been changed **so far**.
