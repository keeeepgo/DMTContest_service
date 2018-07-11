var url = 'http://localhost:8080/AllUsers';
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

$(document).ready(function() {
    layui.use(['table','laydate'], function() {
        var table = layui.table,
            laydate = layui.laydate;
            
        laydate.render({
            elem: '#searchdateinput' //指定元素
        });
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
                    var urlstr = "http://localhost:8080/AllUsers?userId=" + obj.data.id;
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