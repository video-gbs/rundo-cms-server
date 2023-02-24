@echo off
if not exist .env (
  echo version.txt file not exist
  goto end
)

for /f "tokens=1,2 delims==" %%i in (.env) do (
  docker build -f Dockerfile -t registry.cn-hangzhou.aliyuncs.com/hyy-rundo-gms/rundo-parsing-engine:%%j .
  docker push registry.cn-hangzhou.aliyuncs.com/hyy-rundo-gms/rundo-parsing-engine:%%j
)