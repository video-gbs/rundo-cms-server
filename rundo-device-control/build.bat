@echo off
if not exist .env (
  echo .env file not exist
  goto end
)
set version=%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%
echo version=%version% > .env
docker build -f Dockerfile -t registry.cn-hangzhou.aliyuncs.com/hyy-rundo-gms/rundo-device-control:%version% .
docker push registry.cn-hangzhou.aliyuncs.com/hyy-rundo-gms/rundo-device-control:%version%
echo Build Successfully version=%version%