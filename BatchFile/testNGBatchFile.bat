set projectLocation=C:\Users\pavan.kumar\eclipse-workspace\qualidex_new


cd %projectLocation%



set classpath=%projectLocation%\target\*;%projectLocation%\lib\*



java org.testng.TestNG %projectLocation%\testng.xml



pause