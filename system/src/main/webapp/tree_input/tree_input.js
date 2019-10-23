
function TreeInfo(history_id,unique_id,tree_num,locationX,locationY,tree_type,area,
		stand,plot,is_dead) {
    this.history_id = history_id;
    this.unique_id = unique_id;
    this.tree_num = tree_num;
    this.locationX = locationX;
    this.locationY = locationY;
    this.tree_type = tree_type;
    this.area = area;
    this.stand = stand;
    this.plot = plot;
    this.is_dead = is_dead;
}

var opertorType = "add";

$(function () {
	$("#jqGrid").jqGrid({
        url: '../trees/list',
        datatype: "json",
        colNames:['历史编码','唯一编码', '树牌号','大区号','小区号','样地号','横坐标','纵坐标','是否死亡','树种'],
        colModel: [
        	{name: 'historyId', index: 'history_id',width: 110,search:false},
            {name: 'uniqueId', index: 'unique_id', width: 110,searchoptions:{sopt:['eq','ne']}},
            {name: 'treeNum', index: 'tree_num', width: 60,searchoptions:{sopt:['eq','ne']}},
            {name: 'area', index: 'area', width: 50,searchoptions:{sopt:['eq','ne']}},
	        {name: 'stand', index: 'stand', width: 50,searchoptions:{sopt:['eq','ne']}},
            {name: 'plot', index: 'plot', width: 50,searchoptions:{sopt:['eq','ne']}},
            {name: 'locationX', index: 'locationX', width: 50,search:false},
            {name: 'locationY', index: 'locationY', width: 50,search:false},
            {name: 'isDead', index: 'is_dead', width: 50,searchoptions:{sopt:['eq','ne']}},
            {name: 'treeType', index: 'tree_type', width: 70,searchoptions:{sopt:['eq','ne']}}
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
        caption: "树木信息",
        subGrid: true,
        subGridOptions: {
        	//selectOnExpand: false,
        	//expandOnLoad: true
        },
        subGridRowExpanded: function(subgrid_id, row_id) {
        	var myUrl = "../trees/liveTree";
        	var myColNames = ['树高','活枝高','胸高直径','冠幅东','冠幅西','冠幅南','冠幅北','录入时间'];
        	var myColModel = [
                {name:"height",index:"height",width:70},
                {name:"blc",index:"blc",width:70},
                {name:"dbh",index:"dbh",width:70},
                {name:"cwe",index:"cwe",width:70},
                {name:"cww",index:"cww",width:70},
                {name:"cwn",index:"cwn",width:70},
                {name:"cws",index:"cws",width:70},
                {name:"datetime",index:"datetime",width:110,formatter:function(cellvalue){
              	  return formatTimeStamp(cellvalue);
              	  //return cellvalue;
                }}];
        	if(jQuery("#jqGrid").jqGrid('getRowData',row_id)['isDead'] != 0)
       	 	{
       		 	myUrl = "../trees/deadTree";
       		    myColNames = ['备注','死亡类型','死亡时间'];
       		    myColModel = [
                 {name:"note",index:"note",width:100},
                 {name:"number",index:"number",width:100},
                 {name:"datetime",index:"datetime",width:110,formatter:function(cellvalue){
               	  return formatTimeStamp(cellvalue);
               	  //return cellvalue;
                 }}];
       	 	}
        	//alert(myUrl);
        	var uniqueId = $("#jqGrid").jqGrid('getRowData',row_id)['uniqueId'];
            var subgrid_table_id, pager_id;
            subgrid_table_id = subgrid_id+"_t";
            pager_id = "p_"+subgrid_table_id;
            $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>");
            $("#"+subgrid_table_id).jqGrid({
                url:myUrl+"?unique_id="+uniqueId,
                datatype: "json",
                colNames: myColNames,
                colModel: myColModel,
                   autowidth: true,
                   rowNum:20,
                   height: '100%',
                   jsonReader: {
                       root: "data.list"
                   },
              });
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
    });
	$("#jqGrid").jqGrid('filterToolbar',{searchOperators:true});
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
            	var editFlag = true;
            	var deleteFlag = true;
            	var importFlag = true;
            	for(ps of pss){
            		if(ps == 'add_tree'){
            			addFlag = false;
            			importFlag = false;
            		}
            		if(ps == 'delete_tree') deleteFlag = false;
            		if(ps == 'change_tree') editFlag = false;
            	}
            	$("#btn_add").attr('disabled',addFlag);
            	$("#btn_edit").attr('disabled',editFlag);
            	$("#btn_delete").attr('disabled',deleteFlag);
            	$("#import_excel").attr('disabled',addFlag);
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
	$("#gsh_jqGrid_rn").find("div").text("筛选");
});

/**
* jqGrid重新加载
*/
function reload() {
   reset();
   var page = $("#jqGrid").jqGrid('getGridParam', 'page');
   $("#jqGrid").jqGrid('setGridParam', {
       page: page
   }).trigger("reloadGrid");
}

function formatTimeStamp(timestamp) {
    var date = new Date(timestamp);
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = (date.getDate()< 10 ? '0'+ date.getDate() : date.getDate()) + ' ';
    h = (date.getHours()< 10 ? '0'+ date.getHours() : date.getHours()) + ':';
    m = (date.getMinutes()< 10 ? '0'+ date.getMinutes() : date.getMinutes()) + ':';
    s = date.getSeconds()< 10 ? '0'+ date.getSeconds() : date.getSeconds();
    return Y+M+D+h+m+s;
}

function addNewTree() {
	opertorType = "add";
	$("#txt_idu").attr("readOnly",false);
	var modal = new Custombox.modal({
        content: {
            effect: 'fadein',
            target: '#modalAdd',
            onClose: function() {
            	closeModal();
			},
            close: false
        }
    });
    modal.open();
}

//添加Modal确认
$('#mainAddConfirm').click(function () {
    //closeModal();
    Custombox.modal.closeAll();
    if($('#select_dead').val() == 0){
    	var modal = new Custombox.modal({
            content: {
                effect: 'fadein',
                target: '#LiveTreeAdd'
            }
        });
        modal.open();
    }
    else{
    	datepickerInit();
    	var modal = new Custombox.modal({
            content: {
                effect: 'fadein',
                target: '#DeadTreeAdd'
            }
        });
        modal.open();
    }
    
})

$('#LiveTreeConfirm').click(function (){
	var myUrl = '../trees/liveTreeAdd';
	if(opertorType == "edit"){
		myUrl = '../trees/liveTreeEdit';
	}
	var data = $("#modalAddForm").serialize()+"&"+$("#LiveTreeAddForm").serialize();
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: myUrl,//url
        //contentType: "application/json; charset=utf-8",
        data: data,
        success: function (result) {
            if (result.meta.success == true) {
                closeModal();
                alertMessage("success",result.meta.message);
                //reload
                reload();
            }
            else {
                closeModal();
                alertMessage("failure",result.meta.message);
            }
        },
        error: function () {
        	closeModal();
        	alertMessage("failure","接口错误，请联系管理员!");
        }
    });
})

