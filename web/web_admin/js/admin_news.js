var url = 'http://localhost:8080/AllNews';
var alldata_url = 'http://localhost:8080/AllNews';
var table_cols = [
                [{
                    field: 'newsId',
                    title: 'ID',
                    width: '10%',
                    sort: true
                }, {
                    field: 'newsUrl',
                    title: '原文地址',
                    width: '20%',
                    sort: true
                }, {
                    field: 'newsTitle',
                    title: '资讯标题',
                    width: '50%'
                }, {
                    field: 'newsDate',
                    title: '资讯日期',
                    width: '20%'
                }, {
                    fixed: 'right',
                    width: 200,
                    align: 'center',
                    toolbar: '#bar'
                }]
            ]

function formatDate(res) {
    for (var i = 0; i < res.data.length; ++i) {
        res.data[i]["realnewsDate"] = res.data[i]["newsDate"];
        res.data[i]["shownewsDate"] = new Date(res.data[i]["newsDate"]).toLocaleString();
        var temp = new Date(res.data[i]["newsDate"]);
        temp.setMonth(temp.getMonth()+1);
        res.data[i]["newsDate"] = temp.getFullYear()+"-"+temp.getMonth()+"-"+temp.getDate();
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
                    title: '资讯详情',
                    maxmin: true,
                    skin: 'layui-layer-molv',
                    area: ['700px', '500px'],
                    content: 'newsdetail.html?newsId=' + obj.data.newsId,
                });
            }else if (obj.event === 'del') {
                layer.confirm('真的删除该资讯么', function(index) {
                    var urlstr = "http://192.168.0.122:8080/api/user/" + obj.data.id;
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
            } else if (obj.event === 'edit') {
                var editurl,edittitle;
                editurl = 'newsedit.html?newsId='+obj.data.newsId;
                edittitle = '编辑资讯';
                layer.open({
                    type: 2,
                    title: edittitle,
                    maxmin: true,
                    skin: 'layui-layer-molv',
                    area: ['500px', '600px'],
                    content: editurl,
                    cancel: function(thisindex, layero) {
                        layer.confirm('确认退出？所有修改不会保存！', {icon: 3, title:'提示'}, function(index){
                            layer.close(index);
                            layer.close(thisindex);
                        });
                        return false;
                    }
                });
            }
        });
        var $ = layui.$,
            active = {
                newRow: function() {
                    layer.open({
                        type: 2,
                        title: '新增资讯',
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
                    url: url,
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
                            var temp = res.data[i]["newsDate"];
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