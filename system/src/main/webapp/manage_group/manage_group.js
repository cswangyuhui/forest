var addFlag = true;
var editFlag = true;
var deleteFlag = true;
logInUserName = ""
logInUserRole = ""
$(function () {

	$("#jqGrid").jqGrid({
        url: '../group/list',
        datatype: "json",
        colNames:['小组名','队长邮箱','队长名','队长角色'],
        colModel: [
        	{name: 'teamName', index: 'teamName',width: 110,searchoptions:{sopt:['eq','ne']}},
            {name: 'teamLeaderNumber', index: 'teamLeaderNumber', width: 110,searchoptions:{sopt:['eq','ne']}},
            {name: 'leaderName', index: 'leaderName',width: 110,searchoptions:{sopt:['eq','ne']}},
            {name: 'leaderRole', index: 'leaderRole',width: 110,formatter: myFormatter, unformat: myUnformatter,searchoptions:{sopt:['eq','ne']}}
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
        caption: "小组信息",
        subGrid: true,
        subGridOptions: {
        	//selectOnExpand: false,
        	//expandOnLoad: true
        },
        subGridRowExpanded: function(subgrid_id, row_id) {
        	var myUrl = "../group/getGroupMembers";
        	var myColNames = ['组员邮箱','组员名','组员角色','删除'];
        	var myColModel = [
                {name:"email",index:"email",width:70},
                {name:"name",index:"name",width:70},
                {name:"role",index:"role",width:70,formatter: myFormatter, unformat: myUnformatter},
                {name:"delete",index:"dbh",width:70}];
        	//alert(myUrl);
        	var team_leader_number = $("#jqGrid").jqGrid('getRowData',row_id)['teamLeaderNumber'];
            var subgrid_table_id, pager_id;
            subgrid_table_id = subgrid_id+"_t";
            pager_id = "p_"+subgrid_table_id;
            $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>");
            $("#"+subgrid_table_id).jqGrid({
                url:myUrl+"?email="+team_leader_number,
                datatype: "json",
                colNames: myColNames,
                colModel: myColModel,
                   autowidth: true,
                   rowNum:20,
                   height: '100%',
                   jsonReader: {
                       root: "data.list"
                   },
                   gridComplete: function () {
                	   var ids = jQuery("#"+subgrid_table_id).jqGrid('getDataIDs');
                	   for (var i = 0; i < ids.length; i++) {
                	   var id = ids[i];
                	   var memberEmail = jQuery("#"+subgrid_table_id).jqGrid('getRowData', ids[i])['email'];
                	   //alert(memberEmail);
                	   var DeleteBtn = "<a href='#' style='color:#f60' onclick='deleteGroupMember(this)' id="+team_leader_number+"*"+memberEmail+">Abolish</a>";
                	   jQuery("#"+subgrid_table_id).jqGrid('setRowData', ids[i], { delete: DeleteBtn })}
                   }
            })
            //jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false})
          },
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
    })
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
                logInUserName = result.data.role.email;
                logInUserRole = userrole;
                for(ps of pss){
                    if(ps == 'group_add'){
                        addFlag = false;
                    }
                    if(ps == 'group_delete') deleteFlag = false;
                    if(ps == 'group_change') editFlag = false;
                }
                $("#btn_add").attr('disabled',addFlag);
                $("#btn_edit").attr('disabled',editFlag);
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
})


function deleteGroupMember(t)
{
	if(editFlag == true){
		alertMessage("warning","您没有权限删除组员");
		return;
	}
	var leaderNumber = t.id.split("*")[0];
	if(logInUserRole!="Administrator" && leaderNumber!=logInUserName){
		alertMessage("warning","您没有权限删除"+leaderNumber+"小组的组员");
		return;
	}
	//alert(leaderNumber);
	//alert(memberNumber);
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: '../group/deleteGroupMember',//url
        //contentType: "application/json; charset=utf-8",
        traditional: true,
        data: "infString="+t.id,
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

function addGroup()
{
	$('#myModal').modal();
}

function groupAddConfirm(){
	var email = $("#txt_leader_number").val();
	var name = $("#txt_team_name").val();
	if(email == ""){
		alertMessage("warning","邮箱输入不能为空!");
		return;
	}
	if(name == "")
	{
		alertMessage("warning","小组名称输入不能为空!");
		return;
	}
	if(valideEmail(email) == false)
	{
		alertMessage("warning","请输入正确的邮箱!");
		return;
	}
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: '../group/addGroup',//url
        //contentType: "application/json; charset=utf-8",
        traditional: true,
        data: {email:email,name:name},
        success: function (result) {
            if (result.meta.success == true) {
                //closeModal();
                alertMessage("success",result.data);
                window.location.reload();
            }
            else {
                //closeModal();
                alertMessage("failure",result.meta.message);
            }
        },
        error: function () {
        	//closeModal();
        	alertMessage("failure","接口错误，请联系管理员!");
        }
    });
	
}

function deleteGroup()
{
	//alert("deleteGroup");
	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
	if(ids.length != 1)
	{
		alertMessage("warning","请选择一行进行修改操作");
		return;
	}
	if(logInUserRole!="Administrator" && $("#jqGrid").jqGrid('getCell',ids[0],'teamLeaderNumber') != logInUserName){
		alertMessage("warning","您没有权限删除"+$("#jqGrid").jqGrid('getCell',ids[0],'teamLeaderNumber')+"小组");
		return;
	}
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: '../group/deleteGroup',//url
        //contentType: "application/json; charset=utf-8",
        traditional: true,
        data: "email="+$("#jqGrid").jqGrid('getCell',ids[0],'teamLeaderNumber'),
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
function addGroupMember(){
	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
	if(ids.length != 1)
	{
		alertMessage("warning","请选择一行进行修改操作");
		return;
	}
	//alert(leader);
	$('#myModal2').modal();
}

function memberAddConfirm(){
	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
	leader = $("#jqGrid").jqGrid('getCell',ids[0],'teamLeaderNumber');
	//alert(leader);
	var email = $("#txt_member_number").val();
	if(email == "")
	{
		alertMessage("warning","邮箱输入不能为空!");
		return;
	}
	if(email == leader)
	{
		alertMessage("warning","请输入要添加的组员邮箱!");
		return;
	}
	if(valideEmail(email) == false)
	{
		alertMessage("warning","请输入正确的邮箱!");
		return;
	}
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: '../group/addMember',//url
        //contentType: "application/json; charset=utf-8",
        traditional: true,
        data: {leader:leader,member:email},
        success: function (result) {
            if (result.meta.success == true) {
                //closeModal();
                alertMessage("success",result.data);
                window.location.reload();
            }
            else {
                //closeModal();
                alertMessage("failure",result.meta.message);
            }
        },
        error: function () {
        	//closeModal();
        	alertMessage("failure","接口错误，请联系管理员!");
        }
    });
	
}

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

function valideEmail(email)
{
	var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
	if(!reg.test(email)) return false;
	else return true;
}