$('#DeadTreeConfirm').click(function (){
	var myUrl = '../trees/deadTreeAdd';
		if(opertorType == "edit"){
			myUrl = '../trees/deadTreeEdit';
		}
	var data = $("#modalAddForm").serialize()+"&"+$("#DeadTreeAddForm").serialize();
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: myUrl,//url
        //contentType: "application/json; charset=utf-8",
        data: data,
        success: function (result) {
            if (result.meta.success == true) {
                closeModal();
                alertMessage("success",result.meta.message);
                //reload
                reload();
            }
            else {
                closeModal();
                alertMessage("failure",result.meta.message);
            }
        },
        error: function () {
        	closeModal();
        	alertMessage("failure","接口错误，请联系管理员!");
        }
    });
})

$('#exportModalConfirm').click(function () {
	var data = $("#exportModalForm").serialize();
	var url = "../files/exportTreeExcel?"+ data;
	alertMessage("info","导出数据中，请稍等......")
	window.location.href = url;
})

//添加Modal关闭
$('#mainAddCancel').click(function () {
    closeModal();
})
$('#LiveTreeAddCancel').click(function () {
    closeModal();
})
$('#DeadTreeAddCancel').click(function () {
    closeModal();
})
$('#exportModalCancel').click(function () {
    closeModal();
})

