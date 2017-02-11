ROYALTY JERSEY SPRING
---------------------

DESCRIPTION
Implement a system to record and calculate royalty payments owed to Rights Owners based on viewing activity of customers.  Royalties will be calculated at the Rights Owner level, so each episode belonging to a specific Rights Owner will be worth the same amount.  The system must meet the provided REST API specification and accept/return JSON.

Customer viewings will be tracked by a POST to the VIEWING endpoint.  The system is not concerned about whether a given customer ID is valid, it assumes the consuming system is sending a valid customer ID.  The episode ID must be valid and exist in the system for the viewing to be tracked.
Royalties owed will be listed by a GET to the PAYMENTS endpoint.  This will return a list of the royalty payments owed to the studios in GBP£.  
Royalties owed to a specific Rights Owner will be returned by a GET to the PAYMENTS/{Rights Owner GUID} endpoint.  This will return a single object representing the royalty payment owed to the studio in GBP£.
The Reset endpoint will reset the internal state of the system (setting viewing counters back to 0).


REQUIREMENT
- Install Java 8 (lower versions do not work)
- Install Gradle ( it was used 3.2.1, wrapper is available)
- Install any server (Tomcat, Jetty, any server should work for this project)
	Right now the project does not have any embedded server.
- Optional: Eclipse with Gradle plugging and/or a configured server.

GRADLE COMMANDS
-  $ gradle build : generates the WAR file, ready for be included in the server.
-  $ gradle test : runs the unitary test
-  $ gradle integrationTest : runs the integration test
- Optional(for preparing to Eclipse): $ gradle cleaneclipse eclipse

DEPENDENCIES
Check the build.graddle file for a full list of dependencies and their versions.

 
FOR DEVELOPERS
Possible improvements points could be:
- To handle default exceptions with WebApplicationException or ExceptionMappers. 
- To change the persistence tier to JPA with any implementation and a generic CRUD Dao.
- To change the attributes in the model for matching the business layer, The result tag can be customized.
...
 

LOCAL URLs
(The ".../rest/..." part is configured in server, in Tomcat is in Web Modules part.
 Probably by default is going to be instead ".../royalty/..." for rootProject.name in graddle)
 
- POST: http://localhost:8080/rest/royaltymanager/reset
- POST: http://localhost:8080/rest/royaltymanager/viewing
		{ 
		  "episode": "<GUID>",
		  "customer": "<GUID>"
		}
- GET: http://localhost:8080/rest/royaltymanager/payments
- GET: http://localhost:8080/rest/royaltymanager/payments/<GUI>
- POST: http://localhost:8080/rest/royaltymanager/studios
	[{
		"id": "665115721c6f44e49be3bd3e26606026",
		"name": "HBO",
		"paymentUnit": 12
	}, {
		"id": "8d713a092ebf4844840cb90d0c4a2030",
		"name": "Sky UK",
		"paymentUnit": 14.67
	}, {
		"id": "75aee18236484501b209aa36f95c7e0f",
		"name": "Showtime",
		"paymentUnit": 13.45
	}, {
		"id": "49924ec6ec6c4efca4aa8b0779c89406",
		"name": "Fox",
		"paymentUnit": 17.34
	}]

