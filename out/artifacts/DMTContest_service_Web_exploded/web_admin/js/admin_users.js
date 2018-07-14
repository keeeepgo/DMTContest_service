var alldata_url = 'http://localhost:8080/AllUsers';
var table_cols = [
                [{
                    field: 'userId',
                    title: 'ID',
                    width: '10%',
                    sort: true
                }, {
                    field: 'userName',
                    title: '用户名',
                    width: '30%',
                    sort: true
                }, {
                    field: 'registerDate',
                    title: '注册日期',
                    width: '30%'
                }, {
                    field: 'loginDate',
                    title: '最近登录日期',
                    width: '30%'
                }, {
                    fixed: 'right',
                    width: 200,
                    align: 'center',
                    toolbar: '#bar'
                }]
            ]
var sevenLine;
function formatDate(res) {
    for (var i = 0; i < res.data.length; ++i) {
        res.data[i]["realreigsterDate"] = res.data[i]["registerDate"];
        res.data[i]["showregisterDate"] = new Date(res.data[i]["registerDate"]).toLocaleString();
        var temp = new Date(res.data[i]["registerDate"]);
        temp.setMonth(temp.getMonth()+1);
        res.data[i]["registerDate"] = temp.getFullYear()+"-"+temp.getMonth()+"-"+temp.getDate();

        res.data[i]["realloginDate"] = res.data[i]["loginDate"];
        res.data[i]["showloginDate"] = new Date(res.data[i]["loginDate"]).toLocaleString();
        temp = new Date(res.data[i]["loginDate"]);
        temp.setMonth(temp.getMonth()+1);
        res.data[i]["loginDate"] = temp.getFullYear()+"-"+temp.getMonth()+"-"+temp.getDate();
    }
    return res;
}           

function freshtable() {
    layui.use('table', function() {
        var table = layui.table;
        table.render({
            elem: '#people-table',
            width: 800,
            url: url,
            done: function(res) {
                res = formatDate(res);
                table.render({
                    elem: '#people-table',
                    data: res.data,
                    width: 1000,
                    cols: table_cols,
                    skin: 'row' //表格风格
                        ,
                    even: true,
                    page: true
                });
            },
            cols: table_cols,
            skin: 'row',
            even: true,
            page: true
        });
    });
}

function updatecharts() {
    sevenLine.showLoading();
    var sl_data_s = [];
    var sl_data_f = [];
    var x_sl = [];
    var today = new Date();
    Date.prototype.reduceDays = function(days) {
        var date = new Date(this);
        date.setDate(date.getDate() - days);
        return date;
    }
    var legend = [];
    for (var i = 6; i >= 0; i--) {
        sl_data_s[i] = new Object();
        sl_data_s[i].name = today.reduceDays(7-i).toLocaleDateString();
        sl_data_s[i].value = 0;
        sl_data_f[i] = new Object();
        sl_data_f[i].name = today.reduceDays(7-i).toLocaleDateString();
        sl_data_f[i].value = 0;
        //    console.log(sl_data_f);
        x_sl[i] = sl_data_s[i].name;
    }
    $.get(alldata_url, function(giveObj) {
        var all = giveObj.data;
        var max = 0;
        for (var i = 6; i >= 0; i--) {
            for (var j = 0; j < giveObj.count; ++j) {
            //    console.log(i+"    "+j+"   "+ new Date(all[i]["time"]).toLocaleDateString() +"   "+ new Date(sl_data_s[j].name).toLocaleDateString());
                if (new Date(all[j]["newsDate"]).toLocaleDateString() == new Date(sl_data_s[i].name).toLocaleDateString()) {
            //        console.log(all[i]["status"]);
                    sl_data_s[i].value++;
                    
                    if(sl_data_s[i].value>max){
                        max=sl_data_s[i].value;
                    }
                }
                if (new Date(all[j]["registerDate"]).toLocaleDateString() == new Date(sl_data_s[i].name).toLocaleDateString()) {
                    sl_data_f[i].value++;
                    
                    if(sl_data_f[i].value>max){
                        max=sl_data_f[i].value;
                    }
                }
            }
        }
        var y_sl = [];
        for(var i=0;i<=max+5;i++){
            y_sl[i] = i;
        }
        sevenLine.setOption({
            title: {
                text: '近七天登录注册状况'
            },
            legend: {
                data: ['登录', '注册']
            },
            xAxis: {
                name: "日期",
                data: x_sl
            },
            yAxis: {
                name: "人数",
                data: y_sl
            },
            series: [{
                name: '登录',
                type: 'line',
                data: sl_data_s
            }, {
                name: '注册',
                type: 'line',
                data: sl_data_f
            }]
        });
        sevenLine.hideLoading();
    });
}

