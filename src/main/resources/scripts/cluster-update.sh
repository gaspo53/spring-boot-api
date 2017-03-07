#!/bin/bash
echo "################## CLUSTER UPDATE STARTED #######################"
USERNAME=root
HOSTS="unboundly-api-cluster-01 unboundly-api-cluster-02 unboundly-api-cluster-03 unboundly-api-cluster-04"
SCRIPT="cd /root/git/unboundly-api; git checkout master; git pull; /etc/init.d/unboundly-api stop; /etc/init.d/unboundly-api start"
for HOSTNAME in ${HOSTS} ; do
    echo "Performing scripts in ${HOSTNAME}"
    ssh "${USERNAME}@${HOSTNAME}" "${SCRIPT}"
done

echo "################## CLUSTER UPDATE FINISHED #######################"