- POST: http://localhost:8080/rest/royaltymanager/episodes
	[
		{
			"id": "6a1db5d6610a4c048d3df9a6268c68dc",
			"name": "Game of Thrones S1:E1",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "111cd2dfd8c94682988e61ca087a09a4",
			"name": "Game of Thrones S1:E2",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "cd01aadd88fa4f8ca3290d118d9621a1",
			"name": "Game of Thrones S1:E3",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "a4cfda21457e4e548f2c3a472decc7cb",
			"name": "Game of Thrones S1:E4",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "8c67dbe173ff42f28f5e2116c111ec7a",
			"name": "The Wire S2:E3",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "269c71ebfa44448ba9bfdfa7e5332def",
			"name": "The Wire S2:E4",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "04072c89f0df4ef9abd329e1a7b21779",
			"name": "The Wire S2:E5",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "a1b61b64ac2b443a81a492e8bddd9461",
			"name": "The Sopranos S1:E10",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "6e23bc6a2163458f834d68be9a97a257",
			"name": "The Sopranos S1:E11",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "e0de6ac6d72f4adc97564c358b2bbab5",
			"name": "The Sopranos S1:E12",
			"rightsOwner": "665115721c6f44e49be3bd3e26606026"
		}, {
			"id": "fcfba01219464541a70eb8677260de4d",
			"name": "Moone Boy S2:E4",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "82fcbde4285b4a58977915ea15aa18ac",
			"name": "Moone Boy S2:E5",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "78a7efb2bb36491996ff562f118d5a3d",
			"name": "Moone Boy S2:E6",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "1d8cc21b96a44d3aaa5eebd78b4663d5",
			"name": "Moone Boy S2:E7",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "907d2138009d471a9a3b7ce68c3f032d",
			"name": "A League of Their Own S3:E5",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "5dce6bf9a7c54103bee9f52fadd2bafe",
			"name": "A League of Their Own S3:E6",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "9f5da1a29cdc4b8f98efefb27da94a3c",
			"name": "Trollied S4:E4",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "3817c276f8464c3cbcf232f05402c8d8",
			"name": "Trollied S4:E5",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "25284b37846e4b8fa17fdceaf992237e",
			"name": "Trollied S4:E6",
			"rightsOwner": "8d713a092ebf4844840cb90d0c4a2030"
		}, {
			"id": "453500796ecc476ca142c25d652a95bd",
			"name": "Billions S1:E1",
			"rightsOwner": "75aee18236484501b209aa36f95c7e0f"
		}, {
			"id": "dfde22b2a3f24401b12eeccc28cf1570",
			"name": "Billions S1:E2",
			"rightsOwner": "75aee18236484501b209aa36f95c7e0f"
		}, {
			"id": "710ccbe9d75445c0bc60737aa655e283",
			"name": "Billions S1:E3",
			"rightsOwner": "75aee18236484501b209aa36f95c7e0f"
		}, {
			"id": "f233b0fc72e14d549d1bddfcdb2c9933",
			"name": "Billions S1:E4",
			"rightsOwner": "75aee18236484501b209aa36f95c7e0f"
		}, {
			"id": "5646cdd4ac874431bf40e52237d54bea",
			"name": "Billions S1:E5",
			"rightsOwner": "75aee18236484501b209aa36f95c7e0f"
		}, {
			"id": "ef35e86beb7d404b8cd2dd3f2451c33c",
			"name": "Billions S1:E6",
			"rightsOwner": "75aee18236484501b209aa36f95c7e0f"
		}, {
			"id": "8ad7222c40214f8eb98473bd09bab29c",
			"name": "Billions S1:E7",
			"rightsOwner": "75aee18236484501b209aa36f95c7e0f"
		}, {
			"id": "1731355b2309475bb436ae938c93c801",
			"name": "Dexter S1:E1",
			"rightsOwner": "75aee18236484501b209aa36f95c7e0f"
		}, {
			"id": "89eb6371df374163859c5d69ae0fc561",
			"name": "Futurama S1:E5",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "e44eea15940f4b1ebd26ab3b114c0a14",
			"name": "Futurama S1:E6",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "13f7c592d73342c98f936620e65197e2",
			"name": "Futurama S1:E7",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "c1b1eb7020b345189d05000dbb05029d",
			"name": "The Simpsons S21:E15",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "d5ca9218562a4c94bdca9955cec2870e",
			"name": "The Simpsons S21:E16",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "e67fec32bc75428999342b782d224d37",
			"name": "The Simpsons S21:E17",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "709d6e2cd99f43649921c6b1df4d725f",
			"name": "The Simpsons S21:E18",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "e45bf5573d8a472292b363bd89a1379f",
			"name": "The Simpsons S22:E1",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "a285e0579e01416b90260e1209c301de",
			"name": "Family Guy S2:E4",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "0d146cbda0754d179bcbfb4bea360d92",
			"name": "Family Guy S2:E5",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "c847be63088c4e95a818f82e3a834f22",
			"name": "Family Guy S2:E6",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "1ef8e5664305443f8c07a210c5887646",
			"name": "Prison Break S5:E2",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "a86ae1d1de574eb3866c2fadd7cd8a77",
			"name": "Prison Break S5:E3",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}, {
			"id": "9159d302c3104e58a01fa397a3382b0d",
			"name": "Prison Break S5:E4",
			"rightsOwner": "49924ec6ec6c4efca4aa8b0779c89406"
		}
	]







 
