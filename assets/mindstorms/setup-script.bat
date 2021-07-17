@echo off 
set jar=Mindstorms-Driver.jar
set /p ip=< ip-adresse.txt

scp -c aes128-cbc -oKexAlgorithms=+diffie-hellman-group1-sha1 %jar% root@%ip%:/home/lejos/programs