#! /bin/bash
#Intall Startup script

sudo cp startup.sh /etc/init.d/example-api
sudo update-rc.d example-api defaults
