# case-backend
API Server For Case(Call Recording Summary Web Service)

## AWS EC2 배포 방법(실행 전)
```
sudo apt-get update
```

```
sudo apt install docker.io

sudo apt install docker-compose

sudo usermod -aG docker ${USER}

sudo reboot
```

```
sudo apt install openjdk-11-jdk

nano ~/.bashrc

export JAVA_HOME= $(dirname $(dirname $(readlink -f $(which java))))
export PATH=$PATH:$JAVA_HOME/bin

source ~/.bashrc
```

## 실행
```
git clone https://github.com/khu-capstone-design-case/case-backend.git

cd ./case-backend

[Write down "MYSQL_ROOT_PASSWORD", "MONGO_INITDB_ROOT_PASSWORD" in docker-compose.yml]

docker-compose up -d

[Write down {JWT SECRET KEY} in /src/main/resources/application.yml] 

[Write down "spring.datasource.password", "spring.data.mongodb.password" in /src/main/resources/application-db.yml] 

sudo chmod 755 ./gradlew

./gradlew build --exclude-task test

java -jar build/libs/case-backend-0.0.1-SNAPSHOT.jar
```
