SOMETHING YOU NEED FOR BUILD PROJECT:
1.Install  nodejs
2.Install node package manager (npm).
3.Install yarn.
4.Install java.
5.Install VSCODE.
6.Install INTELLIJ   IDE.
* ALL THE THINGS I JUST MENTIONED ON YOUTUBE HAVE THE EASIEST TO UNDERSTAND INSTALLATION INSTRUCTION, SO IF YOUR LAPTOP OR PC DOESN’T HAVE ANYTHING, JUST SEARCH WITH KEYWORD “HOW TO INSTALL ..... TO WINDOW/ MACBOOK”
 I have write all statement below of file “ deployment.yaml “ you can see the code in there. The statement has “-s -f “ is statement for remove and remain for pulling container.
Correct statements pulling always have “ up  -d”, it’s look like this: 
“ docker-compose -f ./deployment.yaml up -d mysql8-container “

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/b85aeb46-3bcd-4755-bbf7-191cf413b473)

You can check all  container by : “ docker ps “ for The container is running
 and “ docker ps -a “ for all container you have.

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/d4ecf729-6680-4de2-a797-f39bda3110c9)

Below are step by step  running my project.

Go to folder contain project

Check position: run:->  ls -la
If you have the same result like this, you are correct

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/d3d8d182-613a-4861-a2aa-407f89e244ad)


Run this statement: docker-compose -f ./deployment.yaml up -d mysql8-container

The result:




After that need check container was pulling in docker by: docker ps

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/54275826-c522-4d8a-9935-4f47c52a7ad4)

You need to check network after create container mysql8, statement: docker network ls. 

The result like this:


![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/ff8b5027-49b8-4cfa-bd78-711f09892a48)


* Pulling container phpmyadmin.
Attention: If you are using window or mac intel, you need to NO comment line 21 in file deployment.yaml. And comment lines 23. And if you are using mac m1 you need to NO comment line 23. And comments lines 21.


![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/780c5c09-f686-4229-9326-22ab2c5bdf3f)


![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/cb010dab-f5e8-4931-8669-5f883bbf0bb3)


Next step is creating phpmyadmin8-container. READ ATTENTION BEFORE RUN THIS STATEMENT.
Run this statement :  docker-compose -f ./deployment.yaml up -d phpmyadmin8-container

Result same as create mysql8-container. My docker have phpmyadmin before, so my result like this:

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/d7a38279-8065-4786-b85e-cefdaeaaf695)


Then, check image in your laptop or pc. statement is:  “ docker image ls “  or 
“ docker images ”

result here:

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/acd61099-83ee-431a-b91d-fb36b52339a5)


Next stage, open browser and find url: “ localhost:8100 ”(recommend using chrome for build project).

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/2bcd987c-13ec-4c92-ba87-1cfe0429c53d)

username is: root 
password in deployment.yaml: Abc123456789@. 
you can change the password before you create phpmyadmin and mysql8

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/d6a4c6bf-e4af-4c4c-aabd-1532ee6fdeed)

Next step you in import file database i was already build and put it on folder contain project.

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/ebae6866-28a3-41be-95c8-fc0663f0ad89)

Rolling down and press import.

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/fd6fc112-fa81-482e-ba81-cc00848336a9)

You have successfully for project’s database.
Now we need connect backend to frontend. Follow instruction.

**_I have written statement below file deployment.yaml._**

Run statements create redis:
“docker-compose -f ./deployment.yaml up -d redis-container”
Result: 

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/b4027470-ffaf-466f-83e6-9714e8ee753a)


Now go to file kafka-deployment.yaml to build zookeeper and kafka.

Run statement: create zookeeper-01
“ docker-compose -f ./kafka-deployment.yaml up -d zookeeper-01 ”

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/6150a379-b9db-4621-aec5-c02dfbca4ec4)


Run statement: create zookeeper-02

“ docker-compose -f ./kafka-deployment.yaml up -d zookeeper-02 ”

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/d1d068dc-e4a5-4b53-ab17-1efefcb65394)

Run statement: create zookeeper-03

“ docker-compose -f ./kafka-deployment.yaml up -d zookeeper-03 ”

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/dd54be63-b97b-49d4-84bc-72dab3661274)


**_*DON’T WORRY ABOUT WHY CREATE ZOOKEEPER 02 AND 03 SO QUICKLY. BECAUSE THEY REUSE IMAGES*_**

If you run "docker ps" and you have results same as me, you are correct.

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/698de85a-7f0a-4129-9cd0-850d1b1416d5)


***Create kafka-broker-01.**
Run statement: 
“ docker-compose -f ./kafka-deployment.yaml up -d kafka-broker-01 ”
The result:

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/3cec1aee-5a48-47c2-aca1-ceacff04fc40)


Next step, I prefer using IDE INTELLIJ. Open Folder  “shopapp-backend”. And add plugin 

“LOMBOK”. Then press apply - >  ok.


![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/aa94024b-8814-4424-b31c-e38f831c49db)


Open Postman and import two file into postman.

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/662d03b9-1697-4b45-9338-6923641e0767)

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/ac4da4c0-94d7-4b32-8a4b-ec590a64df33)

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/059c46a2-20f4-4d43-91de-eb64a3ef2d75)

If you have screen same as picture you are done.

Open VSCODE. Open folder contain project. Use terminal in vscode to go to deep folder 
shopapp-angular. And run code “ yarn install ” 

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/6ff4e714-5ab8-4b67-a695-cd7bab19ff74)


Press yes if you permission or no if you don’t want.
Then run  “ yarn start ” to run this project.

![image](https://github.com/sontbui/E-commerce-Platform/assets/106475239/e63efda4-8510-452b-9f38-ff8b06b30fd3)

**_Finally you go to webbrowser and search web with url: localhost:4200_**

**_Account admin: phonenumber:  0123456789
                  password: 123123
_**
**_Account user: _username: 0345872900 
           password: 123123
_**


