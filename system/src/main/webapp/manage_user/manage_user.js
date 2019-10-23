function myFormatter(cellvalue, options, rowObject) {
	if(cellvalue == "Administrator")
		return "管理员";
	else if(cellvalue == "group_leader")
		return "组长";
	else return "组员";
}

function myUnformatter(cellvalue, options, cell) {
	if(cellvalue == "管理员")
		return "Administrator";
	else if(cellvalue == "组长")
		return "group_leader";
	else return "group_member";
}

$(function () {

    //1.初始化Table
    //var oTable = new TableInit();
    //oTable.Init();
	$("#jqGrid").jqGrid({
        url: '../users/list',
        datatype: "json",
        colNames:['邮箱','账号名', '角色名'],
        colModel: [
        	{name: 'email', index: 'email',width: 60,searchoptions:{sopt:['eq','ne']}},
            {name: 'name', index: 'name', width: 60,searchoptions:{sopt:['eq','ne']}},
            {name: 'role', index: 'role', width: 60,formatter: myFormatter, unformat: myUnformatter,searchoptions:{sopt:['eq','ne']}}
        ],
        height: 485,
        rowNum: 50,
        rowList: [50,80,100],
        loadtext: '信息读取中...',
        styleUI: 'Bootstrap',
        rownumbers: true,
        rownumWidth: 35,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        caption: "用户信息",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        /*onSelectRow: function(id) {
        	alert(id);
        	 if($("#jqGrid").jqGrid('getRowData',id)['isDead'] == 0)
        	 {
        		 alert("haha");
        	 }	 
		},*/
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
	//$("#jqGrid").jqGrid('filterToolbar',{searchOperators:true});
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
                var addFlag = true;
                var deleteFlag = true;
                for(ps of pss){
                    if(ps == 'user_add'){
                        addFlag = false;
                    }
                    if(ps == 'user_delete') deleteFlag = false;
                }
                $("#btn_edit").attr('disabled',addFlag);
                $("#btn_delete").attr('disabled',deleteFlag);
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

function changUserRole()
{	
	
	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
	if(ids.length != 1)
	{
		alertMessage("warning","请选择一行进行修改操作");
		return;
	}
	$("#txt_email").val($("#jqGrid").jqGrid('getCell',ids[0],'email'));
	$("#txt_name").val($("#jqGrid").jqGrid('getCell',ids[0],'name'));
	$("#txt_email").attr("readonly","readonly");
	$("#txt_name").attr("readonly","readonly");
	var rolename = $("#jqGrid").jqGrid('getCell',ids[0],'role');
	$("#txt_role").val(rolename);
	$('#myModal').modal();
}

function changeConfirm(){
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: '../users/changeUserRole',//url
        //contentType: "application/json; charset=utf-8",
        traditional: true,
        data: {email:$("#txt_email").val(),role:$("#txt_role").val()},
        success: function (result) {
            if (result.meta.success == true) {
                //closeModal();
                alertMessage("success",result.data);
                window.location.reload();
            }
            else {
                //closeModal();
                alertMessage("failure","Some error happened!");
            }
        },
        error: function () {
        	//closeModal();
        	alertMessage("failure","接口错误，请联系管理员!");
        }
    });
}

function deleteUser(){
	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
	if(ids.length != 1)
	{
		alertMessage("warning","请选择一行进行修改操作");
		return;
	}
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: '../users/deleteUserByEmail',//url
        //contentType: "application/json; charset=utf-8",
        traditional: true,
        data: "email="+$("#jqGrid").jqGrid('getCell',ids[0],'email'),
        success: function (result) {
            if (result.meta.success == true) {
                //closeModal();
                alertMessage("success",result.data);
                window.location.reload();
            }
            else {
                //closeModal();
                alertMessage("failure","Some error happend!");
            }
        },
        error: function () {
        	//closeModal();
        	alertMessage("failure","接口错误，请联系管理员!");
        }
    });
}
