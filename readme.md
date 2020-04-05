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

**Changing default data directory for Zookeeper and Kafka**  
create **data** folder inside kafka home folder (/home/padma/Downloads/kafka_2.13-2.4.1) using command **mkdir data**  
go to data folder and create another folder inside for zookeeper **mkdir data/zookeeper**  

Edit Zookeeper properties to use the data folder  
from kafka home folder , run command **vim config/zookeeper.properties**  
edit the **dataDir** property to below 
**dataDir=/home/padma/Downloads/kafka_2.13-2.4.1/data/zookeeper**  
Save the file and verify using **cat config/zookeeper.properties** command  

Start Zookeeper server and verify  
**bin/zookeeper-server-start.sh config/zookeeper.properties**  
if everything goes well , we should see below log in console  
**INFO binding to port 0.0.0.0/0.0.0.0:2181**  

Edit kafka properties to use the data folder
from kafka home folder, create data folder for kafka **mkdir data/kafka**  
run command **vim config/server.properties**  
change the property to **log.dirs=/home/padma/Downloads/kafka_2.13-2.4.1/data/kafka**  

from kafka home directory run below command to run kafka server
**kafka-server-start.sh config/server.properties**  
if everything goes well , we should see below log in the terminal  
**INFO [KafkaServer id=0] started (kafka.server.KafkaServer)**  
go to folder  /home/padma/Downloads/kafka_2.13-2.4.1/data/kafka , 5 files should be there.  
**Note : Zookeeper should be running before kafka server run command**  




 

 


 
 