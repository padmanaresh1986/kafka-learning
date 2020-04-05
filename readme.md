**Install java 8**  
sudo apt install openjdk-8-jdk openjdk-8-jre

java --version

nano /etc/environment  
JAVA_HOME= /usr/lib/jvm/java-8-openjdk-amd64  
JRE_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre  

**Download kafka**  
Goto below link and download  
https://www.apache.org/dyn/closer.cgi?path=/kafka/2.4.1/kafka_2.13-2.4.1.tgz  
Click on the mirror link and save file

After download complete , goto downloads folder (/home/padma/Downloads) and run ls command, you should see file kafka_2.13-2.4.1.tgz  

Extract file using command   **tar -xvf kafka_2.13-2.4.1.tgz**  
Goto folder **/home/padma/Downloads/kafka_2.13-2.4.1/bin**  
Run command **./kafka-topics.sh**  
if above command returns help documentation then kafka installed successfully  

Update path
Go to user home directory (/home/padma)  
Edit .bashrc file to add kafka to PATH

**vim .bashrc**
add below line at the end  
**export PATH=/home/padma/Downloads/kafka_2.13-2.4.1/bin:$PATH** 
save and close the file , verify using **cat .bashrc** command  

Open new terminal and run below command  form any path
**kafka-topics.sh**
if everything goes well , we should see kafka help documentation  


 

 


 
 