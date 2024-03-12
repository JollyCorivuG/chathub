# 运行 jar 包
nohup java -Dspring.profiles.active=prod \
-Xmx300m-Xms300m -Xmn200m \
-jar chathub-server-1.0-SNAPSHOT.jar \
> log.txt \
&