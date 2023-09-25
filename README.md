# Getting Started

### Coding task (JAVA)
A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.
A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar
spent over $50 in each transaction
(e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).
Given a record of every transaction during a three months period, calculate the reward points earned for each
customer per month and total.

### Assumptions
Due to the lack of some requirements and clarification, some assumptions have been made.
Changing them may require a code refactor.
* Service is showing in report zero values for specific month if there is no reward points earned by customer.
* Tn task there is no information how many transactions are estimated per customer in database for required time period (ex. three months). 
If there is significant number of them - we might need to add some pagination to improve performance.
* If change in reward strategy is needed, then create new class that implements interface `RewardStrategy`
* If there is a need to change of time range for report ex 6 months there is property in `application.properties` that can be changed `number.of.months.in.report` or if we need dynamic report parameter just add RequestParam to controller. 
* Report is done for last three months (included current month but it can be changed with property `should.include.current.month.in.report`). Code needs to be refactored is we need some particular, specified date range. 

### Build & Run
* java 17, spring-boot 3.1 supported
* Data (customers and transactions) are stored in H2 database that is started and schema created during startup
* To run tests: `./gradlew check`
* To run application: `./gradlew bootRun`
* Service is working on port _9000_ and has endpoints described in swagger at:
http://localhost:9000/swagger-ui/index.html

* to generate reports You need to add Customers with `POST /api/customers`
* then add Transactions with `POST /api/customers/{customerId}/transactions`
* then just generate reports:

-`http://localhost:9000/api/rewards` to get full report of reward points earned for each customer per month and total

-`http://localhost:9000/api/rewards?customerId=1` to get report for specified customer