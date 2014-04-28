#!/bin/sh

pid=$(ps aux | grep "JuliusCaesar.jar" | grep -v grep | awk '{print$2}')
kill $pid
echo "Kill complete , muhahahhaah"
nohup java -jar /home0/ashok/workspace/Tweetylze/JuliusCaesar.jar &


