NB: This project was developed as a part of a coursework assessment for the University of Sussex BSc Computer Science and Artificial Intelligence final year module, Web Applications and Services (2020). This implementation was awarded a 100% grade. I have made this implementation publically available on 31/01/2020 as a learning resource.

# An Online Payment Service
## 1. Introduction
This assignment is about the design and implementation of a web-based, multi-user payment service using Java Enterprise Edition (J2EE) technologies. The system is a much simplified version of PayPal. Through a JSF-based web interface, users should be able to send money to other registered users (e.g. using their registered email address as their unique identifier), request money from other registered users and manage their own account (e.g. look at their recent transactions). Super-users (i.e. admins) should be able to access all user accounts and transactions. Optionally, you will deploy your application on the cloud (e.g. on Amazon AWS, Microsoft Azure, or any similar infrastructure).

After successfully completing the assignment, you will have demonstrated that you can:
* design and implement user interfaces using Java Server Faces
* design and implement business logic using enterprise Java beans (EJBs)
* design and implement a secure multi-user system

## 2. Project Description
Online payment services, such as PayPal, allow users to connect their online accounts to their bank accounts, debit and credit cards. In such systems, users are usually able to transfer money from their bank accounts to the online account, receive payments to this account from other users, push money from the online account to their bank accounts etc.

For simplicity, we will assume that, for this project, all registered users start with a specific amount of money (e.g. £1000 pounds) and no connections to bank accounts exist.

**_Note: this is pretend money and no connection to real sources of money should exist._**

Each user has a single online account whose currency is selected upon registration. A user can select to have their account in GB Pounds, US dollars or Euros. In that case, the system should make the appropriate conversion to assign the right initial amount of money (e.g. if the baseline is the £1000, then the initial amount should be 1000 * GBP_to_USD_rate US dollars).

A user can instruct the system to make a direct payment to another user. If this request is accepted (i.e. the recipient of the payment exists and there are enough funds), money is transferred (within a single J2EE transaction) to the recipient immediately. A user should be able to check for notifications regarding payments in their account.

A user can instruct the system to request payment from some other user. A user should be able to check about such notifications for requests for payment. They can reject the request, or, in response to it, make a payment to the requesting user.

Users can access all their transactions, that is, sent and received payments and requests for payments as well as their current account balance.

An administrator can see all user accounts and all transactions.

Currency conversion must be implemented by a separate RESTful web service (see Section 3.3). The actual exchange rates will be statically assigned (hard-coded) in the RESTful service source code.

## 3. Plagiarism and Collusion
The coursework you submit is supposed to have been produced by you and you alone. This means that you should not:
* work together with anyone else on this assignment
* give code for the assignment to other students
* request help from external sources
* do anything that means that the work you submit for assessment is not wholly your own work, but consists in part or whole of other people’s work, presented as your own, for which you should in fairness get no credit
* if you need help ask your tutor

The University considers it misconduct to give or receive help other than from your tutors, or to copy work from uncredited sources, and if any suspicion arises that this has happened, formal action will be taken. Remember that in cases of collusion (students helping each other on assignments) the student giving help is regarded by the University as just as guilty as the person receiving help, and is liable to receive the same penalty.

Also bear in mind that suspicious similarities in student code are surprisingly easy to spot and sadly the procedures for dealing with it are stressful and unpleasant. Academic misconduct also upsets other students, who do complain to us about unfairness. So please don’t collude or plagiarise.

