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
    $.ajax({
        type: 'GET',//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "../plots/getPlots",//url
        //contentType: "application/json; charset=utf-8",
        //data: getCookie("token"),
        success: function (result) {
            if (result.meta.success == true) {
            	plots = JSON.parse(result.data)
                console.log(plots);
            	for(plot of plots){
            		console.log(plot);
            	}
            	
            	//页面显示逻辑
            	var dom = document.getElementById("container");
            	var myChart = echarts.init(dom);
            	var app = {};
            	option = null;
            	var data = [
            	    {name: '1-1-1',value: 'hehe'},
            	    {name: '1-1-2',value: "dd"},
            	    {name: '1-1-3',value: "dsds"},
            	];
            	var geoCoordMap = {
            	    '1-1-1':[130.1,43.3],
            	    '1-1-2':[130.166367,43.360332],
            	    '1-1-3':[130.167511,43.360424],
            	};

            	var convertData = function () {
            	    var res = [];
            	    for(plot of plots){
                		console.log(plot);
                		showInf = []
                		showInf.push(plot.longitude);
                		showInf.push(plot.latitude);
                		showInf.push(plot.shape);
                		res.push({
        	                name: "大区号:"+plot.area+" 小区号:"+plot.stand+" 样地号:"+plot.plot,
        	                value: showInf
        	            });
                	}
//            	    for (var i = 0; i < data.length; i++) {
//            	        var geoCoord = geoCoordMap[data[i].name];
//            	        if (geoCoord) {
//            	            res.push({
//            	                name: data[i].name,
//            	                value: geoCoord.concat(data[i].value)
//            	            });
//            	        }
//            	    }
//            	    console.log(res);
            	    return res;
            	};

            	option = {
            	    title: {
            	        text: '全国样地分布图',
            	        left: 'center'
            	    },
            	    tooltip : {
            	        //trigger: 'item'
	    	        	showDelay : 0,
				        formatter : function (params) {
				            return params.name+ ' :<br/><br/>'+
				            "经度:" + params.value[0]+' <br/>'+
				            "纬度:" + params.value[1]+' <br/>'+
				            "形状:" + params.value[2];
				        }
            	    },
            	    legend: {
            	        orient: 'vertical',
            	        y: 'bottom',
            	        x:'right',
            	        data:['样地信息'],
            	        textStyle: {
            	            color: '#fff'
            	        }
            	    },
            	    bmap: {
            	        center: [104.114129, 37.550339],
            	        zoom: 5,
            	        roam: true,
            	        mapStyle: {
            	            styleJson: [{
            	                'featureType': 'water',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'color': '#d1d1d1'
            	                }
            	            }, {
            	                'featureType': 'land',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'color': '#f3f3f3'
            	                }
            	            }, {
            	                'featureType': 'railway',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'visibility': 'off'
            	                }
            	            }, {
            	                'featureType': 'highway',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'color': '#fdfdfd'
            	                }
            	            }, {
            	                'featureType': 'highway',
            	                'elementType': 'labels',
            	                'stylers': {
            	                    'visibility': 'off'
            	                }
            	            }, {
            	                'featureType': 'arterial',
            	                'elementType': 'geometry',
            	                'stylers': {
            	                    'color': '#fefefe'
            	                }
            	            }, {
            	                'featureType': 'arterial',
            	                'elementType': 'geometry.fill',
            	                'stylers': {
            	                    'color': '#fefefe'
            	                }
            	            }, {
            	                'featureType': 'poi',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'visibility': 'off'
            	                }
            	            }, {
            	                'featureType': 'green',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'visibility': 'off'
            	                }
            	            }, {
            	                'featureType': 'subway',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'visibility': 'off'
            	                }
            	            }, {
            	                'featureType': 'manmade',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'color': '#d1d1d1'
            	                }
            	            }, {
            	                'featureType': 'local',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'color': '#d1d1d1'
            	                }
            	            }, {
            	                'featureType': 'arterial',
            	                'elementType': 'labels',
            	                'stylers': {
            	                    'visibility': 'off'
            	                }
            	            }, {
            	                'featureType': 'boundary',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'color': '#fefefe'
            	                }
            	            }, {
            	                'featureType': 'building',
            	                'elementType': 'all',
            	                'stylers': {
            	                    'color': '#d1d1d1'
            	                }
            	            }, {
            	                'featureType': 'label',
            	                'elementType': 'labels.text.fill',
            	                'stylers': {
            	                    'color': '#999999'
            	                }
            	            }]
            	        }
            	    },
            	    series : [
            	        {
            	            name: '样地信息：',
            	            type: 'scatter',
            	            coordinateSystem: 'bmap',
            	            data: convertData()/*[{name:'1-1-1',value:[130.1,43.3]},
            	            	{name:'1-1-2',value:[130.4,43.5]},
            	           {name:'1-1-3',value:[130.5,43.6]}]*/,
            	            symbolSize: 10/*function (val) {
            	                return val[2] / 10;
            	            }*/,
            	            label: {
            	                normal: {
            	                    formatter: '{b}',
            	                    position: 'right',
            	                    show: true
            	                },
            	                emphasis: {
            	                    show: true
            	                }
            	            },
            	            itemStyle: {
            	                normal: {
            	                    color: 'green'
            	                }
            	            }
            	        },
            	    ]
            	};
            	    
            	if (option && typeof option === "object") {
            	    myChart.setOption(option, true);
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
});