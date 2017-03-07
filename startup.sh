### BEGIN INIT INFO
# Provides:          example-api
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start daemon at boot time of the example API
# Description:       Starts the example API
### END INIT INFO

# /etc/init.d/example-api
#

#FIXME
API_HOME="/root/git/example-api/"

# Some things that run always
touch /var/lock/example-api

# Carry out specific functions when asked to by the system
case "$1" in
  start)
    echo "Starting example API"
    cd $API_HOME
    ../gradlew bootRun > ../api.log&
    echo "Done!"
    ;;
  stop)
    echo "Stopping Starting example API"
    #FIXME: Kill by the PID. Not by this way.
    pkill -9 java
    echo "Done!"
    ;;
  *)
    echo "Usage: /etc/init.d/blah {start|stop}"
    exit 1
    ;;
esac

exit 0
