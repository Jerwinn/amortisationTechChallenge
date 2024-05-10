Hello, welcome to the repo.

I have implemented the logic for calculating an amortisation schedule with and without a balloon payment.
Along with two GET and one POST.

To start and build application think it will just be a matter of building and running the application.
I have used Java 22 for the project.

I have used a postgresql database and a container will be made in docker. It should be created once the application is ran.
The database does not have any initial data so the first step is to create some.

If using Postman make a POST request to:
http://localhost:8090/api/amortisations

With the body of:

For without balloon payments:

{
"loanAmount": 25000,
"deposit": 5000,
"interestRate": 0.075,
"numberOfPayments": 12,
"includeBalloonPayment": false,

}

With balloon payments:

{
"loanAmount": 25000,
"deposit": 5000,
"interestRate": 0.075,
"numberOfPayments": 12,
"includeBalloonPayment": true,
"balloonPayment": 10000
}

Theres another two GET methods
http://localhost:8090/api/amortisations
Will get all of the amortisation schedules and other data

http://localhost:8090/api/amortisations/{id}
will get the amortisation schedule based on the id.

I have written some tests but it's incomplete

Initial tests done from a tdd approach was when I was using doubles, so I had to change midway to Big decimals