//编辑Modal关闭
$('#mainEditCancel').click(function () {
    closeModal();
})

function closeModal() {
	reset();
    Custombox.modal.closeAll();
}

function reset() {
    //隐藏错误提示框
    //$('.add-error-info').css("display", "none");
    //$('.edit-error-info').css("display", "none");
    //清空数据
    $('#txt_idt').val('');
    $('#txt_idu').val('');
    $('#txt_treeNum').val('');
    $('#txt_x').val('');
    $('#txt_y').val('');
    $('#txt_sp').val('');
    $('#txt_area').val('');
    $('#txt_stand').val('');
    $('#txt_plot').val('');
    $('#select_dead').val(0); 
    
    $('#txt_height').val('');
    $('#txt_blc').val('');
    $('#txt_dbh').val('');
    $('#txt_cwe').val('');
    $('#txt_cww').val('');
    $('#txt_cws').val('');
    $('#txt_cwn').val('');
    
    $('#export_area').val('');
    $('#export_stand').val('');
    $('#export_plot').val('');
    $('#export_dead').val(-1);
    
    $('#txt_note').val('');
    $('#txt_dead_tree').val('');
    $('#datetimepicker').val('2018-01-01 00:00:00');
}

function datepickerInit(){
	$.fn.datetimepicker.dates['zh-CN'] = {
    		days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
    		daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
    		daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
    		months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    		monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    		today: "今天",
    		suffix: [],
    		meridiem: ["上午", "下午"]
    };
    $('#datetimepicker').datetimepicker({
        format: 'yyyy-mm-dd hh:ii:ss',
        autoclose: true,
        todayBtn: true,
        language:'zh-CN',
        pickerPosition:"top-right"
    });
}

function deleteTree(){
	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
	if(ids.length == 0)
	{
		alertMessage("warning","请至少选择一行");
		return;
	}
	$('#deleteConfirm').modal('show');
}

function treeImport() {
	$('#importModal').modal('show');
}

new AjaxUpload('#uploadTree', {
    action: '../files/importTreeExcel',
    name: 'file',
    autoSubmit: true,
    responseType: "json",
    onSubmit: function (file, extension) {
       //文件格式限制
        if (!(extension && /^(xlsx)$/.test(extension.toLowerCase()))) {
        	alertMessage("failure","请选择xlsx文件");
            return false;
        }
    },
    onComplete: function (file, r) {
    	$('#importModal').modal('hide');
          //请求成功后的处理
        if (r.meta.success == true) {
                //提示用户
            alertMessage("success","成功导入" + r.data + "条记录！");
              //列表数据重新加载
            reload();
        } else {
        	alertMessage("failure","导入失败");
        }
    }
});

