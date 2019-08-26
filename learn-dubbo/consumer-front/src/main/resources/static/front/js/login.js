		function memberLogin(){
			var $loginWindowForm = $("#loginWindowForm");
			var yzm = document.getElementById("yzm").value;
			var loginWindowMemberUsername=document.getElementById("loginWindowMemberUsername").value;
			var loginWindowMemberPassword=document.getElementById("loginWindowMemberPassword").value;
//			alert($loginWindowForm.serialize());
			if(loginWindowMemberUsername==""){
				alert("请输入用户名");
				return false;
			}
			if(loginWindowMemberPassword==""){
				alert("请输入密码");
				return false;
			}
			if(yzm == 0){
				alert("验证码错误");
				return false;
			}
			$.ajax({
				url:  "/user/frontLogin",
				data: $loginWindowForm.serialize(),
				type: "POST",
				cache: false,
				success: function(data) {
					// alert("hello1");
					if (data == "success") {
						alert("登录成功");
						// window.location.href="/front/index";
						// alert(document.referrer);
						if("http://www.iworkact.cn/user/frontRegister"==document.referrer){
                            window.location.href="/front/index";
						}else if("http://www.iworkact.cn/user/frontLogin" == document.referrer){
                            window.location.href="/front/index";
						}else if("http://www.iworkact.cn/modifyPassword?id".substring(0,36)==document.referrer.substring(0,36)){
                            window.location.href="/front/index";
						}else{
                            window.location.href=document.referrer;
						}
					}else if(data =="admin"){
						alert("登录失败，原因：您是管理员！！！");
					}else if(data == "frozen"){
                        alert("登录失败，原因：由于您的不良操作，您已经被冻结！！！");
                        window.location.href="/front/jdApply";
					}else if(data == "error"){
						alert("登录失败，原因：密码错误！！！");
					}else{
						alert("登录失败，原因：可能不存在这样的用户名！！！")
					}
				},
				complete: function() {
					// alert("hello");
				}
			});
		}
		
		
		function memberRegister(){
			var $registerWindowForm = $("#registerWindowForm");
			var $registerWindowMemberUsername = document.getElementById("username").value;
			var $registerWindowMemberPassword = document.getElementById("password").value;
			var $registerWindowReMemberPassword = document.getElementById("confirm").value;
			var $registerWindowMemberEmail = document.getElementById("email").value;
//			var $registerWindowMemberContact = document.getElementById("contactWay").value;
//			var $registerWindowMemberEmail = document.getElementById("address").value;
//			var $registerWindowMemberEmail = document.getElementById("email").value;
//			alert($registerWindowForm.serialize());
			if ($registerWindowMemberUsername == "") {
				alert("请输入用户名")
				return false;
			}
			if (!/^[\u0391-\uFFE5\w]+$/.test($registerWindowMemberUsername)) {
//				$registerWindowMemberUsername.focus();
				alert("用户名只允许包含中文、英文、数字和下划线!");
				return false;
			}
			if(!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test($registerWindowMemberEmail)){
				alert("邮箱格式不正确");
				return false;
			}
			if ($registerWindowMemberUsername.length < 2 || $registerWindowMemberUsername.length > 20) {
//				$registerWindowMemberUsername.focus();
				alert("用户名长度只允许在2-20之间!");
				return false;
			}
			if ($registerWindowMemberPassword == "") {
//				$registerWindowMemberPassword.focus();
				alert("请输入密码!");
				return false;
			}
			if ($registerWindowMemberPassword.length < 4 || $registerWindowMemberPassword.length > 20) {
//				$registerWindowMemberPassword.focus();
				alert("密码长度只允许在4-20之间!");
				return false;
			}
			if ($registerWindowReMemberPassword == "") {
//				$registerWindowReMemberPassword.focus();
				alert("请输入重复密码!");
				return false;
			}
			if ($registerWindowReMemberPassword != $registerWindowMemberPassword) {
//				$registerWindowReMemberPassword.focus();
				alert("两次密码输入不相同!");
				return false;
			}
//			alert("asdfsdfsadfdsf");
			$.ajax({
				url: "/user/isExistUsername",
				data:{"username": $registerWindowMemberUsername},
				type: "POST",
				cache: false,
				success: function(data) {
					if (data == "true") {
						// alert("正常");
						$.ajax({
							url:  "/user/frontRegister",
							data: $registerWindowForm.serialize(),
							type: "POST",
							cache: false,
							success: function(data) {
								if (data == "success") {
									alert("注册成功");
									window.location.href="/user/frontLogin";
								}else{
									alert("注册失败");
								}
//								$.message({type: data.status, content: data.message});
							},
							complete: function() {
//								alert("error");
//								$registerWindowForm.find("button").attr("disabled", false);
//								$registerWindowCaptcha.val("");
//								registerWindowCaptchaImageRefresh();
							}
						});
					} else {
//						$registerWindowMemberUsername.focus();
						alert("用户名已存在,请重新输入!");
					}
				}
			});
			return false;
		}
		
		
		
		function updatepassword(){
			var $validateForm = $("#validateForm");
			var password=document.getElementById("password").value;
			var confirm=document.getElementById("confirm").value;
			// alert($validateForm.serialize());
			if(password==""){
				alert("请输入新的密码");
				return false;
			}
			if(confirm==""){
				alert("请再次输入新的密码");
				return false;
			}
			if(confirm != password){
				alert("两次输入的密码不一致");
				return false;
			}
			$.ajax({
				url:  "/user/updatePassword",
				data: $validateForm.serialize(),
				type: "POST",
				cache: false,
				success: function(data) {
					if (data == "success") {
						alert("修改密码成功");
//						$.message({type: data.status, content: data.message});
						window.location.href="/mycenter/myprofile";
					}else{
						alert("修改密码失败，请重试！！！");
					}
//					$.message({type: data.status, content: data.message});
//					if(redirectUrl != null) {
//						location.href = redirectUrl;
//					}
				},
				complete: function() {
//					$loginWindowForm.find("button").attr("disabled", false);
//					$loginWindowCaptcha.val("");
//					window.location.href="index.action"; 
				}
			});
		}


		//获取赛事信息

		function getSportInfo($id,count){
            $.ajax({
                url:  "/sport/getSportInfoById?id="+$id,
                type: "GET",
                cache: false,
                success: function(data) {
                    var result = data;
                    document.getElementById("name"+count).value = result.name;
                    document.getElementById("attendNum"+count).value = result.attendNum;
                    document.getElementById("content"+count).value = result.content;
                    document.getElementById("filePath"+count).src = "/upload/"+result.filePath;
                }
            });
		}
	//获取用户信息
        function getUser($id,count){
            $.ajax({
                url:  "/user/getUserById?id="+$id,
                type: "GET",
                cache: false,
                success: function(data) {
                    var result = data;
                    var userType= "";
                    if(result.type == 1){
                    	userType="管理员";
					}else{
                    	userType="普通用户";
					}
                    document.getElementById("username"+count).value = result.username;
                    document.getElementById("email"+count).value = result.email;
                    document.getElementById("phone"+count).value = result.phone;
                    document.getElementById("city"+count).value = result.city;
                    document.getElementById("type"+count).value = userType;
                }
            });
        }