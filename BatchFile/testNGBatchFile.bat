set projectLocation=C:\Users\pavan.kumar\eclipse-workspace\qualidex


cd %projectLocation%



set classpath=%projectLocation%\target\*;%projectLocation%\lib\*



java org.testng.TestNG %projectLocation%\testng2.xml



pause