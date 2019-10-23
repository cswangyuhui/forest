

window.onload = function() {
	$.ajax({
        type: 'GET',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url:'../plots/getPlots',//url
        //contentType: "application/json; charset=utf-8",
        success: function (result) {
            if (result.meta.success == true) {
            	$("#selectArea").empty();
            	$("#selectStand").empty();
            	$("#selectPlot").empty();
            	var plots = JSON.parse(result.data);
            	var areaSet = new Set();
            	var standSet = new Set();
            	var plotSet = new Set();
            	for(var i=0;i<plots.length;i++)
        		{
            		areaSet.add(plots[i].area);
            		standSet.add(plots[i].stand);
            		plotSet.add(plots[i].plot);
        		}
            	for(var i of areaSet)
        		{
            		$("#selectArea").append("<option value='"+i+"'>"+i+"</option>");
        		}
            	for(var i of standSet)
        		{
            		$("#selectStand").append("<option value='"+i+"'>"+i+"</option>");
        		}
            	for(var i of plotSet)
        		{
            		$("#selectPlot").append("<option value='"+i+"'>"+i+"</option>");
        		}
            	$("#myTextArea").val(result.data);
            }
            else {
            	$("#myTextArea").val(result.meta.message);
            }
        },
        error: function () {
        	$("#myTextArea").val("接口错误，请联系管理员!");
        }
    });
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
                var Flag = true;
                for(ps of pss){
                    if(ps == 'view_map_in') Flag = false;
                }
                $("#map_in_search").attr('disabled',Flag);
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
}

