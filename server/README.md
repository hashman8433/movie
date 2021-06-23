
export LC_ALL="zh_CN.UTF-8"
export LANG="zh_CN.UTF-8"

#2020-10-06
bug

    1.分页错误 
    
优化 

    1.生成图片 由于影片长短 导致图片生成数量不同 
    2.搜索文件后缀 avi rmvb wmv
    
    
docker run -d --name mysql -v /opt/mysql-data/mysql:/opt/data/mysql5.7 -v /opt/mysql-data/mysql-conf:/opt/data/mysql5.7/1ysql-conf -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:5.7
docker run --name movie-redis -p 6380:6379 -d redis --requirepass "123456"
