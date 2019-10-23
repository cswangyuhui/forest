//提示框
function alertMessage(info,message){

	//参数设置，若用默认值可以省略以下面代

	    toastr.options = {

	        "closeButton": true, //是否显示关闭按钮

	        "debug": false, //是否使用debug模式

	        "positionClass": "toast-top-center",//弹出窗的位置

	        "showDuration": "300",//显示的动画时间

	        "hideDuration": "500",//消失的动画时间

	        "timeOut": "2000", //展现时间

	        "extendedTimeOut": "1000",//加长展示时间

	        "showEasing": "swing",//显示时的动画缓冲方式

	        "hideEasing": "linear",//消失时的动画缓冲方式

	        "showMethod": "fadeIn",//显示时的动画方式

	        "hideMethod": "fadeOut" //消失时的动画方式

	    };

	    if(info=="success") toastr.success(message);
	    else if(info=="failure") toastr.error(message);
	    else if(info=="warning") toastr.warning(message);
	    else if(info=="info") toastr.info(message);

	};



$(function () {
    //显示更改密码模态框
    $("#change_password").click(function () {
        $("#myModalLabel_changepassword").text("更改密码");
        $("#myModal_changepassword").find(".form-control").val("");
        $('#myModal_changepassword').modal()

    });
//是否要退出 提问框
    $("#sign_out").click(function () {
        var mymessage=confirm("确定要退出吗")         ;
        if(mymessage==true)
        {
            window.close(this);
            clearCookie();
        	window.location.href = "/system/login.html";
        }
    });
    //修改密码button点击事件
    $("#btn_newpassword").click(function(){
    	var oldpassword = $("#txt_oldpassword").val();
    	var newpassword = $("#txt_newpassword").val();
    	var confirmpassword = $("#txt_confirmpassword").val();
    	var username = $("#username").text();
    	console.log(oldpassword+" "+newpassword+" "+confirmpassword);
    	if(oldpassword == '' || newpassword == '' || confirmpassword == '')
    	{
    		alertMessage("warning","有输入框为空");
    		return;
		}
    	if(newpassword.length < 6)
    	{
    		alertMessage("warning","密码长度必须不小于六");
    		return;
		}
    	if(newpassword != confirmpassword)
    	{
    		alertMessage("warning","两次输入密码不一致");
    		return;
		}
    	$.ajax({
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "../users/renewPassword",
            data: {"oldPassword":oldpassword,"newPassword":newpassword,"username":username},
            success: function (result) {
                if (result.meta.success == true) {
                    alertMessage("success", result.data);
                }
                if (result.meta.success == false) {
                    alertMessage("failure",result.meta.message);
                    return;
                }
            	//window.location.href = location.pathname;
            },
            error: function () {
                /*$('.alert-danger').css("display", "none");
                showErrorInfo("接口异常，请联系管理员！");
                return;*/
            	alertMessage("failure","接口异常!请联系管理员!");
                return;
                //window.location.href = location.pathname;
            }
        });
    	
    });

    //显示用户名
    function setName(name)
    {
        var username=document.getElementById("username");
        username.innerHTML=name;
    }
//显示身份
    function showrole()
    {
        var userrole=document.getElementById("user_role");
        // userrole.innerHTML=???
    }
    //setName(getCookie("user"));
    /*$.ajax({
        type: "GET",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("token", getCookie("token"));
        },
        url: "../users/tokenToName",
        //contentType: "application/json; charset=utf-8",
        success: function (result) {
        	if(result.meta.success == true){
        		//alertMessage("success",JSON.stringify(result));
        		setName(result.data);
        		//alert("----");
        	}
        	else{
        		alertMessage("failure",JSON.stringify(result));
        	}
        },
        error: function () {
        	alertMessage("failure","接口异常，请联系管理员！");
        }
    });*/
    
    
});

/**
 * 读取cookie
 * @param name
 * @returns {null}
 */
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)){
        return unescape(arr[2]);
    }
    else
        return null;
}

/**
 * 删除cookie
 * @param name
 */
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null){
    	//alert(cval);
    	document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
    }
}

/**
 * 检查cookie
 */
function checkCookie() {
    if (getCookie("token") == null) {
        alert("未登录！");
        window.location.href = "../login.html";
    }
}
/**
 * 清除所有cookie
 */
function clearCookie(){
    var date=new Date();
    date.setTime(date.getTime()-10);
    var keys=document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
        for (var i =  keys.length; i--;)
          document.cookie=keys[i]+"=0; expire="+date.toGMTString()+"; path=/";
    }
}

function formatRoleName(english){
	if(english == 'group_member') return "组员";
	else if(english == 'group_leader') return "组长";
	else return "管理员";
}