$(document).ready(function() {
    sevenLine = echarts.init(document.getElementById('pie-type'));
    updatecharts();

    layui.use(['table','laydate'], function() {
        var table = layui.table,
            laydate = layui.laydate;
            
        laydate.render({
            elem: '#searchdateinput' //指定元素
        });
        table.render({
            elem: '#people-table',
            width: 800,
            url: alldata_url, 
            done: function(res) {
                res = formatDate(res);
                table.render({
                    elem: '#people-table',
                    data: res.data,
                    width: 1000,
                    cols: table_cols,
                    skin: 'row' //表格风格
                        ,
                    even: true,
                    page: true
                });
            },
            cols: table_cols,
            skin: 'row' //表格风格
                ,
            even: true,
            page: true
        });
        //监听工具条
        table.on('tool(demo)', function(obj) {
            var data = obj.data;
            if (obj.event === 'detail') {
                layer.open({
                    type: 2,
                    title: '用户详情',
                    maxmin: true,
                    skin: 'layui-layer-molv',
                    area: ['700px', '500px'],
                    content: 'userdetail.html?userId=' + obj.data.userId,
                });
                console.log(obj.data.userId);
            }else if (obj.event === 'del') {
                layer.confirm('真的删除该人员么', function(index) {
                    var urlstr = "http://localhost:8080/AllUsers?userId=" + obj.data.userId;
                    console.log(urlstr);
                    $.ajax({
                        url: urlstr,
                        type: 'DELETE',
                        success: function(result) {
                            // Do something with the result
                            // obj.del();
                            freshtable();
                        }
                    });
                    layer.close(index);
                });
            } 
        });
        var $ = layui.$,
            active = {
                newRow: function() {
                    layer.open({
                        type: 2,
                        title: '新增人员',
                        maxmin: true,
                        skin: 'layui-layer-molv',
                        area: ['500px', '600px'],
                        content: 'newpeople.html?id=0&new&wy', //弹框显示的u500
                        cancel: function(thisindex, layero) {
                            layer.confirm('确认退出？所有修改不会保存！', {icon: 3, title:'提示'}, function(index){
                              //do something
                              layer.close(index);
                              layer.close(thisindex);
                            });
                            return false;
                        }
                    });
                    freshtable(id);
                },
            };
        $('.demoTable .layui-btn').on('click', function() {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
        $("#fresh").click(function() {
            freshtable();
        });
        $("#search").click(function() {
            var want = $("#searchinput").val();
            console.log(want);
            if (want == "") {
                layer.msg("关键字不能为空");
                return;
            }
            layui.use('table', function() {
                var table = layui.table;
                table.render({
                    elem: '#people-table',
                    width: 800,
                    url: alldata_url,
                    cols: table_cols,
                    done: function(res, curr, count) {
                        for (var i = 0; i < res.data.length; i++) {
                            var have = 0;
                            delete res.data[i]["LAY_TABLE_INDEX"];
                            for (var key in res.data[i]) {
                                var temp = res.data[i][key];
                                console.log(temp);
                                if(temp == null){
                                    continue;
                                }
                                if(typeof(temp) != "string"){
                                    temp = temp.toString();
                                }
                                if (temp.indexOf(want) != -1) {
                                    have = 1;
                                }
                            }
                            if (have == 0) {
                                res.data.splice(i, 1);
                                i--;
                            }
                        }
                        res = formatDate(res);
                        table.render({
                            elem: '#people-table',
                            data: res.data,
                            width: 1000,
                            cols: table_cols,
                            skin: 'row' //表格风格
                                ,
                            even: true,
                            page: true
                        });
                    }
                });
            });
        });
        $("#searchdate").click(function() {
            layui.use('table', function() {
                var table = layui.table;
                
                table.render({
                    elem: '#people-table',
                    width: 1000,
                    url: alldata_url,
                    cols: table_cols,
                    done: function(res, curr, count) {
                        res = formatDate(res);
                        var want = $("#searchdateinput").val();
                        want = new Date(want);
                        want.setMonth(want.getMonth()+1);
                        want = want.getFullYear()+"-"+want.getMonth()+"-"+want.getDate();
                        var have = 0;
                        for (var i = 0; i < res.data.length; i++) {
                            have = 0;
                            delete res.data[i]["LAY_TABLE_INDEX"];
                            var temp = res.data[i]["registerDate"];
                            if (temp == want) {
                                have = 1;
                            }
                            
                            temp = res.data[i]["loginDate"];
                            if (temp == want) {
                                have = 1;
                            }

                            if (have == 0) {
                                res.data.splice(i, 1);
                                i--;
                            }
                        }
                        table.render({
                            elem: '#people-table',
                            data: res.data,
                            width: 1000,
                            cols: table_cols,
                            skin: 'row' //表格风格
                                ,
                            even: true,
                            page: true
                        });
                    }
                });
            });
        });
    });
});