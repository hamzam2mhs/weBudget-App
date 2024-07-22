What technical debt has been cleaned up
========================================

In this iteration we cleaned up a few pieces of technical debt. The first one was [offloading all string to integer parsing](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/commit/7bf6cdb730184528e4a6cb86b5b30bb9df6ab35d) to the logic layer so that the UI layer just needs to pass the strings entered into the EditText fields. I would classify this technical debt as reckless inadvertant because it is a reckless violation of the layering design pattern. The next was that we had a fair amount of [logic in our test group database](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/commit/8026a9cf8756affa1b51705d2401a8e330aad0b0) that needed to be offloaded into the logic layer aswell. This again would be reckless indadvertant technical debt because we disregarded the 3 tier architecture when we coded it.


What technical debt did you leave?
==================================

One piece of technical debt we would have liked to have addressed if we had another iteration was the way that the Transaction domain specific objects are created and stored in the database. The way we did it made it extremely difficult to obtain the usernames of the users involved in the transaction which hindered our ability to add username to the transaction details on the calendar. We would have liked to of refactored all of the transaction logic and peristence classes to allow for username storing and dislpaying it in the UI. This debt would be prudent inadvertant. At the time we thought we were doing it the best way possible, then in hindsight as the project grew we looked back and said "Now we know how we should have done it".

Discuss a Feature or User Story that was cut/re-prioritized
============================================

In our final iteration we cut the features [Attach reciepts to transaction](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/10) and [Cusomization of account](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/11). We decided that given the remaining time it was not feasable, and we had over-budgeted what we thought we could get done in a given iteration. To get these features functional it would require massive refactoring of persistence, logic, and UI layers.

Acceptance test/end-to-end
==========================

The end-to-end test that we decided to write was centered around the ability to add funds to a users wallet for use in groups or transferring to other users. We tested the user is able to login, and [deposit money to their account](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/blob/develop/app/src/androidTest/java/com/comp3350/webudget/WalletTest.java), and verified the funds had been successfully added to their account. The test was set up to not be flaky through a few hours of trial and error, and finally landing on utilizing proper before and after functions in the test class to ensure predicable user balance values were maintained. 


Acceptance test, untestable
===============

The first challenge in acceptance testing is setting up the acceptance test properly to run the application correctly. Something difficult or impossible to test was the calendar view fragment, as there is no nice way to interact with the calendar using Espresso to select the correct day and view the transactions on that day.


Velocity/teamwork
=================

Our estimates remained consistnent throughout the project with the "hicuup" of iteration 2 when we implemented the HSQLDB database for our application. The extra efforts required to accomplish this task caused our actual hours spent to outweigh our estimated hours and throw off our project velocity. Throughout the project (with the exception of iteration 2) we had been over estimating how long things would take, even with efforts being made to better estimate cost of tasks we still had a tendency to over-estimate. An example of the effect that our database implementation had on our velocity can be found in this [issue](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/33). The following tasks show our tendency to over-estimate.
- - [Transaction Logic](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/57)
- - [Create Calendar Fragment](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/40)
 
