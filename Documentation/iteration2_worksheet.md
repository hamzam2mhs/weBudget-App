# Iteration 2 Worksheet

Paying off technical debt
-------------------------
- the technical debt we are currently paying off stems from overusing concrete classes instead of making abstract interface classes and using dependency injection. This design allows for a more flexable use of objects within a class, and in our case required objects to be reworked to make fit our new design as our application evolves. We had to move logic out of these classes and alter the existing instance variables. We feel this was a prudent deliberate type of technical debt as we knew better, but were still figuring things out and trying to get our classes together. [Link to commit](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/commit/cfbaf14f99aa21938b815c34d783d4ecaa52c033)
- the second instance of technical debt we are paying off is another instance of relying on a concrete class that didnt work with a database for storing objects. The constructor assumed we had an altogether new object and auto incremented the ID. Now we create new objects when we grab their data from the database to work with that data. This resulted in our constructors needing to be re-worked to accomodate this new design. We classify this as prudent inadvertant becase at the time we did not know how we were going to interact and store objects in the database and did the best we could with the knowledge we had. [Link to commit](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/commit/e72998a4f1384f16dc937a8d8e9c4d6790c5496d)

SOLID
------
- [Link](https://code.cs.umanitoba.ca/3350-winter-2021-a02/group-4/group4project/-/issues/27) to issue created in Group 4 A02 repository. 


Retrospective
-------------
- In our retrospective we decided we needed to have a more even distribution of tasks, and do a better job at tracking the development tasks. This is prevelant in our closed [dev tasks](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues?scope=all&utf8=%E2%9C%93&state=closed) for this iteration compared to the last iteration. We have an abundance of dev tasks for iteration two with even distribution among all members of the group.

Design Patterns
----------------
- In our [Services.java](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/blob/develop/app/src/main/java/com/comp3350/webudget/application/Services.java) code we used the "Singleton" design pattern to ensure only one instance of our specific databases exist in our application. Since all instances of the database are acquired through Services it adds a nice safeguard in place to ensure multiple instances cannot be passed around.

Iteration 1 Feedback Fixes
--------------------------
- The [graders issue](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/issues/46) was that we were printing to standard output when we should have been throwing errors in our domain specific object. What we did to address this issue was take this logic out of the domain specific object and put it in the logic layer, where we are throwing errors rather than printing to standard output. [Link to commit](https://code.cs.umanitoba.ca/3350-winter-2021-a01/weBudget/-/commit/ae004ad8c07dda6357dc34d3755e413eba5795a1).