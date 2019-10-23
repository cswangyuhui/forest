//控制是否显现
var permissionChecked = "";
var permissionOfRole = "";

function reset_permission(){
	$.ajax({
        type: 'GET',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "../permission/renew",//url
        //contentType: "application/json; charset=utf-8",
        success: function (result) {
            if (result.meta.success == true) {
                alertMessage("success","重置角色权限成功!");
                window.location.reload();
                return;
            }
            else {
                alertMessage("failure","Some error happend!");
                return;
            }
        },
        error: function () {
        	alertMessage("failure","接口错误，请联系管理员!");
        	return;
        }
    });
}

function  myModalConfirm(){
	if(permissionChecked == false)
	{
		$.ajax({
	        type: "POST",//方法类型
	        dataType: "json",//预期服务器返回的数据类型
	        url: "../permission/delete",
	        data: "psString="+permissionOfRole,
	        success: function (result) {
	            if (result.meta.success == true) {
	            	alertMessage("success",result.data);
	            	return;
	            }
	            if (result.meta.success == false) {
	                alertMessage("failure","Some error happend!");
	                return;
	            }
	        	//window.location.href = location.pathname;
	        },
	        error: function () {
	        	alertMessage("failure","接口异常!请联系管理员!");
	            return;
	            //window.location.href = location.pathname;
	        }
	    });
	}
	else{
		$.ajax({
	        type: "POST",//方法类型
	        dataType: "json",//预期服务器返回的数据类型
	        url: "../permission/add",
	        data: "psString="+permissionOfRole,
	        success: function (result) {
	            if (result.meta.success == true) {
	            	alertMessage("success",result.data);
	            	return;
	            }
	            if (result.meta.success == false) {
	                alertMessage("failure","Some error happend!");
	                return;
	            }
	        	//window.location.href = location.pathname;
	        },
	        error: function () {
	        	alertMessage("failure","接口异常!请联系管理员!");
	            return;
	            //window.location.href = location.pathname;
	        }
	    });
	}
}

function myModalCancel(){
	if(permissionChecked == false){
		$("#"+permissionOfRole).prop("checked", "checked");
	}
	else {
		$("#"+permissionOfRole).prop("checked", "");
	}
}

function checkboxInit(){
	$("[id^='Administrator_']").attr("disabled",true);
	$("[id^='group_leader_view_']").attr("disabled",true);
	$("[id^='group_member_view_']").attr("disabled",true);
	$("[id$='_view']").attr("disabled",true);
}

function  part_show(box) {
	// console.log(box.id);
	permissionOfRole = box.id;
	permissionChecked = $("#"+box.id).prop("checked");
	//alert(permissionChecked);
	$('#myModal').modal("show");
}

$(function () {
	$.ajax({
        type: 'GET',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "../permission/list",//url
        //contentType: "application/json; charset=utf-8",
        success: function (result) {
            if (result.meta.success == true) {
                //alertMessage("success",result.meta.message);
                
                $("#Administrator_delete_tree").prop("checked", "")
                var permissions = result.data;
                for (var ps of permissions){
                	$("#"+ps.role+"_"+ps.permission).prop("checked", "checked")
                }
            }
            else {
                alertMessage("failure",result.meta.message);
            }
        },
        error: function () {
        	alertMessage("failure","接口错误，请联系管理员!");
        }
    });
	checkboxInit();
	checkCookie();
    //alert(getCookie("token"));
    $.ajax({
        type: 'GET',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        beforeSend:function(request){
            request.setRequestHeader("token",getCookie("token"));
        },
        url: "../authorization/getPermission",//url
        //contentType: "application/json; charset=utf-8",
        //data: getCookie("token"),
        success: function (result) {
            if (result.meta.success == true) {
                var pss = result.data.permission;
                var username = result.data.username;
                var userrole = result.data.role;
                console.log(pss,username,userrole);
                $("#username").text(username);
                $("#rolenameInBar").text(formatRoleName(userrole));
                var editFlag = true;
                for(ps of pss){
                    if(ps == 'ps_change'){
                        editFlag = false;
                    }
                }
                if(editFlag == true){
                	$("[id^='Administrator_']").attr("disabled",true);
                	$("[id^='group_']").attr("disabled",true);
                }
                $("#reset_button").attr("disabled",editFlag);
            }
            else {
                alertMessage("failure",result.meta.message);
                window.location.href = "/system/login.html";
            }
        },
        error: function () {
            alertMessage("failure","接口错误，请联系管理员!");
            window.location.href = "/system/login.html";
        }
    });
});