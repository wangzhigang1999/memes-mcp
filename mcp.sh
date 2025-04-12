image="registry.cn-beijing.aliyuncs.com/bupt2018/memes-mcp:latest"
# check image
if [ -z $image ]; then
    echo "image is empty, try to find image memes"
    exit 1
fi
# if image is not empty, pull image
docker pull $image

#  get the container id if it exists
container_id=$(docker ps -a | grep $image | awk '{print $1}')
# if container exists, stop and remove it
if [ -n "$container_id" ]; then
    echo "container exists, stop and remove it"
    docker stop "$container_id"
    docker rm "$container_id"
fi

# start container
docker run -d -p 9999:8000 \
    -e TZ=Asia/Shanghai \
    $image