$(function () {

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
            		if(ps == 'add_plot'){
            			addFlag = false;
            			importFlag = false;
            		}
            		if(ps == 'delete_plot') deleteFlag = false;
            		if(ps == 'change_plot') editFlag = false;
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
	initJqGird();
    //2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();


});

function initJqGird() {
	$("#jqGrid").jqGrid({
        url: '../plots/list',
        datatype: "json",
        colNames:['大区号','小区号', '样地号','横坐标偏正北角度','纵坐标偏正北角度','形状','横坐标','纵坐标'],
        colModel: [
        	{name: 'area', index: 'area',width: 60,searchoptions:{sopt:['eq','ne']}},
            {name: 'stand', index: 'stand', width: 60,searchoptions:{sopt:['eq','ne']}},
            {name: 'plot', index: 'plot', width: 60,searchoptions:{sopt:['eq','ne']}},
            {name: 'xAxis', index: 'xAxis', width: 80,search:false},
            {name: 'yAxis', index: 'yAxis', width: 80,search:false},
            {name: 'shape', index: 'shape', width: 60,searchoptions:{sopt:['eq','ne']}},
            {name: 'longitude', index: 'longitude', width: 80,search:false},
            {name: 'latitude', index: 'latitude', width: 80,search:false}
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
        caption: "样地信息",
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
}
var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};
    /*
    * postdata.AREA
    * postdata.STAND
    * postdata.PLOT
    * postdata.XDIRECT
    * postdata.YDIRECT
    postdata.NOTE2 备注
    *
    *
    * */
    oInit.Init = function () {
        $("#btn_add").click(function () {
        	postdata.IDU = 'add';
            $("#myModalLabel").text("新增");
            $("#myModal").find(".form-control").val("");
            $('#txt_shape').val('长方形'); 
            $('#myModal').modal();
        });
        
        $('#plotAddConfirm').click(function () {
        	var data = $("#plotAddForm").serialize();
        	postdata.AREA = $("#txt_area").val();
            postdata.STAND = $("#txt_stand").val();
            postdata.PLOT = $("#txt_plot").val();
            postdata.XDIRECT = $("#txt_xdirect").val();
            postdata.YDIRECT = $("#txt_ydirect").val();
            postdata.SHAPE = $("#txt_shape option:selected").val();
            postdata.longitude = $("#txt_longitude").val();
            postdata.latitude = $("#txt_latitude").val();
            if(isNull(postdata.AREA) == true||isNull(postdata.STAND) == true ||
            		isNull(postdata.PLOT) == true || isNull(postdata.XDIRECT) == true || 
            		isNull(postdata.YDIRECT) == true || isNull(postdata.SHAPE) == true ||
            		isNull(postdata.longitude) == true || isNull(postdata.latitude))
            {
            	alertMessage("failure","请按要求输入数据");
            	return;
            }
            if(isNumber(postdata.XDIRECT) == false)
        	{
            	alertMessage("failure","横坐标偏正北角度必须为数值(角度值)");
            	return;
        	}
            if(isNumber(postdata.YDIRECT) == false)
        	{
            	alertMessage("failure","纵坐标偏正北角度必须为数值(角度值)");
            	return;
        	}
            if(isNumber(postdata.longitude) == false || isNumber(postdata.latitude) == false)
        	{
            	alertMessage("failure","输入的经纬度必须为数值");
            	return;
        	}
            if(postdata.IDU != 'edit'){
            	$.ajax({
                    type: 'POST',//方法类型
                    dataType: "json",//预期服务器返回的数据类型
                    url:'../plots/plotAdd',//url
                    //contentType: "application/json; charset=utf-8",
                    data: data,
                    success: function (result) {
                        if (result.meta.success == true) {
                            alertMessage("success",result.meta.message);
                            //reload
                            reload();
                        }
                        else {
                            alertMessage("failure",result.meta.message);
                        }
                    },
                    error: function () {
                    	alertMessage("failure","接口错误，请联系管理员!");
                    }
                });
            	return;
        	}
            $.ajax({
                type: 'POST',//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url:'../plots/plotEdit',//url
                //contentType: "application/json; charset=utf-8",
                data: data,
                success: function (result) {
                    if (result.meta.success == true) {
                        alertMessage("success",result.meta.message);
                        //reload
                        reload();
                    }
                    else {
                        alertMessage("failure",result.meta.message);
                    }
                },
                error: function () {
                	alertMessage("failure","接口错误，请联系管理员!");
                },
                complete: function() {
                	$("#txt_area").removeAttr("readonly");
                    $("#txt_stand").removeAttr("readonly");
                    $("#txt_plot").removeAttr("readonly");
                }
            });
        });

        $("#btn_edit").click(function () {
        	$("#myModalLabel").text("修改");
        	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
        	if(ids.length != 1)
        	{
        		alertMessage("warning","请选择一行进行修改操作");
        		return;
        	}
            
            $("#txt_area").val($("#jqGrid").jqGrid('getCell',ids[0],'area'));
            $("#txt_stand").val($("#jqGrid").jqGrid('getCell',ids[0],'stand'));
            $("#txt_plot").val($("#jqGrid").jqGrid('getCell',ids[0],'plot'));
            $("#txt_shape").val($("#jqGrid").jqGrid('getCell',ids[0],'shape'));
            $("#txt_xdirect").val($("#jqGrid").jqGrid('getCell',ids[0],'xAxis'));
            $("#txt_ydirect").val($("#jqGrid").jqGrid('getCell',ids[0],'yAxis'));
            $("#txt_longitude").val($("#jqGrid").jqGrid('getCell',ids[0],'longitude'));
            $("#txt_latitude").val($("#jqGrid").jqGrid('getCell',ids[0],'latitude'));

            postdata.IDU = 'edit';
            $("#txt_area").attr("readonly", "readonly");
            $("#txt_stand").attr("readonly", "readonly");
            $("#txt_plot").attr("readonly", "readonly");
            $('#myModal').modal();
        });

        $("#btn_delete").click(function () {
            var paramArea = [];
        	var paramStand = [];
        	var paramPlot = [];
        	var ids = $("#jqGrid").jqGrid('getGridParam','selarrrow');
        	if(ids.length < 1)
        	{
        		alertMessage("warning","请至少选择一行有效数据");
        		return;
        	}
        	jQuery(ids).each(function(){
                paramArea.push($("#jqGrid").jqGrid('getCell',this,'area'));
                paramStand.push($("#jqGrid").jqGrid('getCell',this,'stand'));
                paramPlot.push($("#jqGrid").jqGrid('getCell',this,'plot'));
            });
        	$.ajax({
                type: 'POST',//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: '../plots/deletePlot',//url
                //contentType: "application/json; charset=utf-8",
                traditional: true,
                data: {paramArea:paramArea,paramStand:paramStand,paramPlot:paramPlot},
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
        });

        $("#btn_submit").click(function () {
            postdata.AREA = $("#txt_area").val();
            postdata.STAND = $("#txt_stand").val();
            postdata.PLOT = $("#txt_plot").val();
            postdata.XDIRECT = $("#txt_xdirect").val();
            postdata.YDIRECT = $("#txt_ydirect").val();
            postdata.SHAPE = $("#txt_shape option:selected").val();
            postdata.NOTE2=$("#txt_note2").val();

            $.ajax({
                type: "post",
                url: "/Home/GetEdit",
                data: { "": JSON.stringify(postdata) },
                success: function (data, status) {
                    if (status === "success") {
                        toastr.success('提交数据成功');
                        $("#tb_departments2").bootstrapTable('refresh');
                    }
                },
                error: function () {
                    toastr.error('Error');
                },
                complete: function () {

                }

            });
        });

        $("#btn_query").click(function () {
            $("#tb_departments2").bootstrapTable('refresh');
        });
    };

    return oInit;
};

/**
* jqGrid重新加载
*/
function reload() {
   var page = $("#jqGrid").jqGrid('getGridParam', 'page');
   $("#jqGrid").jqGrid('setGridParam', {
       page: page
   }).trigger("reloadGrid");
}

function isNull(val){
	if(val == "" || val == null){
        return true;
	}
	return false;
}

function isNumber(val){

    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }

}