$("#map_in_search").click(function() {
	var area = $("#selectArea").val();
	var stand = $("#selectStand").val();
	var plot = $("#selectPlot").val();
	//alert(area);
	$.ajax({
        type: 'POST',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url:'../plots/getTreesOfPlot',//url
        //contentType: "application/json; charset=utf-8",
        data:{area:area,stand:stand,plot:plot},
        success: function (result) {
            if (result.meta.success == true) {
            	var showPlot = result.data.plot;
            	var showTrees = result.data.trees;
            	var liveTrees = result.data.livetrees;
            	console.log("showTreesLength:"+showTrees.length+" liveTreesLength:"+liveTrees.length);
            	//alert(showPlot);
            	//alert(showTrees);
            	//$("#myTextArea").val("该样地信息如下:\n");
            	//$("#myTextArea").val($("#myTextArea").val()+JSON.stringify(showPlot)+"\n");
            	//$("#myTextArea").val($("#myTextArea").val()+"样地树木信息如下:\n");
            	//$("#myTextArea").val($("#myTextArea").val()+JSON.stringify(showTrees)+"\n");
              	//alert(result.data.trees);
              	//alert(result.data.trees[0].historyId);
              	var dataLength = showTrees.length;
              	var uniqueTreeList = new Array();
              	function searchTreeType(searchType) {
              		var loc = 0;
					for(;loc< uniqueTreeList.length;loc++)
					{
						//console.log("%o",uniqueTreeList[0]);
						//console.log(searchType);
						if(uniqueTreeList[loc].treeType == searchType)
							return loc;
					}
					return -1;
				}
              	for(var i=0;i<dataLength;i++)
          		{
              		var searchLoc = searchTreeType(showTrees[i].treeType);
              		// alert(searchLoc);
              		if(searchLoc == -1)
              		{
              			if(showTrees[i].isDead == 0){
              			var treeElement = new Object();
              			treeElement.treeType =  showTrees[i].treeType;
              			treeElement.detail = new Object();
              			treeElement.detail.name = showTrees[i].treeType;
              			treeElement.detail.name = showTrees[i].treeType;
              			treeElement.detail.type = 'scatter';
              			treeElement.detail.data = [];
              			var locList = [];
              			locList.push(showTrees[i].locationX);
              			locList.push(showTrees[i].locationY);
              			//console.log("^^^"+showTrees[i].locationX+" "+showTrees[i].locationY+" "+showTrees[i].uniqueId);
              			locList.push(showTrees[i].uniqueId);
              			locList.push(liveTrees[i].height);
              			locList.push(liveTrees[i].dbh);
              			locList.push(liveTrees[i].blc);
              			locList.push(liveTrees[i].cwe);
              			locList.push(liveTrees[i].cww);
              			locList.push(liveTrees[i].cws);
              			locList.push(liveTrees[i].cwn);
              			treeElement.detail.data.push(locList);
              			uniqueTreeList.push(treeElement);
              			}
              		}
              		else{
              			if(showTrees[i].isDead == 0)
              			{
              				var treeElement = uniqueTreeList[searchLoc];
                  			var locList = [];
                  			locList.push(showTrees[i].locationX);
                  			locList.push(showTrees[i].locationY);
                  			locList.push(showTrees[i].uniqueId);
                  			locList.push(liveTrees[i].dbh);
                  			locList.push(liveTrees[i].blc);
                  			locList.push(liveTrees[i].cwe);
                  			locList.push(liveTrees[i].cww);
                  			locList.push(liveTrees[i].cws);
                  			locList.push(liveTrees[i].cwn);
                  			treeElement.detail.data.push(locList);
              			}
              		}
          		}
              	console.log(uniqueTreeList.length);
              	console.log("%o",uniqueTreeList[0]);
              	//for(var item in uniqueTreeList)
              		// console.log(item);
				var showTypeDetail = [];
				for(var i=0;i<uniqueTreeList.length;i++)
				{
					showTypeDetail.push(uniqueTreeList[i].treeType);
				}
				var showLocDetail = new Array();
				for(var i=0;i<uniqueTreeList.length;i++)
				{
					showLocDetail.push(uniqueTreeList[i].detail);
				}
				//alert(showTypeDetail);
				console.log(showLocDetail);
				console.log(JSON.stringify(showLocDetail));
              	//console.log("dataLength"+dataLength);
            	var dom = document.getElementById("total_map");
         	 	//console.log(dom);
				var myChart = echarts.init(dom);
				var app = {};
				option = null;
				option = {
				    title : {
				        text: '样地号：'+showPlot.area+'/'+showPlot.stand+'/'+showPlot.plot,
				        subtext: '树木分布'
				    },
				    grid: {
				        left: '3%',
				        right: '7%',
				        bottom: '3%',
				        containLabel: true
				    },
				    tooltip : {
				        // trigger: 'axis',
				        showDelay : 0,
				        formatter : function (params) {
				            if (params.value.length > 1) {
				                return params.seriesName + ' :<br/><br/>'
				                	+ '唯一坐标:'+params.value[2] + '<br/><br/>'
				                	+ '胸高直径:'+params.value[3] + '<br/><br/>'
				                	+ '活枝高:'+params.value[4] + '<br/><br/>'
				                	+ '冠幅东:'+params.value[5] + ' '
				                	+ '冠幅西:'+params.value[6] + '<br/><br/>'
				                	+ '冠幅南:'+params.value[7] + ' '
				                	+ '冠幅北:'+params.value[8] + '<br/><br/>'
				                    + '横坐标:'+params.value[0] + 'm '
				                    + '纵坐标:'+params.value[1] + 'm<br/><br/>'
				            }
				            else {
				                return params.seriesName + ' :<br/>'
				                    + params.name + ' : '
				                    + params.value + 'm ';
				            }
				        },
				        axisPointer:{
				            show: true,
				            type : 'cross',
				            lineStyle: {
				                type : 'dashed',
				                width : 1
				            }
				        }
				    },
				    toolbox: {
				        feature: {
				            dataZoom: {},
				            brush: {
				                type: ['rect', 'polygon', 'clear']
				            }
				        }
				    },
				    brush: {
				    },
				    legend: {
				        data: showTypeDetail,
				        left: 'center'
				    },
				    xAxis : [
				        {
				            type : 'value',
				            scale:true,
				            axisLabel : {
				                formatter: '{value} m'
				            },
				            splitLine: {
				                show: false
				            }
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value',
				            scale:true,
				            axisLabel : {
				                formatter: '{value} m'
				            },
				            splitLine: {
				                show: false
				            }
				        }
				    ],
				    series : showLocDetail
				};
				;
				if (option && typeof option === "object") {
				    myChart.setOption(option, true);
				}
				var unique3=new Array();
                for(var i=0;i<uniqueTreeList.length;i++)
                {
                    var treeElement2= new Object();
                    treeElement2.value=uniqueTreeList[i].detail.data.length;
                    treeElement2.name=uniqueTreeList[i].detail.name;
                    //unique2.data.push(treeElement2);
                    unique3.push(treeElement2);
                }
				var dom2 = document.getElementById("total_map2");
                var myChart2 = echarts.init(dom2);

                option = {
                    title : {
                        text: '样地树木分布饼图',
                        //subtext: '纯属虚构',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        data: showTypeDetail,
                        left: 40
                    },
                    series : [
                        {
                            name: '树木:',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:unique3,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };

                ;
                if (option && typeof option === "object") {
                    myChart2.setOption(option, true);
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
})
