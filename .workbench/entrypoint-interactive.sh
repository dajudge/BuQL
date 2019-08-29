#! /bin/bash
exec sudo -u ${USER} -i sh -c "cd $(pwd); $@"