function treeExport() {
	$.ajax({
        type: 'GET',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: '../files/getSelectOption',//url
        //contentType: "application/json; charset=utf-8",
        traditional: true,
        success: function (result) {
            if (result.meta.success == true) {
            	var data = JSON.parse(result.data);
            	var area = data.area;
            	var stand = data.stand;
            	var plot = data.plot;
            	$("#export_area").empty();
            	$("#export_area").append('<option value="0">无</option>');
            	$("#export_stand").empty();
            	$("#export_stand").append('<option value="0">无</option>');
            	$("#export_plot").empty();
            	$("#export_plot").append('<option value="0">无</option>');
            	for(var i in area)
        		{
            		var str = '<option value="' + area[i] + '">' + area[i] + '</option>';
            		$("#export_area").append(str);
        		}
            	for(var i in stand)
        		{
            		var str = '<option value="' + stand[i] + '">' + stand[i] + '</option>';
            		$("#export_stand").append(str);
        		}
            	for(var i in plot)
        		{
            		var str = '<option value="' + plot[i] + '">' + plot[i] + '</option>';
            		$("#export_plot").append(str);
        		}
            	var modal = new Custombox.modal({
                    content: {
                        effect: 'fadein',
                        target: '#exportModal'
                    }
                });
                modal.open();
            }
            else {
                //closeModal();
                alertMessage("failure","服务器错误发生");
            }
        },
        error: function () {
        	//closeModal();
        	alertMessage("failure","接口错误，请联系管理员!");
        }
    });
}

function deleteSubmit(){
	$('#deleteConfirm').modal('hide')
	var paramId = [];
	var paramIsDead = [];
	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
	jQuery(ids).each(function(){
        paramId.push($("#jqGrid").jqGrid('getCell',this,'uniqueId'));
        paramIsDead.push($("#jqGrid").jqGrid('getCell',this,'isDead'));
    });
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: '../trees/deleteTree',//url
        //contentType: "application/json; charset=utf-8",
        traditional: true,
        data: {paramId:paramId,paramIsDead:paramIsDead},
        success: function (result) {
            if (result.meta.success == true) {
                //closeModal();
                alertMessage("success",result.meta.message);
                //reload
                reload();
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

function editTree(){
	opertorType = "edit";
	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
	if(ids.length != 1)
	{
		alertMessage("warning","请选择一行进行修改操作");
		return;
	}
	$('#txt_idt').val($("#jqGrid").jqGrid('getCell',ids[0],'historyId'));
	$('#txt_idu').val($("#jqGrid").jqGrid('getCell',ids[0],'uniqueId'));
	$("#txt_idu").attr("readOnly",true);
    $('#txt_treeNum').val($("#jqGrid").jqGrid('getCell',ids[0],'treeNum'));
    $('#txt_x').val($("#jqGrid").jqGrid('getCell',ids[0],'locationX'));
    $('#txt_y').val($("#jqGrid").jqGrid('getCell',ids[0],'locationY'));
    $('#txt_sp').val($("#jqGrid").jqGrid('getCell',ids[0],'treeType'));
    $('#txt_area').val($("#jqGrid").jqGrid('getCell',ids[0],'area'));
    $('#txt_stand').val($("#jqGrid").jqGrid('getCell',ids[0],'stand'));
    $('#txt_plot').val($("#jqGrid").jqGrid('getCell',ids[0],'plot'));
    $('#select_dead').val($("#jqGrid").jqGrid('getCell',ids[0],'isDead')); 
    //console.log($("#jqGrid_"+ids[0]+"_t").jqGrid('getRowData',1));
    if($('#select_dead').val() == 0){
    	$('#txt_height').val($("#jqGrid_"+ids[0]+"_t").jqGrid('getCell',1,'height'));
        $('#txt_blc').val($("#jqGrid_"+ids[0]+"_t").jqGrid('getCell',1,'blc'));
        $('#txt_dbh').val($("#jqGrid_"+ids[0]+"_t").jqGrid('getCell',1,'dbh'));
        $('#txt_cwe').val($("#jqGrid_"+ids[0]+"_t").jqGrid('getCell',1,'cwe'));
        $('#txt_cww').val($("#jqGrid_"+ids[0]+"_t").jqGrid('getCell',1,'cww'));
        $('#txt_cws').val($("#jqGrid_"+ids[0]+"_t").jqGrid('getCell',1,'cws'));
        $('#txt_cwn').val($("#jqGrid_"+ids[0]+"_t").jqGrid('getCell',1,'cwn'));
    }
    var modal = new Custombox.modal({
        content: {
            effect: 'fadein',
            target: '#modalAdd'
        }
    });
    modal.open();
}