1. 打成jar包
	cd bin
	jar cvf JmeterPlugins-SSO.jar  kg
2. 将jar包复制到jmeter\lib\ext文件夹下，打开jmeter，函数助手中可以看到新增的函数

注意：传的参数不用引号扩起来，比如：${__LastTime(xxx)}是正确的   ${__LastTime("xxx")}是错误的。