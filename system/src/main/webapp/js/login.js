$().ready(function() {
    $("#login_form").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: {
                required: true,
                minlength: 6
            },
        },
        messages: {
            email: {
                required: "请输入邮箱",
                email: "请输入有效邮箱"
            },
            password: {
                required: "请输入密码",
                minlength: jQuery.format("密码不能小于{0}个字符")
            },
        }
    });
    /*第一步：首先到导入jquery-validate第三方资源，
    第二步：创建好form表单，初始化validate
    注意这里的login_form必须是form表单上的选择器，笔者因为将其设置在div上，控制台显示settings没有被定义的错误。
    这里的username和password都是form表单中的name值；rules是规则，message是提示的信息

    required:true表示该字段为必填，
    minlength表示长度至少为5，maxlength是html5支持的，所以不用在这里面设置
    equalTo表示与某某相同，后面接的是第一个值，"#id"或者是".class"
    message中对应的内容后面就是提示的文字信息。可以直接copy我的代码，然后根据自己的需要修改。
    */
    $("#register_form").validate({
        rules: {
            username: "required",
            password: {
                required: true,
                minlength: 6
            },
            rpassword: {
                equalTo: "#register_password"
            },
            email: {
                required: true,
                email: true
            }

        },
        messages: {
            username: "请输入用户名",
            password: {
                required: "请输入密码",
                minlength: jQuery.format("密码不能小于{0}个字符")
            },
            rpassword: {
                equalTo: "两次密码不一样"
            },
            email: {
                required: "请输入邮箱",
                email: "请输入有效邮箱"
            }


        },


        // 错误标签元素
        errorElement: 'span',

        // 错误提示位置
        errorPlacement: function (error, element) { // error是错误信息的提示元素，element是当前input域
            error.appendTo(element.parent()).prev('.success').remove(); //追加提示元素，去掉之前的成功元素
        },

        // 验证通过
        success: function (span) { // span 是错误的提示元素
            span.removeClass('error').addClass('success').siblings('.success').remove(); //追加成功样式，去掉错误样式和重复的成功样式
        }


    });
});
$(function() {
    $("#register_btn").click(function() {
        $("#register_form").css("display", "block");
        $("#login_form").css("display", "none");
    });
    $("#back_btn").click(function() {
        $("#register_form").css("display", "none");
        $("#login_form").css("display", "block");
    });
});

function login() {
    //var email = $("#email").val();
    //var password = $("#password").val();
    //var data = {"email": email, "password": password};
    //alert(JSON.stringify(data));
	var x=document.getElementsByClassName("label.error");
	console.log(x);
	if(x.length!=0){
        alertMessage("failure","您的输入不符合要求");
        return;
    }
    $.ajax({
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "users/login",
        data: $("#login_form").serialize(),
        success: function (result) {
            if (result.meta.success == true) {
                setCookie("token", result.data.userToken);
                //setCookie("user", result.data.name);
                //alert(result.data.userToken);
                window.location.href = "/system/home/forest_total.html";
            }
            if (result.meta.success == false) {
                alertMessage("failure","登陆失败!请检查账号和密码！");
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
}

function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";

}

function register() {
	var y=document.getElementsByClassName("error");
    if(y.length!=0){
        alertMessage("failure","您的输入不符合要求");
        return;
    }
    var email = $("#register_email").val();
    var password = $("#register_password").val();
    var name = $("#register_name").val();
    var data = "email="+email+"&name="+name+"&password="+password;
    //alert(data);
    $.ajax({
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "users/addUser",
        data: data,
        success: function (result) {
        	//alert(JSON.stringify(result));
            if (result.meta.success == true) {
                alertMessage("success",result.meta.message);
            }
            if (result.meta.success == false) {
                alertMessage("failure",result.meta.message);
                return;
            }
        },
        error: function () {
        	alertMessage("failure","接口异常！请联系管理员！");
            return;
        }
    });
}
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
