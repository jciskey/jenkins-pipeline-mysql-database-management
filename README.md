# jenkins-pipeline-mysql-database-management
A shared library of Jenkins Pipeline steps to help setup and teardown MySQL databases


## Steps available
This shared library makes the following steps available to your Declarative Pipeline scripts:


### buildTestMySQLDatabase
This step will build a new test database for your Pipeline to use, as well as adding a test user with full privileges on that database. The name of the test database and the test user will be returned in a Groovy map.


### destroyTestMySQLDatabase
This step will destroy the test database when it is no longer needed, also removing the test user and revoking all privileges it had.

