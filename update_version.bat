:: 项目信息指定
SET oldVersion=0.1.0-SNAPSHOT
SET newVersion=0.2.0
SET projectName=cache

:: maven 命令的执行
mvn versions:set -DgroupId=com.github.houbb -DartifactId=%projectName% -DoldVersion=%oldVersion% -DnewVersion=%newVersion%
mvn -N versions:update-child-modules
mvn versions:commit

:: 用于更新 maven 版本
:: author: houbb
:: LastUpdateTime:  2019-2-23 10:52:22
:: 用法：双击运行，或者当前路径 cmd 直接输入 update_version.bat