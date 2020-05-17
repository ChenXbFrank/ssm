<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>电子邮件发送</title>
    <script type="text/javascript">
        function Checkform(myform){
            for(i=0;i<myform.length;i++){
                if(myform.elements[i].value==""){
                    alert(myform.elements[i].title+"不能为空！");
                    myform.elements[i].focus();
                    return false;
                }
            }
        }
    </script>
</head>
<body>
<form action="${pageContext.request.contextPath }/file/send" method="post" name="form1" id="form" onsubmit="return Checkform(form1)">
    收件人：<input type="text" name="receiver" title="收件人" id="receiver" size="60" value="810464826@qq.com"><p>
    发件人：<input type="text" name="sender" title="发件人" id="sender" size="60"><p>
    授权码：<input type="password" name="password" id="password" title="邮箱授权码" size="60" value="nzzcrklhgwjkbdef"><p>
    主&nbsp;&nbsp;&nbsp;&nbsp;题：<input type="text" name="subject" id="subject" title="邮件主题" size="60"><p>
    内&nbsp;&nbsp;&nbsp;&nbsp;容：<textarea rows="7" cols="59" name="content" id="content" title="邮件内容"></textarea><p>
    <input type="submit" name="submit" value="发送">
    <input type="reset" name="reset" value="重置">
</form>
</body>
</html>
